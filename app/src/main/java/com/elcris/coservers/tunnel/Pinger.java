package com.elcris.coservers.tunnel;

import android.content.Context;
import android.os.Handler;
import com.elcris.coservers.config.Settings;
import com.elcris.coservers.logger.SkStatus;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Pinger {
    private Context context;
    private Settings config;
    private boolean loadpinger = false;
    private boolean iniciando = true;
    private Handler handler;
    
    public Pinger(Context c, Handler h) {
        this.context = c;
        this.handler = h;
        config = new Settings(context);
        
    }
    
    public void startping() {
        loadpinger = true;
        Thread pingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (loadpinger != false) {
                    try {
                        URL url = new URL("https://clients3.google.com");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("HEAD");
                        long starttime = System.currentTimeMillis();
                        int response = connection.getResponseCode();
                        long endtime = System.currentTimeMillis();
                                    
                        if (response == HttpURLConnection.HTTP_OK) {
                            long pingtime = endtime - starttime;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String finalping = "";     
                                    if (pingtime < 100) {
                                        finalping = "<font color='#00FF00'>" + pingtime + "</font>";
                                    } else if (pingtime < 200) {
                                        finalping = "<font color='#7FFF00'>" + pingtime + "</font>";
                                    } else if (pingtime < 300) {
                                        finalping = "<font color='#FFFF00'>" + pingtime + "</font>";
                                    } else if (pingtime < 400) {
                                        finalping = "<font color='#FFD700'>" + pingtime + "</font>";
                                    } else if (pingtime < 500) {
                                        finalping = "<font color='#FFA500'>" + pingtime + "</font>";        
                                    } else if (pingtime < 600) {
                                        finalping = "<font color='#FF7F50'>" + pingtime + "</font>";
                                    } else if (pingtime < 700) {
                                        finalping = "<font color='#FF6347'>" + pingtime + "</font>";
                                    } else if (pingtime < 800) {
                                        finalping = "<font color='#FF4500'>" + pingtime + "</font>";
                                    } else if (pingtime < 900) {
                                        finalping = "<font color='#FF0000'>" + pingtime + "</font>";
                                    } else {
                                        finalping = "<font color='#FF0000'>" + pingtime + "</font>";
                                    }
                                    SkStatus.logInfo("HTTP Ping" + " (" + response + "OK" + ") " + finalping + " ms");
                                }
                            });    
                        }
                        Thread.sleep(1000);  
                    } catch (IOException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (iniciando)  {
                                    iniciando = false;
                                } else {
                                    SkStatus.logError("Error: " + e.getMessage());
                                }      
                            }    
                        });
                    } catch (InterruptedException e) {
                        loadpinger = false;
                        iniciando = true;    
                    }
                }
            } 
        });
        pingThread.start();
    }
    
    public void stotpinger() {
        loadpinger = false;
        iniciando = true;
    }
}