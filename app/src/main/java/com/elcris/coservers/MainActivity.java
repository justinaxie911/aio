package com.elcris.coservers;

import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import com.elcris.coservers.logger.LogItem;
import com.elcris.coservers.config.Settings;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.textfield.TextInputLayout;
import com.elcris.coservers.R;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.elcris.coservers.fragments.ExitDialogFragment;
import android.widget.CompoundButton;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.content.Context;

import com.elcris.coservers.util.Utils;

import android.widget.TextView;
import androidx.core.view.GravityCompat;

import com.google.android.material.textfield.TextInputEditText;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.Button;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import com.elcris.coservers.activities.ConfigGeralActivity;
import android.view.LayoutInflater;
import android.text.Html;
import androidx.appcompat.app.AlertDialog;
import android.content.pm.PackageInfo;
import com.elcris.coservers.logger.SkStatus;

import android.widget.LinearLayout;
import android.os.Build;
import android.app.Activity;
import com.elcris.coservers.logger.ConnectionStatus;
import android.os.Handler;

import com.elcris.coservers.activities.ConfigExportFileActivity;
import com.elcris.coservers.activities.ConfigImportFileActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import com.elcris.coservers.fragments.CustomProxyDialogFragment;
import android.os.PersistableBundle;
import android.content.ClipboardManager;
import android.content.ClipData;
import com.elcris.coservers.util.ShareService;
import com.elcris.coservers.util.VPNUtils;
import android.content.res.Configuration;
import androidx.annotation.NonNull;

import com.elcris.coservers.config.ConfigParser;

import android.content.DialogInterface;
import com.elcris.coservers.tunnel.TunnelManagerHelper;
import com.elcris.coservers.activities.AboutActivity;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.elcris.coservers.activities.BaseActivity;
import com.elcris.coservers.tunnel.TunnelUtils;

import androidx.annotation.Nullable;

import com.elcris.coservers.view.PayloadGenerator;
import com.elcris.coservers.model.ExceptionHandler;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.elcris.coservers.adapter.LogsAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.elcris.coservers.fragments.CustomSNIDialogFragment;
import com.elcris.coservers.util.ToastUtil;
import com.elcris.coservers.fragments.ImportOnlineFragment;

import es.dmoral.toasty.Toasty;
import me.dawson.proxyserver.ui.ProxySettings;

