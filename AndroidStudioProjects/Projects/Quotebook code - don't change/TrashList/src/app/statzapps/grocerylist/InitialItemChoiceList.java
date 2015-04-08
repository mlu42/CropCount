package app.statzapps.grocerylist;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class InitialItemChoiceList extends ListActivity{
	private ArrayList<String> items = new ArrayList<String>();
	Context context = this;
	//private ListView list;
	//private View view;
	
	//private OnItemClickListener listClickedHandler = new OnItemClickListener() {
	    //public void onItemClick(AdapterView parent, View v, int position, long id)
	    //{        
	    	//view = list.getChildAt(((Long)id).intValue());
	    	//TextView text = (TextView)view.findViewById(R.id.choicename); // This line appears to be causing an exception.
	    	//text.setTextColor(Color.BLACK);
	    	
	    //}
	//};
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		String[] choices = getResources().getStringArray(R.array.items);
		
		for(int i = 0; i < choices.length; i++)
		{
			items.add(choices[i]);
		}		
		
		//list = (ListView)findViewById(R.id.itemchoicelist);
		setListAdapter(new InitialItemChoiceAdapter(context, R.layout.initialitemchoiceitem, items));
		//list.setOnItemClickListener(listClickedHandler);
	}

}
