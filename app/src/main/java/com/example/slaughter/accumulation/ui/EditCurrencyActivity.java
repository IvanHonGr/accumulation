package com.example.slaughter.accumulation.ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slaughter.accumulation.R;
import com.example.slaughter.accumulation.data.Currency;
import com.example.slaughter.accumulation.data.DBContract;
import com.example.slaughter.accumulation.data.EntryDbHelper;

import java.util.Locale;

public class EditCurrencyActivity extends AppCompatActivity {

    private EditText name, sign, exchangeRate;
    int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_currency);

        name = (EditText) findViewById(R.id.currency_name);
        sign = (EditText) findViewById(R.id.currency_sign);
        exchangeRate = (EditText) findViewById(R.id.currency_rate);

        setValueIfNotEmpty(name, Currency.NAME);
        setValueIfNotEmpty(sign, Currency.SIGN);
        float currentRate = getIntent().getFloatExtra(Currency.RATE, 0);
        if (currentRate != 0) {
            exchangeRate.setText(String.format(Locale.US, "%.2f", currentRate));
        }

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        currentId = getIntent().getIntExtra("id", -1);
        if (currentId == -1) {
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void setValueIfNotEmpty(EditText editText, String value) {
        String current = getIntent().getStringExtra(value);
        if (current!= null && !current.isEmpty()) {
            editText.setText(current);
        }
    }

    public void saveCurrency(View view) {
        Currency currency = new Currency(name.getText().toString(), sign.getText().toString(), Float.valueOf(exchangeRate.getText().toString()));
        updateEntry(currency, currentId);
        finish();
    }

    public void delete(View view) {
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String where = DBContract.EntryTable._ID + "= ?";
        String[] i = {Integer.toString(currentId)};
        db.delete(DBContract.CurrencyTable.TABLE_NAME, where, i);

        Toast.makeText(this, String.format("%s entry was deleted", name.getText()), Toast.LENGTH_LONG).show();
        finish();
    }

    private void updateEntry(Currency currency, int index) {
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
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
