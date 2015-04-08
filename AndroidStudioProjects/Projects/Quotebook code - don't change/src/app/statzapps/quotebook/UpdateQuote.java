package app.statzapps.quotebook;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateQuote extends Activity{
	
	final DataHelper dataHelper = new DataHelper(this);
	Context context = this;
	private static final int VOICE_RECOGNITION = 1234;
	ArrayList<String> speechResults = new ArrayList<String>();
	int last;
	EditText editText;
	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatequote);		
		
		Button mainMenu = (Button)findViewById(R.id.mainMenu);
		final Button update = (Button)findViewById(R.id.update);
		final EditText name = (EditText)findViewById(R.id.name);
		final EditText quoteText = (EditText)findViewById(R.id.quote);
		final Button nameMic = (Button)findViewById(R.id.nameMic);
		final Button quoteMic = (Button)findViewById(R.id.quoteMic);
		
		Intent intent = getIntent();		
		final long id = intent.getLongExtra("rowId", 0);
		
		dataHelper.openForRead();
		Cursor c = dataHelper.getCursorQuote(id);
		Quote q = dataHelper.convertToQuote(c);
		
		name.setText(q.getName());
		quoteText.setText(q.getQuote());
		
		mainMenu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), QuoteList.class);
				startActivity(myIntent);				
				dataHelper.close();
			}
		});		
		
		update.setOnClickListener(new View.OnClickListener() {
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
						dataHelper.updateQuote(id, name.getText().toString(), 
								quoteText.getText().toString());						
						Intent myIntent = new Intent(view.getContext(), QuoteList.class);
		                startActivity(myIntent);
					}
				}
			}
		});
		
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

