package app.statzapps.quotebook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import app.statzapps.quotebook.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class QuoteList extends Activity {
	// Instance variables
	private ArrayList<Quote> quotes;
	private ListView listView;
	int resID = R.layout.quoteitem;
	long sId = 0;
	Context context = this;
	final DataHelper dataHelper = new DataHelper(this);
	
	// Updates the list of quotes to be displayed.
	public ArrayList<Quote> updateList()
    {
	    dataHelper.openForRead();
		quotes = new ArrayList<Quote>();					
		Cursor allCursor = dataHelper.getAllQuotes();			
		int count = allCursor.getCount();
		for(int i = 0; i < count; i++)
		{
			quotes.add(dataHelper.getQuote(allCursor, i));			
		}		
		return quotes;		
    }
	
	// Called by default when the menu button is pressed.
	// Informs the user that they can see options for any
	// quote by performing a long-click on that quote.
	public boolean onCreateOptionsMenu(Menu menu)
	{		
		 if(quotes.size()>0)
			 {MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.layout.menu, menu);
		    return true;
			 }
		 else
			 return false;
	}
	
	/*
	 * Launches a dialog when the user clicks the button to clear all quotes.
	 * Gives the user a yes/no option to delete all of their quotes.
	 */
	public void launchClearAllDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);		
		dialog.setTitle("Clear all quotes?");
		dialog.setMessage("Are you sure you want to delete all of your quotes?");
		dialog.setPositiveButton("Yes", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){
						dataHelper.openForRead();
		        		dataHelper.removeAllQuotes();
		        		listView.setAdapter(new QuoteAdapter(context, resID, updateList()));			
		        		dataHelper.close();
		        		
		        		launchDialog();
					}
		});
		
		dialog.setNegativeButton("No", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){}
		});
		
		dialog.show();
	}
	
	/*
	 * Launches a dialog when the user elects to delete a single quote.
	 * Gives them a yes/no option to delete the quote.
	 */
	public void launchDeleteDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);		
		dialog.setTitle("Delete quote?");
		dialog.setMessage("Are you sure you want to delete this quote?");
		dialog.setPositiveButton("Yes", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){
						dataHelper.openForRead();						
						long id = quotes.get(((Long)sId).intValue()).getId();						
						dataHelper.removeQuote(id);						
						listView.setAdapter(new QuoteAdapter(context, resID, updateList()));						
						if(quotes.size()<=0)
						{
							launchDialog();
						}						
						dataHelper.close();
					}
		});
		
		dialog.setNegativeButton("No", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){}
		});
		
		dialog.show();
	}
	
	/*
	 * Launches a dialog that alerts the user that they don't have any quotes stored.
	 */
	public void launchDialog()
	{
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);		
			dialog.setTitle("Oh, no!");
			dialog.setMessage("You don't have any quotes!" + "\n" + "Add some!");
			dialog.setPositiveButton("Add quote", 
					new OnClickListener(){
						public void onClick(DialogInterface dialog, int arg1){
							Intent intent = new Intent(context, EnterNewQuote.class);
							startActivity(intent);}
			});
			
			dialog.setNegativeButton("Not now", 
					new OnClickListener(){
						public void onClick(DialogInterface dialog, int arg1){}
			});
			
			dialog.show();
	}
	
	/*
	 * Creates a context menu when a quote is long-clicked.
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);		
		AdapterView.AdapterContextMenuInfo info = 
	        (AdapterView.AdapterContextMenuInfo)menuInfo;		
		sId = info.position;		
		menu.setHeaderTitle("Options");
		menu.add("Edit quote");
		menu.add("Delete");		
	}
	
	// Performs the necessary actions when items in the context menu are clicked upon.
	public boolean onContextItemSelected(MenuItem item)
	{
		if(item.getTitle().equals("Delete"))
		{			
			launchDeleteDialog();
		}
		if(item.getTitle().equals("Edit quote"))
		{
			Intent intent = new Intent(this, UpdateQuote.class);
			long id = quotes.get(((Long)sId).intValue()).getId(); //Gets the rowID of the quote clicked on.
			intent.putExtra("rowId", id); // Passes the rowID as an intent so that the next activity can 
										  // fill the Edit Texts with the necessary values.
			startActivity(intent);
		}
		return false;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quotelist);		
		
		listView = (ListView)findViewById(R.id.list);	    
	    Button clearQuotes = (Button)findViewById(R.id.clearQuotes);
	    Button newQuote = (Button)findViewById(R.id.newQuote);	    
	    
	    listView.setAdapter(new QuoteAdapter(this, resID, updateList()));			
		dataHelper.close();
		
		if(quotes.size()<=0)
		{
			launchDialog();
		}
		
		clearQuotes.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){        		
        		if(quotes.size()>0)
        			launchClearAllDialog();        		
        	}
        });
		
		newQuote.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		Intent myIntent = new Intent(view.getContext(), EnterNewQuote.class);
                startActivityForResult(myIntent, 0);        		
        	}
        });
				
		registerForContextMenu(listView);			
		
	}
}