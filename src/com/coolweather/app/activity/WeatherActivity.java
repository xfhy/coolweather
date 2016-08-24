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
 * 2016年8月23日8:49:12
 * 
 * 显示天气信息的活动
 * 
 * @author XFHY
 * 
 */
public class WeatherActivity extends Activity {
	
	private LinearLayout weatherInfoLayout;
	private LinearLayout wendu_layout;
	 /**
	  * 用于显示城市名
	  */
	private TextView cityNameText;
	/**
	 * 用于显示当前日期
	 */
	private TextView currentDateText;
	/**
	 * 当前温度
	 */
	private TextView currentTemp;
	/**
	 * 风向信息
	 */
	private TextView windDirect;
	/**
	 * 风力信息
	 */
	private TextView windPower;
	/**
	 * 用于显示天气描述信息
	 */
	private TextView weatherDespText;
	/**
	 * 用于显示气温1
	 */
	private TextView lowTempText;
	/**
	 * 用于显示气温2
	 */
	private TextView highTempText;
	/**
	 * 显示今天,明天,后天天气预报的ListView
	 */
	private ListView listview;
	DayWeaAdapter adapter;// = new DayWeaAdapter<DayWeather>();
	private List<DayWeather> dayWeatherList = new ArrayList<DayWeather>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//初始化各控件
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
			//有县级代号时就去查询天气
			currentDateText.setText("同步中...");
			currentTemp.setVisibility(View.INVISIBLE);
			weatherDespText.setVisibility(View.INVISIBLE);
			wendu_layout.setVisibility(View.INVISIBLE);
			listview.setVisibility(View.INVISIBLE);
			
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			//没有县级代号时就直接显示本地天气
			showWeather();
		}
	}

	/**
	 * 查询县级代号所对应的天气代号
	 * @param countyCode  县级代号
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		Toast.makeText(getApplicationContext(),
				"查询县级代号所对应的天气代号"+countyCode, Toast.LENGTH_SHORT).show();
		queryFromServer(address, "countyCode");
	}
	
	/**
	 * 查询天气代号所对应的天气
	 * @param weatherCode   天气代号  
	 */
	private void queryWeatherInfo(String weatherCode){
		
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+weatherCode;
		Log.d("xfhy","查询天气代号所对应的天气"+weatherCode);
		
		queryFromServer(address, "weatherCode");
	}
	
	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	 * @param address   地址
	 * @param string    类型
	 */
	private void queryFromServer(final String address, final String type) {
		//给服务器发送请求
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			/*这里用到了java的回调机制,sendHttpRequest()方法里面开启线程并接收服务器返回的数据,这个数据放到
			*HttpCallbackListener接口的onFinish()参数response中,在这里即可获取到正确的数据
			*而将错误的信息放到onError()方法中
			**/
			
			@Override
			public void onFinish(final String response) {
				//Toast.makeText(getApplicationContext(),
						//"服务器已将返回的数据通过onFinish()方法返回", Toast.LENGTH_SHORT).show();
				if("countyCode".equals(type)){   //如果传入的类型是县级代号
					if(!TextUtils.isEmpty(response)){  //如果服务器返回的数据不为空
						//从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){   //服务器的数据是    县级代号|天气代号   县级只有一个信息
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);   //将天气代号传入  查询天气代号所对应的天气
						}
					} 
				} else if("weatherCode".equals(type)){
					//处理服务器返回的天气信息              解析服务器返回的JSON数据,并将解析出来的数据存储到本地      
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					
					//现在这里是子线程中,需要更新UI的话需要切换到主线程
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							showWeather();
							Toast.makeText(getApplicationContext(),
									"更新天气", Toast.LENGTH_SHORT).show();
						}
						
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						currentDateText.setText("同步失败");
						Toast.makeText(getApplicationContext(),
								"请求服务器失败,并返回到了onError()方法中", Toast.LENGTH_SHORT).show();
					}
					
				});
			}
		});
	}

	/**
	 * 从SharedPreferences文件中读取存储的天气信息,并显示到界面上
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));   //第一个参数是键,第二个参数是默认值      城市名
		currentTemp.setText(prefs.getString("current_temp", "")+"°C"); //当前温度
		windDirect.setText(prefs.getString("wind_direct", ""));   //风向
		windPower.setText(prefs.getString("wind_power", ""));     //风力
		lowTempText.setText(prefs.getString("low_temp", ""));     //最低温度
		highTempText.setText(prefs.getString("high_temp", ""));   //最高温度
		weatherDespText.setText(prefs.getString("weather_desp", ""));   //天气类型
		currentDateText.setText(prefs.getString("current_date", ""));   //当前日期
		
		ListViewDB listViewDB = new ListViewDB();
		dayWeatherList = listViewDB.loadDayWeather();    //读取DayWeather数据到ListView中
		adapter = new DayWeaAdapter(WeatherActivity.this, R.layout.dayitem, dayWeatherList);
		listview.setAdapter(adapter);
		//adapter.notifyDataSetChanged();
		
		currentTemp.setVisibility(View.VISIBLE);
		weatherDespText.setVisibility(View.VISIBLE);
		wendu_layout.setVisibility(View.VISIBLE);
		listview.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);   //设置可见
		cityNameText.setVisibility(View.VISIBLE);
	}

	/**
	 * 用于其他活动启动当前这个活动     这是启动活动的最佳写法
	 * @param context    想启动当前活动的活动context
	 * @param countyCode 县级代号
	 */
	public static void actionStart(Context context,String countyCode){
		Intent intent = new Intent(context,WeatherActivity.class);
		intent.putExtra("county_code", countyCode);
		context.startActivity(intent);
	}
	
}
