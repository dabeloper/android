package com.dabeloper.android.sqlite_model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dabeloper.android.model.IOSApp;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 11/09/2017.
 */

public class IOSActionSQLite {

    private static final String TABLE_NAME = IOSActionContract.FeedEntry.TABLE_NAME;

    public static Cursor getOne( SQLiteDatabase db, String appId ){
        String columns[] = new String[]{IOSActionContract.FeedEntry.COLUMN_NAME_ID};
        String selection = IOSActionContract.FeedEntry.COLUMN_NAME_ID + " LIKE ?"; // WHERE id LIKE ?
        String selectionArgs[] = new String[]{appId};

        return db.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }//END getOne


    public static Cursor getAll( SQLiteDatabase db ){
        return db.query(
                TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                null,  // Columnas para la cláusula WHERE
                null,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );
    }//END getAll


    public static long newOne( SQLiteDatabase db , String appId , String type ){
        ContentValues values = new ContentValues();
            values.put(IOSActionContract.FeedEntry._ID, appId);
            values.put(IOSActionContract.FeedEntry.COLUMN_NAME_ID, appId);
            values.put(IOSActionContract.FeedEntry.COLUMN_NAME_TYPE, type);
            values.put(IOSActionContract.FeedEntry.COLUMN_NAME_COMMENT, "");
        return db.insert(
                    TABLE_NAME,
                    null,
                    values
                );
    }//END newOne


    public static long updateOne( SQLiteDatabase db , String appId , String type , String comment ){
        String selection = IOSActionContract.FeedEntry.COLUMN_NAME_ID + " LIKE ?"; // WHERE id LIKE ?
        String selectionArgs[] = new String[]{appId};
        ContentValues values = new ContentValues();
            values.put(IOSActionContract.FeedEntry.COLUMN_NAME_ID, appId);
            values.put(IOSActionContract.FeedEntry.COLUMN_NAME_TYPE, type);
            values.put(IOSActionContract.FeedEntry.COLUMN_NAME_COMMENT, comment);
        return db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }//END updateOne


    public static long removeOne( SQLiteDatabase db , String appId ){
        String selection = IOSActionContract.FeedEntry.COLUMN_NAME_ID + " LIKE ?"; // WHERE id LIKE ?
        String selectionArgs[] = new String[]{appId};
        return db.delete(
                TABLE_NAME,
                selection,
                selectionArgs
        );
    }//END updateOne


}
