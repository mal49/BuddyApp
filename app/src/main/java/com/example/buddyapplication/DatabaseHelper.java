package com.example.buddyapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "buddy.db";
    private static  int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUser = "CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT)";

        String createFriend = "CREATE TABLE friends (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, gender TEXT, phone TEXT, email TEXT, " +
                "addr1 TEXT, addr2 TEXT, addr3 TEXT, addr4 TEXT, state TEXT)";

        db.execSQL(createUser);
        db.execSQL(createFriend);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS friends");
        onCreate(db);
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM users WHERE username=?",
                new String[]{username});

        if (c.getCount() > 0) {
            c.close();
            return false;
        }
        c.close();

        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);

        long result = db.insert("users", null, cv);
        return result != -1;
    }

    public void insertDefaultUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users", null);

        if(c.getCount() == 0){
            ContentValues cv = new ContentValues();
            cv.put("username", "admin");
            cv.put("password", "1234");
            db.insert("users", null, cv);
        }
        c.close();
    }

    public boolean insertFriend(friend f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", f.name);
        cv.put("gender", f.gender);
        cv.put("phone", f.phone);
        cv.put("email", f.email);
        cv.put("addr1", f.addr1);
        cv.put("addr2", f.addr2);
        cv.put("addr3", f.addr3);
        cv.put("addr4", f.addr4);
        cv.put("state", f.state);

        long result = db.insert("friends", null, cv);
        return result != -1;
    }

    public ArrayList<friend> getAllFriend() {
        ArrayList<friend> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM friends", null);
        if (c.moveToFirst()) {
            do {
                friend f = new friend();
                f.id = c.getInt(0);
                f.name = c.getString(1);
                f.gender = c.getString(2);
                f.phone = c.getString(3);
                f.email = c.getString(4);
                f.addr1 = c.getString(5);
                f.addr2 = c.getString(6);
                f.addr3 = c.getString(7);
                f.addr4 = c.getString(8);
                f.state = c.getString(9);

                list.add(f);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean updateFriend(friend f){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", f.name);
        cv.put("gender", f.gender);
        cv.put("phone", f.phone);
        cv.put("email", f.email);
        cv.put("addr1", f.addr1);
        cv.put("addr2", f.addr2);
        cv.put("addr3", f.addr3);
        cv.put("addr4", f.addr4);
        cv.put("state", f.state);

        int result = db.update("friends", cv, "id=?", new String[]{String.valueOf(f.id)});
        return result > 0;
    }

    public boolean deleteFriend(friend f){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("friends", "id=?", new String[]{String.valueOf(f.id)});
        return result > 0;
    }

    public ArrayList<friend> searchFriends(String keyword){
        ArrayList<friend> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM friends WHERE name LIKE ? OR phone LIKE ?",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"}
        );

        if (c.moveToFirst()) {
            do {
                friend f = new friend();
                f.id = c.getInt(0);
                f.name = c.getString(1);
                f.gender = c.getString(2);
                f.phone = c.getString(3);
                f.email = c.getString(4);
                f.addr1 = c.getString(5);
                f.addr2 = c.getString(6);
                f.addr3 = c.getString(7);
                f.addr4 = c.getString(8);
                f.state = c.getString(9);

                list.add(f);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

}
