package app.statzapps.grocerylist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MainListAdapter extends ArrayAdapter<GroceryList>{
	// Instance variables
	int resource;
	private ArrayList<GroceryList> lists;
	
	/*
	 * Constructs a MainListAdapter object.
	 * @param _context: the context in which the MainListAdapter will be used.
	 * @param _resource: the "R.layout.*" reference to the parent layout
	 * 					 that will be used for each item in the ListView.
	 * @param _lists: the data which will be binded to the ListView.
	 */
	public MainListAdapter(Context _context, int _resource, ArrayList<GroceryList> _lists)
	{
		super(_context, _resource, _lists);
		resource = _resource;
		lists = _lists;
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
		GroceryList list = lists.get(position);		
		if(list != null){
			TextView name = (TextView)view.findViewById(R.id.name);
			if(name!=null)
				name.setText(list.getName());					
		}		
		return view;
	}

}
