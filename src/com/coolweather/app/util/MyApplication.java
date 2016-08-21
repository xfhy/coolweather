package com.coolweather.app.util;

import android.app.Application;
import android.content.Context;

/**
 * 2016��8��21��9:27:44
 * 
 * ȫ�ֻ�ȡContext�ļ���   ��������ڿ���ȫ�ֻ�ȡContext
 * 
 * @author XFHY
 * 
 *  ��ʱ����Ҫ�õ�Context,����Toast,�����,���͹㲥,�������ݿ�,ʹ��֪ͨ��ʱ����ҪContext
 * ����ʱ���ȡContext�е��Ѷ�.���������и��������Խ����������.
 * 
 * Android�ṩ��һ��Application��,ÿ��Ӧ�ó���������ʱ��,ϵͳ�ͻ��Զ���������ʼ��.
 * �����ǿ��Զ���һ���Լ���Application��,�Ա��ڹ��������һЩȫ�ֵ�״̬��Ϣ,����˵Context
 * 
 * ����һ���Լ���Application,��Ҫ�̳���Application,��onCreate()�����г�ʼ���õ�һ��Ӧ�ü����Context,�½�һ����̬����,
 * ���Է���Context;  ��������Ҫ��֪ϵͳ,����������ʱӦ�ó�ʼ��MyApplication��,������Ĭ�ϵ�Application��.��AndroidManifest.xml
 * �ļ���<application>��ǩ�½���ָ���Ϳ�����.
 * 
 */
public class MyApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		//ͨ������getApplicationContext()�����õ�һ��Ӧ�ü����Context
		context = getApplicationContext();
	}
	
	//��̬����ȫ�ֶ��ɵ�������ȡContext
	public static Context getContext(){
		return context;
	}
	
}
