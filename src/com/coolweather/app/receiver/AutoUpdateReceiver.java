package com.coolweather.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 2016��8��26��19:56:35
 * ���Ǹ��㲥,��������AutoUpdateService����
 * @author XFHY
 * �㲥��Ҫȥע��
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context,AutoUpdateReceiver.class);
		context.startService(i);   //��������������
	}

}
