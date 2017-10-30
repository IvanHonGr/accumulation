package com.example.slaughter.accumulation.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.slaughter.accumulation.R;
import com.example.slaughter.accumulation.data.Currency;

import java.util.ArrayList;
import java.util.List;

public class ManageCurrencyActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<Currency> currencyList = new ArrayList<>();
    private MyArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_currency);

        mListView = (ListView) findViewById(R.id.listView);

        mArrayAdapter = new MyArrayAdapter(this, R.layout.list_item,
                android.R.id.text1, currencyList);

        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(myOnItemClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        readSavedResults();
        mArrayAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent = new Intent(ManageCurrencyActivity.this, EditCurrencyActivity.class);
            Currency currency = currencyList.get(position);
            intent.putExtra(Currency.NAME, currency.getName());
            intent.putExtra(Currency.SIGN, currency.getSign());
            intent.putExtra(Currency.RATE, currency.getExchangeRate());
            intent.putExtra("id", currency.getId());
            startActivity(intent);
        }
    };

    public void addCurrency(View view) {
        Intent intent = new Intent(this, EditCurrencyActivity.class);
        startActivityForResult(intent, 0);
    }

    private void readSavedResults() {
        currencyList.clear();
        currencyList.addAll(Currency.getListOfCurrencies(this));
    }

    private class MyArrayAdapter extends ArrayAdapter<Currency> {

        public MyArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<Currency> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item, parent, false);
            }

            Currency currency = getItem(position);
            TextView checkedTextView = (TextView) row
                    .findViewById(R.id.entryTextView);
            checkedTextView.setText(currency.getListName());

            return row;
        }
    }
}
