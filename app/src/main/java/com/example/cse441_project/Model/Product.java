package com.example.cse441_project.Model;

public class Product {
    private String brand;
    private String description;
    private String name;
    private double price;
    private String productId;
    private int stock_quantity;
    private String imageUrl;

    // Constructor
    public Product(String brand,String description, String imageUrl ,String name , double price, String productId, int stock_quantity ) {
        this.brand = brand;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.stock_quantity = stock_quantity;
    }

    public Product(String name , double price, String imageUrl){
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    // Getter methods
    public String getBrand() { return brand; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getProductId() { return productId; }
    public int getStock_quantity() { return stock_quantity; }
}
