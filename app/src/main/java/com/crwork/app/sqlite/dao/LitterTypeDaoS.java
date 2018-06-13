package com.crwork.app.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crwork.app.domain.LitterTypeDomain;
import com.crwork.app.sqlite.CRDBH;

import java.util.ArrayList;

public class LitterTypeDaoS {
    private final static String TAG = "LitterTypeDaoS";
    private CRDBH mCRDBH;

    public LitterTypeDaoS(Context context) {
        mCRDBH = new CRDBH(context);

    }

    /**
     * insert litter type
     *
     * @param mLitterTypeDomain
     * @return
     */
    public boolean insertLitterType(LitterTypeDomain mLitterTypeDomain) {
        Log.i(TAG, "----insert----1");
        SQLiteDatabase db = mCRDBH.getWritableDatabase();
        Log.i(TAG,
                "----insert----2" + "ID:" + String.valueOf(mLitterTypeDomain.getID()) + " LittertypeID:"
                        + String.valueOf(mLitterTypeDomain.getLittertypeID()) + " TypeName:"
                        + String.valueOf(mLitterTypeDomain.getTypeName()) + " Typemark:"
                        + String.valueOf(mLitterTypeDomain.getTypemark() + " Price:")
                        + String.valueOf(mLitterTypeDomain.getPrice()) + "\n");
        ContentValues values = new ContentValues();
        values.put("littertypeID", mLitterTypeDomain.getLittertypeID());
        values.put("typeName", mLitterTypeDomain.getTypeName());
        values.put("typemark", mLitterTypeDomain.getTypemark());
        values.put("price", mLitterTypeDomain.getPrice());
        db.insert(mCRDBH.LITTERTYPETABLE, null, values);
        Log.i(TAG, "----insert----3");
        db.close();
        return true;
    }

    /**
     * get litter type
     *
     * @return
     */
    public ArrayList<LitterTypeDomain> getLitterTypes() {
        Log.i(TAG, "----query----1");
        SQLiteDatabase db = mCRDBH.getReadableDatabase();
        Cursor cursor;
        ArrayList<LitterTypeDomain> list = new ArrayList<LitterTypeDomain>();
        cursor = db.rawQuery("SELECT * FROM " + mCRDBH.LITTERTYPETABLE, null);
        Log.i(TAG, "----query----2" + String.valueOf(cursor));
        while (cursor.moveToNext()) {
            LitterTypeDomain mLitterTypeDomain = new LitterTypeDomain();
            mLitterTypeDomain.setID(cursor.getInt(cursor.getColumnIndex("_id")));
            mLitterTypeDomain.setLittertypeID(cursor.getInt(cursor.getColumnIndex("litterTypeID")));
            mLitterTypeDomain.setTypeName(cursor.getString(cursor.getColumnIndex("typeName")));
            mLitterTypeDomain.setTypemark(cursor.getInt(cursor.getColumnIndex("typemark")));
            mLitterTypeDomain.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            list.add(mLitterTypeDomain);
        }
        cursor.close();
        db.close();
        if (list.size() == 0) {
            System.out.print(TAG + "");
        }
        return list;
    }

    public double getPriceByLitterTypeID(int litterTypeID) {
        Double mPrice = 0.00;
        Log.i(TAG, "----query----1");
        SQLiteDatabase db = mCRDBH.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT price FROM " + mCRDBH.LITTERTYPETABLE + " where littertypeID=?",
                new String[]{String.valueOf(litterTypeID)});
        Log.i(TAG, "----query----2" + String.valueOf(cursor));
        while (cursor.moveToNext()) {
            mPrice = cursor.getDouble(cursor.getColumnIndex("price"));
            Log.i(TAG, "----query----3" + mPrice);
        }
        cursor.close();
        db.close();
        return mPrice;
    }
}
