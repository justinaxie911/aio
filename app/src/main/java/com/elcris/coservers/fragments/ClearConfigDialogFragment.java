package com.elcris.coservers.fragments;

import android.app.Dialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;

import com.elcris.coservers.R;

import androidx.fragment.app.DialogFragment;

import com.elcris.coservers.logger.SkStatus;
import com.elcris.coservers.MainActivity;
import com.elcris.coservers.config.Settings;

public class ClearConfigDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.ThemeOverlay_App_MaterialAlertDialog).
			create();
		//dialog = new AlertDialog(getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog);

		dialog.setTitle(getActivity().getString(R.string.attention));
		dialog.setMessage(getActivity().getString(R.string.alert_clear_settings));

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Borrar Config",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Settings.clearSettings(getContext());				
					SkStatus.clearLog();
					MainActivity.updateMainViews(getContext(), "si");
				}
			}
		);

		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			}
		);
		
		return dialog;
	}








}
