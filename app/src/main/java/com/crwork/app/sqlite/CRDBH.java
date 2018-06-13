package com.crwork.app.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CRDBH extends SQLiteOpenHelper {
    // DB name
    public static final String DBNAME = "crwork.db";
    // DB version number
    public static final int VERSION = 2;
    // data table
    public static final String LITTERTABLE = "crwork_litter";
    public static final String LITTERTYPETABLE = "crwork_littertype";
    public static final String USERTABLE = "crwork_user";
    public static final String CITYSTABLE = "crwork_citys";

    public CRDBH(Context context) {
        super(context, DBNAME, null, VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //db.execSQL("CREATE TABLE IF NOT EXISTS " + LITTERTABLE
        //        + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER, littertypeID INTEGER, weight REAL, litterdate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LITTERTYPETABLE
                + "(_id integer primary key autoincrement, littertypeID integer, typeName varchar, typemark integer, price double)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS " + USERTABLE
        //        + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER, userName TEXT, villageID INTEGER, userType INTEGER,registeredDate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CITYSTABLE
                + "(_id integer primary key autoincrement, parent_id integer,city_name_zh varchar,city_name_en varchar,city_level integer,city_code varchar,city_status_cr integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    /**
     * 数据库打开时，此方法会调用
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        super.onOpen(db);
    }

}
