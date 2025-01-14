package com.example.dam2_e2_t6_mpgm.LocalStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;

public class LocalDBDao {
    private final LocalDB localDB;

    public LocalDBDao(Context context) {
        localDB = new LocalDB(context);
    }

    public long addLanguage(String lenguageKode) {
        SQLiteDatabase db = localDB.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LocalDB.COLUMN_HIZKUNTZAKODE, lenguageKode);
        long id = db.insert(LocalDB.TABLE_HIZKUNTZA, null ,values);

        return id;
    }

    public String getLanguage(){
        SQLiteDatabase db = localDB.getReadableDatabase();

        String[] zutabeak = {
                LocalDB.COLUMN_HIZKUNTZAKODE,
        };

        Cursor cursor = db.query(LocalDB.TABLE_HIZKUNTZA, zutabeak,
                null, null, null, null, null);

        cursor.moveToFirst();
        String hizkuntzaKode = cursor.getString(cursor.getColumnIndexOrThrow(LocalDB.COLUMN_HIZKUNTZAKODE));

        cursor.close();
        db.close();

        return hizkuntzaKode;
    }

    public int changeLanguage(String lenguageKode){
        SQLiteDatabase db = localDB.getWritableDatabase(); // Idazteko moduan ireki datu-basea
        ContentValues values = new ContentValues();

        values.put(LocalDB.COLUMN_HIZKUNTZAKODE, lenguageKode);

        int eguneratutakoLerroKop = db.update(LocalDB.COLUMN_HIZKUNTZAKODE, values,
                LocalDB.COLUMN_ID + "=?", new String[]{String.valueOf(1)});
        db.close();

        return eguneratutakoLerroKop;
    }
}
