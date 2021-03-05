package com.example.service;

import com.example.entity.Product;
import com.example.entity.User;

import java.util.List;

public interface BackToStockService {

    void subscribe(User user, Product product);

    List<User> subscribedUsers(Product product);

    void notifyProductAddedToStock(Product product);

}
