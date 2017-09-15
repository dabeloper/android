package com.dabeloper.android.sqlite_model;

import android.provider.BaseColumns;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 31/07/2017.
 */


public final class DummyContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DummyContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME           = "dummyData";
        public static final String COLUMN_NAME_TITLE    = "question";
        public static final String COLUMN_NAME_BODY     = "answer";
        public static final String COLUMN_NAME_TYPE     = "options";
        public static final String COLUMN_NAME_COLLECTION_ID        = "collection";
        public static final String COLUMN_NAME_CREATED_DT           = "created_date";
        public static final String COLUMN_NAME_UPDATED_DT           = "updated_date";
        public static final String COLUMN_NAME_LAST_ACCESS_DT       = "last_access_date";

        //Collection Zero means Downloads from server
        public static final int DOWNLOADS_COLLECTION_ID     = 0;
        public static final int USER_COLLECTION_ID          = 1;
    }
}

