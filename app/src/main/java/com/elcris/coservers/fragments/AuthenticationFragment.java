package com.elcris.coservers.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.elcris.coservers.config.Settings;
import com.elcris.coservers.R;
import com.elcris.coservers.MainActivity;

public class AuthenticationFragment extends DialogFragment
implements View.OnClickListener {

    private Settings mConfig;
    private TextInputEditText user;
	private TextInputEditText pass;

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
        View view = li.inflate(R.layout.auth, null); 

        user = view.findViewById(R.id.username);
		pass = view.findViewById(R.id.password);
		
        Button cancelButton = view.findViewById(R.id.fragment_sni_remoteCancelButton);
        Button saveButton = view.findViewById(R.id.fragment_sni_remoteSaveButton);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        user.setText(mConfig.getPrivString(Settings.USUARIO_KEY));
		pass.setText(mConfig.getPrivString(Settings.SENHA_KEY));
		

        return new AlertDialog.Builder(getActivity())
            . setTitle(R.string.title_auth)
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
                String mCustomSNI = user.getEditableText().toString();
				String mCustomSN = pass.getEditableText().toString();
				
				SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
				edit.putString(Settings.USUARIO_KEY, mCustomSNI);
				edit.putString(Settings.SENHA_KEY, mCustomSN);
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
