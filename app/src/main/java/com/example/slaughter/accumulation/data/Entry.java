package com.example.slaughter.accumulation.data;


public class Entry {
    public static final String PLACE = "Place";
    public static final String VALUE = "Value";
    public static final String CURRENCY = "Currency";

    private String place,
            value;

    private Currency currency;

    private int id;

    public Entry(String place, String value, Currency currency) {
        this.place = place;
        this.value = value;
        this.currency = currency;
    }

    public Entry(String place, String value) {
        this.place = place;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {

        return place;
    }

    public String getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", place, value, currency.getSign());
    }
}
