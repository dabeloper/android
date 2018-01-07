package com.dabeloper.android.sqlite_model;

import android.provider.BaseColumns;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 10/09/2017.
 */

public class IOSActionContract {

    public static final int TYPE_FAV = 1;
    public static final int TYPE_DEL = 2;


    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "iosActionApp";

        public static final String COLUMN_NAME_ID       = "id";
        public static final String COLUMN_NAME_TYPE     = "type";
        public static final String COLUMN_NAME_COMMENT  = "comment";
    }

}
