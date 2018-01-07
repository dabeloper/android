package com.dabeloper.android.sqlite_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dabeloper.android.model.Person;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 31/07/2017.
 */


public class DbHandler extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DABOLA.db";

    private boolean firstTime = false;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String TABLE_DUMMY =
                                                "CREATE TABLE " +   DummyContract.FeedEntry.TABLE_NAME +
                                                        " (" +
                                                        DummyContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                                                        DummyContract.FeedEntry.COLUMN_NAME_TITLE             + TEXT_TYPE + COMMA_SEP +
                                                        DummyContract.FeedEntry.COLUMN_NAME_BODY              + TEXT_TYPE + COMMA_SEP +
                                                        DummyContract.FeedEntry.COLUMN_NAME_TYPE              + TEXT_TYPE + COMMA_SEP +
                                                        DummyContract.FeedEntry.COLUMN_NAME_COLLECTION_ID      + INT_TYPE + " DEFAULT "+ DummyContract.FeedEntry.USER_COLLECTION_ID+"," +
                                                        DummyContract.FeedEntry.COLUMN_NAME_CREATED_DT         + TEXT_TYPE + COMMA_SEP +
                                                        DummyContract.FeedEntry.COLUMN_NAME_UPDATED_DT         + TEXT_TYPE + COMMA_SEP +
                                                        DummyContract.FeedEntry.COLUMN_NAME_LAST_ACCESS_DT     + TEXT_TYPE +
                                                        " )";
    private static final String TABLE_PERSON =
                                                "CREATE TABLE " +   PersonContract.FeedEntry.TABLE_NAME +
                                                        " (" +
                                                        PersonContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                                                        PersonContract.FeedEntry.COLUMN_NAME_NAME             + TEXT_TYPE + COMMA_SEP +
                                                        PersonContract.FeedEntry.COLUMN_NAME_PROFESSION       + TEXT_TYPE + COMMA_SEP +
                                                        PersonContract.FeedEntry.COLUMN_NAME_CURRICULUM       + TEXT_TYPE +
                                                        " )";

    private static final String TABLE_IOS_ACTION =
                                                "CREATE TABLE " +   IOSActionContract.FeedEntry.TABLE_NAME +
                                                        " (" +
                                                            IOSActionContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                                                            IOSActionContract.FeedEntry.COLUMN_NAME_ID             + INT_TYPE + COMMA_SEP +
                                                            IOSActionContract.FeedEntry.COLUMN_NAME_TYPE           + INT_TYPE + COMMA_SEP +
                                                            IOSActionContract.FeedEntry.COLUMN_NAME_COMMENT        + TEXT_TYPE +
                                                        " )";


    private static final String SQL_DELETE_TABLE_DUMMY      = "DROP TABLE IF EXISTS " + DummyContract.FeedEntry.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_PERSON     = "DROP TABLE IF EXISTS " + PersonContract.FeedEntry.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_IOS_ACTION = "DROP TABLE IF EXISTS " + IOSActionContract.FeedEntry.TABLE_NAME;

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        //System.out.println( " DbHandler > onCreate : firstTime " );
        firstTime = true;
        db.execSQL(TABLE_DUMMY);
        db.execSQL(TABLE_PERSON);
        db.execSQL(TABLE_IOS_ACTION);

        // Registros ejemplos 20
        for (int i=0; i<20; i++) {
            ContentValues valores = new ContentValues();
            valores.put(PersonContract.FeedEntry._ID, i);
            valores.put(PersonContract.FeedEntry.COLUMN_NAME_NAME, "Nombre profesional "+i);
            valores.put(PersonContract.FeedEntry.COLUMN_NAME_PROFESSION, "Profesion "+i);
            valores.put(PersonContract.FeedEntry.COLUMN_NAME_CURRICULUM, "Curriculum "+i);
            db.insertOrThrow(PersonContract.FeedEntry.TABLE_NAME, null, valores);
        }

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        switch(oldVersion) {
            case 1:
                //upgrade logic from version 1 to 2
            case 2:
                //upgrade logic from version 2 to 3
            case 3:
                //upgrade logic from version 3 to 4
                break;
            default:
                throw new IllegalStateException(
                        "onUpgrade() with unknown oldVersion " + oldVersion);
        }
//        Notice the missing break statement in case 1 and 2. This is what I mean by incremental upgrade.
//        Say if the old version is 2 and new version is 4, then the logic will upgrade the database from 2 to 3 and then to 4
//        If old version is 3 and new version is 4, it will just run the upgrade logic for 3 to 4

        db.execSQL(SQL_DELETE_TABLE_DUMMY);
        db.execSQL(SQL_DELETE_TABLE_PERSON);
        db.execSQL(SQL_DELETE_TABLE_IOS_ACTION);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean isFirstTime(){
        //System.out.println( " DbHandler > isFirstTime " + firstTime );
        return firstTime; }

    public void noFirstTime(){
        //System.out.println( " DbHandler > noFirstTime " );
        firstTime = false; }
}
