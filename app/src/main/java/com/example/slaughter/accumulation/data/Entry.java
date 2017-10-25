package com.example.slaughter.accumulation.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    public static void updateEntry(Context context, Entry entry, int index) {
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.EntryTable.COLUMN_PLACE, entry.getPlace());
        values.put(DBContract.EntryTable.COLUMN_VALUE, entry.getValue());
        values.put(DBContract.EntryTable.COLUMN_CURRENCY, entry.getCurrency().getId());

        if (index >= 0) {

            String where = DBContract.EntryTable._ID + "= ?";
            String[] i = {Integer.toString(index)};

            db.update(DBContract.EntryTable.TABLE_NAME, values, where, i);
        } else {
            db.insert(DBContract.EntryTable.TABLE_NAME, null, values);
        }
    }

    public static void delete(Context context, int id) {
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String where = DBContract.EntryTable._ID + "= ?";
        String[] i = {Integer.toString(id)};
        db.delete(DBContract.EntryTable.TABLE_NAME, where, i);
    }

    public static List<Entry> getAllEntries(Context context) {
        List<Entry> list = new ArrayList<>();
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String rawQuery = "SELECT " + DBContract.EntryTable.TABLE_NAME + "." + DBContract.EntryTable._ID + ", "
                + DBContract.EntryTable.TABLE_NAME + "." + DBContract.EntryTable.COLUMN_PLACE + ", "
                + DBContract.EntryTable.TABLE_NAME + "." + DBContract.EntryTable.COLUMN_VALUE + ", "
                + DBContract.CurrencyTable.TABLE_NAME + "." + DBContract.CurrencyTable.COLUMN_NAME + ", "
                + DBContract.CurrencyTable.TABLE_NAME + "." + DBContract.CurrencyTable.COLUMN_SIGN + ", "
                + DBContract.CurrencyTable.TABLE_NAME + "." + DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE
                + " FROM " + DBContract.EntryTable.TABLE_NAME + " INNER JOIN " + DBContract.CurrencyTable.TABLE_NAME
                + " ON " + DBContract.EntryTable.TABLE_NAME + "." + DBContract.EntryTable.COLUMN_CURRENCY +
                " = " + DBContract.CurrencyTable.TABLE_NAME + "." + DBContract.CurrencyTable._ID;
        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            int idColumnIndex = cursor.getColumnIndex(DBContract.EntryTable._ID);
            int placeColumnIndex = cursor.getColumnIndex(DBContract.EntryTable.COLUMN_PLACE);
            int valueColumnIndex = cursor.getColumnIndex(DBContract.EntryTable.COLUMN_VALUE);

            int currencyNameColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_NAME);
            int currencySignColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_SIGN);
            int currencyExchangeRateColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE);
//            int currencyDefaultColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_IS_DEFAULT);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String currentPlace = cursor.getString(placeColumnIndex);
                String currentValue = cursor.getString(valueColumnIndex);
                String currencyName = cursor.getString(currencyNameColumnIndex);
                String currencySign = cursor.getString(currencySignColumnIndex);
                float currencyExchangeRate = cursor.getFloat(currencyExchangeRateColumnIndex);
//                int currencyIsDefault = cursor.getInt(currencyDefaultColumnIndex);
                Currency currentCurrency = new Currency(currencyName, currencySign, currencyExchangeRate);
                Entry entry = new Entry(currentPlace, currentValue, currentCurrency);
                entry.setId(id);
                list.add(entry);
            }
        } finally {
            cursor.close();
        }
        return list;
    }
}
