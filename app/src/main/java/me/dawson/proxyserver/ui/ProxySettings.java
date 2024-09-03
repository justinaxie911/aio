package me.dawson.proxyserver.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.elcris.coservers.tunnel.TunnelUtils;
import com.elcris.coservers.BuildConfig;
import com.elcris.coservers.R;
import com.elcris.coservers.MainActivity;


import java.util.Objects;


public class ProxySettings extends AppCompatActivity implements ServiceConnection,
        OnCheckedChangeListener {


    public static final String TAG = "ProxySettings";
    protected static final String KEY_PREFS = "proxy_pref";
    protected static final String KEY_ENABALE = "proxy_enable";
    private static final String Inters = "ca-app-pub-6741116462771721/7920275824";
    private static final int NOTIFICATION_ID = 20140701;
    private InterstitialAd interstitialAd;
    private IProxyControl proxyControl = null;
    private AdView adsBannerView;
    private TextView tvInfo;
    private CheckBox cbEnable;
    private ToggleButton cbEnable1, cbEnable2;
    private TextView ipproxy;
    private ImageView mButtonSet;
    private ImageView sinwifi, conwifi, wifimedio;


    //WIFI p2p
    public static final String TAG1 = "HotspotManager";
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private boolean isWifiP2pEnabled = false;
    private boolean isHotspotEnabled = false;
    private int connectedDeviceCount = 0;
    private EditText editText;
    private TextView MDevzIP;
    private ImageView buttonwifi;
    private WifiManager wifiManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_shared_2);

        MDevzIP = (TextView) findViewById(R.id.MDevzIP);
        TextView textView = findViewById(R.id.textViewLog);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView = findViewById(R.id.textViewDevices);
        textView.setMovementMethod(new ScrollingMovementMethod());


        conwifi = (ImageView) findViewById(R.id.conwifi);
        sinwifi = (ImageView) findViewById(R.id.sinwifi);
        wifimedio = (ImageView) findViewById(R.id.wifimedio);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ProxySettings.PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
            // After this point you wait for callback in
            // onRequestPermissionsResult(int, String[], int[]) overridden method
        }

        editText = findViewById(R.id.editPassword);
        String password = "QW0034SAX";

        mButtonSet = (ImageView) findViewById(R.id.atras);
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hostshare2 = new Intent(ProxySettings.this, MainActivity.class);
                startActivity(hostshare2);
                showInterstitial();


            }
        });
        checkNetwork();
        conwifi = (ImageView) findViewById(R.id.conwifi);
        sinwifi = (ImageView) findViewById(R.id.sinwifi);
        wifimedio = (ImageView) findViewById(R.id.wifimedio);








        if (initP2p()){

            wifimedio.setVisibility(View.VISIBLE);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                startActivityForResult(panelIntent, 545);
                comprobador();
            }
            wifimedio.setVisibility(View.GONE);
        }


        buttonwifi = findViewById(R.id.buttonwifi);
        buttonwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                    startActivityForResult(panelIntent, 545);
                }
               comprobador();
            }
        });




        adsBannerView = (AdView) findViewById(R.id.adBannerSecondView);
        if (!BuildConfig.DEBUG) {
            //adsBannerView.setAdUnitId(SocksHttpApp.ADS_UNITID_BANNER_SOBRE);
        }

        // carrega anúncio
        if (TunnelUtils.isNetworkOnline(this)) {

            adsBannerView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (adsBannerView != null) {
                        adsBannerView.setVisibility(View.VISIBLE);

                    }
                }
            });

            adsBannerView.loadAd(new AdRequest.Builder()
                    .build());
        }


        Button wifiTetherButton = (Button) findViewById(R.id.WiFiTetherButton);
        wifiTetherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHotspotSettings();
                //wifisettiengs();
            }
        });

        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartapp();
            }
        });

        tvInfo = (TextView) findViewById(R.id.tv_info);
        TextView ipproxy = (TextView) findViewById(R.id.ipproxy); // ipproxy
        ipproxy.setText(TunnelUtils.getLocalIpAddress()); //version
        //ipproxy.setText("" + getIPAddress(true) + "");

        cbEnable1 = (ToggleButton) findViewById(R.id.cb_enable1);

        cbEnable1.setOnCheckedChangeListener(this);

        Intent intent = new Intent(this, ProxyService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        showInterstitial();
    }



    public void buttonwifi(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
            startActivityForResult(panelIntent, 545);
        }
    }







    private void comprobador(){

        try {
            Thread.sleep(4000);
            if (initP2p()){
                wifimedio.setVisibility(View.VISIBLE);
            }else{
                wifimedio.setVisibility(View.GONE);
            }
        } catch (InterruptedException e) {
            // ignored
        }



    }


    //ACA EMPIEZA EL P2P


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG1, "Fine location permission is not granted!");
                    finish();
                }
                break;
        }
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean initP2p() {
        // Device capability definition check
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)) {
            Log.e(TAG1, "Wi-Fi Direct is not supported by this device.");
            return false;
        }
        // Hardware capability check
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager == null) {
            Log.e(TAG1, "Cannot get Wi-Fi system service.");
            return false;
        }
        if (!wifiManager.isP2pSupported()) {
            Log.e(TAG1, "Wi-Fi Direct is not supported by the hardware or Wi-Fi is off.");
            return false;
        }
        manager = (WifiP2pManager) getApplicationContext().getSystemService(WIFI_P2P_SERVICE);
        if (manager == null) {
            Log.e(TAG1, "Cannot get Wi-Fi Direct system service.");
            return false;
        }
        channel = manager.initialize(this, getMainLooper(), null);
        if (channel == null) {
            Log.e(TAG1, "Cannot initialize Wi-Fi Direct.");
            return false;
        }
        return true;
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onButtonStartTapped() {
        if (!isWifiP2pEnabled) {
            outputLog("error: cannot start hotspot. WifiP2p is not enabled\n");
            return;
        }

       // EditText editText = findViewById(R.id.editSSID);
        String ssid = "DIRECT-Wifi-CO Servers"; //+ editText.getText().toString();
        String password = "QW0034SAX";



        //TextView passred = findViewById(R.id.passred);
        //TextView namered = findViewById(R.id.namered);
        int band = WifiP2pConfig.GROUP_OWNER_BAND_AUTO;
        if (((RadioButton) findViewById(R.id.radioButton2G)).isChecked()) {
            band = WifiP2pConfig.GROUP_OWNER_BAND_2GHZ;
        } else if (((RadioButton) findViewById(R.id.radioButton5G)).isChecked()) {
            band = WifiP2pConfig.GROUP_OWNER_BAND_5GHZ;
        }

        WifiP2pConfig config = new WifiP2pConfig.Builder()
                .setNetworkName(ssid)
                .setPassphrase(password)
                .enablePersistentMode(false)
                .setGroupOperatingBand(band)
                .build();

        int finalBand = band;

        manager.createGroup(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                updateProxy();
                TextView passred = findViewById(R.id.passred);
                TextView namered = findViewById(R.id.namered);
                namered.setText(ssid);
                passred.setText(password);
                outputLog("hotspot started\n");
                isHotspotEnabled = true;
                outputLog("------------------- Hotspot Info -------------------\n");
                outputLog("SSID: " + ssid + "\n");
                outputLog("Password: " + password + "\n");
                outputLog("Band: " + ((finalBand == WifiP2pConfig.GROUP_OWNER_BAND_2GHZ) ? "2.4" : "5") + "GHz\n");
                outputLog("-----------------------------------------------------------\n");
            }

            @Override
            public void onFailure(int i) {

                TextView passred = findViewById(R.id.passred);
                TextView namered = findViewById(R.id.namered);
                namered.setText(ssid);
                passred.setText(password);
                outputLog("hotspot started\n");
                isHotspotEnabled = true;
                outputLog("------------------- Hotspot Info -------------------\n");
                outputLog("SSID: " + ssid + "\n");
                outputLog("Password: " + password + "\n");
                outputLog("Band: " + ((finalBand == WifiP2pConfig.GROUP_OWNER_BAND_2GHZ) ? "2.4" : "5") + "GHz\n");
                outputLog("-----------------------------------------------------------\n");
                //outputLog("hotspot failed to start. reason: " + String.valueOf(i) + "\n");
            }
        });
    }

    public void onButtonStopTapped() {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onSuccess() {
                outputLog("hotspot stopped\n");
                isHotspotEnabled = false;
                connectedDeviceCount = 0;
                updateProxy();
                showInterstitial();
                TextView passred = findViewById(R.id.passred);
                TextView namered = findViewById(R.id.namered);
                namered.setText("Detenida");
                passred.setText("Detenida");
                TextView textViewDevices = findViewById(R.id.textViewDevices);
                textViewDevices.setText("");
            }

            @Override
            public void onFailure(int i) {
                outputLog("hotspot failed to stop. reason: " + String.valueOf(i) + "\n");
            }
        });
    }

    public void onButtonUpdateTapped(View view) {
        outputLog("updating connected device list...\n");
        updateConnectedDeviceList();
    }

    private void checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isConnected())
        {
            MDevzIP.setText(TunnelUtils.getLocalIpAddress());

        } else if (mMobile.isConnected()) {
            MDevzIP.setText(TunnelUtils.getLocalIpAddress());


        } else {
            MDevzIP.setText("NO CONNECTION");

        }
    }


    public void updateConnectedDeviceList() {
        if (!isHotspotEnabled) {
            return;
        }

        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                TextView textViewDevices = findViewById(R.id.textViewDevices);
                textViewDevices.setText("");
                int i = 0;
                for (WifiP2pDevice client : wifiP2pGroup.getClientList()) {
                    textViewDevices.append("  Dispositivo " + ++i + ":  " + client.deviceAddress + "\n");
                }
                if (i > connectedDeviceCount) {
                    outputLog("Dispositivo Conectado\n");
                    connectedDeviceCount = i;
                } else if (i < connectedDeviceCount) {
                    outputLog("Dispositivo Desconectado\n");
                    connectedDeviceCount = i;
                }
            }
        });
    }

    private void outputLog(String msg){
        TextView textViewLog = findViewById(R.id.textViewLog);
        textViewLog.append("  " + msg);
    }





