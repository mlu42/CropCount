package app.statzapps.quotebook;

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
	private static final String DATABASE_NAME = "myDatabase.db";
	private static final String DATABASE_TABLE = "quotes";
	private static final int DATABASE_VERSION = 0;	

	//The name and column index of each column in the database
	public static final String KEY_ID = "_id";
	public static final int ID_COLUMN = 0;
	public static final String KEY_NAME = "name";
	public static final int NAME_COLUMN = 1;
	public static final String KEY_QUOTE = "quote";
	public static final int QUOTE_COLUMN = 2;

	//SQL statement to create a new database
	private static final String DATABASE_CREATE = 
		"create table " + DATABASE_TABLE + " (" +
		KEY_ID + " integer primary key autoincrement, " +
		KEY_NAME + " text not null, " +
		KEY_QUOTE + " text not null " +
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
	public DataHelper openForWrite() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
		return this;
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

	/*
	 * Inserts a quote into the database.
	 * @param n: the name of the person who said the quote.
	 * @param q: the quote.
	 */
	public long insertQuote(String n, String q)
	{
		ContentValues quote = new ContentValues();		
		quote.put(KEY_NAME, n);
		quote.put(KEY_QUOTE, q);		
		return db.insert(DATABASE_TABLE, null, quote);
	}
    
	/*
	 * Removes the selected quote from the database
	 * @param _rowIndex: the rowIndex of the quote to be removed.
	 */
	public void removeQuote(long _rowIndex)
	{
		db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null);
	}
	
	//Removes all of the quotes from the database.
	public void removeAllQuotes()
	{
		db.delete(DATABASE_TABLE, KEY_ID, null);
	}
    
	//Returns a cursor over the entire database.
	public Cursor getAllQuotes()
	{
		Cursor q = db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_NAME, KEY_QUOTE},
				null, null, null, null, null);		
		q.moveToFirst();		
		return q;
	}
    
	//Returns a cursor over the selected quote.
	//@param _rowIndex: the rowIndex of the quote to be returned.
	public Cursor getCursorQuote(long _rowIndex)
	{
		Cursor q = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_QUOTE},
				KEY_ID + "=" + _rowIndex, null, null, null, null);
		
		if(q != null)
		{
			q.moveToFirst();
		}
		
		return q;		
	}
	
	/*
	 * Returns a quote in the form of a Quote object from a Cursor.
	 * @param cursor: the cursor to use.
	 * @param pos: the position (rowIndex) of the quote to be returned.
	 */
	public Quote getQuote(Cursor cursor, int pos)
	{
		cursor.moveToPosition(pos);
		String name = cursor.getString(cursor.getColumnIndex("name"));
		String quote = cursor.getString(cursor.getColumnIndex("quote"));
		long id = cursor.getLong(cursor.getColumnIndex("_id"));		
		return new Quote(name, quote, id);
	}
	
	/*
	 * Returns a quote in the form of a Quote object from a cursor.
	 * @param c: the cursor to use.
	 * Precondition: the Cursor must consist of only one quote.
	 */
	public Quote convertToQuote(Cursor c)
	{
		String name = c.getString(NAME_COLUMN);
		String quote = c.getString(QUOTE_COLUMN);
		long id = c.getLong(ID_COLUMN);		
		return new Quote(name, quote, id);
	}
	
	/*
	 * Returns a quote in the form of a String from a cursor.
	 * @param c: the cursor to use.
	 * Precondition: the Cursor must consist of only one quote.
	 */
	public String quoteToString(Cursor c)
	{		
		return "Name: " + c.getString(NAME_COLUMN) + "\n" +
		"Quote: " + c.getString(QUOTE_COLUMN);
	}
	
	/*
	 * Updates the quote in the given row with the string values provided.
	 * @param _rowIndex: the quote to update/
	 * @param n: the new name of the quote.
	 * @param q: the new quote text of the quote.
	 */
	public boolean updateQuote(long _rowIndex, String n, String q)
	{
		ContentValues quote = new ContentValues();
		quote.put(KEY_NAME, n);
		quote.put(KEY_QUOTE, q);
		db.update(DATABASE_TABLE, quote, KEY_ID + "=" + _rowIndex, null);
		return true;
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
			_db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
			//Create a new one.
			onCreate(_db);
		}
	}
}