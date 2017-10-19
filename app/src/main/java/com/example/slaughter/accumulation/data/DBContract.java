package com.example.slaughter.accumulation.data;

import android.provider.BaseColumns;

public final class DBContract {

    public static final class EntryTable implements BaseColumns {
        public final static String TABLE_NAME = "entries";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PLACE = "place";
        public final static String COLUMN_VALUE = "value";
        public final static String COLUMN_CURRENCY = "currency";
    }

    public static final class CurrencyTable implements BaseColumns {
        public static final String TABLE_NAME = "currencies";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SIGN = "sign";
        public static final String COLUMN_EXCHANGE_RATE = "exchange_rate";
        public static final String COLUMN_IS_DEFAULT = "is_default";
    }

}
