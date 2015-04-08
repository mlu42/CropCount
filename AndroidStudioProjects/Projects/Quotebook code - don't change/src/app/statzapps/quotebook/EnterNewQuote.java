package app.statzapps.quotebook;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterNewQuote extends Activity{
	//Instance variables
	final DataHelper dataHelper = new DataHelper(this);
	Context context = this;
	private static final int VOICE_RECOGNITION = 1234;
	ArrayList<String> speechResults = new ArrayList<String>();
	int last;
	EditText editText;
	
	
	
	/*
	 * Launches a dialog that notifies the user that they
	 * have not entered any text for the quote.  This is 
	 * necessary for the quote.  Therefore, they are only
	 * given one option, to go back and enter something.
	 */
	public void launchQuoteDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Invalid entry");
		dialog.setMessage("Nothing was entered for the quote text.");
		dialog.setPositiveButton("Back", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){}
		});
		
		dialog.show();
	}
	
	/*
	 * Launches a dialog that notifies the user that they have
	 * not entered anything for the name of the person who said
	 * the quote.  The user then has the option to either name
	 * the person as 'Unknown' or to go back and enter a name.
	 */
	public void launchNameDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Invalid entry");
		dialog.setMessage("Nothing was entered as the person's name.");
		dialog.setPositiveButton("Name 'Unknown'", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){						
						dataHelper.insertQuote("Unknown", 
								((EditText)findViewById(R.id.quote)).getText().toString());						
						Intent myIntent = new Intent(context, QuoteList.class);
		                startActivity(myIntent);						
					}
		});
		
		dialog.setNegativeButton("Back", 
				new OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1){}
		});
		
		dialog.show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == VOICE_RECOGNITION && resultCode == RESULT_OK)
		{
			ArrayList<String> results;
			results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			speechResults=results;
			switch(last){
			case 0: editText = (EditText)findViewById(R.id.name); break;
			case 1: editText = (EditText)findViewById(R.id.quote); break;			
			}
			setText(editText);
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void setText(EditText e)
	{
		String text = ((CharSequence)e.getText()).toString();
		if(e.getSelectionStart() == text.length())
		{
			e.setText(e.getText() +speechResults.get(0));
		}
		else
		{
			e.setText(text.substring(0, e.getSelectionStart()) + speechResults.get(0) + text.substring(e.getSelectionStart()));
		}
	}
	
	public void launchSpeechRecognition()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
		startActivityForResult(intent, VOICE_RECOGNITION);
	}
    
	/*
	 * The onCreate activity is launched by default when
	 * the activity is launched.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enternewquote);
		
		// Opens the database for the activity
		// The database is then closed at necessary times throughout the activity.
		dataHelper.openForWrite();
		
		// Instantiating views for use
		
	    Button quoteList = (Button)findViewById(R.id.mainMenu);		
		Button save = (Button)findViewById(R.id.save);
		final Button nameMic = (Button)findViewById(R.id.nameMic);
		final Button quoteMic = (Button)findViewById(R.id.quoteMic);
		final EditText quoteText = (EditText)findViewById(R.id.quote);
		final EditText name = (EditText)findViewById(R.id.name);
		
		/*
		 * Sets the onClick action of the quoteList button.
		 * When it is clicked, the QuoteList activity will be launched.
		 */
		quoteList.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), QuoteList.class);
				startActivity(myIntent);				
				dataHelper.close();
			}
		});
		
		
		
		
		/*
		 * Sets the onClick action of the save button.
		 * If either of the EditTexts are empty, the appropriate
		 * dialog will be launched, the most important being the
		 * quote text.  If not, a new quote will be inserted into
		 * the database using the values of the EditTexts.
		 */
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {				
				if(quoteText.getText().toString().equals(""))
				{
					launchQuoteDialog();
				}
				else
				{
					if(name.getText().toString().equals(""))
					{
						launchNameDialog();
					}
					else
					{					
						dataHelper.insertQuote(name.getText().toString(), 
								quoteText.getText().toString());
						
						Intent myIntent = new Intent(view.getContext(), QuoteList.class);
		                startActivity(myIntent);
					}
				}				
			}
		}
	);
		
		nameMic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				last = 0;
				launchSpeechRecognition();
			}
		}
		);
		
		quoteMic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				last = 1;
				launchSpeechRecognition();				
			}
		}
		);
}
}