package cropmanager.calc.ipcm.cropcalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import ipcm.calc.cropmanager.R;



public class ForageMoisture extends Fragment {
	
	EditText input1;
	EditText input2;
	EditText input3;
	TextView result;
    //Button emailButton;
    RelativeLayout mainLayout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.layout_foragemoisture, container, false);
        //setContentView(R.layout.layout_foragemoisture);

        return view;
        
    }

    public void onResume(){
        super.onResume();

        mainLayout.setBackgroundColor(getResources().getColor(R.color.grey_ddd));
    }

    public void onStart(){
        super.onStart();
        initControl();
    }

    private void initControl() {
        // Get the instance variables
        input1 = (EditText)getView().findViewById(R.id.input1); // Negotiated price
        input2 = (EditText)getView().findViewById(R.id.input2); // In-field moisture
        input3 = (EditText)getView().findViewById(R.id.input3); // Negotiated moisture
        result = (TextView)getView().findViewById(R.id.result);

        //emailButton = (Button)getView().findViewById(R.id.email);
        mainLayout = (RelativeLayout)getView().findViewById(R.id.forage_main);

        mainLayout.setBackgroundColor(getResources().getColor(R.color.grey_ddd));

        // Set the attributes
        input1.setRawInputType(Configuration.KEYBOARD_12KEY);
        input2.setRawInputType(Configuration.KEYBOARD_12KEY);
        input3.setRawInputType(Configuration.KEYBOARD_12KEY);

        input1.addTextChangedListener( new CurrencyWholeFormatWatcher(input1, this));
        input2.addTextChangedListener( new PercentFormatWatcher( input2, this ) );
        input3.addTextChangedListener( new PercentFormatWatcher(input3, this));

    }





    
    public void calculate()
    {
    	double fp = 0;
    	double fm = 0;
    	double nm = 0;
    	double r = -1;
    	
    	if(!input1.getText().toString().equals(""))
    	{
    		fp = Integer.parseInt(input1.getText().toString());
    	}
    	if(!input2.getText().toString().equals(""))
    	{
    		fm = Integer.parseInt(input2.getText().toString());
    	}
    	if(!input3.getText().toString().equals(""))
    	{
    		nm = Integer.parseInt(input3.getText().toString());
    	}
    	
    	if( nm != 100  && fp != 0 && fm != 0){
    		r = (fp*(100-fm))/(100-nm);

			DecimalFormat twoPlaces = new DecimalFormat("#.##");
			r = Double.valueOf(twoPlaces.format(r));
    	}
    	else{
    		r = 0.0;
    	}
    	
    	if(r != -1)
		{
			String s = ((Double)r).toString();
			
			String sper = s.substring(s.indexOf("."));
			
			if(sper.length() == 2)
			{
				s += "0";
			}
			
			result.setText("$" + s);
		}
		else
		{
			result.setText("$0.00");
		}
    	
    	
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, new InfoForage())
//                        .commit();

                helpinfo("file:///android_asset/forage_help.html");
                return true;
            case R.id.action_Email:
                emailReport();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    Executed when the email button is clicked
     */
    private void emailReport(){

        if(result.getText().equals("$0.00")){
            Toast toast = Toast.makeText(getActivity(), "Perform a calculation first", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);								// Sets the intent to be an email intent
        intent.setType("plain/text");												// I don't know what this does but it's necessary
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });					// The email address to send to. We don't know who the user will want to send it to.
        intent.putExtra(Intent.EXTRA_SUBJECT, "Corn silage moisture report");	    // The subject line

        // Build the body text string
        String bodyText = "";
        bodyText += "Negotiated moisture: " + input3.getText() + "%\nNegotiated price: $" + input1.getText() + ".00/ton\n\n";
        bodyText += "In-field moisture: " + input2.getText() + "%\nCalculated price: " + result.getText() + "/ton\n\n";
        bodyText += "This email generated by Crop Management Calculators, an Android app by the University of Wisconsin-Madison's NPM program\n";
        bodyText += "http://ipcm.wisc.edu/apps/";

        intent.putExtra(Intent.EXTRA_TEXT, bodyText);			// The body text
        startActivity(Intent.createChooser(intent, ""));		// Starts the email activity, passing the given data with it

    }
    private void helpinfo(String curURL) {

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.Theme_Dialog));
        alert.setTitle("How to use the Silage Moisture Cost Adjuster");

        WebView wv = new WebView(getActivity());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(curURL);
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        Dialog d = alert.setView(wv).create();
        d.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());

        //lp.width = WindowManager.LayoutParams.FILL_PARENT;
        //lp.height = WindowManager.LayoutParams.FILL_PARENT;
        d.getWindow().setAttributes(lp);



    }





}