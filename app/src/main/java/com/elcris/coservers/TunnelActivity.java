package com.elcris.coservers;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.RadioButton;

import com.elcris.coservers.config.Settings;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import com.elcris.coservers.logger.SkStatus;
import com.elcris.coservers.util.ToastUtil;

public class TunnelActivity extends AppCompatActivity implements View.OnClickListener {

	private Toolbar toolbar_main;
	private RadioButton btnDirect;
    private RadioButton btnHTTP;
	private RadioButton btnSSL;
    private RadioButton btnPaySSL;
    private RadioButton btnSSLHTTP;
	private RadioButton btnSlowDNS;
	private Switch customPayload;
    private RadioButton btnSSH;
	private Settings mConfig;
	private SharedPreferences prefs;
	private Button save;
    private ToastUtil Aviso;

	private TextView mTextView;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnDirect:
			btnHTTP.setChecked(false);
			btnSSL.setChecked(false);
            btnSSLHTTP.setChecked(false);
            btnPaySSL.setChecked(false);
			btnSlowDNS.setChecked(false);
			customPayload.setEnabled(true);
			if (customPayload.isChecked()) {
				mTextView.setText(getString(R.string.direct) + getString(R.string.custom_payload1));
			} else {
				mTextView.setText(getString(R.string.direct));			
    		}
			break;
			
			case R.id.btnHTTP:
				btnDirect.setChecked(false);
				btnSSL.setChecked(false);
                btnPaySSL.setChecked(false);
                btnSSLHTTP.setChecked(false);
				btnSlowDNS.setChecked(false);
				customPayload.setEnabled(true);
				if (customPayload.isChecked()) {
					mTextView.setText(getString(R.string.http) + getString(R.string.custom_payload1));
				} else {
					mTextView.setText(getString(R.string.http));			
				}
			break;
			
			case R.id.btnSSL:
				btnHTTP.setChecked(false);
				btnDirect.setChecked(false);
                btnPaySSL.setChecked(false);
                btnSSLHTTP.setChecked(false);
				btnSlowDNS.setChecked(false);
				customPayload.setEnabled(false);
				customPayload.setChecked(false);
				mTextView.setText(getString(R.string.ssl));		
		    break;
            
            case R.id.btnSSLPay:
                btnHTTP.setChecked(false);
				btnDirect.setChecked(false);
				btnSSL.setChecked(false);
                btnSSLHTTP.setChecked(false);
				btnSlowDNS.setChecked(false);
				btnPaySSL.setChecked(true);
				customPayload.setEnabled(false);
				customPayload.setChecked(true);
				//btnSslRp.setChecked(false);
				if (customPayload.isChecked()) {
					mTextView.setText(getString(R.string.payssl) + getString(R.string.custom_payload1));
				} else {
					mTextView.setText(getString(R.string.payssl));			
				}
				break;
                
            case R.id.btnSSLHTTP:
                btnDirect.setChecked(false);
                btnHTTP.setChecked(false);
                btnPaySSL.setChecked(false);
                btnSSL.setChecked(false);
                btnSlowDNS.setChecked(false);
                customPayload.setEnabled(false);
                customPayload.setChecked(true);
				mTextView.setText(getString(R.string.sslhttp) + getString(R.string.custom_payload1));		

                break;
                
            case R.id.btnSSH:
			    btnHTTP.setChecked(false);
				btnHTTP.setEnabled(true);
				btnSSL.setChecked(false);
				btnSSL.setEnabled(true);
				btnDirect.setChecked(true);
				btnDirect.setEnabled(true);
                btnPaySSL.setChecked(false);
				btnPaySSL.setEnabled(true);
				btnSSLHTTP.setChecked(false);
				btnSSLHTTP.setEnabled(true);
				btnSlowDNS.setChecked(false);
				customPayload.setEnabled(true);
				customPayload.setChecked(false);
				mTextView.setText(getString(R.string.ssh));
				
				break;    
			
