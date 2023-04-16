package com.example.bluetoothapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class FragmentTwo extends Fragment {
    public FragmentTwo() { }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    WebView myWebView;
    private final Handler mHandler = new Handler();
    ProgressDialog progressDialog;

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fragment_two, container, false);

        myWebView = root.findViewById(R.id.Webview_webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setGeolocationEnabled(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        load_webview();

        progressDialog = new ProgressDialog(requireActivity(),R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ImageButton btn_location= root.findViewById(R.id.imageview_location);
        btn_location.setOnClickListener(v -> {
            load_webview();
        });

        mToastRunnable.run();

        return root;
    }

    private void load_webview() {
        SharedPreferences sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String lat = sh.getString("mLatitudeLabel", "0");
        String lon = sh.getString("mLongitudeLabel", "0");
        String my_location = "https://maps.google.com/maps?q="+lat+", "+lon+"&z=15&output=embed";
        String data_html   = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:white;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe src=\""+my_location+"\" width=\"100%\" height=\"400\" frameborder=\"0\" style=border:0\"></iframe> </body> </html> ";
        myWebView.loadDataWithBaseURL("http://vimeo.com", data_html, "text/html", "UTF-8", null);
    }

    private final Runnable mToastRunnable = new Runnable() {
        @Override
        public void run()
        {
            mHandler.postDelayed(this, 1000);
            SharedPreferences sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
            boolean is_updated   = sh.getBoolean("location_updated", false);
            if (progressDialog.isShowing()&&is_updated)
            {
                progressDialog.dismiss();
                SharedPreferences shl = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEditl = shl.edit();
                myEditl.putBoolean("location_updated", false);
                myEditl.apply();
                load_webview();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mToastRunnable);
    }
}


