package com.example.weather_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    // History Table
    private static final String TABLE_HISTORY = "history";
    private static final String COL_HISTORY_ID = "_id";
    private static final String COL_CITY = "city";
    private static final String COL_TEMP = "temperature";
    private static final String COL_DESC = "description";
    private static final String COL_DATE = "date";

    // Favorites Table
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COL_FAV_ID = "_id";
    private static final String COL_FAV_CITY = "city";

    public WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create History Table
        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COL_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CITY + " TEXT, " +
                COL_TEMP + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_DATE + " TEXT)";
        db.execSQL(createHistoryTable);

        // Create Favorites Table
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COL_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FAV_CITY + " TEXT UNIQUE)";
        db.execSQL(createFavoritesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // Add to History
    public void addHistory(String city, String temperature, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        values.put(COL_CITY, city);
        values.put(COL_TEMP, temperature);
        values.put(COL_DESC, description);
        values.put(COL_DATE, currentDate);

        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    // Get History
    public Cursor getHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COL_HISTORY_ID + " AS _id, " +
                COL_CITY + ", " + COL_TEMP + ", " +
                COL_DESC + ", " + COL_DATE +
                " FROM " + TABLE_HISTORY +
                " ORDER BY " + COL_HISTORY_ID + " DESC", null);
    }

    // Clear History
    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
        db.close();
    }

    // Add to Favorites
    public void addFavorite(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FAV_CITY, city);

        try {
            db.insertOrThrow(TABLE_FAVORITES, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    // Get Favorites
    public Cursor getFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COL_FAV_ID + " AS _id, " +
                COL_FAV_CITY + " AS city FROM " + TABLE_FAVORITES, null);
    }

    // Remove Favorite
    public void removeFavorite(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COL_FAV_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}