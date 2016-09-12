package com.coolweather.app.receiver;

import com.coolweather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 2016��8��26��19:56:35
 * 
 * ���Ǹ��㲥,��������AutoUpdateService����
 * 
 * @author XFHY
 * 
 * �㲥��Ҫȥע��
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//����:Context,������
		//Intent i = new Intent(context,com.coolweather.app.service.AutoUpdateService.class);
		Intent i = new Intent(context,AutoUpdateService.class);
		
		context.startService(i);   //��������������
	}

}
