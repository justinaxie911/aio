package com.elcris.coservers.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.elcris.coservers.config.Settings;
import com.elcris.coservers.R;
import com.elcris.coservers.MainActivity;

public class CustomSNIDialogFragment extends DialogFragment
implements View.OnClickListener {

    private Settings mConfig;
    private TextInputEditText customSNI;

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
        View view = li.inflate(R.layout.custom_sni_dialog_fragment, null); 

        customSNI = view.findViewById(R.id.fragment_custom_sni);

        Button cancelButton = view.findViewById(R.id.fragment_sni_remoteCancelButton);
        Button saveButton = view.findViewById(R.id.fragment_sni_remoteSaveButton);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        customSNI.setText(mConfig.getPrivString(Settings.CUSTOM_SNI));


        return new AlertDialog.Builder(getActivity())
            .setTitle(Html.fromHtml("<strong><font color='#FFEB3B'>Información del Nombre del Servidor (SNI)</font></strong>"))
			.setMessage(Html.fromHtml("<font color='#ffffff'>SNI (Ex: @Crisis1823)</font>"))

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
            case R.id.fragment_sni_remoteSaveButton:
                String mCustomSNI = customSNI.getEditableText().toString();

				SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
				edit.putString(Settings.CUSTOM_SNI, mCustomSNI);
				edit.apply();

			    MainActivity.updateMainViews(getContext(), "no");
				dismiss();

                break;

            case R.id.fragment_sni_remoteCancelButton:
                dismiss();
                break;
        }
    }

}
