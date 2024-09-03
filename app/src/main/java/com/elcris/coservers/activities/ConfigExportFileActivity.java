package com.elcris.coservers.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.app.ProgressDialog;
import android.text.method.LinkMovementMethod;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdView;

import host.stjin.expandablecardview.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.elcris.coservers.R;
import com.elcris.coservers.config.ConfigParser;
import com.elcris.coservers.config.Settings;
import com.elcris.coservers.util.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import es.dmoral.toasty.Toasty;

import com.google.android.material.textfield.TextInputLayout;
import com.elcris.coservers.util.VPNUtils;

import java.util.Random;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kimchangyoun.rootbeerFresh.RootBeer;

import android.content.pm.PackageInfo;
import androidx.annotation.NonNull;
import android.content.pm.PackageManager;
import java.util.HashMap;
import java.util.Map;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.android.gms.tasks.OnCompleteListener;

public class ConfigExportFileActivity
extends BaseActivity
implements CompoundButton.OnCheckedChangeListener, View.OnClickListener
{
	private Settings mConfig;
	public static int REQUEST_CODE_ARBITRARY = 1;
	private Button PreviewMSG;
	public File fileDir;
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mConfig = new Settings(this);

		doLayout();

		// requista permissões ao armazenamento externo
		requestPermissions();
		
		if (Build.VERSION.SDK_INT >= 30) {
			fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getString(R.string.app_name));
		} else {
            fileDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        }
		
		TelephonyManager telephonyManager = ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
		OperadorString = telephonyManager.getSimOperatorName();
	}

	@Override
	public boolean onSupportNavigateUp()
	{
		super.onBackPressed();
		return true;
	}


	/**
	 * Main Views
	 */
	private CheckBox validadeCheck;
	private CheckBox BlockOperador;
	private AppCompatCheckBox OnlyData;
	private TextView validadeText;
	private TextView operadorText;
	private String OperadorString;
	private String mOperador = "";
	private String P = "";
	private String mHwid = "";
	private EditText nomeEdit;
	private EditText mensagemEdit;
	public EditText mensageEdit;
	public TextInputLayout MensajeInputLayout;
    private LinearLayout autorlayout;
	private AdView adsBannerView;
	private boolean mIsProteger = false;
	private String mMensagem = "";
    private String mAutor = "";
	private boolean mPedirSenha = false;
	private boolean mOnlyDataMovil = false;
	private boolean mOnlyPlayStore = false;
	private boolean mBloquearRoot = false;
	private boolean mPAll = false;
	private boolean mBlockOperador = false;
	private boolean mBlockHwid = false;
	private boolean mLoginHwid = false;
	private boolean mMessage = false;
	private boolean mPPayload = false;
	private boolean mPProxy =  false;
	private boolean mPSni = false;
	private boolean mPServer = false;
	private boolean mPPort = false;
	private boolean mPUser = false;
	private boolean Pass = false;
	private boolean mPPass = false;
    private boolean IsAutor = false;
	public EditText SshHwid;
    private EditText autoredit;
	public TextInputLayout InputLayoutHwid1;
	private TextInputLayout InputLayoutMensaje;
	private FloatingActionButton exportarButton;
	private LinearLayout LinearMensaje;
	private LinearLayout LinearHwid;
	private TextInputEditText inputpass;
	private LinearLayout Options;
	private CheckBox PAll;
	private CheckBox PPayload;
	private CheckBox PProxy;
	private CheckBox PSni;
	private CheckBox PServer;
	private CheckBox PPort;
	private CheckBox PUser;
	private CheckBox PPass;
	private CheckBox CPass;
    private CheckBox autorcb;
	private ExpandableCardView cardssh;
	private ExpandableCardView cardsecureop;
    private ProgressDialog progressDialog;
    private androidx.appcompat.app.AlertDialog exportando;
    private androidx.appcompat.app.AlertDialog generando;
	
	
	private void doLayout() {
		setContentView(R.layout.activity_config_export);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// impede autoinicio dos editText
		/**findViewById(R.id.activity_config_exportLinearLayout)
			.requestFocus();**/

		nomeEdit = (EditText) findViewById(R.id.activity_config_exportNomeEdit);
		Random random = new Random();
		StringBuilder sb2 = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb2.append("0123456789abcdefghijklmnopqrz".charAt(random.nextInt(10)));
        }
		String Name = getString(R.string.app_name) + "_" + sb2.toString();
		//nomeEdit.setText(Name);
		Options = (LinearLayout) findViewById(R.id.card_options);
		AppCompatCheckBox protegerCheck = (AppCompatCheckBox) findViewById(R.id.activity_config_exportProtegerCheck);
		//OnlyData = (AppCompatCheckBox) findViewById(R.id.activity_config_onlydata);
		validadeCheck = (CheckBox) findViewById(R.id.activity_config_exportValidadeCheck);
		//validadeText = (TextView) findViewById(R.id.activity_config_exportValidadeText);
		//operadorText = (TextView) findViewById(R.id.activity_config_operadorText);
		mensagemEdit = (EditText) findViewById(R.id.activity_config_exportMensagemEdit);
		SshHwid = (EditText) findViewById(R.id.hwidtext);
		InputLayoutHwid1 = (TextInputLayout) findViewById(R.id.inputlayouthwid);
		InputLayoutHwid1.setEnabled(false);
		exportarButton = (FloatingActionButton) findViewById(R.id.activity_config_exportButton);
		LinearMensaje = (LinearLayout) findViewById(R.id.layout_msg);
		LinearHwid = (LinearLayout) findViewById(R.id.layout_hwid);
		CheckBox blockRootCheck = (CheckBox) findViewById(R.id.activity_config_exportBlockRootCheck);
		CheckBox onlyDataMovil = (CheckBox) findViewById(R.id.activity_config_exportOnlyDataBlock);
		CheckBox onlyPlayStore = (CheckBox) findViewById(R.id.activity_config_exportOnlyPlayStore);
		BlockOperador = (CheckBox) findViewById(R.id.activity_config_exportOperadorCheck);
		CheckBox BlockHwid = (CheckBox) findViewById(R.id.activity_config_exportBlockHwid);
		CheckBox LoginHwid = (CheckBox) findViewById(R.id.activity_config_exportHdwidLogin);
		CheckBox EnableMessage = (CheckBox) findViewById(R.id.msg_check);
		//CPass = (CheckBox) findViewById(R.id.activity_config_exportPass);
		PPayload = (CheckBox) findViewById(R.id.ppayload);
		PProxy = (CheckBox) findViewById(R.id.pproxy);
		PSni = (CheckBox) findViewById(R.id.psni);
		PAll = (CheckBox) findViewById(R.id.pall);
		PUser = (CheckBox) findViewById(R.id.puser);
		PPass = (CheckBox) findViewById(R.id.pass);
		PServer = (CheckBox) findViewById(R.id.pserver);
		PPort = (CheckBox) findViewById(R.id.pport);
        autorcb = (CheckBox) findViewById(R.id.autor_check);
        autoredit = (EditText) findViewById(R.id.activity_config_exportautoredit);
		cardssh = (ExpandableCardView) findViewById(R.id.card_ssh);
        autorlayout = (LinearLayout) findViewById(R.id.layout_autor);
		cardsecureop = (ExpandableCardView) findViewById(R.id.card_secure);
		
		//Separador1 = (TextView) findViewById(R.id.separador1);
		//PreviewMSG = (Button) findViewById(R.id.export_preview_msg_id);
		//PreviewMSG.setOnClickListener(this);
		//mensageEdit = (EditText) findViewById(R.id.etMessage);
		//TextInputLayout MensajeInputLayout = (TextInputLayout) findViewById(R.id.messageLayout);
		showSegurancaLayout(false);
		mensagemEdit.setText(mConfig.getMensagemConfigExportar());
        autoredit.setText(mConfig.getAutorMsg());
		InputLayoutMensaje = (TextInputLayout) findViewById(R.id.activity_config_exportLayoutMensagemEdit);
		InputLayoutMensaje.setEndIconOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ConfigExportFileActivity.this);
					//builder.setTitle(R.string.preview_msg);
					String obj = ConfigExportFileActivity.this.mensagemEdit.getText().toString();
					if (obj.isEmpty()) {
						//obj = getString(R.string.msg_empty);
						} else if (!obj.contains("<br>") && !obj.contains("</br>") && !obj.contains("<p>") && !obj.contains("</p>")) {
						obj = obj.replace("\n", "<br></br>");
					}
					ScrollView scrollView = new ScrollView(ConfigExportFileActivity.this);
					scrollView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
					scrollView.setSmoothScrollingEnabled(true);
					TextView textView = new TextView(ConfigExportFileActivity.this);
					textView.setMovementMethod(LinkMovementMethod.getInstance());
					if (Build.VERSION.SDK_INT >= 24) {
						textView.setText(Html.fromHtml(obj, 63));
						} else {
						textView.setText(Html.fromHtml(obj));
					}
					textView.setPadding(10, 10, 10, 10);
					scrollView.addView(textView);
					builder.setView(scrollView);
					builder.setPositiveButton((R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.dismiss();
						}
					});
					builder.create().show();
				}
				});
		validadeCheck.setOnCheckedChangeListener(this);
		PAll.setOnCheckedChangeListener(this);
		PUser.setOnCheckedChangeListener(this);
		protegerCheck.setOnCheckedChangeListener(this);
		PProxy.setOnCheckedChangeListener(this);
		exportarButton.setOnClickListener(this);
		blockRootCheck.setOnCheckedChangeListener(this);
		PPass.setOnCheckedChangeListener(this);
		onlyDataMovil.setOnCheckedChangeListener(this);
		//CPass.setOnCheckedChangeListener(this);
		PPayload.setOnCheckedChangeListener(this);
		onlyPlayStore.setOnCheckedChangeListener(this);
		BlockOperador.setOnCheckedChangeListener(this);
		PServer.setOnCheckedChangeListener(this);
		BlockHwid.setOnCheckedChangeListener(this);
		PSni.setOnCheckedChangeListener(this);
        autorcb.setOnCheckedChangeListener(this);
		LoginHwid.setOnCheckedChangeListener(this);
		EnableMessage.setOnCheckedChangeListener(this);
		PPort.setOnCheckedChangeListener(this);
		
		

		/**adsBannerView = (AdView) findViewById(R.id.adView3);
        if (TunnelUtils.isNetworkOnline(this)) {
			
			adsBannerView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (adsBannerView != null) {
						adsBannerView.setVisibility(View.VISIBLE);
					}
				}
			});

			adsBannerView.loadAd(new AdRequest.Builder()
				.build());
		}**/
        
	}
    
    /*private void uploadsucess(String token) {



		copytoken();




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Configuracion Exportada Exitosamente");
        builder.setMessage("Token: " + token +"\n\nToken copiado al portapapeles");
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ClipboardManager)getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("token", token));
                onBackPressed();     
            }    
        });       

        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

	private void copytoken(String token, String enlace) {
		final androidx.appcompat.app.AlertDialog alertDialog;
		final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.exportar_ly_token, null);

		
		TextView tokencp = (TextView)v.findViewById(R.id.tokencp);
		tokencp.setText(token);
        TextView enlacecp = (TextView)v.findViewById(R.id.enlacecp);
		enlacecp.setText(enlace);

		Button btnCtoken = (Button)v.findViewById(R.id.hadsButton1);
		ImageView btnCancel = (ImageView) v.findViewById(R.id.hadsButton2);
		builder.setView(v);
		alertDialog = builder.create();

		alertDialog.show();
		btnCtoken.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {




						Intent compartir = new Intent(Intent.ACTION_SEND);
						compartir.setType("text/plain");
						String mensaje = "Utiliza el token o el enlace para importar la configuracion\n\n" + "Token: " + token + "\n\n Enlace: " + enlace;
						compartir.putExtra(android.content.Intent.EXTRA_SUBJECT, "Token");
						compartir.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
						startActivity(Intent.createChooser(compartir, "Compartir Via:"));
						alertDialog.dismiss();
					}
				}
		);


		btnCancel.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();

					}
				}
		);
		alertDialog.show();




	}




















	private void exportConfiguracao(String nome)
	throws IOException {
		if (!FileUtils.isExternalStorageWritable()) {
			throw new IOException(getString(R.string.error_permission_writer_required));
		}

		if (!fileDir.exists()) {
			fileDir.mkdir();
		}

		File fileExport = new File(fileDir, String.format("%s.%s", nome, ConfigParser.FILE_EXTENSAO));
		if (!fileExport.exists()) {
			try {
				fileExport.createNewFile();
			} catch(IOException e) {
				throw new IOException(getString(R.string.error_save_settings));
			}
		}

		// salva mensagem para ser reutilizada
		if (mIsProteger) {
			mConfig.setMensagemConfigExportar(mMensagem);
            mConfig.setAutorMsg(mAutor);
		}

		try {
			ConfigParser.convertDataToFile(new FileOutputStream(fileExport), this,
										   mIsProteger, mPedirSenha, mBloquearRoot, mMensagem, mValidade, mOnlyDataMovil, mOnlyPlayStore, mBlockOperador, mOperador, mBlockHwid, mHwid, mLoginHwid, mPPayload, mPProxy, mPSni, mPServer, mPPort, mPUser, mPPass, Pass, mAutor);
		} catch(IOException e) {
			fileExport.delete();
			throw e;
		}
	}
    
    private void exportConfiguracaotoCloud(String token)
	throws IOException {

		// salva mensagem para ser reutilizada
		if (mIsProteger) {
			mConfig.setMensagemConfigExportar(mMensagem);
            mConfig.setAutorMsg(mAutor);
		}

		try {
            Exportar(this, token, mIsProteger, mPedirSenha, mBloquearRoot, mMensagem, mValidade, mOnlyDataMovil, mOnlyPlayStore, mBlockOperador, mOperador, mBlockHwid, mHwid, mLoginHwid, mPPayload, mPProxy, mPSni, mPServer, mPPort, mPUser, mPPass, mAutor);
		} catch(IOException e) {
            Toasty.error(ConfigExportFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
			//throw e;
		}
	}


	/**
	 * Validade
	 */

	private long mValidade = 0;

	private void setValidadeDate() {

		// Get Current Date
		Calendar c = Calendar.getInstance();
		final long time_hoje = c.getTimeInMillis();

		c.setTimeInMillis(time_hoje+(1000*60*60*24));

		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		mValidade = c.getTimeInMillis();

		final DatePickerDialog dialog = new DatePickerDialog(this,
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker p1, int year, int monthOfYear, int dayOfMonth) {
					Calendar c = Calendar.getInstance();
					c.set(year, monthOfYear, dayOfMonth);

					mValidade = c.getTimeInMillis();
				}
			},
			mYear, mMonth, mDay);

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog2, int which) {
					DateFormat df = DateFormat.getDateInstance();
					DatePicker date = dialog.getDatePicker();

					Calendar c = Calendar.getInstance();
					c.set(date.getYear(), date.getMonth(), date.getDayOfMonth());

					mValidade = c.getTimeInMillis();

					if (mValidade < time_hoje) {
						mValidade = 0;

						Toasty.error(ConfigExportFileActivity.this, R.string.error_date_selected_invalid, Toast.LENGTH_SHORT, true).show();

						if (validadeCheck != null)
							validadeCheck.setChecked(false);
					}
					else {
						long dias = ((mValidade-time_hoje)/1000/60/60/24);

						if (validadeText != null) {
							validadeCheck.setText(String.format("%s (%s)", dias, df.format(mValidade)));
						}
					}
				}
			}
		);

		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mValidade = 0;

					if (validadeCheck != null) {
						validadeCheck.setChecked(false);
					}
				}
			}
		);

		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface v1) {
					mValidade = 0;
					if (validadeCheck != null) {
						validadeCheck.setChecked(false);
					}
				}
			});

		dialog.show();
	}

	private void requestPermissions() {
		FileUtils.requestForPermissionExternalStorage(this);
	}


	/**
	 * Oculta/Mostra layout com opções
	 */

	private int[] idsProtegerViews = {
		R.id.activity_config_exportValidadeCheck,
		R.id.pproxy,
		R.id.pass,
		//R.id.activity_config_exportValidadeText,
		R.id.activity_config_exportMensagemEdit,
		R.id.psni,
		R.id.activity_config_exportLayoutMensagemEdit,
		R.id.activity_config_exportBlockRootCheck,
		//R.id.activity_config_exportPass,
		R.id.activity_config_exportOnlyDataBlock,
		R.id.activity_config_exportOnlyPlayStore,
		R.id.activity_config_exportOperadorCheck,
		R.id.activity_config_exportBlockHwid,
		R.id.pserver,
		R.id.activity_config_exportHdwidLogin,
		R.id.ppayload,
		R.id.msg_check,
		R.id.puser,
		R.id.pport,
        R.id.autor_check
	};

	private int[] idsProtegerChecksView = {
		R.id.activity_config_exportValidadeCheck,
		R.id.activity_config_exportBlockRootCheck,
		R.id.activity_config_exportOnlyDataBlock,
		//R.id.activity_config_exportPass,
		R.id.pproxy,
		R.id.pass,
		R.id.activity_config_exportOnlyPlayStore,
		R.id.ppayload,
		R.id.activity_config_exportOperadorCheck,
		R.id.psni,
		R.id.activity_config_exportBlockHwid,
		R.id.pserver,
		R.id.activity_config_exportHdwidLogin,
		R.id.msg_check,
		R.id.puser,
		R.id.pport,
        R.id.autor_check
	};

	private void showSegurancaLayout(boolean is) {
		if (is) {
			Toasty.error(this, R.string.alert_block_settings, Toast.LENGTH_SHORT, true).show();
		}
		else {
			for (int id : idsProtegerChecksView) {
				((CheckBox) findViewById(id)).setChecked(false);
			}
		}

		for (int id : idsProtegerViews) {
			findViewById(id).setEnabled(is);
		}
	}


	@Override
	public void onCheckedChanged(CompoundButton p1, boolean is)
	{
		switch (p1.getId()) {
			case R.id.activity_config_exportValidadeCheck:
				if (is) {
					setValidadeDate();
				}
				else {
					mValidade = 0;
					/**if (validadeText != null) {
						validadeCheck.setText(R.string.check_date_valid);
					}**/
				}
				break;

			case R.id.activity_config_exportProtegerCheck:
				mIsProteger = is;
				showSegurancaLayout(is);
				if (mIsProteger != false) {
					Options.setVisibility(View.VISIBLE);

					PAll.setChecked(true);
				} else {
					PAll.setChecked(false);
					Options.setVisibility(View.GONE);
				}
				break;
				
			case R.id.pall:
			    mPAll = is;
				if (mPAll != false) {
					PPayload.setChecked(true);
					PProxy.setChecked(true);
					PSni.setChecked(true);
					PServer.setChecked(true);
					PPort.setChecked(true);
					PUser.setChecked(true);
					PPass.setChecked(true);
				} else {
					PPayload.setChecked(false);
					PProxy.setChecked(false);
					PSni.setChecked(false);
					PServer.setChecked(false);
					PPort.setChecked(false);
					PUser.setChecked(false);
					PPass.setChecked(false);
				}
				break;
					
			case R.id.activity_config_exportBlockRootCheck:
				mBloquearRoot = is;
				break;
				
			/**case R.id.activity_config_exportPass:
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			    //String checkpass = mConfig.getPrivString(Settings.CP);
				Pass = is;
				if (Pass != false) {
					// load the dialog_promt_user.xml layout and inflate to view
					LayoutInflater layoutinflater = LayoutInflater.from(context);
					View promptUserView = layoutinflater.inflate(R.layout.dialog_input, null);
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptUserView);
					
					inputpass = (TextInputEditText) promptUserView.findViewById(R.id.input_pass);
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setTitle("Bloquear Servidor con Contraseña");
					alertDialogBuilder.setMessage("Ingresa tu Contraseña");
					
					alertDialogBuilder.setNegativeButton((R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
						CPass.setChecked(false);
                        dialog.cancel();
						
						}
						});
					// prompt for username
					alertDialogBuilder.setPositiveButton((R.string.ok),new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							String checkpass1 = inputpass.getText().toString();
							// and display the username on main activity layout
							if (checkpass1.isEmpty()) {
								CPass.setChecked(false);
								Toasty.warning(ConfigExportFileActivity.this, "La contraseña no puede estar vacia", Toast.LENGTH_SHORT, true).show();
							} else {
								CPass.setChecked(true);
								SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();
								edit.putString(Settings.CUSTOM_SNI, checkpass1);
								edit.apply();
							}
							
							}
							});
							
							// all set and time to build and show up!
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
				}
				break;**/
			case R.id.activity_config_exportOnlyDataBlock:
			    mOnlyDataMovil = is;
				break;
				
			case R.id.ppayload:
			    mPPayload = is;
				break;
				
			case R.id.pproxy:
			    mPProxy = is;
				break;
				
			case R.id.psni:
			    mPSni = is;
				break;
				
			case R.id.pserver:
			    mPServer = is;
				break;
				
			case R.id.pport:
			    mPPort = is;
				break;
				
			case R.id.puser:
			    mPUser = is;
				break;
				
			case R.id.pass:
			    mPPass = is;
				break;
				
			case R.id.activity_config_exportOnlyPlayStore:
			    mOnlyPlayStore = is;
				break;
			case R.id.activity_config_exportOperadorCheck:
			    mBlockOperador = is;
				if (mBlockOperador != false) {
					BlockOperador.setText(getString(R.string.operator_checkbox_tittle) + " " + "(" + OperadorString + ")");
				} else {
						BlockOperador.setText(R.string.operator_checkbox_tittle);
					}
				break;
			case R.id.activity_config_exportBlockHwid:
			    mBlockHwid = is;
				if (mBlockHwid != false) {
					InputLayoutHwid1.setEnabled(true);
					LinearHwid.setVisibility(View.VISIBLE);
					SshHwid.setText(VPNUtils.getHWID());
				} else {
					LinearHwid.setVisibility(View.GONE);
					InputLayoutHwid1.setEnabled(false);
				}
				break;
				
			case R.id.activity_config_exportHdwidLogin:
			    mLoginHwid = is;
				break;
			case R.id.msg_check:
			    mMessage = is;
			    if (mMessage != false) {
					LinearMensaje.setVisibility(View.VISIBLE);
				} else {
					LinearMensaje.setVisibility(View.INVISIBLE);
				}
				break;
            case R.id.autor_check:
                IsAutor = is;
                if (IsAutor != false) {
                    autorlayout.setEnabled(true);
                    autorlayout.setVisibility(View.VISIBLE);
                } else {
                    autorlayout.setEnabled(false);
                    autorlayout.setVisibility(View.GONE);
                }
                break;
		}
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {






			case R.id.activity_config_exportButton:

				exportarLayout();









				/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exportar:");
                builder.setMessage("Selecciona una opcion");        // add the buttons
                builder.setPositiveButton("Local", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();        
                        String nomeConfig = nomeEdit.getText().toString();
			        	mMensagem = mIsProteger ? mensagemEdit.getText().toString() : "";
			        	mOperador = mIsProteger ? OperadorString : "";
			        	mHwid = mIsProteger ? SshHwid.getText().toString() : "";

			        	if (nomeConfig.isEmpty()) {
				        	Toasty.error(ConfigExportFileActivity.this, R.string.error_empty_name_file, Toast.LENGTH_SHORT, true).show();
				        	return;
			        	}

				        if (mIsProteger == false || mValidade < 0) {
				        	mValidade = 0;
			        	}

				        try {
					        exportConfiguracao(nomeConfig);
						    Toasty.success(ConfigExportFileActivity.this, getString(R.string.success_export_settings) + getString(R.string.file_saved_to) + "" + fileDir, Toast.LENGTH_LONG, true).show();
				        } catch (IOException e) {
						    Toasty.error(ConfigExportFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                            
			        	}
				        	onBackPressed();
                    }        
                });;





                builder.setNeutralButton("Cloud", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("Exportando");
                        progressDialog.setMessage("Por favor espere...");
                        progressDialog.show();
                        Random random = new Random();
	                	StringBuilder sb2 = new StringBuilder(12);
                        for (int i = 0; i < 12; i++) {
                            sb2.append("0123456789abcdefghijklmnopqrz".charAt(random.nextInt(10)));
                        }
                        
                        String nomeConfig = nomeEdit.getText().toString();
			        	mMensagem = mIsProteger ? mensagemEdit.getText().toString() : "";
			       	 mOperador = mIsProteger ? OperadorString : "";
			        	mHwid = mIsProteger ? SshHwid.getText().toString() : "";
                        
                        if (nomeConfig.isEmpty()) {
                            progressDialog.dismiss();
				        	Toasty.error(ConfigExportFileActivity.this, R.string.error_empty_name_file, Toast.LENGTH_SHORT, true).show();
				        	return;
			        	}

			        	if (mIsProteger == false || mValidade < 0) {
			       	 	mValidade = 0;
			        	}
                        try {
                            exportConfiguracaotoCloud(sb2.toString());
                        } catch(IOException e) {
                            Toasty.error(ConfigExportFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                        final Handler handler = new Handler();
	                	handler.postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 progressDialog.dismiss();
                                 uploadsucess(sb2.toString());
                                 //onBackPressed();   
                             }
                       }, 1500);         
                    }
                });



                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });        
            AlertDialog dialog = builder.create();
                dialog.show();*/
				
	     }
	}




	private void exportarLayout(){
		final androidx.appcompat.app.AlertDialog alertDialog;
		final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.exportar_ly, null);

		Button btnCloud = (Button)v.findViewById(R.id.hadsButton1);
		Button btnLocal = (Button)v.findViewById(R.id.hadsButton2);
		ImageView btnCancel = (ImageView)v.findViewById(R.id.hadsButton3);
		builder.setView(v);
		alertDialog = builder.create();



		btnCloud.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
                        if (isOnline(ConfigExportFileActivity.this)) {
                            alertDialog.dismiss();
						    ly_cloud();
                        } else {
                            Toasty.error(ConfigExportFileActivity.this, "No hay Conexion a Internet", Toast.LENGTH_SHORT, true).show();
                        }
						

					}
				}
		);

		btnLocal.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						String nomeConfig = nomeEdit.getText().toString();
						mMensagem = mIsProteger ? mensagemEdit.getText().toString() : "";
						mOperador = mIsProteger ? OperadorString : "";
						mHwid = mIsProteger ? SshHwid.getText().toString() : "";
                        mAutor = mIsProteger ? autoredit.getText().toString() : "";

						if (nomeConfig.isEmpty()) {
							Toasty.error(ConfigExportFileActivity.this, R.string.error_empty_name_file, Toast.LENGTH_SHORT, true).show();
							return;
						}

						if (mIsProteger == false || mValidade < 0) {
							mValidade = 0;
						}

						try {
							exportConfiguracao(nomeConfig);
							Toasty.success(ConfigExportFileActivity.this, getString(R.string.success_export_settings) + getString(R.string.file_saved_to) + "" + fileDir, Toast.LENGTH_LONG, true).show();
						} catch (IOException e) {
							Toasty.error(ConfigExportFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();

						}
						onBackPressed();


						alertDialog.dismiss();

					}
				}
		);


		btnCancel.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();

					}
				}
		);
		alertDialog.show();

	}

	private void ly_cloud() {
        
		final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.exportar_cloud, null);
		builder.setView(v);
		exportando = builder.create();
		exportando.show();

		Random random = new Random();
		StringBuilder sb2 = new StringBuilder(12);
		for (int i = 0; i < 12; i++) {
			sb2.append("0123456789abcdefghijklmnopqrz".charAt(random.nextInt(10)));
		}

		String nomeConfig = nomeEdit.getText().toString();
		mMensagem = mIsProteger ? mensagemEdit.getText().toString() : "";
		mOperador = mIsProteger ? OperadorString : "";
		mHwid = mIsProteger ? SshHwid.getText().toString() : "";
        mAutor = mIsProteger ? autoredit.getText().toString() : "";

		if (nomeConfig.isEmpty()) {
			exportando.dismiss();
			Toasty.error(ConfigExportFileActivity.this, R.string.error_empty_name_file, Toast.LENGTH_SHORT, true).show();
			return;
		}

		if (mIsProteger == false || mValidade < 0) {
			mValidade = 0;
		}
		try {
			exportConfiguracaotoCloud(sb2.toString());
		} catch(IOException e) {
			Toasty.error(ConfigExportFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();
		}


	}
    
    private void generandolink() {
        
		final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.generando_enlace, null);
		builder.setView(v);
		generando = builder.create();
		generando.show();
	}
    
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    
    public void Exportar(Context context, String document, boolean mIsProteger, boolean mPedirSenha, boolean isBloquearRoot, String mMensagem, long mValidade, boolean mOnlyDataMovil, boolean mOnlyPlayStore, boolean mBlockOperador, String mOperador, boolean mBlockHwid, String mHwid, boolean mLoginHwid, boolean mPPayload, boolean mPProxy, boolean mPSni, boolean mPServer, boolean mPPort, boolean mPUser, boolean mPPass, String autormsg)throws IOException{
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
               exportando.dismiss();
			   throw new IOException("Server host o puerto vacio");
               
			}
        if (mIsProteger && isDefaultPayload.equals("0") && payload_config.isEmpty()) {
                exportando.dismiss();
				throw new IOException("Payload Vacio");
			}
        int tunnel_type = mPrefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
        if (tunnel_type == Settings.bTUNNEL_TYPE_SSL_PROXY) {
				if (mIsProteger && sni_config.isEmpty()) {
                    exportando.dismiss();
					throw new IOException("SNI VACIO");
				}
				} else if (tunnel_type == Settings.bTUNNEL_TYPE_SSL_RP){
				if (mIsProteger && sni_config.isEmpty()) {
                    exportando.dismiss();
					throw new IOException("SNI VACIO");
			   }
			} else if (tunnel_type == Settings.bTUNNEL_TYPE_SLOWDNS) {
			   if (mIsProteger && (chave.isEmpty() || nameserver.isEmpty() || dns.isEmpty())) {
                        exportando.dismiss();
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
        map.put("proxy_line", settings.getLineProxy());
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
        map.put("autoreplace", settings.getAutoReplace());
        
        db.collection(context.getString(R.string.app_name)).document(document).set(map).addOnSuccessListener(new OnSuccessListener<Void>()  {
            @Override
            public void onSuccess(Void unused) {
                exportando.dismiss();
                crearEnlace(document);
                /**DialogFragment dialog = new ImportOnlineFragment();
                dialog.show(getSupportFragmentManager(), "import"); **/     
                //Toast.makeText(context, "Creado exitosamente", Toast.LENGTH_SHORT).show();
               //finish();
                    
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e)  {
                exportando.dismiss();    
                //Toasty.error(context, "Ocurrio un error no se pudo guardar la configuracion", Toast.LENGTH_SHORT, true).show();         
                Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();    
                return;
                //Toast.makeText(context, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
      
        
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
    
    public void crearEnlace(String document) {
        generandolink();
		Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://elcris.coservers.com?token=" + document))
                .setDomainUriPrefix("https://elcris.coservers.com")//paginaweb
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.elcris.coservers")
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.elcris.coservers")
                                .setAppStoreId("whatever")
                                .setMinimumVersion("1.0.1")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("CO Servers")
                                .setDescription("Aplicacion multiprotocolo VPN,SSH,WS,SLOWDNS")
                                .setImageUrl(Uri.parse("https://previews.dropbox.com/p/thumb/AB1qvTmcpQEmtR4zcgYF6PaQ5Df0WHOcgsrmHMwL_qLIoBiGVK2HhfTPvQSRHeOzmj5FOc3LunuJJNFyslmnMAED1K6Egf3iXYC-eCyWQmtpqMHcIb84tstAOGNkLVCQFeJogiam7HSxw5avJzGxUcsbELMxVQtLgraI9UqTiEbgmWdmFYL4UnNKbbenx4OkXGk0SqYgWUAa9RHBwaZtFeLbF2qSCXUQVg1NxskOdomKY3wBuxN8c-b7ZgRBpD-SjmrtFwXpfgMX-aVm9BPjjOKJdEkhPuI9jy9d0BQwNrORBHtTdveQPQpx8niqOryFTmWzV7KkbNmDu8IHE5iJdkoj1-Rq27FVlCbcE2H_oJXOVU_rYIMVKkADl-FHkezde4M/p.png"))
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            generando.dismiss();
                            String enlace = task.getResult().getShortLink().toString();
                            copytoken(document, enlace);
                            // Short link created
                        } else {
                            generando.dismiss();
                            Toasty.error(ConfigExportFileActivity.this, task.getException().toString(), Toast.LENGTH_LONG, true).show();
                            //Log.d("WALKIRIA", "ERROR " + task.getException());
                        }
                    }
                });
	}


}
