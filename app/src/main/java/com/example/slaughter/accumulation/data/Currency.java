package com.example.slaughter.accumulation.data;

public class Currency {
    public static final String NAME = "Name";
    public static final String SIGN = "Sign";
    public static final String RATE = "Rate";

    private String name;
    private String sign;
    private float exchangeRate;
    private int id;
    private boolean isDefault;

    public Currency(String name, String sign) {
        this.name = name;
        this.sign = sign;
    }

    public Currency(String name, String sign, float exchangeRate) {
        this.name = name;
        this.sign = sign;
        this.exchangeRate = exchangeRate;
    }

    public Currency(String name, String sign, float exchangeRate, int id) {
        this.name = name;
        this.sign = sign;
        this.exchangeRate = exchangeRate;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    public float getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public static Currency getCurrencyByName(String name) {
        return null;
    }

    public int getId() {
        return id;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getListName() {
        return String.format("%s    %s %s", name, exchangeRate, sign);
    }
}
