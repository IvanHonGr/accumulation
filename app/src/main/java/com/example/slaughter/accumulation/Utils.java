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

    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static String getCurrentDate() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        return today.format("%Y/%m/%d");
    }

    public static String difference(String number1, String number2) {
        float num1 = number1.isEmpty() ? 0 : Float.parseFloat(number1);
        float num2 = number2.isEmpty() ? 0 : Float.parseFloat(number2);
        return String.format(Locale.US, "%.2f", num2 - num1);
    }

    public static float multiply(String value, float factor) {
        float stringFactor = Float.valueOf(value);
        return stringFactor * factor;
    }
}
