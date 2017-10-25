package com.example.slaughter.accumulation.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.slaughter.accumulation.Utils;

import java.util.ArrayList;
import java.util.List;

public class BalanceHelper {

    public static String getBalanceForPreviousMonth(Context context) {
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String balance = "";

        String rawQuery = "SELECT * FROM " + DBContract.BalanceData.TABLE_NAME +
                " WHERE " + DBContract.BalanceData.COLUMN_DATE + " like '%" + Utils.getMonth(-1)
                + "/%/" + Utils.getYear() + "' ORDER BY date DESC";
        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            int balanceColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_TOTAL_UAH);
//            int dateColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_DATE);
//            int amountColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_AMOUNT);

            if (cursor.moveToNext()) {
                balance = cursor.getString(balanceColumnIndex);
//                String date = cursor.getString(dateColumnIndex);
//                String amount = cursor.getString(amountColumnIndex);
            }
        } finally {
            cursor.close();
        }
        return balance;
    }

    public static List<String> getBalanceRowsForCurrentMonth(Context context) {
        List<String> result = new ArrayList<>();
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String rawQuery = "SELECT * FROM " + DBContract.BalanceData.TABLE_NAME /*+
                " WHERE " + DBContract.BalanceData.COLUMN_DATE + " like '%" + Utils.getMonth()
                + "/%/"  + Utils.getYear()*/ + " ORDER BY date DESC";
        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            int balanceColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_TOTAL_UAH);
            int dateColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_DATE);
            int noteColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_NOTE);
            int amountColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_AMOUNT);
            int rateColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_USD_EXCHANGE_RATE);

            while (cursor.moveToNext()) {
                String balance = cursor.getString(balanceColumnIndex);
                String date = cursor.getString(dateColumnIndex);
                String note = cursor.getString(noteColumnIndex);
                String amount = cursor.getString(amountColumnIndex);
                if (!amount.isEmpty()) {
                    if (!amount.startsWith("-")) {
                        amount = "+" + amount;
                    }
                    amount = " " + amount + " uah";
                }
                String rate = cursor.getString(rateColumnIndex);
                result.add(String.format("%s%s %s. %s, usd rate %s", date, amount, note, balance, rate));
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    public static void addBalance(Context context, String sum, String usdRate, String amount, String date, String note) {
        EntryDbHelper mDbHelper = new EntryDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.BalanceData.COLUMN_TOTAL_UAH, sum);
        values.put(DBContract.BalanceData.COLUMN_USD_EXCHANGE_RATE, usdRate);
        values.put(DBContract.BalanceData.COLUMN_AMOUNT, amount);
        values.put(DBContract.BalanceData.COLUMN_DATE, date);
        values.put(DBContract.BalanceData.COLUMN_NOTE, note);
        db.insert(DBContract.BalanceData.TABLE_NAME, null, values);
    }
}