			case R.id.btnSlowDNS:
				btnSSH.setChecked(false);
                btnHTTP.setChecked(false);
                btnHTTP.setEnabled(false);
				btnSSL.setChecked(false);
                btnSSL.setEnabled(false);
                btnSSLHTTP.setChecked(false);
                btnSSLHTTP.setEnabled(false);
				btnDirect.setChecked(false);
                btnDirect.setEnabled(false);
                btnPaySSL.setChecked(false);
                btnPaySSL.setEnabled(false);
				customPayload.setEnabled(false);
				customPayload.setChecked(false);
				mTextView.setText(getString(R.string.slowdns));		
		    break;
		   
			case R.id.customPayload:
				if (customPayload.isChecked()) {
				if (btnDirect.isChecked()) {
					mTextView.setText(getString(R.string.direct) + getString(R.string.custom_payload1));
				} else if (btnHTTP.isChecked()) {
					mTextView.setText(getString(R.string.http) + getString(R.string.custom_payload1));
				}
			  } else {
				  if (btnDirect.isChecked()) {
					  mTextView.setText(getString(R.string.direct));
				  } else if (btnHTTP.isChecked()) {
					  mTextView.setText(getString(R.string.http));
				  }
			  }
			break;
			
			case R.id.saveButton:
			doSave();	
			break;
		}
	}

	private void doSave() {
		SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
		
		if (btnDirect.isChecked()) {
			edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);	
		
		} else if (btnHTTP.isChecked()) {
			edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY);	
			
		} else if (btnSSL.isChecked()) {
			edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSL_PROXY);
			
	    } else if(btnPaySSL.isChecked()) {
            edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_PAY_SSL);
            
        } else if (btnSlowDNS.isChecked()) {
		   edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SLOWDNS);	
	    } else if (btnSSLHTTP.isChecked()) {
           edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSL_RP);
        }
		
		if (customPayload.isChecked()) {
			edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false);
			
		} else {
			edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true);
		}
		edit.apply();
		startActivity(new Intent(this, MainActivity.class));
		MainActivity.updateMainViews(getApplicationContext(), "no");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tunnel_type);
		mConfig = new Settings(this);
		prefs = mConfig.getPrefsPrivate();
		toolbar_main = (Toolbar) findViewById(R.id.toolbar_main1);
		setSupportActionBar(toolbar_main);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setupButton();
        Aviso = new ToastUtil(this);
	}

	private void setupButton() {
		mTextView = (TextView) findViewById(R.id.tunneltypeTextView1);
		btnSSH = (RadioButton) findViewById(R.id.btnSSH);
		btnSSH.setOnClickListener(this);
        btnDirect = (RadioButton) findViewById(R.id.btnDirect);
		btnDirect.setOnClickListener(this);
		btnHTTP = (RadioButton) findViewById(R.id.btnHTTP);
		btnHTTP.setOnClickListener(this);
		btnSSL = (RadioButton) findViewById(R.id.btnSSL);
		btnSSL.setOnClickListener(this);
        btnPaySSL = (RadioButton) findViewById(R.id.btnSSLPay);
        btnPaySSL.setOnClickListener(this);
        btnSSLHTTP = (RadioButton) findViewById(R.id.btnSSLHTTP);
        btnSSLHTTP.setOnClickListener(this);
		btnSlowDNS = (RadioButton) findViewById(R.id.btnSlowDNS);
		btnSlowDNS.setOnClickListener(this);
		
		customPayload = (Switch) findViewById(R.id.customPayload);
		customPayload.setOnClickListener(this);
		
		save = (Button) findViewById(R.id.saveButton);
		save.setOnClickListener(this);
	
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
		
	    customPayload.setChecked(!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true));
		
		switch (tunnelType) {
			case Settings.bTUNNEL_TYPE_SSH_DIRECT:
			btnDirect.setChecked(true);
			btnHTTP.setChecked(false);
			btnSSL.setChecked(false);
            btnPaySSL.setChecked(false);
            btnSSLHTTP.setChecked(false);
			btnSlowDNS.setChecked(false);
			customPayload.setEnabled(true);
			
			if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
			mTextView.setText(getString(R.string.direct) + getString(R.string.custom_payload1));
			} else {
			mTextView.setText(getString(R.string.direct));			
    		}
			break;
			
			case Settings.bTUNNEL_TYPE_SSH_PROXY:
				btnHTTP.setChecked(true);
				btnDirect.setChecked(false);
				btnSSL.setChecked(false);
                btnPaySSL.setChecked(false);
                btnSSLHTTP.setChecked(false);
				btnSlowDNS.setChecked(false);
				customPayload.setEnabled(true);
				if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
					mTextView.setText(getString(R.string.http) + getString(R.string.custom_payload1));
				} else {
					mTextView.setText(getString(R.string.http));			
				}
				break;

			case Settings.bTUNNEL_TYPE_SSL_PROXY:
				btnSSL.setChecked(true);
				btnHTTP.setChecked(false);
                btnPaySSL.setChecked(false);
				btnDirect.setChecked(false);
                btnSSLHTTP.setChecked(false);
				btnSlowDNS.setChecked(false);
				customPayload.setEnabled(false);
				customPayload.setChecked(false);
				mTextView.setText(getString(R.string.ssl));			
				break;
                
            case Settings.bTUNNEL_TYPE_PAY_SSL:
                btnSlowDNS.setChecked(false);
				btnHTTP.setChecked(false);
				btnSSL.setChecked(false);
                btnSSLHTTP.setChecked(false);
				btnDirect.setChecked(false);
				btnPaySSL.setChecked(true);
				customPayload.setEnabled(true);
				//btnSslRp.setChecked(false);
				mTextView.setText(getString(R.string.payssl));
				if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
					mTextView.setText(getString(R.string.payssl) + getString(R.string.custom_payload1));
				} else {
					mTextView.setText(getString(R.string.payssl));			
				}
                break;
                
            case Settings.bTUNNEL_TYPE_SSL_RP:
                btnSSLHTTP.setChecked(true);
                btnPaySSL.setChecked(false);
                btnSSL.setChecked(false);
                btnHTTP.setChecked(false);
                btnDirect.setChecked(false);
                btnSlowDNS.setChecked(false);
                customPayload.setEnabled(false);
                customPayload.setChecked(true);
                mTextView.setText(getString(R.string.sslhttp) + getString(R.string.custom_payload1));
				break;    

			case Settings.bTUNNEL_TYPE_SLOWDNS:
				btnSlowDNS.setChecked(true);
                btnSSH.setChecked(false);
				btnHTTP.setChecked(false);
				btnSSL.setChecked(false);
				btnDirect.setChecked(false);
				customPayload.setEnabled(false);
				customPayload.setChecked(false);
				mTextView.setText(getString(R.string.slowdns));		
				break;
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu,add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tunnel_menu, menu);//Menu ResourceFile
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miLimparConfig:
                if (!SkStatus.isTunnelActive()) {
                    Settings.clearSettings(TunnelActivity.this);
                    SkStatus.clearLog();	
                    MainActivity.updateMainViews(getApplicationContext(), "no");
                    btnDirect.setChecked(false);
                    btnHTTP.setChecked(false);
                    btnSSL.setChecked(true);
					btnSSL.setEnabled(true);
                    btnPaySSL.setChecked(false);
					btnSSLHTTP.setChecked(false);
                    btnSlowDNS.setChecked(false);
                    customPayload.setChecked(false);
                    customPayload.setEnabled(false);

                    if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true)) {
                        mTextView.setText(getString(R.string.direct) + getString(R.string.custom_payload1));
                    } else {
                        mTextView.setText(getString(R.string.direct));          
                    }
                } else {
                    Aviso.ToastError(getString(R.string.error_tunnel_service_execution));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    
}
