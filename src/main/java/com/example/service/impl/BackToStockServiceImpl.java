package com.example.service.impl;

import com.example.entity.Product;
import com.example.entity.User;
import com.example.service.BackToStockService;
import com.example.service.UserNotifierService;
import com.example.type.ProductCategory;

import java.util.*;

public class BackToStockServiceImpl implements BackToStockService {

    private static final int OLD_AGE = 70;
    private final UserNotifierService userNotifier;
    private final Map<Product, List<User>> subscriptionMap = new HashMap<>();

    public BackToStockServiceImpl(UserNotifierService userNotifier) {
        this.userNotifier = userNotifier;
    }

    @Override
    public void subscribe(User user, Product product) {
        if (!subscriptionMap.containsKey(product)) {
            subscriptionMap.put(product, new ArrayList<>());
        }
        List<User> users = subscriptionMap.get(product);
        users.add(user);
    }

    @Override
    public List<User> subscribedUsers(Product product) {
        return Optional.ofNullable(subscriptionMap.get(product))
                .orElse(Collections.emptyList());
    }

    @Override
    public void notifyProductAddedToStock(Product product) {
        List<User> users = Optional.ofNullable(subscriptionMap.get(product)).orElse(Collections.emptyList());
        Optional<User> highPriorityUserToNotify = users.stream()
                .filter(user -> isMedicinePriorityOrPremium(user, product.getCategory()))
                .findFirst();

        if (highPriorityUserToNotify.isPresent()) {
            User user = highPriorityUserToNotify.get();
            userNotifier.notifyUser(user, product);
            users.remove(user);
        } else {
            users.stream()
                    .max(Comparator.comparing(user -> isOldPerson(user.getAge())))
                    .ifPresent(user -> {
                        userNotifier.notifyUser(user, product);
                        users.remove(user);
                    });
        }
    }

    private boolean isMedicinePriorityOrPremium(User user, ProductCategory category) {
        return user.isPremium() || (ProductCategory.MEDICAL.equals(category) && isOldPerson(user.getAge()));
    }

    private boolean isOldPerson(int age) {
        return age > OLD_AGE;
    }
}
