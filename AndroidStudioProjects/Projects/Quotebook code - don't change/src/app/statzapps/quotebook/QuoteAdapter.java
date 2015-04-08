package app.statzapps.quotebook;

import java.util.ArrayList;

import app.statzapps.quotebook.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuoteAdapter extends ArrayAdapter<Quote>{	
	// Instance variables
	int resource;
	private ArrayList<Quote> quotes;
	
	/*
	 * Constructs a QuoteAdapter object.
	 * @param _context: the context in which the QuoteAdapter will be used.
	 * @param _resource: the "R.layout.*" reference to the parent layout
	 * 					 that will be used for each item in the ListView.
	 * @param _quotes: the data which will be binded to the ListView.
	 */
	public QuoteAdapter(Context _context, int _resource, ArrayList<Quote> _quotes)
	{
		super(_context, _resource, _quotes);
		resource = _resource;
		quotes = _quotes;
	}
	
	/*
	 * Used by default to create a ListView item for each item in the list of data.
	 * @return: the view to insert into the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;		
		if(view == null)
		{
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    view = vi.inflate(resource, null);
		}
		Quote quote = quotes.get(position);		
		if(quote != null){
			TextView name = (TextView)view.findViewById(R.id.name);
			if(name!=null)
				name.setText(quote.getName());			
			TextView quoteView = (TextView)view.findViewById(R.id.quote);
			if(quoteView!=null)
				quoteView.setText("\"" + quote.getQuote() + "\"");			
		}		
		return view;
	}
}