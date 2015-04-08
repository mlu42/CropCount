package npm.calc.ipcm.npmcalc;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main extends TabActivity {


    /*
     * Called when the activity is first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Empty the internal output file
        String filename_M = "EmailManure";
        String filename_L = "EmailLegume";
        String string = "";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename_M, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
            fos = openFileOutput(filename_L, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main); 			// Sets the layout to draw from main.xml in the res/layout directory

        Resources res = this.getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost(); // Gets the TabHost from the TabActivity
        tabHost.setup();				// The activity TabHost
        TabHost.TabSpec spec;  			// Resusable TabSpec for each tab
        Intent intent;  				// Reusable Intent for each tab



        // // Create an Intent to launch an Activity for the tab (to be reused)


        intent = new Intent().setClass(this, LegumeCalc.class);
        spec = tabHost.newTabSpec("Legume");
        spec.setIndicator("Legume Credit", res.getDrawable(R.drawable.ic_action_email));
        spec.setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ManureCalc.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("Manure");            // Sets the "tag" used to keep track of this tab
        spec.setIndicator("Analyze Manure", res.getDrawable(R.drawable.ic_action_email));  // Sets the title and icon
        spec.setContent(intent);            // Sets the action to take when selected
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ManureAverageCalc.class);
        spec = tabHost.newTabSpec("AverageManure");
        spec.setIndicator("Average Manure", res.getDrawable(R.drawable.ic_action_email));
        //spec.setIndicator("Info");
        spec.setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Email.class);
        spec = tabHost.newTabSpec("Email");
        spec.setIndicator("EMAIL   INFO", res.getDrawable(R.drawable.ic_action_email));
        spec.setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0); // Sets the tab view to be focused on the calculator activity initially



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_settings1:
//                //emailReport();
//            return true;
//            case R.id.action_settings2:
//
//                //helpinfo("file:///android_asset/legume_help.html");
//
//             return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//
////        return super.onOptionsItemSelected(item);
//    }



}
