package com.example.nganth.restaurantapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nganth.restaurantapp.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class FavoritesTable {
    private SQLiteDatabase database;
    private static final String TABLE_NAME = "favorites";
    private static final String COL_ID = "id";
    private static final String COL_RES_ID = "res_id";
    private static final String COL_RES_NAME = "res_name";
    private static final String COL_RES_ADD = "res_add";
    private static final String COL_RES_IMG = "res_img";
    private static final String COL_RES_RATE = "res_rate";
    private static final String COL_USER_EMAIL = "user_email";
    public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME +" (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            COL_RES_ID + " TEXT, " +
            COL_RES_NAME + " TEXT, " +
            COL_RES_ADD + " TEXT, " +
            COL_RES_IMG + " TEXT, " +
            COL_RES_RATE + " REAL, " +
            COL_USER_EMAIL + " TEXT " +
            ");";


    public FavoritesTable(Context context) {
        database = new MySqliteOpenHelper(context).getWritableDatabase();
    }

    public List<Restaurant> find() {
        List<Restaurant> restaurants = new ArrayList<>();

        try {
            Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, COL_ID + " DESC");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(COL_RES_ID); // == 0;
                    String resId = cursor.getString(1);
                    String resName = cursor.getString(2);
                    String resAdd = cursor.getString(3);
                    String resImage = cursor.getString(4);
                    Float resRate = cursor.getFloat(5);
                    String userEmail = cursor.getString(6);
                    restaurants.add(new Restaurant(resId, resName, resAdd, resImage, userEmail, resRate));
                }
                cursor.close();
            }
        } catch (Exception ex) {

        }
        return restaurants;
    }

    public Restaurant find_where(String res_id) {

        try {
            Cursor cursor = database.query(TABLE_NAME, null, COL_RES_ID+"='"+res_id+"'", null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(COL_RES_ID); // == 0;
                    String resId = cursor.getString(1);
                    String resName = cursor.getString(2);
                    String resAdd = cursor.getString(3);
                    String resImage = cursor.getString(4);
                    Float resRate = cursor.getFloat(5);
                    String userEmail = cursor.getString(6);
                    return new Restaurant(resId, resName, resAdd, resImage, userEmail, resRate);
                }
                cursor.close();
            }
        } catch (Exception ex) {
            return null;
        }
        return  null;
    }

    public long insert(Restaurant restaurant) {
        try {
            if (restaurant == null) return -1;

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_RES_ID, restaurant.getResId());// thêm giá trị cho cột hoten
            contentValues.put(COL_RES_NAME, restaurant.getResName());// thêm giá trị cho cột hoten
            contentValues.put(COL_RES_ADD, restaurant.getResAddress());// thêm giá trị cho cột diachi
            contentValues.put(COL_RES_IMG, restaurant.getResImage());
            contentValues.put(COL_RES_RATE, restaurant.getResRate());
            contentValues.put(COL_USER_EMAIL, restaurant.getUserEmail());
            return database.insert(
                    TABLE_NAME, // tên bảng
                    null,
                    contentValues // dữ liệu của 1 dòng
            );
        } catch (Exception ex) {
        }
        return -1;

//        Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
    }

    public int delete(String id) {
        try {
            return database.delete(TABLE_NAME,
                    COL_RES_ID + " = '" + id+"'",
                    null
            );
        } catch (Exception ex) {
        }
        return 0;
    }
}
