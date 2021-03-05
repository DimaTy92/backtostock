package com.example.entity;

public class User {
    private final String name;
    private final boolean premium;
    private final int age;

    public User(String name, boolean premium, int age) {
        this.name = name;
        this.premium = premium;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public boolean isPremium() {
        return premium;
    }

    public int getAge() {
        return age;
    }

}
