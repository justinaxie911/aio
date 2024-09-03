package com.elcris.coservers.cloud;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Toast;

import java.io.IOException;

import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;
import android.view.LayoutInflater;
import com.elcris.coservers.R;
import com.elcris.coservers.config.Settings;
import android.content.pm.PackageManager;
import com.elcris.coservers.util.VPNUtils;
import android.content.pm.PackageInfo;
import com.elcris.coservers.MainActivity;
import es.dmoral.toasty.Toasty;
import java.util.Calendar;
import com.kimchangyoun.rootbeerFresh.RootBeer;
import android.telephony.TelephonyManager;
import android.os.Build;
import android.content.pm.InstallSourceInfo;

import java.text.DateFormat;
import com.elcris.coservers.logger.SkStatus;
import android.view.View;

public class NetFreeCloud {
    public static void Exportar(Context context, String document, boolean mIsProteger, boolean mPedirSenha, boolean isBloquearRoot, String mMensagem, long mValidade, boolean mOnlyDataMovil, boolean mOnlyPlayStore, boolean mBlockOperador, String mOperador, boolean mBlockHwid, String mHwid, boolean mLoginHwid, boolean mPPayload, boolean mPProxy, boolean mPSni, boolean mPServer, boolean mPPort, boolean mPUser, boolean mPPass, String autormsg)throws IOException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Settings settings = new Settings(context);
        SharedPreferences mPrefs = settings.getPrefsPrivate();
		SharedPreferences.Editor edit = mPrefs.edit();
        String user_config;
        String pass_config;
        
