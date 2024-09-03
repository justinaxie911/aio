package com.elcris.coservers;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;

import com.elcris.coservers.config.Settings;

import android.content.res.Configuration;
import com.elcris.coservers.preference.LocaleHelper;

public class MyApplication extends Application
{
	public static final String PREFS_GERAL = "PREFS_GERAL";
	public static final String ADS_UNITID_INTERSTITIAL_MAIN = "ca-app-pub-6741116462771721/7920275824";
    public static final String ADS_UNITID_BANNER_ABOUT = "ca-app-pub-6741116462771721/6882526328";
	public static final String ADS_UNITID_BANNER_MAIN = "ca-app-pub-6741116462771721/6882526328";
	public static final boolean DEBUG = true;
	private static Context app;

	private Settings mConfig;

	
	/*static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }*/
	
	@Override
	public void onCreate() {
		super.onCreate();
		app = getApplicationContext();
		mConfig = new Settings(this);
		setModoNoturno(this);
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.setLocale(base));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LocaleHelper.setLocale(this);
	}
	
	public static Context getApp() {
		return app;
	}
	
	private void setModoNoturno(Context context) {
		String mode = mConfig.getModoNoturno();
		
		if (mode.equals("on")) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppTheme);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
			setTheme(R.style.AppTheme);
		}

	}	
}