public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener,
NavigationView.OnNavigationItemSelectedListener,
View.OnClickListener, SkStatus.StateListener, CompoundButton.OnCheckedChangeListener,
DialogInterface.OnClickListener {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String UPDATE_VIEWS = "MainUpdate";
    private static final String UPDATE_CLEAR = "ClearUpdate";
	public static final String OPEN_LOGS = "com.speedfusion.ssh:openLogs";
    private Settings mConfig;
	private Toolbar toolbar_main;
	private TextView nameapp;
	private Handler mHandler;
	private LinearLayout mainLayout;
	private Button starterButton;
	private LinearLayout configMsgLayout;
	private TextView configMsgText;
    private AdsManager adsManager;
	private static final String COMPLETA = "ca-app-pub-6741116462771721/7920275824";//inter
    private PayloadGenerator paygen;
	private static final String[] tabTitle = {"INICIO","REGISTRO","HERRAMIENTAS"};
	private LogsAdapter mLogAdapter;
	private RecyclerView logList;
	private ViewPager vp;
	private TabLayout tabs;
	private AdView adsBannerView;
    private MenuItem settings;
	private MenuItem ifolder;
    private MenuItem copylog;
    public static boolean isHomeTab = true;
	private LinearLayout tunnelLayout;
	private LinearLayout connectionCardview;
	private TextView tunnelInfo;
	private View payloadLayout;
	private View proxyLayout;
	private View sslLayout;
	private String proxyStr;

    private TextView proxyText;
	private TextInputEditText payloadEdit;
	private EditText payloadEditMrx;
	private TextView sniText, sniText1;
	private NavigationView drawerNavigationView;
	private TextView MDevzIP;
	private TextView MDevzOperator;
	private MenuItem auth;
    private LinearLayout ShareWifi, speedtest;
	private MenuItem settingsSSH;
    private ToastUtil aviso;
	private LinearLayout btntelegram;
	private AppCompatActivity mActivity;
	private InterstitialAd interstitialAd;
	private String PREFS_KEY = "todomrx";
	private EditText todo2, todo11, escondido3;
	private Button activity_starterButtonMain;
    private EditText InputAll, ProxyMRX, DNS1, DNS2;
	private TextView escondido2;
    private TextInputLayout layoutinput;
	private LinearLayout todossh, connection_cardView;
	private LinearLayout payloadLayoutMrx;
	private Calendar calendar;
	FloatingActionMenu actionMenu;
	private TextView dia;
	private TextView mes;
	private TextView anno;
    private CheckBox cbpayload, cbssl, cbslow, cbdns, cbwakelock, cbautoreplace;
    private boolean UsePayload = false ;
    private boolean UseSSL = false;
    private boolean UseSlow = false;
    private boolean OnDNS = false;
    private boolean UseWakeLock = false;
    private boolean UseAutoReplace;
    private String proxymrx = "";
    private String portmrx = "";
	View view;
	private Button buttonwifi;
	private WifiManager wifiManager;

	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




/*
		if(todo11 == null){



		}

		todo11 = findViewById(R.id.todo1);
		escondido2 = findViewById(R.id.escondido);
*/

		//payloadEditMrx = (EditText) findViewById(R.id.payloadEditMrx);
		//connection_cardView = (LinearLayout) findViewById(R.id.connection_cardView);
		todossh = (LinearLayout) findViewById(R.id.todossh);
		ProxyMRX = (EditText) findViewById(R.id.ProxyEditMrx);
		mHandler = new Handler();
		mConfig = new Settings(this);
        paygen = new PayloadGenerator(this);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this)); 
		// se primeira vez
		doLayout();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_VIEWS);
		filter.addAction(OPEN_LOGS);
		LocalBroadcastManager.getInstance(this).registerReceiver(mActivityReceiver, filter);

		aviso = new ToastUtil(this);
        doUpdateLayout();
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		btntelegram = (LinearLayout) findViewById(R.id.jointelegram);
		btntelegram.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				telegramjoin();


			}
		});
        //Toast.makeText(this, testtoast, Toast.LENGTH_SHORT).show();

		actionMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
		actionMenu.setClosedOnTouchOutside(true);
	}


	public static void hideKeyboardFrom(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}



	private void doLayout() {
		setContentView(R.layout.activity_main_drawer);
		toolbar_main = (Toolbar) findViewById(R.id.toolbar_main1);
		doDrawerMain(toolbar_main);
		setSupportActionBar(toolbar_main);
		doTabs();

		todossh = (LinearLayout) findViewById(R.id.todossh);
		//connection_cardView = (LinearLayout) findViewById(R.id.connection_cardView);
        cbpayload = (CheckBox) findViewById(R.id.cbusepayload);
        cbpayload.setOnCheckedChangeListener(this);
        cbssl = (CheckBox) findViewById(R.id.cbusessl);
        cbssl.setOnCheckedChangeListener(this);
        cbslow = (CheckBox) findViewById(R.id.cbslowdns);
        cbslow.setOnCheckedChangeListener(this);
        cbdns = (CheckBox) findViewById(R.id.cbenabledns);
        cbdns.setOnCheckedChangeListener(this);
        cbwakelock = (CheckBox) findViewById(R.id.cbwakelock);
        cbwakelock.setOnCheckedChangeListener(this);
        cbautoreplace = (CheckBox) findViewById(R.id.cbautoreplace);
        cbautoreplace.setOnCheckedChangeListener(this);
        setCheckBoxs(cbssl, cbpayload, cbslow, cbdns, cbwakelock, cbautoreplace);
		InputAll = (EditText) findViewById(R.id.inputall);
		DNS1 = (EditText) findViewById(R.id.dns1EditMrx);
		DNS2 = (EditText) findViewById(R.id.dns1EditMrx);



		ProxyMRX = (EditText) findViewById(R.id.ProxyEditMrx);
        layoutinput = (TextInputLayout) findViewById(R.id.input_layout_all);
        escondido2 = findViewById(R.id.escondido);
		/**SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
		String mUsername = ((EditText) findViewById(R.id.todo1)).getText().toString();
		edit.putString(Settings.USUARIO_KEY, mUsername);
		edit.apply();**/
		//todo2 = findViewById(R.id.todo1);
		//todo2.setText(getValue(getApplicationContext()));

		//escondido1 = (TextView) findViewById(R.id.escondido);
		cargarpreferencias();



		adsBannerView = findViewById(R.id.adBannerMainView);
		if (TunnelUtils.isNetworkOnline(MainActivity.this)) {
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


		// Operator View
		TelephonyManager telephonyManager = ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
		String simOperatorName = telephonyManager.getSimOperatorName();
		String RegionSim = telephonyManager.getSimCountryIso();
		MDevzOperator = (TextView) findViewById(R.id.MDevzOperator);
		MDevzOperator.setText(simOperatorName + "(" + RegionSim.toUpperCase() + ")");


		MDevzIP = (TextView) findViewById(R.id.MDevzIP);
		mainLayout = (LinearLayout) findViewById(R.id.activity_mainLinearLayout);
		starterButton = (Button) findViewById(R.id.activity_starterButtonMain);
    	starterButton.setOnClickListener(this);


		configMsgLayout = (LinearLayout) findViewById(R.id.activitymainCardView1);
		configMsgText = (TextView) findViewById(R.id.activity_mainMensagemConfigTextView);
		tunnelLayout = (LinearLayout) findViewById(R.id.tunnelCardView);
		tunnelLayout.setOnClickListener(this);
		tunnelInfo = (TextView) findViewById(R.id.activitymainTextView1);
		//connectionCardview = (LinearLayout) findViewById(R.id.connection_cardView);
	    proxyText = (TextView) findViewById(R.id.proxyText);
		payloadEdit = (TextInputEditText) findViewById(R.id.payloadEdit);
		payloadEditMrx = (EditText) findViewById(R.id.payloadEditMrx);
		
		payloadLayout = (View) findViewById(R.id.payloadLayout);
		proxyLayout = (View) findViewById(R.id.proxyLayout);
		proxyLayout.setOnClickListener(this);
		sslLayout = (View) findViewById(R.id.sslLayout);
		sslLayout.setOnClickListener(this);
		sniText = (TextView) findViewById(R.id.sslText);
		sniText1 = (TextView) findViewById(R.id.sslText1);
        ShareWifi = (LinearLayout) findViewById(R.id.share_wifi);

        ShareWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share_wifi = new Intent(MainActivity.this, ProxySettings.class);
                share_wifi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(share_wifi);
            }
        });

		speedtest = (LinearLayout) findViewById(R.id.speedtest);
		speedtest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent speed = new Intent(MainActivity.this, SpeedTestActivity.class);
				speed.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(speed);
			}
		});
	}
	
	private synchronized void doSaveData() {
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		SharedPreferences.Editor edit = prefs.edit();
		if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			if (payloadEdit != null && !prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
				if (mainLayout != null)
					mainLayout.requestFocus();
				edit.putString(Settings.CUSTOM_PAYLOAD_KEY, payloadEditMrx.getText().toString());
			}
		}
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		if (tunnelType == Settings.bTUNNEL_TYPE_SLOWDNS) {
			edit.putString(Settings.SERVIDOR_KEY, "127.0.0.1");
			edit.putString(Settings.SERVIDOR_PORTA_KEY, "8989");
			
		}
		edit.apply();
	}
	
	private void doUpdateLayout() {
        setCheckBoxs(cbssl, cbpayload, cbslow, cbdns, cbwakelock, cbautoreplace);
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		boolean isRunning = SkStatus.isTunnelActive();
		setStarterButton(starterButton, this);
		boolean protect = prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false);
        boolean protectppa = prefs.getBoolean(Settings.CONFIG_PROTEGER_PAYLOAD, false);
		boolean protectppr = prefs.getBoolean(Settings.CONFIG_PROTEGER_PROXY, false);
		boolean protectsn = prefs.getBoolean(Settings.CONFIG_PROTEGER_SNI, false);
		boolean protectse = prefs.getBoolean(Settings.CONFIG_PROTEGER_SERVER, false);
		boolean protectpo = prefs.getBoolean(Settings.CONFIG_PROTEGER_PORT, false);
		boolean protectus = prefs.getBoolean(Settings.CONFIG_PROTEGER_USUARIO, false);
		boolean protectserver = prefs.getBoolean(Settings.CONFIG_LINE_INPUT, false);
        
        if (protect) {
            InputAll.setEnabled(false);
            cbpayload.setEnabled(false);
            cbssl.setEnabled(false);
            cbslow.setEnabled(false);
        }





		String proxy = mConfig.getPrivString(Settings.PROXY_IP_KEY);
		
		int msgVisibility = View.GONE;
		String msgText = "";
		
		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {	
		   String msg = mConfig.getPrivString(Settings.CONFIG_MENSAGEM_KEY);
			if (!msg.isEmpty()) {
				msgText = msg.replace("\n", "<br/>");
				msgVisibility = View.VISIBLE;
			}

			if (mConfig.getPrivString(Settings.PROXY_IP_KEY).isEmpty() ||
				mConfig.getPrivString(Settings.PROXY_PORTA_KEY).isEmpty()) {
			}
		}
		configMsgText.setText(msgText.isEmpty() ? "" : Html.fromHtml(msgText));
		configMsgLayout.setVisibility(msgVisibility);
		
		if (mConfig.getPrivString("enable_auth").equals("_true")) {
			Menu menuNav = drawerNavigationView.getMenu();
			auth = menuNav.findItem(R.id.authentication);
			auth.setVisible(false);
		} else {
			Menu menuNav = drawerNavigationView.getMenu();
			auth = menuNav.findItem(R.id.authentication);
			auth.setVisible(false);
		}
		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			proxyText.setText("***************");
			todossh.setVisibility(View.GONE);
			proxyLayout.setEnabled(false);
		} else {
			proxyLayout.setEnabled(!isRunning);
			if (proxy.equals("")) {	
				proxyText.setText(R.string.squid);
			}  else {
			    proxyText.setText(String.format("%s:%s", proxy, mConfig.getPrivString(Settings.PROXY_PORTA_KEY)));
			}
		}
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		if (tunnelType == Settings.bTUNNEL_TYPE_SLOWDNS) {
			Menu menuNav = drawerNavigationView.getMenu();
			settingsSSH = menuNav.findItem(R.id.miSettingsSSH);
			settingsSSH.setTitle(R.string.slowdns_configuration);
		} else {
			Menu menuNav = drawerNavigationView.getMenu();
			settingsSSH = menuNav.findItem(R.id.miSettingsSSH);
			settingsSSH.setTitle(R.string.settings_ssh);
		}
        

		
		switch (tunnelType) {
			case Settings.bTUNNEL_TYPE_SSH_DIRECT:
				if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
                   // connectionCardview.setVisibility(View.VISIBLE);
                    tunnelInfo.setText(getString(R.string.direct) + getString(R.string.custom_payload1));

					if (protectse) {
						layoutinput.setVisibility(View.GONE);
						InputAll.setText("");
					} else{
						layoutinput.setVisibility(View.VISIBLE);
						cargarpreferencias();
					}

                    if (protectppa) {
                        payloadEdit.setEnabled(false);
                        payloadEdit.setText("******");

                    } else {

                        payloadEdit.setEnabled(!isRunning);
                        payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
                    }

                } else {
                    //connectionCardview.setVisibility(View.GONE);
                    tunnelInfo.setText(getString(R.string.direct));
                } 	
			break;
			
			case Settings.bTUNNEL_TYPE_SSH_PROXY:
				if (protectse) {
					layoutinput.setVisibility(View.GONE);
					InputAll.setText("");
				} else{
					layoutinput.setVisibility(View.VISIBLE);
					cargarpreferencias();
				}
				if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
                   // connectionCardview.setVisibility(View.VISIBLE);
                    tunnelInfo.setText(getString(R.string.http) + getString(R.string.custom_payload1));
                    proxyLayout.setVisibility(View.VISIBLE);



                    if (protectppa) {
                        payloadEdit.setEnabled(false);
                        payloadEdit.setText("******SNI1");
						//connection_cardView.setVisibility(View.GONE);
                    } else {
                        payloadEdit.setEnabled(!isRunning);
                        payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
                    }
                } else {
                   // connectionCardview.setVisibility(View.VISIBLE);
                    proxyLayout.setVisibility(View.VISIBLE);
                    payloadLayout.setVisibility(View.GONE);
                    tunnelInfo.setText(getString(R.string.http));
                } 	
			break;
			
			case Settings.bTUNNEL_TYPE_SSL_PROXY:
				if (protectse) {
					layoutinput.setVisibility(View.GONE);
					InputAll.setText("");
				} else{
					layoutinput.setVisibility(View.VISIBLE);
					cargarpreferencias();
				}
				if (protectsn) {
                    sslLayout.setEnabled(false);
                    sniText.setText("******SNI2");
					//connection_cardView.setVisibility(View.GONE);
					//sslLayout.isFocusable(true);
                } else {
                    sslLayout.setEnabled(!isRunning);
                    String ssl = mConfig.getPrivString(Settings.CUSTOM_SNI);
                    if (ssl.isEmpty()) {
                        sniText.setText("Ex. m.google.com");
                    } else {
                        sniText.setText(ssl);   
                    }
                }
               // connectionCardview.setVisibility(View.VISIBLE);
                payloadLayout.setVisibility(View.GONE);
                proxyLayout.setVisibility(View.GONE);
                sslLayout.setVisibility(View.VISIBLE);
                tunnelInfo.setText(getString(R.string.ssl));
			break;
            
            case Settings.bTUNNEL_TYPE_PAY_SSL:
				if (protectse) {
					layoutinput.setVisibility(View.GONE);
					InputAll.setText("");
				} else{
					layoutinput.setVisibility(View.VISIBLE);
					cargarpreferencias();
				}

                if (protectppa) {
                    payloadEdit.setEnabled(false);
                    payloadEdit.setText("Payload-Loked");
					//connection_cardView.setVisibility(View.GONE);
                } else {
					payloadEdit.setEnabled(!isRunning);
					payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
				}
				if (protectse) {
					layoutinput.setVisibility(View.GONE);
					InputAll.setText("");
				} else{
					layoutinput.setVisibility(View.VISIBLE);
					cargarpreferencias();
				}

				
				if (protectsn) {

					sslLayout.setEnabled(false);
					sniText.setText("SNI-Loked");
					//sslLayout.isFocusable(true);
					} else {
                    sslLayout.setEnabled(!isRunning);
                    String ssl = mConfig.getPrivString(Settings.CUSTOM_SNI);
                    if (ssl.isEmpty()) {
                        sniText.setText("Ex. m.google.com");
                    } else {
                        sniText.setText(ssl);   
                    }
                }
               // connectionCardview.setVisibility(View.VISIBLE);
                payloadLayout.setVisibility(View.VISIBLE);
                proxyLayout.setVisibility(View.GONE);
                sslLayout.setVisibility(View.VISIBLE);
                tunnelInfo.setText(getString(R.string.payssl));
            break;
            
            case Settings.bTUNNEL_TYPE_SSL_RP:
				if (protectse) {
					layoutinput.setVisibility(View.GONE);
					InputAll.setText("");
				} else{
					layoutinput.setVisibility(View.VISIBLE);
					cargarpreferencias();
				}
				if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
                   // connectionCardview.setVisibility(View.VISIBLE);
                    tunnelInfo.setText(getString(R.string.http) + getString(R.string.custom_payload1));
                    proxyLayout.setVisibility(View.VISIBLE);
                    if (protectppa) {
                        payloadEdit.setEnabled(false);
                        payloadEdit.setText("******");
						//connection_cardView.setVisibility(View.GONE);
                    } else {
                        payloadEdit.setEnabled(!isRunning);
                        payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
                    }

                }
				
				if (protectsn) {
                    sslLayout.setEnabled(false);
                    sniText.setText("******");
					//connection_cardView.setVisibility(View.GONE);
					//sslLayout.isFocusable(true);
                } else {
                    sslLayout.setEnabled(!isRunning);
                    String ssl = mConfig.getPrivString(Settings.CUSTOM_SNI);
                    if (ssl.isEmpty()) {
                        sniText.setText("Ex. m.google.com");
                    } else {
                        sniText.setText(ssl);   
                    }
                }
                //connectionCardview.setVisibility(View.VISIBLE);
                payloadLayout.setVisibility(View.VISIBLE);
                proxyLayout.setVisibility(View.VISIBLE);
                sslLayout.setVisibility(View.VISIBLE);
                tunnelInfo.setText(getString(R.string.sslhttp));
				break;
			
			case Settings.bTUNNEL_TYPE_SLOWDNS:
				//connectionCardview.setVisibility(View.GONE);
				tunnelInfo.setText(getString(R.string.slowdns));
				
			break;
		}

	
	}

	private void checkNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mWifi.isConnected())
		{
			MDevzIP.setText("IP Local : " + TunnelUtils.getLocalIpAddress());

		} else if (mMobile.isConnected()) {
			MDevzIP.setText("IP Local : " + TunnelUtils.getLocalIpAddress());


		} else {
			MDevzIP.setText("NO CONNECTION");

		}
	}
    
    private void setCheckBoxs(CheckBox cbssl, CheckBox cbpayload, CheckBox cbslow, CheckBox dns, CheckBox wakelock, CheckBox replace) {
        SharedPreferences prefs = mConfig.getPrefsPrivate();
        boolean isdns = mConfig.getVpnDnsForward();
        boolean iswakelock = mConfig.getWakelock();
        boolean isautoreplace = mConfig.getAutoReplace();
        int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
        
        switch (tunnelType) {
            case Settings.bTUNNEL_TYPE_SSH_DIRECT:
                cbssl.setChecked(false);
	    		cbslow.setChecked(false);
	    		cbpayload.setChecked(false);
			break;
			
			case Settings.bTUNNEL_TYPE_SSH_PROXY:
				cbssl.setChecked(false);
			    cbslow.setChecked(false);
		    	cbpayload.setChecked(true);
				break;

			case Settings.bTUNNEL_TYPE_SSL_PROXY:
				cbssl.setChecked(true);
		    	cbslow.setChecked(false);
		    	cbpayload.setChecked(false);			
				break;
                
            case Settings.bTUNNEL_TYPE_PAY_SSL:
                cbssl.setChecked(true);
		    	cbslow.setChecked(false);
		    	cbpayload.setChecked(true);
                break;
                
            case Settings.bTUNNEL_TYPE_SSL_RP:
                cbssl.setChecked(true);
		    	cbslow.setChecked(false);
		    	cbpayload.setChecked(true);
				break;    

			case Settings.bTUNNEL_TYPE_SLOWDNS:
				cbssl.setChecked(false);
		    	cbslow.setChecked(true);
		    	cbpayload.setChecked(false);		
				break;
        }
        
        if (isdns) {
            dns.setChecked(true);
        } else {
            dns.setChecked(false);
        }
        
        if (iswakelock) {
            wakelock.setChecked(true);
        } else {
            wakelock.setChecked(false);
        }
        
        if (isautoreplace) {
            replace.setChecked(true);
        } else {
            replace.setChecked(false);
        }
    }


	private void showInterstitial() {
		if (interstitialAd != null) {
			interstitialAd.show(this);
			//Toast.makeText(this, "cargando intersticial", Toast.LENGTH_SHORT).show();
		} else {
			loadAd();
			//Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
		}
	}


	private void loadAd() {
		AdRequest adRequest = new AdRequest.Builder().build();
		InterstitialAd.load(this, getString(R.string.interstitialad), adRequest, new InterstitialAdLoadCallback() {
			@Override
			public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
				// The mInterstitialAd reference will be null until
				// an ad is loaded.
				MainActivity.this.interstitialAd = interstitialAd;
				interstitialAd.setFullScreenContentCallback(
						new FullScreenContentCallback() {
							@Override
							public void onAdDismissedFullScreenContent() {
								// Called when fullscreen content is dismissed.
								// Make sure to set your reference to null so you don't
								// show it a second time.

								MainActivity.this.interstitialAd = null;
                                Toast.makeText(getApplicationContext(), "Gracias por apoyar la Aplicacion", Toast.LENGTH_SHORT).show();
								//Log.d("TAG", "The ad was dismissed.");
							}

							@Override
							public void onAdFailedToShowFullScreenContent(AdError adError) {
								// Called when fullscreen content failed to show.
								// Make sure to set your reference to null so you don't
								// show it a second time.
								loadAd();
								MainActivity.this.interstitialAd = null;
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
               /*Toast.makeText(
                        MainActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                        .show();*/
			}
		});
	}



	private void generator(){

		paygen.setCancelListener(getString(R.string.cancel), new PayloadGenerator.OnCancelListener(){

                @Override
                public void onCancelListener() {
                }
            });
        paygen.setGenerateListener(getString(R.string.gen), new PayloadGenerator.OnGenerateListener(){

                @Override
                public void onGenerate(String payloadGenerated) {
				SharedPreferences prefs = mConfig.getPrefsPrivate();
				if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)){
                    payloadEdit.setText(payloadGenerated);     
                } else {
                    Toast.makeText(MainActivity.this, R.string.custom_payload_msg, Toast.LENGTH_SHORT).show();
                }                                              
              }
            });
        paygen.show();
  }


	/**
	 * Tunnel SSH
	 */


	public void clickCloudconfig(View view){
		if (!SkStatus.isTunnelActive()) {
			DialogFragment dialog = new ImportOnlineFragment();
			dialog.show(getSupportFragmentManager(), "import");
		}
	}
	public void clickOpenconfig(View view){

		if (SkStatus.isTunnelActive()) {
			Toast.makeText(this, R.string.error_tunnel_service_execution,
					Toast.LENGTH_SHORT).show();
		} else {

			Intent intentImport = new Intent(this, ConfigImportFileActivity.class);
			startActivity(intentImport);
		}

	}
	public void clickSaveconfig(View view){

		SharedPreferences prefs = mConfig.getPrefsPrivate();
		if (SkStatus.isTunnelActive()) {

			Toast.makeText(this, R.string.error_tunnel_service_execution,
					Toast.LENGTH_SHORT).show();

		}
		else if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			Toast.makeText(this, R.string.locked_msg,
					Toast.LENGTH_SHORT).show();
		}
		else {
			Intent intentExport = new Intent(this, ConfigExportFileActivity.class);
			startActivity(intentExport);
		}
	}

	public void doTabs() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		//deleteLogs = (FloatingActionButton)findViewById(R.id.delete_log);
		mLogAdapter = new LogsAdapter(layoutManager,this);
		logList = (RecyclerView) findViewById(R.id.recyclerLog);
		logList.setAdapter(mLogAdapter);
		logList.setLayoutManager(layoutManager);
		mLogAdapter.scrollToLastPosition();
		vp = (ViewPager)findViewById(R.id.viewpager);
		tabs = (TabLayout)findViewById(R.id.tablayout);
		vp.setAdapter(new MyAdapter(Arrays.asList(tabTitle)));

		vp.setOffscreenPageLimit(2);
		tabs.setTabMode(TabLayout.MODE_FIXED);
		tabs.setTabGravity(TabLayout.GRAVITY_FILL);
		tabs.setupWithViewPager(vp);
        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
            {
                @Override
                public void onPageSelected(int position)
                {
                    if (position == 0) {
                        settings.setIcon(R.drawable.ic_settings);
						ifolder.setIcon(R.drawable.ic_config);
                        ifolder.setVisible(false);
						settings.setVisible(true);
                        isHomeTab = true;
                    } else if (position == 1) { 
						ifolder.setVisible(false);
						settings.setVisible(true);
                        settings.setIcon(R.drawable.ic_delete_forever_white_24dp);
                        isHomeTab = false;
                    } else if (position == 2) {
                        ifolder.setVisible(false);
						settings.setVisible(false);
						settings.setIcon(R.drawable.ic_delete_forever_white_24dp);
						isHomeTab = false;
                    }
                }
			});

	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO: Implement this method
			return 3;
		}

		@Override
		public boolean isViewFromObject(View p1, Object p2) {
			// TODO: Implement this method
			return p1 == p2;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int[] ids = new int[]{R.id.tab1, R.id.tab2, R.id.tab3};
			int id = 0;
			id = ids[position];
			// TODO: Implement this method
			return findViewById(id);
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			// TODO: Implement this method
			return titles.get(position);
		}

		private List<String> titles;
		public MyAdapter(List<String> str)
		{
			titles = str;
		}
	}
	
	public void startOrStopTunnel(Activity activity) {
		if (SkStatus.isTunnelActive()) {
			TunnelManagerHelper.stopSocksHttp(activity);
		}
		else {
			// oculta teclado se vísivel, tá com bug, tela verde
			//Utils.hideKeyboard(activity);

			Settings config = new Settings(activity);
			Intent intent = new Intent(activity, LaunchVpn.class);
			intent.setAction(Intent.ACTION_MAIN);
			if (config.getHideLog()) {
				intent.putExtra(LaunchVpn.EXTRA_HIDELOG, true);
			}
			activity.startActivity(intent);
		}
	}


	private void cargarpreferencias() {
        String hostportuserpass1 = mConfig.getPrivString(Settings.CONFIG_LINE_INPUT);
		InputAll.setText(hostportuserpass1);

	}



    private void guardarpreferencias(){
        String hostportuserpass = InputAll.getText().toString(); //edittext

		SharedPreferences.Editor mPrefs = mConfig.getPrefsPrivate().edit();
		mPrefs.putString(Settings.CONFIG_LINE_INPUT, hostportuserpass);
		mPrefs.commit();
        //InputAll.setText(hostportuserpass);

    }


	public void metodomrx() {
		//TextView textoplano = findViewById(R.id.escondido);


		SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
		String mString = InputAll.getText().toString();

		String[] parts = mString.split(":|:\\s|@\\s|@|\\s", 0);
		guardarpreferencias();

		if(parts.length > 3){

			String host = parts[0]; // 123
			String port = parts[1]; // 654321
			String user = parts[2]; // 654321
			String pass = parts[3]; // 654321
			edit.putString(Settings.SERVIDOR_KEY, host);
			edit.putString(Settings.SERVIDOR_PORTA_KEY, port);
			edit.putString(Settings.USUARIO_KEY, user);
			edit.putString(Settings.SENHA_KEY, pass);
			edit.apply();
		}

		else{
			//textoplano.setError("Datos incompletos");
			//Devuelve vacío
		}
	}




	public void saveValue(Context context, String text) {
		String mString = InputAll.getText().toString();
		SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
		SharedPreferences.Editor editor;
		editor = settings.edit();
		editor.putString("valorEditText", text);
		editor.commit();
	}

	public String getValuePreference(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);

		return  preferences.getString("valorEditText", "");
	}



	private void alertdialogpayload(ProgressDialog progress){
        progress.dismiss();
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View Customlayout = getLayoutInflater().inflate(R.layout.payloadlayout, null);
        builder.setView(Customlayout);
        EditText payload = (EditText) Customlayout.findViewById(R.id.payloadEditMrx);
		EditText ProxyMRX = (EditText) Customlayout.findViewById(R.id.ProxyEditMrx);
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		SharedPreferences.Editor edit = prefs.edit();
        alertDialog = builder.create();
        payload.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
        ProxyMRX.setText(mConfig.getLineProxy());
        Button btnAply = (Button)Customlayout.findViewById(R.id.hadsButton2);
        
        btnAply.setOnClickListener(
            new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setView(payload);
                payload.setSingleLine(false);
                payload.setInputType(InputType.TYPE_CLASS_TEXT);
                SharedPreferences prefs = mConfig.getPrefsPrivate();
                SharedPreferences.Editor edit2 = prefs.edit();
                String proxy = "";
                String puerto = "";
                String[] parts = ProxyMRX.getText().toString().split(":|:\\s", 0);
	        	if(parts.length > 1){

		        	proxy = parts[0]; // 123
		        	puerto = parts[1]; // 654321
	        	}else{
			        //Toast.makeText(this, "Proxy Vacio", Toast.LENGTH_SHORT).show();
	        	}
                mConfig.setLineProxy(ProxyMRX.getText().toString());
                edit2.putString(Settings.CUSTOM_PAYLOAD_KEY, payload.getText().toString());
                edit2.putString(Settings.PROXY_IP_KEY, proxy);
		    	edit2.putString(Settings.PROXY_PORTA_KEY, puerto);    
                edit2.apply();




                alertDialog.dismiss();
            }
        }
        );
        ImageView btnCancel = (ImageView)Customlayout.findViewById(R.id.hadsButton1);
        btnCancel.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                alertDialog.dismiss();

              }
            }
        );
        alertDialog.show();
    }

	private void alertdialogpayloadBloqued(){
		final AlertDialog alertDialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View Customlayout = getLayoutInflater().inflate(R.layout.payloadlayoutbloqued, null);
		builder.setView(Customlayout);
		alertDialog = builder.create();
		Button btnAply = (Button)Customlayout.findViewById(R.id.hadsButton2);
		btnAply.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				}
		);
		alertDialog.show();


		ImageView btndenny = (ImageView) Customlayout.findViewById(R.id.hadsButton1);
		btndenny.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				}
		);
		alertDialog.show();



	}



	public void verificapay(ProgressDialog dialog){


		SharedPreferences prefs = mConfig.getPrefsPrivate();
		boolean protectppa = prefs.getBoolean(Settings.CONFIG_PROTEGER_PAYLOAD, false);
		if (protectppa) {
			alertdialogpayloadBloqued();


		} else {

			alertdialogpayload(dialog);
		}




	}


	public void verificasni(){

		SharedPreferences prefs = mConfig.getPrefsPrivate();
		boolean protectsn = prefs.getBoolean(Settings.CONFIG_PROTEGER_SNI, false);
		if (protectsn) {

			alertdialogpayloadBloqued();

		} else {
			alertdialogsni();

		}

	}



	public void verificadns(){

		SharedPreferences prefs = mConfig.getPrefsPrivate();
		boolean protectsn = prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false);
		if (protectsn) {

			alertdialogpayloadBloqued();

		} else {
			midnscustom();

		}

	}

	public void verificaudp(){

		SharedPreferences prefs = mConfig.getPrefsPrivate();
		boolean protectsn = prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false);
		if (protectsn) {

			alertdialogpayloadBloqued();

		} else {
			udpgw();

		}

	}


    private void sslpayosslproxy() {
        SharedPreferences prefs = mConfig.getPrefsPrivate();
        SharedPreferences.Editor edit = prefs.edit();
        String payload_config = prefs.getString(Settings.CUSTOM_PAYLOAD_KEY, "");
        String sni_config = prefs.getString(Settings.CUSTOM_SNI, "");
        String proxy = prefs.getString(Settings.PROXY_IP_KEY, "");
        int tunnel_type = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
        String tunnel = String.valueOf(tunnel_type);
        
        if (tunnel.equals("5") && !payload_config.isEmpty() && !sni_config.isEmpty() && !proxy.isEmpty()) {
            edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSL_RP);
            edit.apply();
        } else if (tunnel.equals("6") && !payload_config.isEmpty() && !sni_config.isEmpty() && proxy.isEmpty()) {
            edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_PAY_SSL);
            edit.apply();
        } else if (tunnel.equals("2") && payload_config.isEmpty() && proxy.isEmpty()) {
            edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
            edit.apply();
        }
    }

	private void alertdialogsni(){
		final AlertDialog alertDialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View Customlayout = getLayoutInflater().inflate(R.layout.snilayout, null);
		builder.setView(Customlayout);
		EditText sni = (EditText) Customlayout.findViewById(R.id.SNIEditMrx);
		sni.setText(mConfig.getPrivString(Settings.CUSTOM_SNI));
		alertDialog = builder.create();

		ImageView btnCancel = (ImageView)Customlayout.findViewById(R.id.hadsButton1);
		btnCancel.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						alertDialog.dismiss();

					}
				}
		);




		Button btnAcept = (Button)Customlayout.findViewById(R.id.hadsButton2);

		btnAcept.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.setView(sni);
						sni.setSingleLine(false);
						sni.setInputType(InputType.TYPE_CLASS_TEXT);
						SharedPreferences prefs = mConfig.getPrefsPrivate();
						SharedPreferences.Editor edit = prefs.edit();
						edit.putString(Settings.CUSTOM_SNI, sni.getText().toString());
						edit.apply();


						//alertDialog.dismiss();
						alertDialog.dismiss();

					}
				}
		);
		alertDialog.show();

	}


	private void udpgw(){
		final AlertDialog alertDialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View Customlayout = getLayoutInflater().inflate(R.layout.layout_udpgw, null);
		builder.setView(Customlayout);
		EditText udpgw = (EditText) Customlayout.findViewById(R.id.UDPGWMrx);
		udpgw.setText(mConfig.getVpnUdpResolver());
		alertDialog = builder.create();

		ImageView btnCancel = (ImageView)Customlayout.findViewById(R.id.hadsButton1);
		btnCancel.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						alertDialog.dismiss();

					}
				}
		);




		Button btnAcept = (Button)Customlayout.findViewById(R.id.hadsButton2);

		btnAcept.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.setView(udpgw);
						udpgw.setSingleLine(false);
						udpgw.setInputType(InputType.TYPE_CLASS_TEXT);
                        mConfig.setVpnUdpResolver(udpgw.getText().toString());

						//alertDialog.dismiss();
						alertDialog.dismiss();

					}
				}
		);
		alertDialog.show();

	}

	private void midnscustom(){
		final AlertDialog alertDialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View DNSlayout = getLayoutInflater().inflate(R.layout.dns_layout, null);
		builder.setView(DNSlayout);



		EditText dns1 = (EditText) DNSlayout.findViewById(R.id.dns1EditMrx);
		EditText dns2 = (EditText) DNSlayout.findViewById(R.id.dns2EditMrx);
		dns1.setText(mConfig.getVpnDnsResolver1());
		dns2.setText(mConfig.getVpnDnsResolver2());
		alertDialog = builder.create();
		Button btnAply = (Button) DNSlayout.findViewById(R.id.hadsButton2);
		btnAply.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.setView(dns1);
						builder.setView(dns2);
						dns1.setSingleLine(true);
						dns2.setSingleLine(true);
						dns1.setInputType(InputType.TYPE_CLASS_TEXT);
						dns2.setInputType(InputType.TYPE_CLASS_TEXT);
						SharedPreferences prefs = mConfig.getPrefsPrivate();
						SharedPreferences.Editor edit = prefs.edit();
						mConfig.setVpnDnsResolver1(dns1.getText().toString());
			            mConfig.setVpnDnsResolver2(dns2.getText().toString());
						alertDialog.dismiss();
					}
				}
		);
		ImageView btnCancel = (ImageView) DNSlayout.findViewById(R.id.hadsButton1);
		btnCancel.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						alertDialog.dismiss();

					}
				}
		);
		alertDialog.show();
	}

	private void telegramjoin(){
		final AlertDialog alertDialog;
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.jointelegram_ly, null);
		Button btnCancel = (Button)v.findViewById(R.id.hadsButton2);
		builder.setView(v);
		alertDialog = builder.create();
		btnCancel.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();

					}
				}
		);
		alertDialog.show();

	}


	public void setStarterButton(Button starterButton, Activity activity) {
		String state = SkStatus.getLastState();
		boolean isRunning = SkStatus.isTunnelActive();
        SharedPreferences prefs = mConfig.getPrefsPrivate();
        //cargarpreferencias();

		if (starterButton != null) {
			int resId;
			SharedPreferences prefsPrivate = new Settings(activity).getPrefsPrivate();
			if (ConfigParser.isValidadeExpirou(prefsPrivate
											   .getLong(Settings.CONFIG_VALIDADE_KEY, 0))) {
				resId = R.string.expired;
				starterButton.setEnabled(false);
				if (isRunning) {
					startOrStopTunnel(activity);

				}
			}
            
            /**else if (prefsPrivate.getBoolean(Settings.CONFIG_BLOCK_ONLY_DATA, false) && ConfigParser.WifiCheckConnected(activity)) {
				resId = R.string.blocked;
				starterButton.setEnabled(false);
				Toasty.error(this, (R.string.block_only_data_movile), Toast.LENGTH_SHORT, true).show();
			}**/
			else if (prefsPrivate.getBoolean(Settings.BLOQUEAR_ROOT_KEY, false) &&
					 ConfigParser.isDeviceRooted(activity)) {
				resId = R.string.blocked;
				starterButton.setEnabled(false);

				Toast.makeText(activity, R.string.error_root_detected, Toast.LENGTH_SHORT)
					.show();

				if (isRunning) {
					startOrStopTunnel(activity);
				}
			}
			else if (SkStatus.SSH_STARTING.equals(state)) {
				resId = R.string.ssh_connecting;
				starterButton.setEnabled(true);
			}else if (SkStatus.SSH_CONNECTED.equals(state)) {
				showInterstitial();
                InputAll.setEnabled(false);
                cbpayload.setEnabled(false);
                cbssl.setEnabled(false);
                cbslow.setEnabled(false);
                cbwakelock.setEnabled(false);
                cbdns.setEnabled(false);
                cbautoreplace.setEnabled(false);
				resId = R.string.stop;
				starterButton.setEnabled(true);

			} else if (SkStatus.SSH_STOPPING.equals(state)) {
				resId = R.string.state_stopping;
				starterButton.setEnabled(false);
			}
			else {
				resId = isRunning ? R.string.stop : R.string.start;
				starterButton.setEnabled(true);
                InputAll.setEnabled(true);
                if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
                    InputAll.setEnabled(false);
                    cbpayload.setEnabled(false);
                    cbssl.setEnabled(false);
                    cbslow.setEnabled(false);
                } else {
                    InputAll.setEnabled(true);
                    cbpayload.setEnabled(true);
                    cbssl.setEnabled(true);
                    cbslow.setEnabled(true);
                    cbwakelock.setEnabled(true);
                    cbdns.setEnabled(true);
                    cbautoreplace.setEnabled(true);
               }
			}

			starterButton.setText(resId);
		}
	}


	/**
	 * Drawer Main
	 */

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle toggle;

	public void doDrawerMain(Toolbar toolbar) {
	    drawerNavigationView = (NavigationView) findViewById(R.id.drawerNavigationView);
		drawerNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.blanco)));
		drawerNavigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBackground));
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
		drawerNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.blanco)));

		// set drawer
		toggle = new ActionBarDrawerToggle(this,
										   drawerLayout, toolbar, R.string.open, R.string.cancel);

        drawerLayout.setDrawerListener(toggle);

		toggle.syncState();
        View view = drawerNavigationView.getHeaderView(0);

		// set app info
		PackageInfo pinfo = Utils.getAppInfo(this);
		if (pinfo != null) {
			String version_nome = pinfo.versionName;
			int version_code = pinfo.versionCode;
			String header_text = String.format("%s (%d)", version_nome, version_code);

			TextView app_info_text = view.findViewById(R.id.nav_headerAppVersion);
			app_info_text.setText(header_text);
		}
        
        TextView AutorNav = view.findViewById(R.id.autornav);
        String by = "<b><font color=\"#0088c1\">" + "Config By " +"</font></b>" + "<b><font color=\"#ffffff\">" + mConfig.getPrivString(Settings.CONFIG_AUTOR_KEY) + "</font></b>";
        AutorNav.setText(mConfig.getPrivString(Settings.CONFIG_AUTOR_KEY).isEmpty() ? getString(R.string.app_name) : Html.fromHtml(by));

		// set navigation view
		drawerNavigationView.setNavigationItemSelectedListener(this);
	}

	@Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (toggle != null)
			toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (toggle != null)
			toggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public void onClick(View p1) {
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		boolean isRunning = SkStatus.isTunnelActive();




		switch (p1.getId()) {


			case R.id.activity_starterButtonMain:
                if (prefs.getBoolean(Settings.CONFIG_BLOCK_ONLY_DATA, false) && ConfigParser.WifiCheckConnected(MainActivity.this)) {
                    Toasty.error(this, (R.string.block_only_data_movile), Toast.LENGTH_SHORT, true).show();
                } else {
                    if (!SkStatus.isTunnelActive()) {
                        //sslpayosslproxy();
                    }
			        	//doSaveData();
                        metodomrx();
			        	startOrStopTunnel(this);
                }
				break;
				
			case R.id.tunnelCardView:
				if (!isRunning) {
					if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			      	startActivity(new Intent(this, TunnelActivity.class));
				    }
				}
			   break;
			   
			 case R.id.proxyLayout:
				//doSaveData();
				 if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
					 if (!isRunning) {
					DialogFragment fragProxy = new CustomProxyDialogFragment();
					fragProxy.show(getSupportFragmentManager(), "proxyDialog");
					}
				}
			  break;
			  
			  case R.id.sslLayout:
				//doSaveData();
				if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
					if (!isRunning) {
						DialogFragment fragProxy = new CustomSNIDialogFragment();
						fragProxy.show(getSupportFragmentManager(), "sni");
					}
				}
			  break;
		}
	}
	
	@Override
	public void onClick(DialogInterface p1, int p2) {
		switch (p2) {
			/**case p1.BUTTON_POSITIVE:
            
				// tudo ok
				break;**/
		}
	}






	@Override
	public void updateState(final String state, String msg, int localizedResId, final ConnectionStatus level, Intent intent) {
		mHandler.post(new Runnable() {
				@Override
				public void run() {
					doUpdateLayout();
                    if (SkStatus.isTunnelActive()){
                        if (level.equals(ConnectionStatus.LEVEL_CONNECTED)){
                            showInterstitial();
                        }
                        if (level.equals(ConnectionStatus.LEVEL_NOTCONNECTED)){
                           // connectionStatus.setText(R.string.servicestop);
                            
                        }   

                        if (level.equals(ConnectionStatus.LEVEL_CONNECTING_SERVER_REPLIED)){
                            //connectionStatus.setText(R.string.authenticating);
                        }       

                        if (level.equals(ConnectionStatus.LEVEL_CONNECTING_NO_SERVER_REPLY_YET)){
                           // connectionStatus.setText(R.string.connecting);
                        }           
                        if (level.equals(ConnectionStatus.LEVEL_AUTH_FAILED)){
                           // connectionStatus.setText(R.string.authfailed);
                            Toast.makeText(MainActivity.this, "autenticacion fallida", Toast.LENGTH_SHORT).show();
                        }    
                            
                        if (level.equals(ConnectionStatus.UNKNOWN_LEVEL)){
                           // connectionStatus.setText(R.string.disconnected);
						   
						}
                        if (level.equals(ConnectionStatus.LEVEL_NONETWORK)){
                           // connectionStatus.setText(R.string.nonetwork);
                        }
                    }              

				}
			});

		switch (state) {
			case SkStatus.SSH_CONNECTED:
				mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (adsBannerView != null && TunnelUtils.isNetworkOnline(MainActivity.this)) {
								adsBannerView.setAdListener(new AdListener() {
									@Override
									public void onAdLoaded() {
										if (adsBannerView != null && !isFinishing()) {
											adsBannerView.setVisibility(View.VISIBLE);
										}
									}
								});

							}
						}
					}, 1000);

				break;
		}
	}


	/**
	 * Recebe locais Broadcast
	 */

	private BroadcastReceiver mActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String actionextra = intent.getStringExtra("clear");
            if (action == null)
                return;

            if (action.equals(UPDATE_VIEWS)) {
				doUpdateLayout();
                doDrawerMain(toolbar_main);
                //cargarpreferencias();
                if (actionextra.equals("si")) {
                    cargarpreferencias();
                }
			}
			else if (action.equals(OPEN_LOGS)) {
					if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
						drawerLayout.openDrawer(GravityCompat.END);
					}
				}
			}
    };


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
		settings = menu.findItem(R.id.miSettings);
		ifolder = menu.findItem(R.id.folder);
        return true;
		
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		
		if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }

		// Menu Itens
		switch (item.getItemId()) {

			case R.id.miLimparConfig:
				if (!SkStatus.isTunnelActive()) {

					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View inflate = inflater.inflate(R.layout.borrar_config, null);
					AlertDialog.Builder builer = new AlertDialog.Builder(this);
					builer.setView(inflate);


					Button ok = inflate.findViewById(R.id.hadsButton1);
					Button no = inflate.findViewById(R.id.hadsButton2);

					final AlertDialog alert = builer.create();
					alert.setCanceledOnTouchOutside(false);
					alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					alert.getWindow().setGravity(Gravity.CENTER);
					alert.show();


					no.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							alert.dismiss();
						}
					});
					ok.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							Settings.clearSettings(MainActivity.this);
							SkStatus.clearLog();
							InputAll.setText("");
							Intent i = new Intent(MainActivity.this, MainActivity.class);
							startActivity(i);
							finish();
							overridePendingTransition(0, 0);

						}});

					alert.show();
				}else{
					Toast.makeText(this, "Por favor detenga el servicio VPN primero..", Toast.LENGTH_SHORT)
							.show();
				}
				break;


			case R.id.miSettings:
				if (isHomeTab == true) {
                    Intent intentSettings = new Intent(this, ConfigGeralActivity.class);
                    //intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentSettings);
                } else {
                    mLogAdapter.clearLog();
				}

				break;








			case R.id.miSettingImportar:
				if (SkStatus.isTunnelActive()) {
					Toast.makeText(this, R.string.error_tunnel_service_execution,
								   Toast.LENGTH_SHORT).show();
				} else {

					Intent intentImport = new Intent(this, ConfigImportFileActivity.class);
					startActivity(intentImport);
				}
				break;

			case R.id.miSettingExportar:
				if (SkStatus.isTunnelActive()) {

					Toast.makeText(this, R.string.error_tunnel_service_execution,
								   Toast.LENGTH_SHORT).show();

				}
				else if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
					Toast.makeText(this, R.string.locked_msg,
								   Toast.LENGTH_SHORT).show();
				}
				else {
					Intent intentExport = new Intent(this, ConfigExportFileActivity.class);
					startActivity(intentExport);
				}
				break;
			case R.id.miLimparLogs:
				break;

			case R.id.miExit:
				if (Build.VERSION.SDK_INT >= 16) {
					finishAffinity();
				}
				System.exit(0);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
	    switch(id) {  
        
        
        case R.id.payload_generator:
                SharedPreferences prefs = mConfig.getPrefsPrivate();
                if (SkStatus.isTunnelActive()) {
                    Toast.makeText(this, R.string.error_tunnel_service_execution,
                                   Toast.LENGTH_SHORT).show();
                } else if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
                    Toast.makeText(this, R.string.payload_locked_msg,
                                   Toast.LENGTH_SHORT).show();
                } else {
                   generator();
                }          
            break;          
			case R.id.miPhoneConfg:
				if (Build.VERSION.SDK_INT >= 30) {
                    Intent in = new Intent(Intent.ACTION_MAIN);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.setClassName("com.android.phone", "com.android.phone.settings.RadioInfo");
                    this.startActivity(in);
                } else {
                    Intent inTen = new Intent(Intent.ACTION_MAIN);
                    inTen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    inTen.setClassName("com.android.settings", "com.android.settings.RadioInfo");
                    this.startActivity(inTen);
                }
				break;

			case R.id.miSettings:
				Intent intent = new Intent(MainActivity.this, ConfigGeralActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;


			case R.id.miPayload:
				//alertdialogpayload();
                ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Cargando");
                progress.show();
				verificapay(progress);
				break;



			case R.id.miSNI:
				verificasni();
				break;


			case R.id.miDNS:
				verificadns();
				break;

			case R.id.miUDPGW:
				verificaudp();
				break;




            case R.id.importonline:
                if (!SkStatus.isTunnelActive()) {
                    DialogFragment dialog = new ImportOnlineFragment();
                    dialog.show(getSupportFragmentManager(), "import");
                }
            break;





			case R.id.speedtest:
				Intent intent6 = new Intent(MainActivity.this, SpeedTestActivity.class);
				intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent6);

				break;






			case R.id.sharewifi:
				Intent i = new Intent(MainActivity.this, ProxySettings.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

				break;



            case R.id.hardware:




				harwardedid();


            break;    

			case R.id.miSettingsSSH:
			    SharedPreferences mPrefs = mConfig.getPrefsPrivate();
				int tunnelType = mPrefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
				
				if (tunnelType == Settings.bTUNNEL_TYPE_SLOWDNS) {
				Intent intent2 = new Intent(MainActivity.this, ConfigGeralActivity.class);
				intent2.setAction(ConfigGeralActivity.OPEN_SETTINGS_DNS);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent2);
				} else {
				Intent intent2 = new Intent(MainActivity.this, ConfigGeralActivity.class);
				intent2.setAction(ConfigGeralActivity.OPEN_SETTINGS_SSH);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent2);
				}
				break;

			case R.id.miAvaliarPlaystore:
				String url = "https://play.google.com/store/apps/details?id=com.elcris.coservers";
				Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent3, getText(R.string.open_with)));
				break;
				
			/*case R.id.speedteste:
				String url1 = "http://velocidade.algartelecom.com.br/";
				Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
				intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent4, getText(R.string.open_with)));
				break;
				
            case R.id.updateApp:
				updateApp(true);
				break;*/
				
			case R.id.tele_group:
				String url2 = "https://t.me/elcrischat";
				Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
				intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent5, getText(R.string.open_with)));
				break;




			case R.id.compartirapp:
			{
				Intent compartir = new Intent(Intent.ACTION_SEND);
				compartir.setType("text/plain");
				String mensaje = "Descarga esta app y crea tus servidores Full: " + "https://play.google.com/store/apps/details?id=com.elcris.coservers";
				compartir.putExtra(android.content.Intent.EXTRA_SUBJECT, "Internet Ilimitado");
				compartir.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
				startActivity(Intent.createChooser(compartir, "Compartir Via:"));
			}
			break;
				
			/*case R.id.sshgratis:
				String url3 = "https://sshbrasil.com.br/sshgratis";
				Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
				intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent6, getText(R.string.open_with)));
				break;*/
				
				

			case R.id.miAbout:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
				startActivity(aboutIntent);
				break;
		} if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawers();
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
		
		else {
			// mostra opção para sair
			new ExitDialogFragment(this)
				.show(getSupportFragmentManager(), "alertExit");
		}
	}

	@Override
    public void onResume() {
        super.onResume();


        //LinkReceiver();
		SkStatus.addStateListener(this);
		//doSaveData();
		showInterstitial();
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						//  updateHeaderCallback();
						checkNetwork();
					}
				});
			}
		}, 0,1000);



		if (adsBannerView != null) {
			adsBannerView.resume();


		}

    }

	@Override
	protected void onPause()
	{
		super.onPause();

		SkStatus.removeStateListener(this);
        guardarpreferencias();
		if (adsBannerView != null) {
			adsBannerView.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//doSaveData();
        guardarpreferencias();
		LocalBroadcastManager.getInstance(this)
			.unregisterReceiver(mActivityReceiver);

		if (adsBannerView != null) {
			adsBannerView.destroy();
		}


	}
    
    private String getTime(LogItem le, int time)
	{
		if (time != LogsAdapter.TIME_FORMAT_NONE)
		{
			Date d = new Date(le.getLogtime());
			java.text.DateFormat timeformat;
			if (time == LogsAdapter.TIME_FORMAT_SHORT)
				timeformat = new SimpleDateFormat("HH:mm a");
			else
				timeformat = DateFormat.getTimeFormat(this);

			return timeformat.format(d);

		}
		else
		{
			return "";
		}
	}


	/**
	 * DrawerLayout Listener
	 */

	@Override
	public void onDrawerOpened(View view) {
		
	}

	@Override
	public void onDrawerClosed(View view) {
		
	}

	@Override
	public void onDrawerStateChanged(int stateId) {}
	@Override
	public void onDrawerSlide(View view, float p2) {}


	/**
	 * Utils
	 */




	public void reload() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}


	private void harwardedid(){
		final androidx.appcompat.app.AlertDialog alertDialog;
		final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.harwarded_id, null);

		String message = VPNUtils.getHWID();
		TextView harwardedms = (TextView)v.findViewById(R.id.harwardedms);
		Button btnCompartir = (Button)v.findViewById(R.id.hadsButton1);
		Button btnCopiar = (Button)v.findViewById(R.id.hadsButton2);
		ImageView btnCancel = (ImageView) v.findViewById(R.id.hadsButton3);
		builder.setView(v);
		alertDialog = builder.create();
        harwardedms.setText(message);


		btnCompartir.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {


						String message = VPNUtils.getHWID();
						ShareService.SendMessage(MainActivity.this, message);

					}
				}
		);

		btnCopiar.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("HWID", VPNUtils.getHWID()));
						Toast.makeText(MainActivity.this, "El Harwarded ID se a copiado exitosamente al portapapeles", Toast.LENGTH_SHORT).show();

						alertDialog.dismiss();

					}
				}
		);


		btnCancel.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();

					}
				}
		);
		alertDialog.show();

	}



	public static void updateMainViews(Context context, String extra) {
		Intent updateView = new Intent(UPDATE_VIEWS);
        updateView.putExtra("clear", extra);
		LocalBroadcastManager.getInstance(context).sendBroadcast(updateView);
	}
    
    @Override
    public void onCheckedChanged(CompoundButton p1, boolean is) {
        SharedPreferences mPrefs = mConfig.getPrefsPrivate();
	    SharedPreferences.Editor edit = mPrefs.edit();
        switch (p1.getId()) {
            case R.id.cbusepayload:
                UsePayload = is;
                if (UsePayload != false) {
                    cbslow.setChecked(false);
                    if (cbpayload.isChecked() && cbssl.isChecked()) {
                        edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_PAY_SSL);
                        edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false);
                        edit.apply();
                    } else {
                        edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY);
                        edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false);
                        edit.apply();
                    }
                } else if (cbssl.isChecked()) {
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSL_PROXY);
                    edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true);
                    edit.apply();
                }  else if (cbslow.isChecked()) {
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SLOWDNS);
                    edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true);
                    edit.apply();
                } else {
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
                    edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true);
                    edit.apply();
                }
            break;
            
            case R.id.cbusessl:
                UseSSL = is;
                if (UseSSL != false) {
                    cbslow.setChecked(false);
                    if (cbpayload.isChecked() && cbssl.isChecked()) {
                        edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_PAY_SSL);
                        edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false);
                        edit.apply();
                    } else  {
                        edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSL_PROXY);
                        edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true);
                        edit.apply();
                    }
                } else if (cbpayload.isChecked()) {
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY);
                    edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false);
                    edit.apply();
                }  else if (cbslow.isChecked()) {
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SLOWDNS);
                    edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true); 
                    edit.apply();
                } else {
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
                    edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true);
                    edit.apply();
                }
            break;
            
            case R.id.cbslowdns:
                UseSlow = is;
                if (UseSlow != false) {
                    cbpayload.setChecked(false);
                    cbssl.setChecked(false);
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SLOWDNS);
                    edit.apply();
                } else {
                    edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
                    edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true);
                    edit.apply();
                }
            break;
            
            case R.id.cbenabledns:
                OnDNS = is;
                if (OnDNS != false) {
                    mConfig.setVpnDnsForward(OnDNS);
                } else {
                    mConfig.setVpnDnsForward(OnDNS);
                }
            break;
            
            case R.id.cbwakelock:
                UseWakeLock = is;
                if (UseWakeLock != false) {
                    mConfig.setWakelock(UseWakeLock);
                } else {
                    mConfig.setWakelock(UseWakeLock);
                }
            break;
            
            case R.id.cbautoreplace:
               UseAutoReplace = is;
               if (UseAutoReplace != false) {
                   mConfig.setAutoReplace(UseAutoReplace);
               } else {
                   mConfig.setAutoReplace(UseAutoReplace);
               }
            break;
        }
    }


}

