package com.elcris.coservers.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;

import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.elcris.coservers.config.Settings;
import com.elcris.coservers.MainActivity;
import com.elcris.coservers.R;

public class CustomProxyDialogFragment extends DialogFragment
implements View.OnClickListener {

    private Settings mConfig;
    private TextInputEditText customProxy;
	private TextInputEditText customPort;

    @Override
    public void onCreate(Bundle savedInstanceState)   {

        super.onCreate(savedInstanceState);

        mConfig = new Settings(getContext());
        SharedPreferences prefs = mConfig.getPrefsPrivate();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.fragment_proxy_remote, null); 

        customProxy = view.findViewById(R.id.fragment_proxy_remoteProxyIpEdit);
		customPort = view.findViewById(R.id.fragment_proxy_remoteProxyPortaEdit);

        Button cancelButton = view.findViewById(R.id.fragment_proxy_remoteCancelButton);
        Button saveButton = view.findViewById(R.id.fragment_proxy_remoteSaveButton);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        customProxy.setText(mConfig.getPrivString(Settings.PROXY_IP_KEY));
		customPort.setText(mConfig.getPrivString(Settings.PROXY_PORTA_KEY));


        return new AlertDialog.Builder(getActivity())
            .setTitle(Html.fromHtml("<strong><font color='#FFEB3B'>Configuración del Proxy</font></strong>"))
			.setMessage(Html.fromHtml("<font color='#ffffff'>HTTP Proxy (Ex: Squid, Websocket)</font>" ))
            .setView(view)
            . show();
    }

    /**
     * onClick implementação
     */

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.fragment_proxy_remoteSaveButton:
                String mCustomProxy = customProxy.getEditableText().toString();
				String mCustomPort = customPort.getEditableText().toString();

				SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
				edit.putString(Settings.PROXY_IP_KEY, mCustomProxy);
				edit.putString(Settings.PROXY_PORTA_KEY, mCustomPort);
				edit.apply();

			    MainActivity.updateMainViews(getContext(), "no");
				dismiss();

                break;

            case R.id.fragment_proxy_remoteCancelButton:
                dismiss();
                break;
        }
    }

}
