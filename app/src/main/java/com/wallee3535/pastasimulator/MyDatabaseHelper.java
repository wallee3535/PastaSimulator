package com.wallee3535.pastasimulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Walter on 7/16/2016.
 * Singleton class provides an instance of SQLiteOpenHelper
 * Handles database querying and updating
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static MyDatabaseHelper instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final String TABLE_DISHES = "dishes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_UNIT = "unit";
    private static final String CREATE_DISHES =
            "CREATE TABLE " + TABLE_DISHES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TYPE + " STRING, " +
                    COLUMN_AMOUNT + " DOUBLE, " +
                    COLUMN_UNIT + " STRING " +
                    ");";
    private static final String DROP_DISHES = "DROP TABLE " + TABLE_DISHES + ";";

    private MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public static synchronized MyDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MyDatabaseHelper(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_DISHES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " STRING, " +
                COLUMN_AMOUNT + " DOUBLE, " +
                COLUMN_UNIT + " STRING " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISHES);
        onCreate(db);
    }

    /** Adds a dish to the database
     * @param dish dish to add
     */
    public void addDish(Dish dish) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, dish.getType());
        values.put(COLUMN_AMOUNT, dish.getAmount());
        values.put(COLUMN_UNIT, dish.getUnit());

        SQLiteDatabase db = getWritableDatabase();
        long lastInsertedRowId = db.insert(TABLE_DISHES, null, values);
        dish.setId(lastInsertedRowId);


        Log.i(Globals.TAG, "lastInsertedRowId: " + Long.toString(lastInsertedRowId));
        Log.i(Globals.TAG, "row count: " + getCount());

        db.close();
    }

    /** Updates dish fields in DISHES table
     * @param dish dish to update
     */
    public void updateDish(Dish dish) {
        /*//alternative way
        String query = "UPDATE " + TABLE_DISHES +
                " SET " + COLUMN_TYPE + " = \"" + dish.getType() + "\", " +
                COLUMN_AMOUNT + " = " + dish.getAmount() + ", " +
                COLUMN_UNIT + " = \"" + dish.getUnit() +
                "\" WHERE " + COLUMN_ID + " = " + dish.getId() + ";";
        Log.i(Globals.TAG, query);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
*/
        ContentValues values = new ContentValues();
        String table = TABLE_DISHES;
        values.put(COLUMN_TYPE, dish.getType());
        values.put(COLUMN_AMOUNT, dish.getAmount());
        values.put(COLUMN_UNIT, dish.getUnit());
        String whereClause = "id=?";
        String[] whereArgs = {Long.toString(dish.getId())};

        SQLiteDatabase db = getWritableDatabase();
        db.update(table, values, whereClause, whereArgs);
    }

    /** deletes a given dish from the database
     * @param dish dish to delete
     */
    public void deleteDish(Dish dish) {
        /*//alternative way
        String query = "DELETE FROM " + TABLE_DISHES + " WHERE " + COLUMN_ID + " = " + dish.getId();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        */
        String table = TABLE_DISHES;
        String whereClause = "id=?";
        String[] whereArgs = new String[]{Long.toString(dish.getId())};

        SQLiteDatabase db = getWritableDatabase();
        db.delete(table, whereClause, whereArgs);
        Log.i(Globals.TAG, "row count: " + getCount());
    }

    /** Get row count from DISHES table
     * @return number of rows in DISHES table
     */
    public int getCount() {
        String countQuery = "SELECT * FROM " + TABLE_DISHES;
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /** Resets DISHES table
     */
    public void resetTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DROP_DISHES);
        db.execSQL(CREATE_DISHES);
    }

    /** Returns arraylist of dish objects from DISHES table
     * @return dishes stored in DISHES table
     */
    public ArrayList<Dish> convertToArrayList() {
        ArrayList<Dish> result = new ArrayList<Dish>();
        String query = "SELECT * FROM " + TABLE_DISHES;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Long id = Long.parseLong(cursor.getString(0));
                String type = cursor.getString(1);
                double amount = Double.parseDouble(cursor.getString(2));
                String unit = cursor.getString(3);

                Dish dish = new Dish(id, type, amount, unit);
                result.add(dish);
            } while (cursor.moveToNext());
        }
        return result;
    }

}
