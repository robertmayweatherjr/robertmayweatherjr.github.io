package com.example.eventtracking_rob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EventTracking.db";

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Events table
    public static final String TABLE_EVENTS = "events";
    public static final String COL_EVENT_ID = "id";
    public static final String COL_EVENT_NAME = "eventName";
    public static final String COL_EVENT_DATE = "eventDate";
    public static final String COL_EVENT_CATEGORY = "category";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create users table
        db.execSQL(
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_USERNAME + " TEXT UNIQUE, " +
                        COL_PASSWORD + " TEXT)"
        );

        // Create events table
        db.execSQL(
                "CREATE TABLE " + TABLE_EVENTS + " (" +
                        COL_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_EVENT_NAME + " TEXT, " +
                        COL_EVENT_DATE + " TEXT, " +
                        COL_EVENT_CATEGORY + " TEXT)"
        );

        // Indexes for performance
        db.execSQL("CREATE INDEX idx_event_name ON " + TABLE_EVENTS + "(" + COL_EVENT_NAME + ")");
        db.execSQL("CREATE INDEX idx_event_date ON " + TABLE_EVENTS + "(" + COL_EVENT_DATE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }


    // User Functions
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE username=? AND password=?",
                new String[]{username, password}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE username=?",
                new String[]{username}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    // Event Functions
    public boolean insertEvent(String name, String date, String category) {

        if (name == null || name.trim().isEmpty()) return false;
        if (date == null || date.trim().isEmpty()) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EVENT_NAME, name);
        cv.put(COL_EVENT_DATE, date);
        cv.put(COL_EVENT_CATEGORY, category);

        long result = db.insert(TABLE_EVENTS, null, cv);
        return result != -1;
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
    }

    public boolean updateEvent(int id, String name, String date, String category) {

        if (name == null || name.trim().isEmpty()) return false;
        if (date == null || date.trim().isEmpty()) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EVENT_NAME, name);
        cv.put(COL_EVENT_DATE, date);
        cv.put(COL_EVENT_CATEGORY, category);

        int result = db.update(TABLE_EVENTS, cv, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_EVENTS, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
