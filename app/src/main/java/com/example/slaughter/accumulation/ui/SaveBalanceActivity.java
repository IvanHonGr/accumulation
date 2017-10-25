package com.example.slaughter.accumulation.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slaughter.accumulation.R;
import com.example.slaughter.accumulation.Utils;
import com.example.slaughter.accumulation.data.BalanceHelper;
import com.example.slaughter.accumulation.data.Currency;
import com.example.slaughter.accumulation.data.Entry;

import java.util.List;
import java.util.Locale;

public class SaveBalanceActivity extends Activity {

    private EditText date, amount, note;
    private Spinner currency, month;
    private TextView currentBalance, reportDetails;
    private String currentBalanceUahValue;
    private String usdRate = "";

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

        currentBalanceUahValue = getSum();
        String result = String.format("Current Balance: $%s", currentBalanceUahValue);
        currentBalance.setText(result);
    }

    public void saveBalance(View view) {
        String amountUah = getText(amount);
        BalanceHelper.addBalance(this, currentBalanceUahValue, usdRate, amountUah, getText(date), getText(note));

        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        initFields();
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }

    public void calculateDiff(View view) {
        String salary = "Salary";
        String lastBalance = "Last Balance";

        String message;
        if (BalanceHelper.getBalanceRowsForCurrentMonth(this).isEmpty()) {
            message = salary;
            String balanceForPreviousMonth = BalanceHelper.getBalanceForPreviousMonth(this);
            amount.setText(Utils.difference(balanceForPreviousMonth, currentBalanceUahValue));
        } else {
            message = lastBalance;
        }
        note.setText(message);

    }

    private String getSum() {
        float resultUsd = 0;
        List<Entry> entries = Entry.getAllEntries(this);
        for (Entry entry: entries) {
            Currency currency = entry.getCurrency();
            resultUsd += Utils.multiply(entry.getValue(), currency.getExchangeRate());
            if ("USD".equals(currency.getName())) {
                usdRate = Float.toString(currency.getExchangeRate());
            }
        }
        return String.format(Locale.US, "%.2f", resultUsd);
    }

    private void activateAdapter() {
        currency = (Spinner) findViewById(R.id.spinnerCur);
        List<Currency> list = Currency.getListOfCurrencies(this);
        ArrayAdapter<Currency> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency.setAdapter(dataAdapter);
    }

    public void showReportDetails(View view) {
        String report = "";
        for (String row: BalanceHelper.getBalanceRowsForCurrentMonth(this)) {
            if (report.isEmpty()) {
                report = row;
            } else {
                report = String.format("%s\n%s", report, row);
            }
        }
        reportDetails.setText(report);
    }

    private void initFields() {
        date.setText(Utils.getCurrentDate());
        month.setSelection(Utils.getMonth() - 1);
        amount.setText("");
        note.setText("");
    }
}
