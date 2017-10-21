package com.example.slaughter.accumulation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EntryDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = EntryDbHelper.class.getSimpleName();

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "entries.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 5;

    /**
     * Конструктор {@link EntryDbHelper}.
     *
     * @param context Контекст приложения
     */
    public EntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String SQL_CREATE_ENTRY_TABLE = "CREATE TABLE " + DBContract.EntryTable.TABLE_NAME + " ("
                    + DBContract.EntryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBContract.EntryTable.COLUMN_PLACE + " TEXT NOT NULL, "
                    + DBContract.EntryTable.COLUMN_VALUE + " TEXT NOT NULL, "
                    + DBContract.EntryTable.COLUMN_CURRENCY + " INTEGER NOT NULL);";
            db.execSQL(SQL_CREATE_ENTRY_TABLE);
        } catch (Exception e){
            Log.e("DB", e.getMessage());
        }

        try {
            String SQL_CREATE_CURRENCY_TABLE = "CREATE TABLE " + DBContract.CurrencyTable.TABLE_NAME + " ("
                    + DBContract.CurrencyTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBContract.CurrencyTable.COLUMN_NAME + " TEXT NOT NULL, "
                    + DBContract.CurrencyTable.COLUMN_SIGN + " TEXT NOT NULL, "
                    + DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE + " FLOAT NOT NULL, "
                    + DBContract.CurrencyTable.COLUMN_IS_DEFAULT + " INTEGER(2) NOT NULL);";
            db.execSQL(SQL_CREATE_CURRENCY_TABLE);
        } catch (Exception e) {
            Log.e("DB", e.getMessage());
        }

        try {
            String SQL_CREATE_CURRENCY_TABLE = "CREATE TABLE " + DBContract.BalanceData.TABLE_NAME + " ("
                    + DBContract.BalanceData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBContract.BalanceData.COLUMN_TOTAL_USD + " TEXT NOT NULL, "
                    + DBContract.BalanceData.COLUMN_AMOUNT + " TEXT NOT NULL, "
                    + DBContract.BalanceData.COLUMN_NOTE + " TEXT NOT NULL, "
                    + DBContract.BalanceData.COLUMN_DATE + " TEXT NOT NULL);";
            db.execSQL(SQL_CREATE_CURRENCY_TABLE);
        } catch (Exception e) {
            Log.e("DB", e.getMessage());
        }

        onUpgrade(db, 0, 1);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            try {
                String SQL_INSERT_BASE_CURRENCY = "INSERT INTO " + DBContract.CurrencyTable.TABLE_NAME + " ("
                        + DBContract.CurrencyTable.COLUMN_NAME + ", " + DBContract.CurrencyTable.COLUMN_SIGN
                        + ", " + DBContract.CurrencyTable.COLUMN_EXCHANGE_RATE
                        + ", " + DBContract.CurrencyTable.COLUMN_IS_DEFAULT
                        + ") VALUES('USD', '$', 26.5, 1)";
                db.execSQL(SQL_INSERT_BASE_CURRENCY);
            } catch (Exception e) {
                Log.e("DB", e.getMessage());
            }
        }
    }
}
