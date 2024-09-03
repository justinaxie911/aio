package com.elcris.coservers.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.elcris.coservers.cloud.NetFreeCloud;
import com.elcris.coservers.logger.SkStatus;
import com.elcris.coservers.R;
import es.dmoral.toasty.Toasty;
import java.io.IOException;

public class ConfigImportOnlineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchvpn);
        if (SkStatus.isTunnelActive()) {
            Toasty.error(ConfigImportOnlineActivity.this, "Detiene el servicio primero", Toast.LENGTH_SHORT, true).show();
            finish();
            //finish();
        }
        else if (ConfigExportFileActivity.isOnline(this)) {
            FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            if (deepLink.getQueryParameter("token") != null) {
								
                                String token = deepLink.getQueryParameter("token");
                                
                                    try {
                                        NetFreeCloud.Importar(ConfigImportOnlineActivity.this, ConfigImportOnlineActivity.this, token, "externo");
                                    } catch (IOException e) {
                                        Toasty.error(ConfigImportOnlineActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                    }
                               
                            }
                        } else {
                            //Toasty.error(MainActivity.this, "Ocurrio un Error", Toast.LENGTH_SHORT, true).show();
                            //Log.d("WALKIRIA", "ERROR WITH DYNAMIC LINK OR NO LINK AT ALL");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
						Toasty.error(ConfigImportOnlineActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //Log.d("WALKIRIA", "ERROR WITH DYNAMIC LINK " + e.toString());

                    }
                });
        } else {
            Toasty.error(ConfigImportOnlineActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
