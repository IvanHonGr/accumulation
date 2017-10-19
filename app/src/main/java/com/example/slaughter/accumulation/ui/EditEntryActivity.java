package com.example.slaughter.accumulation.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.slaughter.accumulation.R;
import com.example.slaughter.accumulation.data.Currency;
import com.example.slaughter.accumulation.data.Entry;
import com.example.slaughter.accumulation.data.DBContract;
import com.example.slaughter.accumulation.data.EntryDbHelper;

import java.util.ArrayList;
import java.util.List;

public class EditEntryActivity extends AppCompatActivity {

    private EditText place,
            value;
    private Spinner spinner;
    private int currentId, currencyPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        place = (EditText) findViewById(R.id.placeValue);
        value = (EditText) findViewById(R.id.value);

        setValueIfNotEmpty(place, Entry.PLACE);
        setValueIfNotEmpty(value, Entry.VALUE);

        activateAdapter();

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        currentId = getIntent().getIntExtra("id", -1);
        if (currentId == -1) {
            deleteButton.setVisibility(View.GONE);
        }
    }

    public void saveEntry(View view) {
        Currency currency = (Currency) spinner.getSelectedItem();
        Entry entry = new Entry(place.getText().toString(), value.getText().toString(), currency);
        updateEntry(entry, currentId);

        finish();
    }

    private void setValueIfNotEmpty(EditText editText, String value) {
        String current = getIntent().getStringExtra(value);
        if (current!= null && !current.isEmpty()) {
            editText.setText(current);
        }
    }

    private void updateEntry(Entry entry, int index) {
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
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

    public void delete(View view) {
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String where = DBContract.EntryTable._ID + "= ?";
        String[] i = {Integer.toString(currentId)};
        db.delete(DBContract.EntryTable.TABLE_NAME, where, i);

        Toast.makeText(this, String.format("%s entry was deleted", place.getText()), Toast.LENGTH_LONG).show();
        finish();
    }

    private void activateAdapter() {
        String currentCurrencyName = getIntent().getStringExtra(Entry.CURRENCY);
        spinner = (Spinner) findViewById(R.id.currency);
        List<Currency> list = new ArrayList<>();
        EntryDbHelper mDbHelper = new EntryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String rawQuery = "SELECT * FROM " + DBContract.CurrencyTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            int currentNumber = 0;
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
                if (currentCurrencyName!= null && currentCurrencyName.equals(currencyName)) {
                    currencyPosition = currentNumber;
                }
                currentNumber++;
            }
        } finally {
            cursor.close();
        }

        ArrayAdapter<Currency> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(currencyPosition);
    }
}
