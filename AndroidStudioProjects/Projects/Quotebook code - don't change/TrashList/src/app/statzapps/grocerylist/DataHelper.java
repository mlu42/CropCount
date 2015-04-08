package app.statzapps.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper {
	//Declaring constants for the database name, table name,
	//and the version of the database
	private static final String DATABASE_NAME = "groceries.db";
	private static final String GROCERY_LISTS = "grocerylists";
	private static final int DATABASE_VERSION = 1;
	
	//The name and column index of each column in the grocery list database
	public static final String KEY_ID = "id";
	public static final int ID_COLUMN = 0;
	public static final String KEY_NAME = "name";
	public static final int NAME_COLUMN = 1;
	public static final String KEY_TABLE = "tableName";
	public static final int TABLE_COLUMN = 2;
	
	//The name and column index of each column in the grocery item database (if not already created)
	public static final String KEY_ITEMNAME = "itemName";
	public static final int ITEMNAME_COLUMN = 1;
	
	//SQL statement to create a new database
	private static final String DATABASE_CREATE = 
		"create table " + GROCERY_LISTS + " (" +
		KEY_ID + " integer primary key autoincrement, " +
		KEY_NAME + " text not null, " +
		KEY_TABLE + " text not null " +
		");";
	
	//Variable to hold the database instance
	private SQLiteDatabase db;
	//Context of the application using the database
	private final Context context;
	//Database open/upgrade helper;
	private DBHelper dbHelper;
    
	// Constructor for a DataHelper
	// @param _context: the context in which the DataHelper will operate
	public DataHelper(Context _context)
	{
		context = _context;
		dbHelper = new DBHelper(context);
	}
	
	//The following two methods are interchangeable.
	//I just use them separately because it helps keep
	//things in order a little better.
	
	//Opens a DataHelper that is writable
	public void openForWrite() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
		//return this;
	}
	
	//Opens a DataHelper that is readable
	public DataHelper openForRead() throws SQLException
	{
		db = dbHelper.getReadableDatabase();
		return this;
	}
    
	//Closes the database.
	public void close()
	{
		db.close();
	}
	
	public long insertGroceryList(String name, String tableName)
	{
		ContentValues list = new ContentValues();		
		list.put(KEY_NAME, name);
		list.put(KEY_TABLE, tableName);
		createNewTable(tableName);
		return db.insert(GROCERY_LISTS, null, list);
	}
	
	public void createNewTable(String newTableName)
	{
		db.execSQL("create table " + newTableName + " (" +
		KEY_ID + " integer primary key autoincrement, " +
		KEY_ITEMNAME + " text not null " +
		");");
	}
	
	public Cursor getAllGroceryLists()
	{
		//Cursor c;
		//c = db.query(GROCERY_LISTS, new String[] {KEY_ID, KEY_NOM, KEY_TABLE}, null, null, null, null, null);
		return db.query(GROCERY_LISTS, new String[]{KEY_ID, KEY_NAME, KEY_TABLE}, null, null, null, null, null);
		//return c;{
	}
	
	public GroceryList getGroceryList(Cursor cursor, int pos)
	{
		cursor.moveToPosition(pos);
		String name = cursor.getString(cursor.getColumnIndex("name"));
		String table = cursor.getString(cursor.getColumnIndex("tableName"));
		long _id = cursor.getLong(cursor.getColumnIndex("id"));
		return new GroceryList(name, table, _id);
	}
	
	public void removeGroceryList(long _rowIndex)
	{
		
		db.delete(GROCERY_LISTS, KEY_ID + "=" + _rowIndex, null);
	}
	
	//Removes all of the quotes from the database.
	public void removeAllGroceryLists()
	{
		//db.execSQL("DROP TABLE String");
		//db.execSQL("DROP TABLE Cat");
		//db.execSQL("DROP TABLE Yarn");
		
		Cursor all = getAllGroceryLists();
		int pos = 0;
		for(int i = 0; i < all.getCount(); i++)
		{
			all.moveToPosition(pos);
			
			db.execSQL("DROP TABLE " + all.getString(all.getColumnIndex("tableName")));
			
			pos++;
			
			
		}
		db.delete(GROCERY_LISTS, KEY_ID, null);
	}
	
	public String getListTitle(long _rowIndex)
	{
		Cursor c = getAllGroceryLists();
		c.moveToPosition(((Long)_rowIndex).intValue());
		return c.getString(c.getColumnIndex(KEY_NAME));
	}
		
	public Cursor getAllItems(String tableName)
	{
		return db.query(tableName, new String[] {KEY_ID, KEY_ITEMNAME}, null, null, null, null, null);
	}
	
	public GroceryItem getGroceryItem(Cursor c, int pos)
	{
		c.moveToPosition(pos);
		return new GroceryItem(c.getString(c.getColumnIndex(KEY_ITEMNAME)));
	}
	
	private static class DBHelper extends SQLiteOpenHelper
	{
		// Constructor for a DBHelper
		// @param _context: the context in which the DataHelper will operate
		public DBHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		//Called when no database exists on disk and that helper class
		//needs to create a new one.
		@Override
		public void onCreate(SQLiteDatabase _db)
		{
			_db.execSQL(DATABASE_CREATE);
		}

		//Called when there is a database version mismatch meaning that the version
		//of the database on disk needs to be upgraded to the current version.
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
		{
			//Log the version upgrade
			Log.w("TaskDataHelper", "Upgrading from version " + _oldVersion 
					+ " to " +
					+ _newVersion + ", which will destroy all old data");

			//Upgrade the existing database to conform to the new version.
			//Multiple previous versions can be handled by comparing _oldVersion
			//and _newVersion values.
			_db.execSQL("DROP TABLE IF EXISTS" + GROCERY_LISTS);
			//Create a new one.
			onCreate(_db);
		}
	}

}

