package com.poswal.serviceecample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by komal on 12-07-2017.
 */

public class MyDatabase extends SQLiteOpenHelper {
    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String mytable="create table gpstable(imei double,lat text,log text,datetime text,provider text,address text)";
        db.execSQL(mytable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String  upgradeteble="drop table if exits gpstable";
        db.execSQL(upgradeteble);
        onCreate(db);
    }
}
