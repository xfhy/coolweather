package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * ��λ�Լ���λ��
 * 
 * @author XFHY
 * 
 */
public class LocationYourPosition {
	/**
	 * LocationManager����
	 */
	public static LocationManager locationManager;
	/**
	 * λ���ṩ��
	 */
	public static String provider;

	/**
	 * ���������ص�����
	 */
	public static StringBuilder resultData = new StringBuilder();
	
	/**
	 * γ��
	 */
	public static String latitude;
	
	/**
	 * ����
	 */
	public static String longitude;
	
	/**
	 * ���췽��
	 */
	public LocationYourPosition() {
	}

	/**
	 * ��ʼ��λ��,��ȡ��γ�� �ж��Ƿ��ֻ��ѿ���GPS����������
	 */
	public static void getLocationXY() {
		// ��ȡ��LocationManager��ʵ��
		locationManager = (LocationManager) MyApplication.getContext()
				.getSystemService(Context.LOCATION_SERVICE);
		// ��ȡ���п��õ�λ���ṩ��
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			//Log.d("xfhy", "�ѿ���GPS");
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
			//Log.d("xfhy", "�ѿ���NETWORK_PROVIDER");
		} else {
			// ��û�п��õ�λ���ṩ��ʱ,����Toast��ʾ�û�
			Toast.makeText(MyApplication.getContext(), "��,��ȷ���ѿ���GPS����������",
					Toast.LENGTH_LONG).show();
			return;
		}

		// ��ѡ��õ�λ���ṩ������getLastKnownLocation()�м��ɵõ�һ��Location����
		// �ö����Ѱ�������,γ��,���ε�һϵ����Ϣ
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			latitude = String.valueOf(location.getLatitude());
			longitude = String.valueOf(location.getLongitude());
			showLocation(location);
		} else {
			Toast.makeText(MyApplication.getContext(), "��,��λʧ��~�뵽�����ĵط�",
					Toast.LENGTH_LONG).show();
		}

		// ���λ�ü����� ����:λ���ṩ��,ʱ����,λ�ü��,������
		locationManager.requestLocationUpdates(provider, 5000, 1,
				locationListener);
	}

	// ʵ���� λ�ü�����
	static LocationListener locationListener = new LocationListener() {

		// ��λ�÷����ı�ʱ
		@Override
		public void onLocationChanged(Location location) {
			Toast.makeText(MyApplication.getContext(), "��,λ���Ѿ��仯��",
					Toast.LENGTH_LONG).show();
			latitude = String.valueOf(location.getLatitude());
			longitude = String.valueOf(location.getLongitude());
			// �Լ�д�ķ���,��ʾ��ǰλ��
			showLocation(location);
			//getDistrict();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

	};

	/**
	 * ��ʾ��ǰλ����Ϣ
	 * 
	 * @param location
	 */
	private static void showLocation(Location location) {
		String currentPosition = "��ǰ��λ����  γ��:" + location.getLatitude() + "\n"
				+ "����:" + location.getLongitude();
		//Log.d("xfhy", "γ��:" + location.getLatitude());
		//Log.d("xfhy", "����:" + location.getLongitude());
		Toast.makeText(MyApplication.getContext(), currentPosition,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * �ر�λ�ü�����
	 */
	public static void onDestroy() {
		if (locationManager != null) {
			// �رռ�����
			locationManager.removeUpdates(locationListener);
		}
	}

	/**
	 * ��ȡ��ǰ��������     �������������ص�json����
	 * @return 
	 */
	public static String getDistrict(String reallyResultData) {
		Log.d("xfhy","getDistrict()");
		String district  = null;
		//ԭʼ���ص���������renderReverse&&renderReverse(....)   ��Ҫ�������������ȥ��
		try {
			int indexOfSmall1 = reallyResultData.indexOf("(");
			int indexOfSmall2 = reallyResultData.lastIndexOf(")");
			Log.d("xfhy","indexOfSmall1 :"+indexOfSmall1);
			Log.d("xfhy","indexOfSmall2 :"+indexOfSmall2);
			reallyResultData = reallyResultData.substring(indexOfSmall1+1, indexOfSmall2);
			Log.d("xfhy","reallyResultData :"+reallyResultData);
			JSONObject jsonObject = new JSONObject(reallyResultData);
			JSONObject jsonResult = jsonObject.getJSONObject("result");
			JSONObject jsonAddressComponent = jsonResult.getJSONObject("addressComponent");
			district = jsonAddressComponent.getString("district");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("xfhy","���� : "+district);
		return district;
	}
	
	/**
	 * �ӷ������˻�ȡ��ǰλ��json����
	 * @param httpUrl
	 */
	public static void getRequest(final HttpCallbackListener listener){
		// ak IOOZbu8un3e9laZTXCtYqoAOoZHdMft4
		/*
		 * http://api.map.baidu.com/geocoder/v2/?
		 * ak=IOOZbu8un3e9laZTXCtYqoAOoZHdMft4& mcode=1A:2C:32:
		 * 7E:1B:9C:16:63:B5:F3:7A:AB:03:E3:62:0B:BF:05:F6:6E;com.coolweather.app.util&
		 * callback=renderReverse& location=39.983424,116.322987& output=json&
		 * pois=1
		 */
		//����
		final String httpUrl = "http://api.map.baidu.com/geocoder/v2/?"
				+ "ak=IOOZbu8un3e9laZTXCtYqoAOoZHdMft4&"
				+ "mcode=1A:2C:32:7E:1B:9C:16:63:B5:F3:7A:AB:03:E3:62:0B:BF:05:F6:6E;com.coolweather.app.util&"
				+ "callback=renderReverse&location=" + latitude + ","
				+ longitude + "&output=json&pois=1";
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(httpUrl);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");    //����URL����ķ���
					connection.setConnectTimeout(8000);    //����һ������ָ���ĳ�ʱֵ���Ժ���Ϊ��λ��
					connection.setReadTimeout(8000);       //������ʱ����Ϊָ���ĳ�ʱֵ���Ժ���Ϊ��λ
					
					//���������ص����������������ķ�ʽ
					InputStream in = connection.getInputStream();
					//���ֽ���ת�����ַ���
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					
					String line = "";
					while( (line=reader.readLine()) != null ){  //��ȡ������������,һ��һ�еض�
						resultData.append(line);
					}
					Log.d("xfhy","resultData :"+resultData.toString());
					
					//�����������ص����ݷŵ���HttpCallbackListenerʵ����onFinish()��,java�Ļص����ƻ��õ���������������ȡ������
					if(listener != null){
						listener.onFinish(resultData.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					//�����ӷ�����ʱ�Ĵ�����Ϣ�ŵ�HttpCallbackListener��ʵ����onError()��,
					//java�Ļص����ƻ��õ���sendHttpRequest()������������ȡ������
					if( listener != null ){
						listener.onError(e);
					}
				} finally {
					//���,�ǵöϿ�
					if(connection != null){
						connection.disconnect();
					}
				}
			}
			
		}).start();
		
	}
	
}
