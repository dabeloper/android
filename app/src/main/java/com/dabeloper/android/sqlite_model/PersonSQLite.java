package com.dabeloper.android.sqlite_model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dabeloper.android.model.Person;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 12/09/2017.
 */

public class PersonSQLite {

    public static Cursor getAllPersons(SQLiteDatabase db ){
        return db.query(
                PersonContract.FeedEntry.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                null,  // Columnas para la cláusula WHERE
                null,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );
    }

    public static Cursor getPersonById( SQLiteDatabase db, String id ){
        String columns[] = new String[]{PersonContract.FeedEntry.COLUMN_NAME_NAME};
        String selection = PersonContract.FeedEntry._ID + " LIKE ?"; // WHERE id LIKE ?
        String selectionArgs[] = new String[]{id};

        return db.query(
                PersonContract.FeedEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    public static long insertPerson(SQLiteDatabase db, Person person) {
        return db.insert(
                PersonContract.FeedEntry.TABLE_NAME,
                null,
                person.toContentValues());
    }

}
