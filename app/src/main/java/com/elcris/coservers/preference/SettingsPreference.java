package com.elcris.coservers.preference;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.elcris.coservers.MainActivity;
import com.elcris.coservers.R;
import com.elcris.coservers.config.Settings;
import com.elcris.coservers.config.SettingsConstants;
import com.elcris.coservers.logger.ConnectionStatus;
import com.elcris.coservers.logger.SkStatus;
import com.elcris.coservers.activities.SplashActivity;

public class SettingsPreference extends PreferenceFragmentCompat
	implements Preference.OnPreferenceChangeListener, SettingsConstants,
		SkStatus.StateListener
{
	private Handler mHandler;
	private SharedPreferences mPref;
	
	public static final String
		SSHSERVER_PREFERENCE_KEY = "screenSSHSettings",
		ADVANCED_SCREEN_PREFERENCE_KEY = "screenAdvancedSettings";
		
	private String[] settings_disabled_keys = {
		DNSFORWARD_KEY,
		DNSRESOLVER_KEY1,
        DNSRESOLVER_KEY2,
		UDPFORWARD_KEY,
		UDPRESOLVER_KEY,
		PINGER_KEY,
		AUTO_CLEAR_LOGS_KEY,
		HIDE_LOG_KEY,
		IDIOMA_KEY
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		SkStatus.addStateListener(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		
		SkStatus.removeStateListener(this);
	}
	
	
	@Override
    public void onCreatePreferences(Bundle bundle, String root_key)
	{
        // Load the Preferences from the XML file
        setPreferencesFromResource(R.xml.app_preferences, root_key);
		
		mPref = getPreferenceManager().getDefaultSharedPreferences(getContext());
		
		Preference udpForwardPreference = (CheckBoxPreference)
			findPreference(UDPFORWARD_KEY);
		udpForwardPreference.setOnPreferenceChangeListener(this);
		
		ListPreference modoNoturno = (ListPreference)
			findPreference(MODO_NOTURNO_KEY);
		modoNoturno.setOnPreferenceChangeListener(this);
		SettingsAdvancedPreference.setListPreferenceSummary(modoNoturno, modoNoturno.getValue());
		
		Preference dnsForwardPreference = (CheckBoxPreference)
			findPreference(DNSFORWARD_KEY);
		dnsForwardPreference.setOnPreferenceChangeListener(this);	
		ListPreference idioma = (ListPreference)
			findPreference(IDIOMA_KEY);
		idioma.setOnPreferenceChangeListener(this);
		SettingsAdvancedPreference.setListPreferenceSummary(idioma, idioma.getValue());
		
		// update view
		setRunningTunnel(SkStatus.isTunnelActive());
	}
	
	private void onChangeUseVpn(boolean use_vpn){
		Preference udpResolverPreference = (EditTextPreference)
			findPreference(UDPRESOLVER_KEY);
		Preference dnsResolverPreference = (EditTextPreference)
			findPreference(DNSRESOLVER_KEY1);
        Preference dnsResolverPreference2 = (EditTextPreference)
            findPreference(DNSRESOLVER_KEY2);    
		
		for (String key : settings_disabled_keys){
			getPreferenceManager().findPreference(key)
				.setEnabled(use_vpn);
		}

		use_vpn = true;
		if (use_vpn) {
			boolean isUdpForward = mPref.getBoolean(UDPFORWARD_KEY, false);
			boolean isDnsForward = mPref.getBoolean(DNSFORWARD_KEY, false);
			
			udpResolverPreference.setEnabled(isUdpForward);
			dnsResolverPreference.setEnabled(isDnsForward);
            dnsResolverPreference2.setEnabled(isDnsForward);
		}
		else {
			String[] list = {
				UDPFORWARD_KEY,
				UDPRESOLVER_KEY,
				DNSFORWARD_KEY,
				DNSRESOLVER_KEY1,
                DNSRESOLVER_KEY2
			};
			for (String key : list) {
				getPreferenceManager().findPreference(key)
					.setEnabled(false);
			}
		}
	}
	
	private void setRunningTunnel(boolean isRunning) {
		if (isRunning) {
			for (String key : settings_disabled_keys){
				getPreferenceManager().findPreference(key)
					.setEnabled(false);
			}
		}
		else {
			onChangeUseVpn(true);
		}
	}

	
	/**
	* Preference.OnPreferenceChangeListener
	* Implementação
	*/
	
	@Override
	public boolean onPreferenceChange(Preference pref, Object newValue) {
		switch (pref.getKey()) {
			
			case MODO_NOTURNO_KEY:
				final String enableModoNoturno = (String)newValue;

				if (enableModoNoturno.equals(mPref.getString(MODO_NOTURNO_KEY, "off"))) {
					return false;
				}

				/*SettingsAdvancedPreference.setListPreferenceSummary(pref_list, (String) newValue);

				 new AlertDialog.Builder(getContext())
				 . setTitle(R.string.attention)
				 . setMessage(R.string.restarting_app_theme)
				 . setCancelable(false)
				 . show();*/

				mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							
							new Settings(getContext())
								.setModoNoturno(enableModoNoturno);

							// reinicia app
							Intent startActivity = new Intent(getContext(), SplashActivity.class);
							int pendingIntentId = 123456;
							PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), pendingIntentId, startActivity, PendingIntent.FLAG_CANCEL_CURRENT);

							AlarmManager mgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
							mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);

							// encerra tudo
							/*if (Build.VERSION.SDK_INT >= 16) {
							 Activity act = getActivity();
							 if (act != null)
							 act.finishAffinity();
							 }*/

							System.exit(0);
						}
					}, 300);

				getActivity().finish();
				return false;
				
			
			case UDPFORWARD_KEY:
				boolean isUdpForward = (boolean) newValue;

				Preference udpResolverPreference = (EditTextPreference)
					findPreference(UDPRESOLVER_KEY);
				udpResolverPreference.setEnabled(isUdpForward);
			break;
			
			case DNSFORWARD_KEY:
				boolean isDnsForward = (boolean) newValue;

				Preference dnsResolverPreference = (EditTextPreference)
					findPreference(DNSRESOLVER_KEY1);
				dnsResolverPreference.setEnabled(isDnsForward);
                
                Preference dnsResolverPreference2 = (EditTextPreference)
                    findPreference(DNSRESOLVER_KEY2);
                dnsResolverPreference2.setEnabled(isDnsForward);
			break;
			case IDIOMA_KEY:
				final String lang = (String) newValue;
				
				if (((String)newValue).equals(new Settings(getContext())
						.getIdioma())) {
					return false;
				}
				
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						LocaleHelper.setNewLocale(getContext(), lang);
						
						// reinicia app
						Intent startActivity = new Intent(getContext(), MainActivity.class);
						int pendingIntentId = 123456;
						PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), pendingIntentId, startActivity, PendingIntent.FLAG_CANCEL_CURRENT);

						AlarmManager mgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
						mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);

						// encerra tudo
						if (Build.VERSION.SDK_INT >= 16) {
							getActivity().finishAffinity();
						}

						System.exit(0);
					}
				}, 300);
				
                getActivity().finish();
            return false;
		}
		return true;
	}

	@Override
	public void updateState(String state, String logMessage, int localizedResId, ConnectionStatus level, Intent intent)
	{
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				setRunningTunnel(SkStatus.isTunnelActive());
			}
		});
	}
	
	
}
