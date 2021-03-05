package com.example.service.impl;

import com.example.entity.Product;
import com.example.entity.User;
import com.example.service.UserNotifierService;

public class ConsoleUserNotifierService implements UserNotifierService {

    private static final String NOTIFY_STRING_PATTERN = "Dear %s, your product: %s is available for buying \n";

    /**
     * Console implementation of User notification that product added to stock.
     * Implementation can be changed on any others  ex : PhoneUserNotifierService (notify user by phone),
     * MailUserNotifierService (notify by email) etc.
     *
     * @param user    User filtered by priority.
     * @param product Product that user can by.
     */
    @Override
    public void notifyUser(User user, Product product) {
        System.out.printf(NOTIFY_STRING_PATTERN, user.getName(), product.getCategory());
    }
}
