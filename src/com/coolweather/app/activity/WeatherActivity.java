package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.db.ListViewDB;
import com.coolweather.app.model.DayWeaAdapter;
import com.coolweather.app.model.DayWeather;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 2016��8��23��8:49:12
 * 
 * ��ʾ������Ϣ�Ļ
 * 
 * @author XFHY
 * 
 */
public class WeatherActivity extends Activity {
	
	private LinearLayout weatherInfoLayout;
	private LinearLayout wendu_layout;
	 /**
	  * ������ʾ������
	  */
	private TextView cityNameText;
	/**
	 * ������ʾ��ǰ����
	 */
	private TextView currentDateText;
	/**
	 * ��ǰ�¶�
	 */
	private TextView currentTemp;
	/**
	 * ������Ϣ
	 */
	private TextView windDirect;
	/**
	 * ������Ϣ
	 */
	private TextView windPower;
	/**
	 * ������ʾ����������Ϣ
	 */
	private TextView weatherDespText;
	/**
	 * ������ʾ����1
	 */
	private TextView lowTempText;
	/**
	 * ������ʾ����2
	 */
	private TextView highTempText;
	/**
	 * ��ʾ����,����,��������Ԥ����ListView
	 */
	private ListView listview;
	DayWeaAdapter adapter;// = new DayWeaAdapter<DayWeather>();
	private List<DayWeather> dayWeatherList = new ArrayList<DayWeather>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		wendu_layout = (LinearLayout)findViewById(R.id.wendu_layout);
		cityNameText = (TextView)findViewById(R.id.city_name);
		currentDateText = (TextView)findViewById(R.id.current_date);
		currentTemp = (TextView)findViewById(R.id.current_temp);
		windDirect = (TextView)findViewById(R.id.wind_direct);
		windPower = (TextView)findViewById(R.id.wind_power);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		lowTempText = (TextView)findViewById(R.id.low_temp);
		highTempText = (TextView)findViewById(R.id.high_temp);
		listview = (ListView)findViewById(R.id.weath_listview);
		/*adapter = new DayWeaAdapter(WeatherActivity.this, R.layout.dayitem, dayWeatherList);
		listview.setAdapter(adapter);*/
		
		String countyCode = getIntent().getStringExtra("county_code");
		
		if(!TextUtils.isEmpty(countyCode)){
			//���ؼ�����ʱ��ȥ��ѯ����
			currentDateText.setText("ͬ����...");
			currentTemp.setVisibility(View.INVISIBLE);
			weatherDespText.setVisibility(View.INVISIBLE);
			wendu_layout.setVisibility(View.INVISIBLE);
			listview.setVisibility(View.INVISIBLE);
			
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			//û���ؼ�����ʱ��ֱ����ʾ��������
			showWeather();
		}
	}

	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 * @param countyCode  �ؼ�����
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		Toast.makeText(getApplicationContext(),
				"��ѯ�ؼ���������Ӧ����������"+countyCode, Toast.LENGTH_SHORT).show();
		queryFromServer(address, "countyCode");
	}
	
	/**
	 * ��ѯ������������Ӧ������
	 * @param weatherCode   ��������  
	 */
	private void queryWeatherInfo(String weatherCode){
		
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+weatherCode;
		Log.d("xfhy","��ѯ������������Ӧ������"+weatherCode);
		
		queryFromServer(address, "weatherCode");
	}
	
	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ
	 * @param address   ��ַ
	 * @param string    ����
	 */
	private void queryFromServer(final String address, final String type) {
		//����������������
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			/*�����õ���java�Ļص�����,sendHttpRequest()�������濪���̲߳����շ��������ص�����,������ݷŵ�
			*HttpCallbackListener�ӿڵ�onFinish()����response��,�����Ｔ�ɻ�ȡ����ȷ������
			*�����������Ϣ�ŵ�onError()������
			**/
			
			@Override
			public void onFinish(final String response) {
				//Toast.makeText(getApplicationContext(),
						//"�������ѽ����ص�����ͨ��onFinish()��������", Toast.LENGTH_SHORT).show();
				if("countyCode".equals(type)){   //���������������ؼ�����
					if(!TextUtils.isEmpty(response)){  //������������ص����ݲ�Ϊ��
						//�ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){   //��������������    �ؼ�����|��������   �ؼ�ֻ��һ����Ϣ
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);   //���������Ŵ���  ��ѯ������������Ӧ������
						}
					} 
				} else if("weatherCode".equals(type)){
					//������������ص�������Ϣ              �������������ص�JSON����,�����������������ݴ洢������      
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					
					//�������������߳���,��Ҫ����UI�Ļ���Ҫ�л������߳�
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							showWeather();
							Toast.makeText(getApplicationContext(),
									"��������", Toast.LENGTH_SHORT).show();
						}
						
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						currentDateText.setText("ͬ��ʧ��");
						Toast.makeText(getApplicationContext(),
								"���������ʧ��,�����ص���onError()������", Toast.LENGTH_SHORT).show();
					}
					
				});
			}
		});
	}

	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ,����ʾ��������
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));   //��һ�������Ǽ�,�ڶ���������Ĭ��ֵ      ������
		currentTemp.setText(prefs.getString("current_temp", "")+"��C"); //��ǰ�¶�
		windDirect.setText(prefs.getString("wind_direct", ""));   //����
		windPower.setText(prefs.getString("wind_power", ""));     //����
		lowTempText.setText(prefs.getString("low_temp", ""));     //����¶�
		highTempText.setText(prefs.getString("high_temp", ""));   //����¶�
		weatherDespText.setText(prefs.getString("weather_desp", ""));   //��������
		currentDateText.setText(prefs.getString("current_date", ""));   //��ǰ����
		
		ListViewDB listViewDB = new ListViewDB();
		dayWeatherList = listViewDB.loadDayWeather();    //��ȡDayWeather���ݵ�ListView��
		adapter = new DayWeaAdapter(WeatherActivity.this, R.layout.dayitem, dayWeatherList);
		listview.setAdapter(adapter);
		//adapter.notifyDataSetChanged();
		
		currentTemp.setVisibility(View.VISIBLE);
		weatherDespText.setVisibility(View.VISIBLE);
		wendu_layout.setVisibility(View.VISIBLE);
		listview.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);   //���ÿɼ�
		cityNameText.setVisibility(View.VISIBLE);
	}

	/**
	 * ���������������ǰ����     ��������������д��
	 * @param context    ��������ǰ��Ļcontext
	 * @param countyCode �ؼ�����
	 */
	public static void actionStart(Context context,String countyCode){
		Intent intent = new Intent(context,WeatherActivity.class);
		intent.putExtra("county_code", countyCode);
		context.startActivity(intent);
	}
	
}
