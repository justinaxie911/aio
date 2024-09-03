package com.elcris.coservers;

import static com.elcris.coservers.R.id.webview01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


public class SpeedTestActivity extends Activity {
    WebView web;
    ProgressBar progressBar;

    private Toolbar toolbar;
    private ImageView mButtonSet;


    //SIN INTERNET
    private Button reload;
    RelativeLayout successlayout;
    LinearLayout nointernetlayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedtest);


        mButtonSet = (ImageView) findViewById(R.id.atras);
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hostshare2 = new Intent(SpeedTestActivity.this, MainActivity.class);
                startActivity(hostshare2);

            }
        });


        successlayout=findViewById(R.id.internetLayout);
        nointernetlayout=findViewById(R.id.nointernetLayout);
        reload=findViewById(R.id.reloadid);

        web = (WebView) findViewById(R.id.webview01);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);

        loadWebPage();


        // SIN INTERNET
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReconectWebSite();
            }
        });






    }

    private void ReconectWebSite() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);



        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            loadWebPage();

            return true;

        }


        @Override
        public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {

            nointernetlayout.setVisibility(View.VISIBLE);
            successlayout.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Sin Conexión a INTERNET, Active el internet e intente de nuevo " + description , Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // SIN INTERNET
    private void loadWebPage(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();


        if (info !=null){

            if(info.isConnected()){
               nointernetlayout.setVisibility(View.GONE);
               web.loadUrl("https://speedtest.net/es");
               successlayout.setVisibility(View.VISIBLE);
               //web.reload();

            }else {
            nointernetlayout.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);
            successlayout.setVisibility(View.GONE);
            }
        }else {
                nointernetlayout.setVisibility(View.VISIBLE);
                reload.setVisibility(View.GONE);
                successlayout.setVisibility(View.GONE);

        }


    }

    private void ReconectWebSite(View view) {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }

}
