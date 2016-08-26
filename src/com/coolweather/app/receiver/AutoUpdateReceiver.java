package com.coolweather.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 2016年8月26日19:56:35
 * 这是个广播,用于启动AutoUpdateService服务
 * @author XFHY
 * 广播需要去注册
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context,AutoUpdateReceiver.class);
		context.startService(i);   //这里是启动服务
	}

}
