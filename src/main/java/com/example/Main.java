package com.example;

import com.example.entity.Product;
import com.example.entity.User;
import com.example.service.BackToStockService;
import com.example.service.UserNotifierService;
import com.example.service.impl.BackToStockServiceImpl;
import com.example.service.impl.ConsoleUserNotifierService;
import com.example.type.ProductCategory;

public class Main {
    public static void main(String[] args) {

        UserNotifierService notifierService = new ConsoleUserNotifierService();
        BackToStockService backToStockService = new BackToStockServiceImpl(notifierService);
        User sergey = new User("Sergey", true, 22);
        User randomUser = new User("User", false, 45);
        User oldUser = new User("Granny", false, 73);

        Product medicalProduct = new Product("solpadein", ProductCategory.MEDICAL);

        backToStockService.subscribe(sergey, medicalProduct);
        backToStockService.subscribe(oldUser, medicalProduct);
        backToStockService.subscribe(randomUser, medicalProduct);

        backToStockService.notifyProductAddedToStock(medicalProduct);

        User newUser = new User("New user", true, 23);
        backToStockService.subscribe(newUser, medicalProduct);

        backToStockService.notifyProductAddedToStock(medicalProduct);
        backToStockService.notifyProductAddedToStock(medicalProduct);
        backToStockService.notifyProductAddedToStock(medicalProduct);
        backToStockService.notifyProductAddedToStock(medicalProduct);


    }
}
