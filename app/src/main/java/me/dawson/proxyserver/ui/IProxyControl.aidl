package com.vpn.wifi.ui;

interface IProxyControl {
	boolean start();
	
	boolean stop();
	
	boolean isRunning();
	
	int getPort();
}