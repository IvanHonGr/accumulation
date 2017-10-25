package com.example.slaughter.accumulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.slaughter.accumulation.Utils;

import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<Currency> getListOfCurrencies(Context context) {
        ArrayList<Currency> list = new ArrayList<>();
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String rawQuery = "SELECT * FROM " + DBContract.CurrencyTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            int idColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable._ID);

            int currencyNameColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_NAME);
            int currencySignColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_SIGN);
            int currencyExchangeRateColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE);
            int currencyDefaultColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_IS_DEFAULT);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String currencyName = cursor.getString(currencyNameColumnIndex);
                String currencySign = cursor.getString(currencySignColumnIndex);
                float currencyExchangeRate = cursor.getFloat(currencyExchangeRateColumnIndex);
                int currencyIsDefault = cursor.getInt(currencyDefaultColumnIndex);
                Currency currentCurrency = new Currency(currencyName, currencySign, currencyExchangeRate, id);
                currentCurrency.setDefault(currencyIsDefault == 1);
                list.add(currentCurrency);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public static void updateCurrency(Context context, Currency currency, int index) {
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.CurrencyTable.COLUMN_NAME, currency.getName());
        values.put(DBContract.CurrencyTable.COLUMN_SIGN, currency.getSign());
        values.put(DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE, currency.getExchangeRate());
        values.put(DBContract.CurrencyTable.COLUMN_IS_DEFAULT, "0");

        if (index >= 0) {

            String where = DBContract.EntryTable._ID + "= ?";
            String[] i = {Integer.toString(index)};

            db.update(DBContract.CurrencyTable.TABLE_NAME, values, where, i);
        } else {
            db.insert(DBContract.CurrencyTable.TABLE_NAME, null, values);
        }
    }

}
