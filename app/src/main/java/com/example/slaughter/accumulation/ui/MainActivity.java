package com.example.slaughter.accumulation.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slaughter.accumulation.R;
import com.example.slaughter.accumulation.data.Currency;
import com.example.slaughter.accumulation.data.Entry;
import com.example.slaughter.accumulation.data.DBContract;
import com.example.slaughter.accumulation.data.EntryDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<Entry> entryList = new ArrayList<>();
    private MyArrayAdapter mArrayAdapter;

    private EntryDbHelper mDbHelper;


    static final private int NEW_ENTRY = 0;
    static final private int EDIT_ENTRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);

        mArrayAdapter = new MyArrayAdapter(this, R.layout.list_item,
                android.R.id.text1, entryList);

        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(myOnItemClickListener);
        mDbHelper = new EntryDbHelper(this);
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent = new Intent(MainActivity.this, EditEntryActivity.class);
            Entry currentEntry = entryList.get(position);
            intent.putExtra(Entry.PLACE, currentEntry.getPlace());
            intent.putExtra(Entry.VALUE, currentEntry.getValue());
            intent.putExtra(Entry.CURRENCY, currentEntry.getCurrency().getName());
            intent.putExtra("id", currentEntry.getId());
            startActivityForResult(intent, EDIT_ENTRY);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        readSavedResults();
        mArrayAdapter.notifyDataSetChanged();
    }

    public void calculateBalance(View v) {
        Intent intent = new Intent(MainActivity.this, SaveBalanceActivity.class);
        startActivityForResult(intent, NEW_ENTRY);
    }

    public void addEntry(View view) {
        Intent intent = new Intent(MainActivity.this, EditEntryActivity.class);
        startActivityForResult(intent, NEW_ENTRY);
    }

    public void addCurrency(View view) {
        Intent intent = new Intent(MainActivity.this, ManageCurrencyActivity.class);
        startActivityForResult(intent, NEW_ENTRY);
    }

    private void readSavedResults() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        entryList.clear();

        String rawQuery = "SELECT " + DBContract.EntryTable.TABLE_NAME + "." + DBContract.EntryTable._ID + ", "
                + DBContract.EntryTable.TABLE_NAME + "." + DBContract.EntryTable.COLUMN_PLACE + ", "
                + DBContract.EntryTable.TABLE_NAME + "." + DBContract.EntryTable.COLUMN_VALUE + ", "
                + DBContract.CurrencyTable.TABLE_NAME + "." + DBContract.CurrencyTable.COLUMN_NAME + ", "
                + DBContract.CurrencyTable.TABLE_NAME + "." + DBContract.CurrencyTable.COLUMN_SIGN
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
//            int currencyExchangeRateColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE);
//            int currencyDefaultColumnIndex = cursor.getColumnIndex(DBContract.CurrencyTable.COLUMN_IS_DEFAULT);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String currentPlace = cursor.getString(placeColumnIndex);
                String currentValue = cursor.getString(valueColumnIndex);
                String currencyName = cursor.getString(currencyNameColumnIndex);
                String currencySign = cursor.getString(currencySignColumnIndex);
//                float currencyExchangeRage = cursor.getFloat(currencyExchangeRateColumnIndex);
//                int currencyIsDefault = cursor.getInt(currencyDefaultColumnIndex);
                Currency currentCurrency = new Currency(currencyName, currencySign);
                Entry entry = new Entry(currentPlace, currentValue, currentCurrency);
                entry.setId(id);
                entryList.add(entry);
            }
        } finally {
            cursor.close();
        }
    }

    private class MyArrayAdapter extends ArrayAdapter<Entry> {

        public MyArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<Entry> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item, parent, false);
            }

            Entry entry = getItem(position);
            TextView checkedTextView = (TextView) row
                    .findViewById(R.id.entryTextView);
            checkedTextView.setText(entry.toString());

            return row;
        }
    }
}

