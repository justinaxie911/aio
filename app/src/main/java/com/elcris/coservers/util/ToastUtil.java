package com.elcris.coservers.util;

import android.content.*;
import android.widget.Toast;
import es.dmoral.toasty.*;

public class ToastUtil {
	
	private Context mContext;
	
	public ToastUtil(Context c) {
		mContext = c;
	}
	
	public void ToastExito(String msg) {
		Toasty.success(mContext, msg, Toast.LENGTH_SHORT, true).show();
	}
	
	public void ToastError(String msg) {
		Toasty.error(mContext, msg, Toast.LENGTH_SHORT, true).show();
	}
    
    public void ToastInformacion(String msg) {
        Toasty.info(mContext, msg, Toast.LENGTH_SHORT, true).show();
    }
    
    public void ToastAdvertencia(String msg) {
        Toasty.warning(mContext, msg, Toast.LENGTH_SHORT, true).show();
    }
    
    public void ToastExitoLargo(String msg) {
        Toasty.success(mContext, msg, Toast.LENGTH_LONG, true).show();

    }
	
}
