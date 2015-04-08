package npm.calc.ipcm.npmcalc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

public class Credits extends Activity {

	WebView webview;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        webview = (WebView) findViewById(R.id.credits_webview);
        
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(webview.getWindowToken(), 0);
        
        // Finds the WebView and loads the content from it
        webview.loadUrl("file:///android_asset/legume_help.html");
        
    }
    
    @Override
    public void onResume()
    {
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(webview.getWindowToken(), 0);
    	
    	super.onResume();
    }



}
