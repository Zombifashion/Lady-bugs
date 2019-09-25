package com.example.pcplus.adddetails;

public class DiscountModel {

    private int id;
    private  String PId;
    private  String discount;
    private  String month;
    private byte[] image;

    public DiscountModel(int id, String PId, String discount, String month) {
        this.id = id;
        this.PId = PId;
        this.discount = discount;
        this.month = month;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getPId() {
        return PId;
    }

    public void setPId(String PId) {
        this.PId = PId;
    }

    public  String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public  String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

}
