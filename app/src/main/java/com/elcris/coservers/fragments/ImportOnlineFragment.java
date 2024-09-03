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

import com.elcris.coservers.cloud.NetFreeCloud;
import com.elcris.coservers.config.Settings;
import com.elcris.coservers.R;

import es.dmoral.toasty.Toasty;
import java.io.IOException;

import android.widget.ImageView;
import android.widget.Toast;

public class ImportOnlineFragment extends DialogFragment
implements View.OnClickListener {

    private Settings mConfig;
    private TextInputEditText token;

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
        View view = li.inflate(R.layout.fragment_import_online, null); 

        token = view.findViewById(R.id.fragment_import_online);

        ImageView cancelButton = view.findViewById(R.id.fragment_import_remoteCancelButton);
        Button saveButton = view.findViewById(R.id.fragment_import_remoteSaveButton);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        return new AlertDialog.Builder(getActivity())
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
            case R.id.fragment_import_remoteSaveButton:
                String customtoken = token.getEditableText().toString();

                try {

                    NetFreeCloud.Importar(getActivity(), getContext(), customtoken, "interno");
                } catch (IOException e) {
                    Toasty.error(getContext(), e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                dismiss();

                break;

            case R.id.fragment_import_remoteCancelButton:
                dismiss();
                break;
        }
    }





    /*@Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.fragment_import_remoteSaveButton:
                String customtoken = token.getEditableText().toString();
                
                if(ConfigExportFileActivity.isOnline(getContext())) {
                    if (SkStatus.isTunnelActive()) {
                        Toasty.error(getContext(), "Deten el servicio VPN primero", Toast.LENGTH_SHORT, true).show();
                        
                    } else if (TextUtils.isEmpty(customtoken)) {
                        Toasty.error(getContext(), "Token vacio ingresa un token", Toast.LENGTH_SHORT, true).show(); //codigo esta bien
                        return;
                    } else {
                        try {
                            MainActivity.hideKeyboardFrom(getContext(), getView());
                            NetFreeCloud.Importar(getActivity(), getContext(), customtoken);
                        } catch (IOException e) {
                            MainActivity.hideKeyboardFrom(getContext(), getView());
                            Toasty.error(getContext(), e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                    }
                } else {
                    Toasty.error(getContext(), "No hay conexion a internet", Toast.LENGTH_SHORT, true).show();
                    return;
                }
				dismiss();

                break;

            case R.id.fragment_import_remoteCancelButton:
                dismiss();
                break;
        }
    }*/
    
    

}
