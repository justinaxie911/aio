package com.elcris.coservers.activities;


import androidx.appcompat.widget.Toolbar;
import android.view.View.OnClickListener;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.elcris.coservers.BuildConfig;
import com.elcris.coservers.R;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import android.widget.TextView;
import android.text.Html;
import android.content.Intent;
import android.net.Uri;
import android.content.pm.PackageInfo;
import com.elcris.coservers.util.Utils;
import com.elcris.coservers.tunnel.TunnelUtils;

public class AboutActivity extends BaseActivity implements OnClickListener {

	private Toolbar tb;
	private View changelog, license, dev, dev2, dev3;
	private AlertDialog.Builder ab;
    private TextView app_info_text;
	private AdView adsBannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		tb = (Toolbar) findViewById(R.id.toolbar_main);
		setSupportActionBar(tb);
		changelog = findViewById(R.id.changelog);
		license = findViewById(R.id.license);
		dev = findViewById(R.id.developer);
		dev2 = findViewById(R.id.developer2);
		dev3 = findViewById(R.id.developer3);
		changelog.setOnClickListener(this);
		license.setOnClickListener(this);
		dev.setOnClickListener(this);
		dev2.setOnClickListener(this);
		dev3.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PackageInfo pinfo = Utils.getAppInfo(this);
        if (pinfo != null) {
            String version_nome = pinfo.versionName;
            int version_code = pinfo.versionCode;
            String header_text = String.format("%s (%d)", version_nome, version_code);
            app_info_text = (TextView) findViewById(R.id.appVersion);
			app_info_text.setText(header_text);
		}
        /**adsBannerView = (AdView) findViewById(R.id.adBannerMainView2);
        if (!MyApplication.DEBUG) {
            adsBannerView.setAdUnitId(MyApplication.ADS_UNITID_BANNER_ABOUT);
		}
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
		}**/






		adsBannerView = (AdView) findViewById(R.id.adBannerMainView2);
		if (!BuildConfig.DEBUG) {
			//adsBannerView.setAdUnitId(SocksHttpApp.ADS_UNITID_BANNER_SOBRE);
		}


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





	}
	@Override
	public void onClick(View view) {

		// TODO: Implement this method
		int id = view.getId();
		if (id == R.id.changelog) {
			changelog();
		} else if (id == R.id.license) {
			license();
		}  else if (id == R.id.developer2) {
			startActivity(new Intent("android.intent.action.VIEW",
					Uri.parse("https://t.me/elcrischat")));
		} else if (id == R.id.developer3) {
			startActivity(new Intent("android.intent.action.VIEW",
					Uri.parse("https://t.me/crisis1823")));
		} else if (id == R.id.developer) {
			startActivity(new Intent("android.intent.action.VIEW",
					Uri.parse("https://t.me/cris1823")));
		}
	}

	private void changelog() {
		// TODO: Implement this method
		ab = new AlertDialog.Builder(this);
		ab.setTitle("EL CRIS ⚡️ Dev!");

		ab.setMessage(Html.fromHtml("<font color='#FFFFFF'>• Protocolo VPN Seguro y Eficaz <br> • Por favor informenos cualquier problema</font></strong>"));
		ab.setPositiveButton(android.R.string.ok, null);
		ab.create().show();
	}

	private void license() {
		// TODO: Implement this method
        startActivity(new Intent(this, LicenseActivity.class));
	}
}
