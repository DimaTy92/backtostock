package com.example.service;

import com.example.entity.Product;
import com.example.entity.User;

public interface UserNotifierService {
     void notifyUser(User user, Product product);
}
