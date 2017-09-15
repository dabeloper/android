package com.dabeloper.android.sqlite_model;

import android.provider.BaseColumns;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 11/08/2017.
 */

public class PersonContract {

    private PersonContract() {}

    public static class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME               = "persons";
        public static final String COLUMN_NAME_NAME         = "name";
        public static final String COLUMN_NAME_CURRICULUM   = "curriculum";
        public static final String COLUMN_NAME_PROFESSION   = "profession";
    }

}