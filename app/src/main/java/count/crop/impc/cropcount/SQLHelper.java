package count.crop.impc.cropcount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import count.crop.impc.cropcount.CountEventTable.CountEvent;

/**
 * SQL helper to manage
 */

public class SQLHelper extends SQLiteOpenHelper {


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CountEvent.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Gallery.db";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CountEvent.TABLE_NAME + " (" +

                        CountEvent.COLUMN_NAME_URI + " TEXT," +
                        CountEvent.COLUMN_NAME_PERCENTAGE + " TEXT," +
                        CountEvent.COLUMN_NAME_COUNT + " INTEGER," +

                        CountEvent.COLUMN_NAME_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +


                        "CREATE INDEX " + CountEvent.COLUMN_NAME_INDEX +
                        " ON " + CountEvent.TABLE_NAME + " (" + CountEvent.COLUMN_NAME_URI + ")" +

                        " )";

        //`id` INTEGER NULL AUTO_INCREMENT DEFAULT NULL,

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }





    public void addToList(String uri, double percentage, int count) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        //values.put(CountEventTable.CountEvent.COLUMN_NAME_INDEX, index);
        values.put(CountEventTable.CountEvent.COLUMN_NAME_URI, uri);
        values.put(CountEventTable.CountEvent.COLUMN_NAME_PERCENTAGE, percentage);
        values.put(CountEventTable.CountEvent.COLUMN_NAME_COUNT, count);
        //values.put(CountEventTable.CountEvent.COLUMN_NAME_DATE, date.);


        SQLiteDatabase db = this.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(CountEventTable.CountEvent.TABLE_NAME,
                null,
                values);
    }

    public String[] getList() {

        String Table_Name = CountEvent.TABLE_NAME;

        String selectQuery = "SELECT  * FROM " + Table_Name;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] data = null;
        if (cursor.moveToFirst()) {
            do {
                // get  the  data into array,or class variable
            } while (cursor.moveToNext());
        }
        db.close();
        return data;


    }

    public boolean deleteProduct(String uri) {


        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(CountEvent.TABLE_NAME, CountEvent.COLUMN_NAME_URI + "=" + uri, null) > 0;
    }



}
