package app.statzapps.grocerylist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class GroceryListItems extends Activity{
	private ArrayList<GroceryItem> items = new ArrayList<GroceryItem>();
	private DataHelper dataHelper = new DataHelper(this);
	String titl = "";
	String id = "";
	
	public ArrayList<GroceryItem> updateList()
    {
	    dataHelper.openForWrite();
		items = new ArrayList<GroceryItem>();					
		Cursor allItemsCursor = dataHelper.getAllItems(id);			
		int count = allItemsCursor.getCount();
		for(int intent = 0; intent < count; intent++)
		{
			items.add(dataHelper.getGroceryItem(allItemsCursor, intent));			
		}
		allItemsCursor.close();
		dataHelper.close();
		return items;		
    }
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.grocerylistoptionsmenu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.newItem : Intent i = new Intent(this, InitialItemChoiceList.class);
							startActivity(i); return true;
		}
		return false;
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grocerylist);
		
		Intent i = getIntent();
		titl = i.getStringExtra("t");
		id = i.getStringExtra("tName");
		
		TextView t = (TextView)findViewById(R.id.listtitle);
		
		
		t.setText(titl);
		
		ListView list = (ListView)findViewById(R.id.itemlist);
		int resID = R.layout.grocerylistitem;
		
		list.setAdapter(new GroceryListAdapter(this, resID, items));
			
	}

}
