package com.eyvot.nbce.domian.entity;

public class Model {

    private int id;
    private String name;
    private int averagePrice;
    private int brandId;


    public Model() {}

    public Model(int id, String name, int averagePrice, int brandId) {
        this.id = id;
        this.name = name;
        this.averagePrice = averagePrice;
        this.brandId = brandId;
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

    public int getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(int averagePrice) {
        this.averagePrice = averagePrice;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

}
