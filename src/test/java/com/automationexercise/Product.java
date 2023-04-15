package com.automationexercise;

public class Product {

    private String id;
    private String name;
    private String price;
    private String brand;
    private String usertype;
    private String category;

    public Product(String id, String name, String price, String brand, String usertype, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.usertype = usertype;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getCategory() {
        return category;
    }
}
