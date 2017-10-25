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
    private List<Entry> entryList = new ArrayList<>();
    private MyArrayAdapter mArrayAdapter;


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
        entryList.clear();
        entryList.addAll(Entry.getAllEntries(this));
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

