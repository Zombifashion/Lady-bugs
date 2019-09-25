package com.example.pcplus.adddetails;

public class Model {
    private int id;
    private String name;
    private String price;
    private String qty;
    private byte[] image;

    public Model(int id) {
        this.id = id;
    }

    public Model(int id, String name, String price, String qty, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}