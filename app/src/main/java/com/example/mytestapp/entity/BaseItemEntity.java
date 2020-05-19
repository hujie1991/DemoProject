package com.example.mytestapp.entity;

public class BaseItemEntity {

    private String titile;
    private String value;

    public BaseItemEntity(String titile, String value) {
        this.titile = titile;
        this.value = value;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
