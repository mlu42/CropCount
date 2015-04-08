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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import ipcm.calc.cropmanager.R;





/*
The activity for the grain yield calculator
 */

public class GrainYield extends Fragment {

    private EditText plantsInput;
    private EditText numberRowsInput;
    private EditText kernelsInput;
    private EditText massInput;
    private TextView result;
    ScrollView scroller;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.layout_grainyield, container, false);
        //setContentView(R.layout.layout_grainyield);



        return view;

    }

    public void onStart(){
        super.onStart();
        initControl();
    }

    public void onResume(){
        super.onResume();

        // Needed to fix a bug that was happening with the color of the background
        scroller.setBackgroundColor(getResources().getColor(R.color.grey_ddd));

    }

    private void initControl() {

        // Get all necessary layout elements
        plantsInput = (EditText)getView().findViewById(R.id.plants_per_acre_input);
        numberRowsInput = (EditText)getView().findViewById(R.id.number_rows_input);
        kernelsInput = (EditText)getView().findViewById(R.id.kernels_per_row_input);
        massInput = (EditText)getView().findViewById(R.id.kernel_mass_input);
        result = (TextView)getView().findViewById(R.id.grainyield_result);
        scroller = (ScrollView)getView().findViewById(R.id.scroller);

        // Set up the text listeners and input types for the EditTexts
        plantsInput.addTextChangedListener( new CurrencyWholeFormatWatcher( plantsInput, this) );
        numberRowsInput.addTextChangedListener( new CurrencyWholeFormatWatcher( numberRowsInput, this) );
        kernelsInput.addTextChangedListener( new CurrencyWholeFormatWatcher( kernelsInput, this) );
        massInput.addTextChangedListener( new CurrencyWholeFormatWatcher( massInput, this) );

        // Ensures that we just get the number keyboard
        plantsInput.setRawInputType(Configuration.KEYBOARD_12KEY);
        numberRowsInput.setRawInputType(Configuration.KEYBOARD_12KEY);
        kernelsInput.setRawInputType(Configuration.KEYBOARD_12KEY);
        massInput.setRawInputType(Configuration.KEYBOARD_12KEY);

        scroller.setBackgroundColor(getResources().getColor(R.color.grey_ddd));

    }



    public void calculate(){

        // Can't do the calculation if any of the inputs are empty
        if(plantsInput.getText().equals("") || numberRowsInput.getText().equals("")
                || kernelsInput.getText().equals("") || massInput.getText().equals("")){
            result.setText("--");
            return;
        }

        int plants;
        int rows;
        int kernels;
        int mass;

        try{
            plants = Integer.parseInt(plantsInput.getText().toString());
            rows = Integer.parseInt(numberRowsInput.getText().toString());
            kernels = Integer.parseInt(kernelsInput.getText().toString());
            mass = Integer.parseInt(massInput.getText().toString());

            Double r = plants * rows * kernels * mass * (1000.0/(56.0*454000.0));

            result.setText(((Integer)r.intValue()).toString());
            return;
        }catch(Exception e){
            // A simple catch-all for any error. Probably not the cleanest solution
            result.setText("--");
            return;
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
//                        .replace(R.id.container, new InfoGrainYield())
//                        .commit();


                helpinfo("file:///android_asset/grainyield_help.html");



                return true;
            case R.id.action_Email:
                emailReport();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*
    Creates the email report and launches the default email activity
     */
    private void emailReport(){

        // We don't allow the user to send an email without having made a calculation
        if(result.getText().equals("--")){
            Toast toast = Toast.makeText(getActivity(), "Perform a calculation", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String body = "Plants per 1000th acre: " + plantsInput.getText().toString() + "\n";
        body += "Rows per ear: " + numberRowsInput.getText().toString() + "\n";
        body += "Kernels per row: " + kernelsInput.getText().toString() + "\n";
        body += "Kernel mass: " + massInput.getText().toString() + "\n\n";
        body += "Grain yield result: " + result.getText().toString() + " bu/A\n\n";
        body += "This email generated by Crop Management Calculators, an Android app by the University of Wisconsin-Madison's NPM program\n";
        body += "http://ipcm.wisc.edu/apps/";

        Intent intent = new Intent(Intent.ACTION_SEND);								// Sets the intent to be an email intent
        intent.setType("plain/text");												// I don't know what this does but it's necessary
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });					// The email address to send to. We don't know who the user will want to send it to.
        intent.putExtra(Intent.EXTRA_SUBJECT, "Grain yield report");    // The subject line

        intent.putExtra(Intent.EXTRA_TEXT, body);			// The body text
        startActivity(Intent.createChooser(intent, ""));		// Starts the email activity, passing the given data with it

    }

    private void helpinfo(String curURL) {




        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.Theme_Dialog));
        alert.setTitle("How to use Grain Yield Estimator");

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
