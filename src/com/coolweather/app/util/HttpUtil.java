package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.widget.Toast;


/**
 * 2016��8��21��8:41:26
 * 
 * ��������ͷ��������н���
 * 
 * @author XFHY
 * 
 *  һ��Ӧ�ó���ܿ��ܻ��ںܶ�ط���ʹ�õ����繦��,������HTTP����Ĵ������������ͬ��
 * ����,ͨ�������,���Ƕ�Ӧ�ý���Щͨ�õ����������ȡ��һ��������������,���ṩһ����̬����,����
 * Ҫ�������������ʱ��ֻ��򵥵ص���һ�������������.
 * 
 * �ڻ�ȡ��������Ӧ�����ݺ����ǾͿ��Զ������н����ʹ�����.������Ҫע��,��������ͨ�������ں�ʱ����,��sendHttpRequest()�������ڲ�
 * ��û�п�ʼ�̵߳Ļ�,�������п��ܵ����ڵ���sendHttpRequest()������ʱ��ʹ�����̱߳�����ס.
 * 
 * ������Ҫ�õ����߳�,���߳�ȥִ�о�����������,ע�����߳����޷���return������������ݵ�.����������ǽ���������Ӧ�����ݴ�����
 * �Լ�д��HttpCallbackListener��onFinish()��������,����������쳣�ʹ���onError()������.
 * ��ʹ��java�Ļص����ƾͿ�����.
 * 
 * �����߳���ʹ��Toast��ʱ��,��Can't create handler inside thread that has not called Looper.prepare()����ʱ,
 * ��Ҫ��Toastǰ�����Looper.prepare();��Looper.loop();
 * 
 */
public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener){
		
		//�����ж�һ�µ�ǰ�����Ƿ��������  �����ԵĻ�,ֱ�Ӳ���ִ������Ĵ���(�����߳����ӷ�����)��
		if( !isNetworkAvailable() ){
			Looper.prepare();  //��Ҫ����Looper.prepare()�����̴߳���һ����Ϣѭ��
			Toast.makeText(MyApplication.getContext(), "�޿�������", Toast.LENGTH_SHORT).show();
			Looper.loop();     //��Looper��ʼ����������Ϣ������ȡ��Ϣ��������Ϣ��
			return ;
		}
		
		//�����߳�
		new Thread(new Runnable(){

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try{
					URL url = new URL(address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");   //���� URL ����ķ���
					connection.setConnectTimeout(8000);   //����һ������ָ���ĳ�ʱֵ���Ժ���Ϊ��λ��
					connection.setReadTimeout(8000);      //������ʱ����Ϊָ���ĳ�ʱֵ���Ժ���Ϊ��λ
					
					InputStream in = connection.getInputStream();   //���������ص����������������ķ�ʽ
					//���ֽ���ת�����ַ���
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuffer response = new StringBuffer();  //���ڴ�ŷ��������ص�����
					String line;
					while( (line=reader.readLine()) != null ){  //��ȡ������������,һ��һ�еض�
						response.append(line);
					}
					
					//�����������ص����ݷŵ���HttpCallbackListenerʵ����onFinish()��,java�Ļص����ƻ��õ���������������ȡ������
					if( listener != null ){   
						listener.onFinish(response.toString());
					}
				} catch(Exception e){
					//�����ӷ�����ʱ�Ĵ�����Ϣ�ŵ�HttpCallbackListener��ʵ����onError()��,
					//java�Ļص����ƻ��õ���sendHttpRequest()������������ȡ������
					if( listener != null ){
						listener.onError(e);
					}
				} finally {
					if( connection != null ){
						connection.disconnect();   //���,�ǵöϿ�����
					} 
				}
			}
			
		}).start();
	}
	
	/**
	 * �ж������Ƿ�����   
	 * �򵥵��ж������Ƿ�����,����һЩ������������ж�,���� �������ƶ����絫�޷�����,������wifi���޷�������.
	 * @return �������򷵻�true,���򷵻�false(�޿�������)
	 */
	public static boolean isNetworkAvailable() {
		
		//������Ҫ����Ȩ��
		//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
		//�õ���������
		ConnectivityManager manager = (ConnectivityManager)
				MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		//ȥ�ж������Ƿ�����
		if(manager.getActiveNetworkInfo() != null){
			return manager.getActiveNetworkInfo().isAvailable();  //�������������Ƿ����
		}
		return false;
	}
	
}
