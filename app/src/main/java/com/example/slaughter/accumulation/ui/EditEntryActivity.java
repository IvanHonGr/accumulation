package com.example.slaughter.accumulation.ui;

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
        Entry.updateEntry(this, entry, currentId);

        finish();
    }

    private void setValueIfNotEmpty(EditText editText, String value) {
        String current = getIntent().getStringExtra(value);
        if (current!= null && !current.isEmpty()) {
            editText.setText(current);
        }
    }

    public void delete(View view) {
        if (currentId != 0) {
            Entry.delete(this, currentId);
            Toast.makeText(this, String.format("%s entry was deleted", place.getText()), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "You can't delete USD", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void activateAdapter() {
        List<Currency> list = Currency.getListOfCurrencies(this);

        //find number of current currency in the list
        String currentCurrencyName = getIntent().getStringExtra(Entry.CURRENCY);
        int currentNumber = 0;
        for (Currency currency: list) {
            if (currentCurrencyName != null && currentCurrencyName.equals(currency.getName())) {
                currencyPosition = currentNumber;
            }
            currentNumber++;
        }

        ArrayAdapter<Currency> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.currency);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(currencyPosition);
    }
}
