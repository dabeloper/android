package com.dabeloper.android.sqlite_model;

import android.provider.BaseColumns;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 10/09/2017.
 */

public class IOSAppContract {

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "iosApp";

        public static final String COLUMN_NAME_ID   = "id";
        public static final String COLUMN_NAME_TYPE = "type";
    }

}
