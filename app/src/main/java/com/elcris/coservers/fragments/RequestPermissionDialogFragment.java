package com.elcris.coservers.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import com.elcris.coservers.R;

public class RequestPermissionDialogFragment extends DialogFragment {
	private RequestPermissionListener dialogInterfaceListener;

	public static RequestPermissionDialogFragment newInstance() {
		return new RequestPermissionDialogFragment();
	}

	public interface RequestPermissionListener {
		public void onClickRequestPermissionDialog(DialogInterface dialog);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			dialogInterfaceListener = (RequestPermissionListener) context;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(Html.fromHtml("<font color='#FFEB3B'>Solicitud de permisos</font>"));
		dialog.setMessage(Html.fromHtml("<font color='#ffffff'>Por favor, es necesario otorgar los permisos necesarios para poder importar el archivo de configuración correctamente.</font>"));
		dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int position) {
				dialogInterfaceListener.onClickRequestPermissionDialog(dialogInterface);
			}
		});
		dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int position) {
				dialogInterface.dismiss();
				getActivity().finish();
			}
		});
		return dialog.create();
	}

}
