package com.eyvot.nbce.domian.entity;

public class Brand {

    private int id;
    private String name;
    private int averagePrice;


    public Brand() {}

    public Brand(int id, String name, int averagePrice) {
        this.id = id;
        this.name = name;
        this.averagePrice = averagePrice;
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

}
