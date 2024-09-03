package com.elcris.coservers.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import com.elcris.coservers.preference.LocaleHelper;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import static android.content.pm.PackageManager.GET_META_DATA;
import com.elcris.coservers.util.AppUpdater;
import org.json.JSONObject;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import com.elcris.coservers.R;
import android.app.NotificationManager;
import android.net.Uri;
import android.media.RingtoneManager;
import android.media.Ringtone;
import android.content.pm.PackageInfo;
import com.elcris.coservers.util.Utils;
import android.content.Intent;
import org.json.JSONException;
import android.content.DialogInterface;

/**
 * Created by Pankaj on 03-11-2017.
 */
public class BaseActivity extends AppCompatActivity
{
	public static int mTheme = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		resetTitles();
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.setLocale(base));
	}
	protected void resetTitles() {
		try {
			ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
			if (info.labelRes != 0) {
				setTitle(info.labelRes);
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void updateApp(final boolean showDialog) {
		new AppUpdater(this, new AppUpdater.OnUpdateListener() {
				@Override
				public void onUpdateListener(String result) {
					try {
						if (!result.contains("Error on getting data")) {			
							final JSONObject obj = new JSONObject(result);
							PackageInfo pinfo = Utils.getAppInfo(getApplicationContext());
							String versionName = pinfo.versionName;
							if (versionName.equals(obj.getString("versionCode"))) {
								if (showDialog){
									AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this);
									alert.setTitle(getString(R.string.app_name))
										.setMessage(getString(R.string.alert_app_on_latest))
										.setPositiveButton("OK", null)
										.show();
								}
							} else {
								notif1();
								notif2();
								AlertDialog.Builder alert = new AlertDialog.Builder(BaseActivity.this);
								alert.setTitle(getString(R.string.alert_update_app))
									.setMessage(obj.getString("Message"))
									.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {  
										public void onClick(DialogInterface dialog, int id) {  
											try  {
												startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("url"))));
											} catch (JSONException e) {

											}
										}  
									}).show();
							}
						} else if(result.contains("Error on getting data") && showDialog){
							errorUpdateDialog(result);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start(showDialog);	
    }
	
	private void errorUpdateDialog(String error) {
		new AlertDialog.Builder(this)
			.setTitle("Update error!!!")
			.setMessage(getString(R.string.alert_update_error) +
						"Error:" + error)
			.setPositiveButton("Ok", null)
			.create().show();
	}
	
	public void notif1() {
		NotificationCompat.Builder b = new NotificationCompat.Builder(this);
		b.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
			.setSmallIcon(R.drawable.ic_launcher)
            .setTicker(getString(R.string.bugs_fixed))
            .setContentTitle(getString(R.string.alert_update_app))
            .setContentText(getString(R.string.alert_update_app_now))
            .setContentInfo(getString(R.string.app_name));


		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(1, b.build());
	}

	private void notif2(){
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
	}
}
