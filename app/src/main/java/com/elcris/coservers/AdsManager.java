package com.elcris.coservers;

import android.content.*;
import android.os.*;

public class AdsManager
{
	private final String TAG = "AdsManager";
	private Context mContext;
	private SharedPreferences mPrefs;

	public AdsManager(Context context){
		mContext = context;
		mPrefs = context.getSharedPreferences(MyApplication.PREFS_GERAL, Context.MODE_PRIVATE);
	}
	
	/**
	 * Ads Timer
	 */
	private CountDownTimer countDownTimer;
	private long timerMilliseconds;

}
