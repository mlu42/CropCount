package app.statzapps.grocerylist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainList extends Activity{
	private ArrayList<GroceryList> lists;
	private int resID = R.layout.mainlistitem;
	final DataHelper dataHelper = new DataHelper(this);
	ListView listview;
	Context context = this;
	long _id = 0;
	Time timekeeper = new Time("America/Chicago");
	
	//private TextView emptyView; 
	
	public String parseTime(Long s)
	{
		String l = s.toString();
		String parsed = "";
		for(int i = 0; i < (l.length() - 1); i++)
		{
			String toParse = l.substring(i,i+1);
			parsed+=parseNumber(toParse);
		}
		return parsed;
				
	}
	
	public String parseNumber(String s)
	{
		if(s.equals("0"))
			return "a";
		else
			if(s.equals("1"))
				return "b";
			else
				if(s.equals("2"))
					return "c";
				else
					if(s.equals("3"))
						return "d";
					else
						if(s.equals("4"))
							return "e";
						else
							if(s.equals("5"))
								return "f";
							else
								if(s.equals("6"))
									return "g";
								else
									if(s.equals("7"))
										return "h";
									else
										if(s.equals("8"))
											return "intent";
										else
											if(s.equals("9"))
												return "j";
											else
												return "";
											
	}
	
	public ArrayList<GroceryList> updateList()
    {
	    dataHelper.openForRead();
		lists = new ArrayList<GroceryList>();					
		Cursor allListsCursor = dataHelper.getAllGroceryLists();			
		int count = allListsCursor.getCount();
		for(int i = 0; i < count; i++)
		{
			lists.add(dataHelper.getGroceryList(allListsCursor, i));			
		}		
		dataHelper.close();
		return lists;
		
    }
	
	// Called by default when the menu button is pressed.
	// Informs the user that they can see options for any
	// quote by performing a long-click on that quote.
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.mainlistoptionsmenu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.addList: launchAddDialog(); return true;
		case R.id.clearAll: dataHelper.openForRead(); 
							dataHelper.removeAllGroceryLists();
							listview.setAdapter(new MainListAdapter(context, resID, updateList()));
							dataHelper.close();
							return true;
		}
		return false;
	}
	
	public void launchAddDialog()
	{
		String titleDefault = "Groceries";
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setIcon(0);
		dialog.setTitle("Create New List");
		dialog.setMessage("Name your new list");
		final EditText name = new EditText(this);
		name.setText(titleDefault);
		dialog.setView(name);
		dialog.setPositiveButton("Save",new OnClickListener(){
			public void onClick(DialogInterface dialog, int arg1){
				dataHelper.openForWrite();
				String t = parseTime(System.currentTimeMillis());
				String n = name.getText().toString();
				Intent intent = new Intent(context, GroceryListItems.class);
        		intent.putExtra("t", name.getText().toString());
        		intent.putExtra("tName", t);
				dataHelper.insertGroceryList(n, t);				
        		listview.setAdapter(new MainListAdapter(context, resID, updateList()));			
        		
        		dataHelper.close();
        		startActivityForResult(intent, 0);
				}
			});
		dialog.show();
		
		/*Dialog dialog = new Dialog(this);
		//dialog.setTitle("Create New List");
		dialog.setContentView(R.layout.addlistdialog);
		dialog.show();*/
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);		
		AdapterView.AdapterContextMenuInfo info = 
	        (AdapterView.AdapterContextMenuInfo)menuInfo;		
		_id = info.position;		
		menu.setHeaderTitle("Options");
		menu.add("Go to...");
		menu.add("Delete");		
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		if(item.getTitle().equals("Delete"))
		{			
			launchDeleteDialog();
		}
		else
			if(item.getTitle().equals("Go to..."))
			{
				dataHelper.openForRead();
				Intent intent = new Intent(context, GroceryListItems.class);
        		intent.putExtra("t", dataHelper.getListTitle(_id));
        		intent.putExtra("tName",
        				dataHelper.getGroceryList(dataHelper.getAllGroceryLists(),
        						((Long)_id).intValue()).getTableName());
        		startActivityForResult(intent, 0);
        		dataHelper.close();
			}
		return false;
		
	}
	
	public void launchDeleteDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);		
		dialog.setTitle("Delete?");
		dialog.setMessage("Are you sure you want to delete this list?");
		dialog.setPositiveButton("Yes", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){
						dataHelper.openForWrite();						
						long id = lists.get(((Long)_id).intValue()).getID();						
						dataHelper.removeGroceryList(id);						
						listview.setAdapter(new MainListAdapter(context, resID, updateList()));					
						dataHelper.close();
					}
		});
		
		dialog.setNegativeButton("No", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){}
		});
		
		dialog.show();
	}
	
	private OnItemClickListener listClickedHandler = new OnItemClickListener() {
	    public void onItemClick(AdapterView parent, View v, int position, long id)
	    {
	    	dataHelper.openForRead();
	        Intent i = new Intent(context, GroceryListItems.class);
	        i.putExtra("t", dataHelper.getListTitle(id));
	        i.putExtra("tName", dataHelper.getGroceryList(dataHelper.getAllGroceryLists(),
        						((Long)id).intValue()).getTableName());
	        dataHelper.close();
	        startActivityForResult(i, 0);
	        
	        
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlist);		
		
	    listview  = (ListView)findViewById(R.id.list);
		
		updateList();
		
		listview.setAdapter(new MainListAdapter(this, resID, lists));
		listview.setOnItemClickListener(listClickedHandler);
		dataHelper.close();
		registerForContextMenu(listview);
		
		
		
	}

}
