package com.example.waleed.phonebook.DBMS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PhoneBookDB extends SQLiteOpenHelper {
  public static String dbName="PhoneBook.db";
  public static int dbVersion=1;
  public static String userDataQuery="CREATE TABLE PBUser(Name TEXT,PhoneNo TEXT)";



    public PhoneBookDB(Context context) {
        super(context, dbName, null, dbVersion);
        Log.e("DBMS","DB is created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userDataQuery);
        Log.e("TABLE","table is created" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNewUser(SQLiteDatabase db,String name,String phoneNo){
        ContentValues cv=new ContentValues();
        cv.put("Name",name);
        cv.put("PhoneNo",phoneNo);
        db.insert("PBUser",null,cv);
    }

    public Cursor getAllUsers(SQLiteDatabase db){
        String[] projections={
                "Name",
                "PhoneNo"
        };
        Cursor mCursor=db.query("PBUser",projections,null,null,null,null,null);
        return mCursor;
    }
}
