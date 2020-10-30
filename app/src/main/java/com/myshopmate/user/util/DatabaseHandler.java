package com.myshopmate.user.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String CART_TABLE = "cart";
    //    public static final String COLUMN_ID = "product_id";
    public static final String VARIENT_ID = "varient_id";
    public static final String STORE_ID = "store_id";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_IMAGE = "product_image";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_REWARDS = "rewards";
    public static final String COLUMN_INCREAMENT = "increament";
    public static final String COLUMN_UNIT_VALUE = "unit_value";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "product_description";
    private static String DB_NAME = "demhynnjf";
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(" + VARIENT_ID + " INTEGER, "//primary key, "
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_UNIT_VALUE + " DOUBLE NOT NULL, "
                + COLUMN_REWARDS + " DOUBLE NOT NULL, "
                + COLUMN_INCREAMENT + " DOUBLE NOT NULL, "
                + COLUMN_STOCK + " DOUBLE NOT NULL, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + STORE_ID + " TEXT NOT NULL "
                + ")";

        db.execSQL(exe);

    }

    public boolean setCart(HashMap<String, String> map, Integer Qty) {
        db = getWritableDatabase();
        if (isInCart(map.get(VARIENT_ID),map.get(STORE_ID))) {
            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + Qty + "' where " + VARIENT_ID + "=" + map.get(VARIENT_ID) + " and " + STORE_ID + "=" + map.get(STORE_ID));
            //db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + Qty + "' where " + VARIENT_ID + "=" + map.get(VARIENT_ID));
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(VARIENT_ID, map.get(VARIENT_ID));
            values.put(STORE_ID, map.get(STORE_ID));
            values.put(COLUMN_QTY, Qty);
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_INCREAMENT, map.get(COLUMN_INCREAMENT));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
            values.put(COLUMN_REWARDS, map.get(COLUMN_REWARDS));
            values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
            values.put(COLUMN_STOCK, map.get(COLUMN_STOCK));
            values.put(COLUMN_TITLE, map.get(COLUMN_TITLE));
            values.put(COLUMN_DESCRIPTION, map.get(COLUMN_DESCRIPTION));
            db.insert(CART_TABLE, null, values);
            return true;
        }
    }

    public boolean isInCart(String variantID, String storeID) {
        db = getReadableDatabase();
        //String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + variantID;
        String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + variantID + " and " + STORE_ID + "=" + storeID;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0;
    }

    public String getCartItemQty(String id) {

        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));

    }

    public String getInCartItemQty(String variantID, String storeID) {
        if (isInCart(variantID,storeID)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + variantID + " and " + STORE_ID + "=" + storeID;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        } else {
            return "0.0";
        }
    }


    public String getInCartItemQtys(String variantID, String storeID) {
        if (isInCart(variantID,storeID)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + variantID + " and " + STORE_ID + "=" + storeID;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        } else {
            return "0";
        }
    }

    public int getCartCount() {
        int count = 0;
        boolean inExp = false;
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        try {
            count = cursor.getCount();
        } catch (Exception e) {
            inExp = true;
            e.printStackTrace();
        } finally {
            cursor.close();
            if (inExp) {
                db = getReadableDatabase();
                cursor = db.rawQuery(qry, null);
                count = cursor.getCount();
                cursor.close();
            }

        }
        return count;
    }

    public String getTotalAmount() {
        db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_QTY + " * " + COLUMN_PRICE + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_amount"));
        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }

    public List<String> getStoreIds() {
        List<String> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select "+ STORE_ID +" from " + CART_TABLE + " GROUP BY " + STORE_ID;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            list.add(cursor.getString(cursor.getColumnIndex(STORE_ID)));
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> getCartAll(String storeID) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " WHERE " + STORE_ID + "=" + storeID;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(VARIENT_ID, cursor.getString(cursor.getColumnIndex(VARIENT_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_REWARDS, cursor.getString(cursor.getColumnIndex(COLUMN_REWARDS)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_INCREAMENT, cursor.getString(cursor.getColumnIndex(COLUMN_INCREAMENT)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            map.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            map.put(STORE_ID, cursor.getString(cursor.getColumnIndex(STORE_ID)));
            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> getCartAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(VARIENT_ID, cursor.getString(cursor.getColumnIndex(VARIENT_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_REWARDS, cursor.getString(cursor.getColumnIndex(COLUMN_REWARDS)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_INCREAMENT, cursor.getString(cursor.getColumnIndex(COLUMN_INCREAMENT)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            map.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            map.put(STORE_ID, cursor.getString(cursor.getColumnIndex(STORE_ID)));
            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public String getColumnRewards() {
        db = getReadableDatabase();
        String qry = "SELECT rewards FROM " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String reward = cursor.getString(cursor.getColumnIndex("rewards"));
        if (reward != null) {

            return reward;
        } else {
            return "0";
        }
    }

    public String getFavConcatString() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String concate = "";
        for (int i = 0; i < cursor.getCount(); i++) {
            if (concate.equalsIgnoreCase("")) {
                concate = cursor.getString(cursor.getColumnIndex(VARIENT_ID));
            } else {
                concate = concate + "_" + cursor.getString(cursor.getColumnIndex(VARIENT_ID));
            }
            cursor.moveToNext();
        }
        return concate;
    }

    public void clearCart() {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE);
    }

    public void clearCart(String storeID) {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE + " WHERE " + STORE_ID + "=" + storeID);
    }

    public void removeItemFromCart(String variantID, String storeID) {
        db = getReadableDatabase();
        //db.execSQL("delete from " + CART_TABLE + " where " + VARIENT_ID + " = " + id);
        db.execSQL("delete from " + CART_TABLE + " where " + VARIENT_ID + " = " + variantID + " and " + STORE_ID + "=" + storeID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
