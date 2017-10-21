package com.example.slaughter.accumulation.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slaughter.accumulation.R;
import com.example.slaughter.accumulation.Utils;
import com.example.slaughter.accumulation.data.Currency;
import com.example.slaughter.accumulation.data.DBContract;
import com.example.slaughter.accumulation.data.EntryDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SaveBalanceActivity extends Activity {

    private EditText date, amount, note;
    private Spinner currency, month;
    private TextView currentBalance, reportDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_balance);

        date = (EditText) findViewById(R.id.date);
        amount = (EditText) findViewById(R.id.amount);
        note = (EditText) findViewById(R.id.note);
        currentBalance = (TextView) findViewById(R.id.currentBalance);
        reportDetails = (TextView) findViewById(R.id.reportDetails);
        month = (Spinner) findViewById(R.id.month);

        initFields();
        activateAdapter();

        String result = String.format("Current Balance: $%s", getSum());
        currentBalance.setText(result);

//        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
//                .show();
    }



    public void saveBalance(View view) {
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.BalanceData.COLUMN_TOTAL_USD, getSum());
        values.put(DBContract.BalanceData.COLUMN_AMOUNT, amount.getText().toString());
        values.put(DBContract.BalanceData.COLUMN_DATE, date.getText().toString());
        values.put(DBContract.BalanceData.COLUMN_NOTE, note.getText().toString());
        db.insert(DBContract.BalanceData.TABLE_NAME, null, values);

        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        initFields();
    }

    public void calculateDiff(View view) {
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String rawQuery = "SELECT * FROM " + DBContract.BalanceData.TABLE_NAME +
                " WHERE " + DBContract.BalanceData.COLUMN_DATE + " like '%" + Utils.getMonth(-1)
                + "/%/%' ORDER BY date DESC";
        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            int dateColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_DATE);
            int balanceColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_TOTAL_USD);
            int amountColumnIndex = cursor.getColumnIndex(DBContract.BalanceData.COLUMN_AMOUNT);

            if (cursor.moveToNext()) {
//                String date = cursor.getString(dateColumnIndex);
                String balance = cursor.getString(balanceColumnIndex);
//                String amount = cursor.getString(amountColumnIndex);

                this.amount.setText(Utils.difference(balance, getSum()));
            }
        } finally {
            cursor.close();
        }

        //if first row for current month
        //Balance with salary
        //else
        //Last Balance

    }

    private String getSum() {
        float resultUsd = 0;
        //todo
        return String.format(Locale.US, "%.2f", resultUsd);
    }

    private void activateAdapter() {
        currency = (Spinner) findViewById(R.id.spinnerCur);
        List<Currency> list = new ArrayList<>();
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String rawQuery = "SELECT * FROM " + DBContract.CurrencyTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            int idColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable._ID);
            int currencyNameColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_NAME);
            int currencySignColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_SIGN);
            int currencyExchangeRateColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String currencyName = cursor.getString(currencyNameColumnIndex);
                String currencySign = cursor.getString(currencySignColumnIndex);
                float currencyExchangeRate = cursor.getFloat(currencyExchangeRateColumnIndex);

                Currency currency = new Currency(currencyName, currencySign, currencyExchangeRate, id);
                list.add(currency);
            }
        } finally {
            cursor.close();
        }

        ArrayAdapter<Currency> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency.setAdapter(dataAdapter);
    }

    public void showReportDetails(View view) {
        reportDetails.setText("today - something +200 uah \n" +
                "yesterday +41500 usd \n" +
                "total bla bla");
    }

    private void initFields() {
        date.setText(Utils.getCurrentDate());
        month.setSelection(Utils.getMonth() - 1);
        amount.setText("");
        note.setText("");
    }
}
