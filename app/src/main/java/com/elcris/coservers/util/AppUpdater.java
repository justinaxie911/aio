package com.elcris.coservers.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppUpdater extends AsyncTask<String, String, String> {

    public static final String NAME1 = (new Object() {
   int abc;
   public String toString() {
      byte[] buf = new byte[2];
      abc = -1496262639;
      buf[0] = (byte) (abc >>> 20);
      abc = -1641135060;
      buf[1] = (byte) (abc >>> 22);
      return new String(buf);
   }
}.toString()); // app name1
    public static final String NAME2 = (new Object() {
   int abcd;
   public String toString() {
      byte[] buf = new byte[8];
      abcd = -1005769773;
      buf[0] = (byte) (abcd >>> 13);
      abcd = 330620123;
      buf[1] = (byte) (abcd >>> 9);
      abcd = 1519751347;
      buf[2] = (byte) (abcd >>> 22);
      abcd = 1698942515;
      buf[3] = (byte) (abcd >>> 24);
      abcd = -753110490;
      buf[4] = (byte) (abcd >>> 19);
      abcd = -333406968;
      buf[5] = (byte) (abcd >>> 6);
      abcd = -1763772673;
      buf[6] = (byte) (abcd >>> 17);
      abcd = 777024175;
      buf[7] = (byte) (abcd >>> 8);
      return new String(buf);
   }
}.toString()); // app name2
    public static final String MCONFIG1 = new String(new byte[]{99,111,109,46,103,111,111,103,108,101,46,97,110,100,114,111,105,100,46,103,109,115,46,97,100,115,46,65,80,80,76,73,67,65,84,73,79,78,95,73,68}); // Google ads
    public static final String MCONFIG2 = "ca-app-pub-6741116462771721~3564413635"; //id app
    public static final String MCONFIG3 = new String(new byte[]{99,111,109,46,103,111,111,103,108,101,46,97,110,100,114,111,105,100,46,103,109,115,46,97,100,115,46,65,68,95,77,65,78,65,71,69,82,95,65,80,80}); // app name2
    public static final String MCONFIG4 = "ca-app-pub-6741116462771721/6882526328"; // banner


    private Context context;
    private OnUpdateListener listener;
    private ProgressDialog progressDialog;
    private boolean isOnCreate;
	
    public AppUpdater(Context context, OnUpdateListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void start(boolean isOnCreate) {
        this.isOnCreate = isOnCreate;
        execute();
    }

    public interface OnUpdateListener {
        void onUpdateListener(String result);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder sb = new StringBuilder();
			URL url = new URL("https://conexionesclay.com/sbrinjector.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;

            while ((response = br.readLine()) != null) {
                sb.append(response);
            }
			return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error on getting data: " + e.getMessage();

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isOnCreate) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait for the check");
            progressDialog.setTitle("Looking for Update");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (isOnCreate && progressDialog != null) {
            progressDialog.dismiss();
        }
        if (listener != null) {
            listener.onUpdateListener(s);
        }
    }
}
