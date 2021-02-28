package com.example.nganth.restaurantapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "restaurant_app";
    public MySqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {// gọi khi tạo file
        sqLiteDatabase.execSQL(FavoritesTable.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {// gọi khi thay đổi version

    }
}
