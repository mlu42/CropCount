package app.statzapps.grocerylist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class InitialItemChoiceAdapter extends ArrayAdapter<String>{
	// Instance variables
	int resource;
	private ArrayList<String> choices;
	
	/*
	 * Constructs a MainListAdapter object.
	 * @param _context: the context in which the MainListAdapter will be used.
	 * @param _resource: the "R.layout.*" reference to the parent layout
	 * 					 that will be used for each item in the ListView.
	 * @param _lists: the data which will be binded to the ListView.
	 */
	public InitialItemChoiceAdapter(Context _context, int _resource, ArrayList<String> _choices)
	{
		super(_context, _resource, _choices);
		resource = _resource;
		choices = _choices;
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
		String choice = choices.get(position);		
		if(choice != null){
			TextView itemname = (TextView)view.findViewById(R.id.choicename);
			if(itemname!=null)
				itemname.setText(choice);					
		}
		//CheckBox check = (CheckBox)view.findViewById(R.id.add);
		//check.setChecked(false);
		return view;
	}
}
