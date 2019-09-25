package com.example.pcplus.adddetails;

public class ProductDiscountModel {

    private int pid;
    private String pname;
    private String price;
    private String qty;
    private String discount;
    private String month;
    private byte[] image;

    public ProductDiscountModel(int pid, String pname, String price, String qty, String discount, String month, byte[] image) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.qty = qty;
        this.discount = discount;
        this.month = month;
        this.image = image;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
