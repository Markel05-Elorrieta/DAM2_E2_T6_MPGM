package com.example.dam2_e2_t6_mpgm.localStorageDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "LocalDB.localdb";
    private static final int DB_VERSION = 1;

    public static final String TABLE_HIZKUNTZA = "hizkuntza";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HIZKUNTZAKODE = "hizkuntza";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_HIZKUNTZA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HIZKUNTZAKODE + " TEXT NOT NULL , + );";

    public LocalDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE); // Taula sortu
        Log.d("pruebas", "create table" + CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIZKUNTZA); // Taula zaharra ezabatu
        onCreate(db); // Taula berria sortu
    }
}