// ACA TERMINA




















    /*public String getIPAddress(boolean useIPv4) {
        try {
            boolean isIPv4;
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        assert sAddr != null;
                        isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }*/

    private void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            loadAd();
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    public void restartapp() {
        Intent intent = new Intent(this, ProxySettings.class);
        int i = 123456;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + ((long) 2000), pendingIntent);
        System.runFinalizersOnExit(true);
        reiniciarwifi();
        //System.exit(0);
        //Process.killProcess(Process.myPid());
    }


    private void iniciadowifi() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.wifi_conectado, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        TextView textowifion = inflate.findViewById(R.id.textowifion);


        String ssid = "DIRECT-Wifi-CO Servers"; //+ editText.getText().toString();
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);




        TextView passred = inflate.findViewById(R.id.passred);
        TextView namered = inflate.findViewById(R.id.namered);


        String password = "QW0034SAX";
        ImageView ok = inflate.findViewById(R.id.hadsButton2);
        //ok.setText("Ver Anuncio");
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        namered.setText(ssid);
        passred.setText(password);
        textowifion.setText(Html.fromHtml("<p><font color='#ffffff'>Asegurese de tener El Wi-fi Encendido para que esta función trabaje correctamente.   " + "<br><br>" + "IP Proxy:  </font>" + "<font color='#467cf8'>" + TunnelUtils.getLocalIpAddress() + "</font>" + "<font color='#ffffff'> -         Puerto: </font> <font color='#FFEB3B'> 9999 </font></p>"));
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.NO_GRAVITY);
        alert.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    showInterstitial();
                    alert.dismiss();
                    //aca va el de pausar tiempo una vez se vea los anuncios

                }

            }
        });

        alert.show();

    }


    private void sinwifi() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.wifi_desconectado, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        ImageView ok = inflate.findViewById(R.id.hadsButton2);
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.NO_GRAVITY);
        alert.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    showInterstitial();
                    alert.dismiss();
                    //aca va el de pausar tiempo una vez se vea los anuncios

                }

            }
        });

        alert.show();

    }


    private void reiniciarwifi() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.wifi_reiniciar, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        ImageView ok = inflate.findViewById(R.id.hadsButton2);
        //ok.setText("Ver Anuncio");
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.NO_GRAVITY);
        alert.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    showInterstitial();
                    alert.dismiss();
                    //aca va el de pausar tiempo una vez se vea los anuncios

                }

            }
        });

        alert.show();

    }


    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, Inters, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                ProxySettings.this.interstitialAd = interstitialAd;
                //Log.i(TAG, "onAdLoaded");
                //Toast.makeText(SocksHttpMainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.

                                ProxySettings.this.interstitialAd = null;
                                //Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                loadAd();
                                ProxySettings.this.interstitialAd = null;
                                //Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                //Log.d("TAG", "The ad was shown.");
                            }
                        });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                //Log.i(TAG, loadAdError.getMessage());
                interstitialAd = null;

                String error =
                        String.format(
                                "domain: %s, code: %d, message: %s",
                                loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
               /* Toast.makeText(
                        SocksHttpMainActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                        .show();*/
            }
        });
    }


    private void launchHotspotSettings() {
        Intent tetherSettings = new Intent();
        tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
        startActivity(tetherSettings);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onServiceConnected(ComponentName cn, IBinder binder) {
        proxyControl = (IProxyControl) binder;
        if (proxyControl != null) {
            updateProxy();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName cn) {
        proxyControl = null;
    }

    @Override
    protected void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sp = getSharedPreferences(KEY_PREFS, MODE_PRIVATE);
        sp.edit().putBoolean(KEY_ENABALE, isChecked).apply();
        updateProxy();
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void updateProxy() {


        if (proxyControl == null) {
            return;
        }

        boolean isRunning = false;
        try {
            isRunning = proxyControl.isRunning();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        boolean shouldRun = getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
                .getBoolean(KEY_ENABALE, false);
        if (shouldRun && !isRunning) {
            startProxy();
            onButtonStartTapped();
            iniciadowifi();


            showInterstitial();
        } else if (!shouldRun && isRunning) {
            stopProxy();
            onButtonStopTapped();
            showInterstitial();

        }

        try {
            isRunning = proxyControl.isRunning();
        } catch (RemoteException e) {
            e.printStackTrace();
        }




            if (isRunning) {

            tvInfo.setText(R.string.proxy_on);
            cbEnable1.setChecked(true);


                if (initP2p()){
                    wifimedio.setVisibility(View.VISIBLE);

                }

                conwifi.setVisibility(View.VISIBLE);


            EditText editText = findViewById(R.id.editPassword);
            String password = "QW0034SAX";


            String ssid = "DIRECT-Wifi-CO Servers"; //+ editText.getText().toString();

            TextView passred = findViewById(R.id.passred);
            TextView namered = findViewById(R.id.namered);
            namered.setText(ssid);
            passred.setText(password);

        } else {
            tvInfo.setText(R.string.proxy_off);
            cbEnable1.setChecked(false);

                if (initP2p()){
                    wifimedio.setVisibility(View.VISIBLE);
                }else{
                    conwifi.setVisibility(View.GONE);
                }


            sinwifi();
            String ssid = "Desactivada"; //+ editText.getText().toString();
            String password = "Desactivada";//editText.getText().toString();
            TextView passred = findViewById(R.id.passred);
            TextView namered = findViewById(R.id.namered);
            namered.setText(ssid);
            passred.setText(password);

        }

    }

    private void startProxy() {
        boolean started = false;
        try {
            started = proxyControl.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (!started) {
            return;
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Context context = getApplicationContext();

        Notification notification = new Notification();
        notification.icon = R.drawable.ic_wifi_tethering_white_24dp;
        notification.tickerText = getResources().getString(R.string.proxy_on);
        notification.when = System.currentTimeMillis();

        CharSequence contentTitle = getResources().getString(R.string.app_name);

        CharSequence contentText = getResources().getString(
                R.string.service_text);
        Intent intent = new Intent(this, ProxySettings.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, PendingIntent.FLAG_IMMUTABLE,
                intent, PendingIntent.FLAG_IMMUTABLE);

        notificationManager(contentTitle, contentText, pendingIntent);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        manager.notify(NOTIFICATION_ID, notification);

        Toast.makeText(this, getResources().getString(R.string.proxy_started),
                Toast.LENGTH_SHORT).show();
    }

    private void notificationManager(CharSequence contentTitle, CharSequence contentText, PendingIntent pendingIntent) {
    }

    private void stopProxy() {
        boolean stopped = false;

        try {
            stopped = proxyControl.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (!stopped) {
            return;
        }

        tvInfo.setText(R.string.proxy_off);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
        Toast.makeText(this, getResources().getString(R.string.proxy_stopped),

                Toast.LENGTH_SHORT).show();
    }

    public void btnUpdate() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProxySettings.this);

        builder1.setTitle(Html.fromHtml("<h1><font color='#467cf8'>Atención!!</font><h1>"));
        builder1.setMessage(Html.fromHtml("<p><font color='#ffffff'>Asegurese de tener la Zona Wi-fi o Anclaje Encendido para que esta función trabaje correctamente.   " + "<br><br>" + "IP Proxy:  </font>" + "<font color='#467cf8'>" + TunnelUtils.getLocalIpAddress() + "</font>" + "<font color='#ffffff'> -         Puerto: </font> <font color='#FFEB3B'> 8080 </font></p>"));

        builder1.setCancelable(false);
        builder1.setPositiveButton("Aceptar", (dialog, which) -> {
        });
        AlertDialog Alert1 = builder1.create();
        Alert1.show();
        ((TextView) Objects.requireNonNull(Alert1.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());
    }












}

