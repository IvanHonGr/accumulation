package com.example.slaughter.accumulation;

import android.text.format.Time;

import java.util.Calendar;
import java.util.Locale;

public class Utils {
    public static int getMonth(int... delta) {
        Calendar calendar = Calendar.getInstance();
        if (delta!= null && delta.length > 0) {
            calendar.add(Calendar.MONTH, delta[0]);
        }
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static String getCurrentDate() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        return today.format("%m/%d/%Y");
    }

    public static String difference(String number1, String number2) {
        float num1 = Float.parseFloat(number1);
        float num2 = Float.parseFloat(number2);
        return String.format(Locale.US, "%.2f", num1 - num2);
    }
}
