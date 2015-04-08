package cropmanager.calc.ipcm.cropcalc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ipcm.calc.cropmanager.R;

/*
Just shows the html file
 */

public class MainInfoFragment extends Fragment {

    private String curURL="file:///android_asset/main_info.html";
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_maininfo, container, false);

        if (curURL != null) {

            WebView webview = (WebView) view.findViewById(R.id.maininfo_webview);
            webview.getSettings().setJavaScriptEnabled(true);



            webview.loadUrl(curURL);

        }

        return view;


    }


}