        if (mLoginHwid == true) {
            user_config = VPNUtils.getHWID();
            pass_config = VPNUtils.getHWID(); 
        } else  {
            user_config = mPrefs.getString(Settings.USUARIO_KEY, "");
            pass_config = mPrefs.getString(Settings.SENHA_KEY, "");
        }
        String server = mPrefs.getString(Settings.SERVIDOR_KEY, "");
	    String server_port = mPrefs.getString(Settings.SERVIDOR_PORTA_KEY, "");
        String payload_config = mPrefs.getString(Settings.CUSTOM_PAYLOAD_KEY, "");
        String sni_config = mPrefs.getString(Settings.CUSTOM_SNI, "");
        String input_all = mPrefs.getString(Settings.CONFIG_LINE_INPUT, "");
        String local_port = mPrefs.getString(Settings.PORTA_LOCAL_KEY, "1080");
        String chave = mPrefs.getString(Settings.CHAVE_KEY, "");
		String nameserver = mPrefs.getString(Settings.NAMESERVER_KEY, "");
		String dns = mPrefs.getString(Settings.DNS_KEY, "");
        String proxy = mPrefs.getString(Settings.PROXY_IP_KEY, "");
        String proxy_port = mPrefs.getString(Settings.PROXY_PORTA_KEY, "");
        String udp_port = settings.getVpnUdpResolver();
        String isDefaultPayload = mPrefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true) ? "1" : "0";
        boolean ondns = settings.getVpnDnsForward();
        boolean onudp = settings.getVpnUdpForward();
        boolean onwakelock = settings.getWakelock();
        boolean config_locked = false;
        if (mIsProteger && isDefaultPayload.equals("0") && payload_config.isEmpty()) {
				throw new IOException();
			}
        int targerId = getBuildId(context);
        if (mIsProteger && (server.isEmpty() || server_port.isEmpty())) {
			   throw new IOException("Server host o puerto vacio");
               
			}
        if (mIsProteger && isDefaultPayload.equals("0") && payload_config.isEmpty()) {
				throw new IOException("Payload Vacio");
			}
        int tunnel_type = mPrefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
        if (tunnel_type == Settings.bTUNNEL_TYPE_SSL_PROXY) {
				if (mIsProteger && sni_config.isEmpty()) {
					throw new IOException("SNI VACIO");
				}
				} else if (tunnel_type == Settings.bTUNNEL_TYPE_SSL_RP){
				if (mIsProteger && sni_config.isEmpty()) {
					throw new IOException("SNI VACIO");
			   }
			} else if (tunnel_type == Settings.bTUNNEL_TYPE_SLOWDNS) {
			   if (mIsProteger && (chave.isEmpty() || nameserver.isEmpty() || dns.isEmpty())) {
						throw new IOException("NAMESERVER VACIO");
			   }
			}
        //String dns_1 = settings.getVpnDnsResolver1();
        //String dns_2 = settings.getVpnDnsResolver2();
        Map<String, Object> map = new HashMap<>();
        map.put("user_config", user_config);
        map.put("pass_config", pass_config);
        map.put("server_host", server);
        map.put("server_port", server_port);
        map.put("payload_config", payload_config);
        map.put("sni_config", sni_config);
        map.put("dns1", settings.getVpnDnsResolver1());
        map.put("dns2", settings.getVpnDnsResolver2());
        map.put("protect_config", mIsProteger);
        map.put("protect_payload", mPPayload);
        map.put("protect_sni", mPSni);
        map.put("protect_server", mPServer);
        map.put("protect_port", mPPort);
        map.put("protect_user", mPUser);
        map.put("protect_pass", mPPass);
        map.put("protect_proxy", mPProxy);
        map.put("block_root", isBloquearRoot);
        map.put("block_dataonly", mOnlyDataMovil);
        map.put("block_playstoreonly", mOnlyPlayStore);
        map.put("block_operator", mBlockOperador);
        map.put("block_hwid", mBlockHwid);
        map.put("block_loginhwid", mLoginHwid);
        map.put("operator_name", mOperador);
        map.put("hwid_text", mHwid);
        map.put("config_message", mMensagem);
        map.put("config_autor", autormsg);
        map.put("config_version", Integer.toString(targerId));
        map.put("expire_date", mValidade);
        map.put("proxy", proxy);
        map.put("proxy_port", proxy_port);
        map.put("local_port", local_port);
        map.put("input_all", input_all);
        map.put("tunnel_type", Integer.toString(mPrefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT)));
        map.put("ondns", ondns);
        map.put("pedirpass", mPedirSenha);
        map.put("default_payload", isDefaultPayload);
        map.put("onudp", onudp);
        map.put("onwakelock", onwakelock);
        map.put("udp_port", udp_port);
        map.put("slow_dns", dns);
        map.put("slow_clave", chave);
        map.put("slow_nameserver", nameserver);
        map.put("config_locked", config_locked);
        
        db.collection(context.getString(R.string.app_name)).document(document).set(map).addOnSuccessListener(new OnSuccessListener<Void>()  {
            @Override
            public void onSuccess(Void unused) {
                /**DialogFragment dialog = new ImportOnlineFragment();
                dialog.show(getSupportFragmentManager(), "import"); **/     
                Toasty.success(context, context.getString(R.string.success_export_settings) , Toast.LENGTH_LONG, true).show();
                //Toast.makeText(context, "Creado exitosamente", Toast.LENGTH_SHORT).show();
               //finish();
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e)  {
                Toasty.error(context, "Ocurrio un error no se pudo guardar la configuracion", Toast.LENGTH_SHORT, true).show();         
                Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();    
                return;
                //Toast.makeText(context, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
      
        
    }
    public static void Importar(Activity activity, Context context, String document, String tipodeimport) throws IOException {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        Settings settings = new Settings(context);
        SharedPreferences mPrefs = settings.getPrefsPrivate();
		SharedPreferences.Editor prefsEdit = mPrefs.edit();
        
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        final androidx.appcompat.app.AlertDialog importando;
		LayoutInflater inflater = activity.getLayoutInflater();
		View v = inflater.inflate(R.layout.importar_cloud, null);
		builder.setView(v);
		importando = builder.create();
		importando.show();
        
        try {
            
            mFirestore.collection(context.getString(R.string.app_name)).document(document).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot){
                    if (documentSnapshot.exists()) {
                        String server_host = documentSnapshot.getString("server_host");
                        String server_port = documentSnapshot.getString("server_port");
                        String payload_config = documentSnapshot.getString("payload_config");
                        String sni_config = documentSnapshot.getString("sni_config");
                        String user_config = documentSnapshot.getString("user_config");
                        String pass_config = documentSnapshot.getString("pass_config");
                        String dns1 = documentSnapshot.getString("dns1");
                        String dns2 = documentSnapshot.getString("dns2");
                        String operator_name = documentSnapshot.getString("operator_name");
                        String hwid_text = documentSnapshot.getString("hwid_text");
                        String config_message = documentSnapshot.getString("config_message");
                        String config_autor = documentSnapshot.getString("config_autor");
                        String input_all = documentSnapshot.getString("input_all");
                        String local_port = documentSnapshot.getString("local_port");
                        String chave = documentSnapshot.getString("slow_clave");
	                    String nameserver = documentSnapshot.getString("slow_nameserver");
	                    String dns = documentSnapshot.getString("slow_dns");
                        String proxy = documentSnapshot.getString("proxy");
                        String proxy_port = documentSnapshot.getString("proxy_port");
                        String udp_port = documentSnapshot.getString("udp_port");
                        String _blockRoot = documentSnapshot.getBoolean("block_root").toString();
                        String _only_data = documentSnapshot.getBoolean("block_dataonly").toString();
                        String proxy_line = documentSnapshot.getString("proxy_line");
                        //boolean isDefaultPayloadl = documentSnapshot.
                        boolean protect_config = documentSnapshot.getBoolean("protect_config");
                        boolean protect_payload = documentSnapshot.getBoolean("protect_payload");
                        boolean protect_sni = documentSnapshot.getBoolean("protect_sni");
                        boolean protect_server = documentSnapshot.getBoolean("protect_server");
                        boolean protect_port = documentSnapshot.getBoolean("protect_port");
                        boolean protect_user = documentSnapshot.getBoolean("protect_user");
                        boolean protect_pass = documentSnapshot.getBoolean("protect_pass");
                        boolean protect_proxy = documentSnapshot.getBoolean("protect_proxy");
                        boolean block_root = documentSnapshot.getBoolean("block_root");
                        boolean block_dataonly = documentSnapshot.getBoolean("block_dataonly");
                        boolean block_playstoreonly = documentSnapshot.getBoolean("block_playstoreonly");
                        boolean block_operator = documentSnapshot.getBoolean("block_operator");
                        boolean block_hwid = documentSnapshot.getBoolean("block_hwid");
                        boolean block_loginhwid = documentSnapshot.getBoolean("block_loginhwid");
                        boolean ondns = documentSnapshot.getBoolean("ondns");
                        boolean onudp = documentSnapshot.getBoolean("onudp");
                        boolean onwakelock = documentSnapshot.getBoolean("onwakelock");
                        boolean autoreplace = documentSnapshot.getBoolean("autoreplace");
                        boolean config_locked = documentSnapshot.getBoolean("config_locked");
                        String pedirLogin = documentSnapshot.getBoolean("pedirpass").toString();
                        String default_payload = documentSnapshot.getString("default_payload").toString();
                        int tunnel_type = Integer.parseInt(documentSnapshot.getString("tunnel_type"));
                        int config_version = Integer.parseInt(documentSnapshot.getString("config_version"));
                        long expire_date = documentSnapshot.getLong("expire_date");    
                        try {
                            
                            // bloqueia root
			                if (block_root) {
				                if (isDeviceRooted(context)) {
                                    importando.dismiss();
					                throw new IOException(context.getString(R.string.error_root_detected));
					            }
			                }
			
			                //Only Data
		                	boolean  mOnlyDataMovil = false;
			                if (_only_data != null) {
			            	    mOnlyDataMovil = _only_data.equals("1") ? true: false;
				                /*if (mOnlyDataMovil) {
				                  	if (WifiCheckConnected(mContext)) {
					             	    throw new IOException(mContext.getString(R.string.block_only_data_movile));
					                }
				              }**/
				
		                	}
                             
                            if (config_locked) {
                                importando.dismiss();    
                                throw new IOException("La configuracion ha sido bloqueada y no se puede usar");
                            }
			
			                //Only Instalation From Play Store
		            	    //boolean mOnlyPlayStore = false;
		                	String packageName = context.getPackageName();
		                	String installerPackageName = "";
		            	    if (Build.VERSION.SDK_INT >= 30) {
                                try {
                                    PackageManager pm = context.getPackageManager();
			                	    InstallSourceInfo info = pm.getInstallSourceInfo(packageName);
                                    if (info != null) {
				                	    installerPackageName = info.getInstallingPackageName();
			                    	}    
                                } catch (PackageManager.NameNotFoundException e) {
                                
                                }
		                	} else {
				                PackageManager pm = context.getPackageManager();
				                installerPackageName = pm.getInstallerPackageName(packageName);
				
		            	    }
			                if (block_playstoreonly) {
					
					            if (!installerPackageName.equals("com.android.vending")) {
                                    importando.dismiss();    
					                throw new IOException(context.getString(R.string.only_play_store_instalation));
					            }
			                }
			
		            	    //Block Operator
		            	
				            if (block_operator) {
				                TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
				                String MyPhoneOperator = telephonyManager.getSimOperatorName();
				                if (!operator_name.equals(telephonyManager.getSimOperatorName())) {
                                    importando.dismiss();    
						            throw new IOException(context.getString(R.string.operator_block_tittle));
					            }
		            	    }
			
		            	    //BlockHWID
			                if (block_hwid) {
				                //String MyHWID = VPNUtils.getHWID();
				                if (!hwid_text.equals(VPNUtils.getHWID())) {
                                    importando.dismiss();    
					                throw new IOException(context.getString(R.string.block_hwid_tittle));
				                }
		            	    }    
                            
                            if (config_version > getBuildId(context)) {
                                importando.dismiss();    
			 	               throw new IOException(context.getString(R.string.alert_update_app));
	            	    	}

			                if (!protect_config || expire_date < 0) {
			        	        expire_date = 0;
		       	            }
		                	else if (expire_date > 0 && isValidadeExpirou(expire_date)){
                                importando.dismiss();    
			            	    throw new IOException(context.getString(R.string.error_settings_expired));
		        	        }
                            
                            try {
                                if (server_host == null) {
                                   importando.dismiss();     
					               throw new Exception();
			        	        }
                                if (protect_config) {
                                    prefsEdit.putString(Settings.CONFIG_AUTOR_KEY, config_autor != null ? config_autor : "");
					                prefsEdit.putString(Settings.CONFIG_MENSAGEM_KEY, config_message != null ? config_message : "");
					            
                                    new Settings(context)
						            .setModoDebug(false);
                                    if (pedirLogin != null)
					                 	prefsEdit.putBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, pedirLogin.equals("1") ? true : false);
				            	    else prefsEdit.putBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false);        
                                } else {
					                prefsEdit.putString(Settings.CONFIG_MENSAGEM_KEY, "");
					                prefsEdit.putBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false);
                                }
                                if (user_config.isEmpty() && pass_config.isEmpty()) {
				                    prefsEdit.putString("enable_auth", "_true");
			                	} else {
				                	prefsEdit.putString("enable_auth", "_false");
			                	}                
                                prefsEdit.putString(Settings.PROXY_IP_KEY, proxy != null ? proxy : "");
			            	    prefsEdit.putString(Settings.PROXY_PORTA_KEY, proxy_port != null ? proxy_port : "");
                                prefsEdit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, !default_payload.equals("1") ? false : true);               
                                prefsEdit.putString(Settings.CHAVE_KEY, chave);
				                prefsEdit.putString(Settings.NAMESERVER_KEY, nameserver);
				                prefsEdit.putString(Settings.DNS_KEY, dns);
                
                                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_PAYLOAD, protect_payload);
                                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_PROXY, protect_proxy);
                                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_SNI, protect_sni);
                                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_SERVER, protect_server);
                                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_PORT, protect_port);
                                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_USUARIO, protect_user);
                                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_CONTRASENA, protect_pass);
                                prefsEdit.putString(Settings.CUSTOM_PAYLOAD_KEY, payload_config);     
				                prefsEdit.putString(Settings.CUSTOM_SNI, sni_config);
				                prefsEdit.putString(Settings.SERVIDOR_KEY, server_host);
				                prefsEdit.putString(Settings.SERVIDOR_PORTA_KEY, server_port);
				                prefsEdit.putString(Settings.USUARIO_KEY, user_config);
				                prefsEdit.putString(Settings.SENHA_KEY, pass_config);
				                prefsEdit.putString(Settings.PORTA_LOCAL_KEY, local_port);
                                prefsEdit.putString(Settings.CONFIG_LINE_INPUT, input_all);
				                prefsEdit.putInt(Settings.TUNNELTYPE_KEY, tunnel_type);
				                prefsEdit.putBoolean(Settings.CONFIG_PROTEGER_KEY, protect_config);
				                prefsEdit.putLong(Settings.CONFIG_VALIDADE_KEY, expire_date);
				                prefsEdit.putBoolean(Settings.BLOQUEAR_ROOT_KEY, block_root);
                                prefsEdit.putBoolean(Settings.CONFIG_BLOCK_ONLY_DATA, block_dataonly);
                                settings.setLineProxy(proxy_line);
                                settings.setVpnDnsForward(ondns);
			                    settings.setVpnDnsResolver1(dns1);
			                	settings.setVpnDnsResolver2(dns2);
                                settings.setVpnUdpForward(onudp);
                                settings.setWakelock(onwakelock);
                                settings.setAutoReplace(autoreplace);
			            	    settings.setVpnUdpResolver(udp_port);
                                prefsEdit.commit();
                                MainActivity.updateMainViews(context, "no");        
                                if (expire_date > 0) {
				                    SkStatus.logInfo("<b><font color=\"#ffffff\">Archivo Valido Hasta: </font></b>" + android.text.format.DateFormat.getDateFormat(context).format(expire_date));
	                    		}    
                                importando.dismiss();   
                                Toasty.success(context, context.getString(R.string.success_import_settings) , Toast.LENGTH_LONG, true).show();
                                if (expire_date > 0 && mPrefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
				                    Calendar c = Calendar.getInstance();
			                    	final long time_hoje = c.getTimeInMillis();
				
		                    		c.setTimeInMillis(time_hoje+(1000*60*60*24));
				
			                    	long dias = ((expire_date-time_hoje)/1000/60/60/24);
		                    		DateFormat df = DateFormat.getDateInstance();
			                    	expire_date = c.getTimeInMillis();
				
			                    	Toasty.success(context, dias + " Dias Restantes" + " - " + "Expira: " + df.format(expire_date), Toast.LENGTH_LONG, true).show();
	                    		}    
                                    
                                if (tipodeimport.equals("externo")) {
                                    activity.finish();
									Intent home = new Intent(context, MainActivity.class);
                                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									context.startActivity(home);
									
								}    
                                
                            } catch (Exception e) {
                                importando.dismiss();    
                                throw new IOException(context.getString(R.string.error_file_settings_invalid));
                            }
                            
                        } catch(IOException e) {
                            importando.dismiss();    
                            Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
	            	    }
                    } else {
                        importando.dismiss();    
                        Toasty.error(context, "Token Invalido", Toast.LENGTH_SHORT, true).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    importando.dismiss();    
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e) {
            importando.dismiss();
		    throw new IOException(context.getString(R.string.error_file_invalid), e);
		} catch (Throwable e) {
            importando.dismiss();
			throw new IOException(context.getString(R.string.error_file_invalid));
		}       
    }    

    private void getBoolean() {
        
    }
    
    public static int getBuildId(Context context) throws IOException {
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pinfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			throw new IOException("Build ID not found");
		}
	}
    
    public static boolean isValidadeExpirou(long validadeDateMillis) {
		if (validadeDateMillis == 0) {
			return false;
		}
		
		// Get Current Date
		long date_atual = Calendar.getInstance()
			.getTime().getTime();
		
		if (date_atual >= validadeDateMillis) {
			return true;
		}
		
		return false;
	}
    
    public static boolean isDeviceRooted(Context context) {
        /*for (String pathDir : System.getenv("PATH").split(":")){
			if (new File(pathDir, "su").exists()) {
				return true;
			}
		}
		
		Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }*/
		
		RootBeer rootBeer = new RootBeer(context);
		
		boolean simpleTests = rootBeer.detectRootManagementApps() || rootBeer.detectPotentiallyDangerousApps() || rootBeer.checkForBinary("su")
			|| rootBeer.checkForDangerousProps() || rootBeer.checkForRWPaths()
			|| rootBeer.detectTestKeys() || rootBeer.checkSuExists() || rootBeer.checkForRootNative() || rootBeer.checkForMagiskBinary();
		//boolean experiementalTests = rootBeer.checkForMagiskNative();
			
		return simpleTests;
	}
}
