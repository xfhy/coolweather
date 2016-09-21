package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Position;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.BaseActivity;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.LocationYourPosition;
import com.coolweather.app.util.LogUtil;
import com.coolweather.app.util.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 2016年8月21日11:19:53
 * 
 * 这个活动用于遍历省市县数据的活动了
 * 
 * @author XFHY
 * 
 */
public class ChooseAreaActivity extends BaseActivity {

	public static final int LEVEL_PROVINCE = 0; // 级别 用于判断当前界面是在哪一个级别 省,市,县
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	/**
	 * 是否从WeatherActivity中跳转过来
	 */
	private boolean isFromWeatherActivity;
	/**
	 * 进度对话框
	 */
	private ProgressDialog progressDialog;
	/**
	 * 标题
	 */
	private TextView titleText;
	/**
	 * 用于设置的Button
	 */
	private Button settingBtn;
	/**
	 * 用于定位的Button
	 */
	private Button locationBtn;
	/**
	 * ListView组件
	 */
	private ListView listView;
	/**
	 * ListView的适配器
	 */
	private ArrayAdapter<String> adapter;
	/**
	 * 用来操作数据库的
	 */
	private CoolWeatherDB coolWeatherDB;
	/**
	 * ListView的专用集合数据
	 */
	private List<String> dataList = new ArrayList<String>();

	/**
	 * 省列表
	 */
	private List<Province> provinceList;
	/**
	 * 市列表
	 */
	private List<City> cityList;
	/**
	 * 县列表
	 */
	private List<County> countyList;
	/**
	 * 选中的省份
	 */
	private Province selectedProvince;
	/**
	 * 当前选中的省份在ListView中的位置
	 */
	private int selecProvinPosition = -1;
	/**
	 * 选中的城市
	 */
	private City selectedCity;
	/**
	 * 当前选中城市ListView中的位置
	 */
	private int selecCityPosition = -1;
	/**
	 * 当前选中的级别
	 */
	private int currentLevel;
	/**
	 * 当前自己的位置
	 */
	LocationYourPosition myPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * 参数说明： appId 和 appSecret 分别为应用的发布 ID 和密钥，由有米后台自动生成，通过在有米后台 > 应用详细信息
		 * 可以获得。 isTestModel : 是否开启测试模式，true 为是，false 为否。（上传有米审核及发布到市场版本，请设置为
		 * false） isEnableYoumiLog: 是否开启有米的Log输出，默认为开启状态
		 * 上传到有米主站进行审核时，务必开启有米的Log，这样才能保证通过审核 开发者发布apk到各大市场的时候，强烈建议关闭有米的Log
		 */
		//AdManager.getInstance(getBaseContext()).init("89a962f77ebf6c0c",
				//"7fab8966d2360133", false, true);
		
		//首先判断是否是从WeatherActivity跳转过来的   默认值是false
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		
		//先读取配置文件
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//如果之前选择过城市且不是从WeatherActivity跳转过来的,则直接跳过这个活动   直接到显示天气信息的活动
		if(prefs.getBoolean("city_selected", false) && !isFromWeatherActivity){   
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return ;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);   //设置全屏
		setContentView(R.layout.choose_ares);
		
		settingBtn = (Button)findViewById(R.id.setting);
		titleText = (TextView) findViewById(R.id.title);
		locationBtn = (Button)findViewById(R.id.location_btn);
		listView = (ListView) findViewById(R.id.list_view);

		settingBtn.setOnClickListener(new SettingListener());
		locationBtn.setOnClickListener(new LocationMyPositionListener());  //设置定位按钮监听器
		
		// 设置ListView适配器,数据为dataList集合提供
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);

		// 获得CoolWeatherDB实例
		coolWeatherDB = CoolWeatherDB.getInstance(this);

		// 设置ListView Item选项点击监听器
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) { // 当前选中的级别是省级 则加载该省城市数据
					selectedProvince = provinceList.get(position); // 获取选中的省份
					selecProvinPosition = position;  //记录当前选择的省份在ListView中的位置
					queryCities(); // 加载城市数据
				} else if (currentLevel == LEVEL_CITY) { // 当选中级别是城市 则加载该城市县级数据
					selectedCity = cityList.get(position); // 获取选中的城市
					selecCityPosition = position;   //记录当前选择的城市在ListView中的位置
					queryCounties(); // 加载县级数据
				} else if(currentLevel == LEVEL_COUNTY){  // 当选中级别是县   则启动显示天气信息的活动
					 String countyCode = countyList.get(position).getCountyCode();
					 WeatherActivity.actionStart(ChooseAreaActivity.this, countyCode);  //启动活动的最佳写法
					 LocationYourPosition.onDestroy();   //关闭位置监听器
				}
			}

		});
		
		queryProvinces(); // 加载省级数据 最初是加载的省级数据
		
	}

	/**
	 * 设置菜单监听器
	 * Sep 21, 2016 8:07:13 PM
	 * @author XFHY
	 *
	 */
	class SettingListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent sysSetIntnet = new Intent(ChooseAreaActivity.this,SystemSettings.class);
			startActivity(sysSetIntnet);
			LocationYourPosition.onDestroy();
		}
		
	}
	
	/**
	 * 定位按钮监听器
	 * Sep 21, 2016 8:04:46 PM
	 * @author XFHY
	 *
	 */
	class LocationMyPositionListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			LocationYourPosition.getLocationXY();  //先定位,获取经纬度
			LocationYourPosition.getRequest(new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) {
					//获取当前的省名,城市名,区县名 放到Location类里面 解析服务器返回的json数据
					LocationYourPosition.getDistrict(response);
					if( Position.getCounty() != null ){
						//WeatherActivity.actionStart(ChooseAreaActivity.this, myDir);
						LogUtil.d("position","ChooseAreaActivity 当前县区  ->   "+Position.getCounty());
						saveCitiesId();
						
					}
				}
				
				@Override
				public void onError(Exception e) {
					//Toast.makeText(MyApplication.getContext(), "获取当前位置失败~", Toast.LENGTH_LONG).show();
				}
				
			});
			
		}
	}
	
	/**
	 * 查询全国所有的省,优先从数据库查询,如果没有查询到,再去服务器查询
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces(); // 读取所有的省份数据
		if (provinceList.size() > 0) {
			dataList.clear(); // 清空之前的ListView

			// 遍历provinceList集合,加载所有的省份信息到ListView里面
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}

			// Notifies the attached View that the underlying data has been
			// changed and it should refresh itself.
			// 通知当前用该适配器的View,它的数据已经改变,通知它刷新自己(潇式翻译....)
			adapter.notifyDataSetChanged();

			if (selecProvinPosition != -1) {
				// Toast.makeText(ChooseAreaActivity.this,
				// "设置省级信息:"+selecProvinPosition, 0).show();
				// 设置当前选择的Item
				listView.setSelection(selecProvinPosition); // 将ListView的位置设置成先前点击的Item位置
				selecProvinPosition = -1;
			} else {
				listView.setSelection(0);
			}

			titleText.setText("中国"); // 肯定是中国的所有省份啦..
			currentLevel = LEVEL_PROVINCE;// 设置当前的选择级别是省级
		} else {
			queryFromServer(null, "province"); // 从服务器查询所有的省份信息
		}
	}

	/**
	 * 查询选中省中所有的市,优先从数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();

			for (City city : cityList) {
				dataList.add(city.getCityName());
			}

			adapter.notifyDataSetChanged();

			if (selecCityPosition != -1) {
				// Toast.makeText(ChooseAreaActivity.this,
				// "设置城市信息:"+selecCityPosition, 0).show();
				listView.setSelection(selecCityPosition);
				selecCityPosition = -1;
			} else {
				listView.setSelection(0);
			}

			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city"); // 从服务器查询该省的城市信息
		}
	}

	/**
	 * 查询选中市内所有的县,优先从数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}

			adapter.notifyDataSetChanged();   //ListView的内容已改变
			listView.setSelection(0);    //默认选择ListView的第1个
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 * 
	 * @param code
	 * @param type
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) { // 市或县级
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else { // 省级
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}

		showProgressDialog(); // 显示进度对话框

		// 首先判断一下当前网络是否可以上网 不可以的话,直接不用执行开启线程连接服务器了
		if (HttpUtil.isNetworkAvailable()) {

			// 这个sendHttpRequest()方法向服务器发送"GET"请求并获取到返回的数据 通过java回调机制将数据返回回来
			HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
				/*
				 * 这里用到了java的回调机制,sendHttpRequest()方法里面开启线程并接收服务器返回的数据,这个数据放到
				 * HttpCallbackListener接口的onFinish()参数response中,在这里即可获取到正确的数据
				 * 而将错误的信息放到onError()方法中
				 */

				@Override
				public void onFinish(String response) { // response:服务器返回的数据

					if (response.equals("当前无可用网络")) {
						closeProgressDialog(); // 关闭进度对话框
						return;
					}

					boolean result = false; // 用户判断 下面的解析事件是否成功

					/*----------------根据类型去调用相应解析方法--------------*/
					if ("province".equals(type)) {
						// 解析和处理服务器返回的省级数据(解析出来并存到数据库中)
						result = Utility.handleProvincesResponse(coolWeatherDB,
								response);
					} else if ("city".equals(type)) {
						result = Utility.handleCitiesResponse(coolWeatherDB,
								response, selectedProvince.getId());
					} else if ("county".equals(type)) {
						result = Utility.handleCountiesResponse(coolWeatherDB,
								response, selectedCity.getId());
					}

					if (result) { // 如果上面的解析成功
						// 通过Activity里面的runOnUiThread()方法回到主线程处理逻辑
						// 实现从子线程切换到主线程.原理基于异步消息处理机制
						// runOnUiThread():让指定的UI线程Run起来
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								closeProgressDialog(); // 关闭进度对话框

								/*-----------根据类型从数据库加载相应数据,并更新ListView界面----------------*/
								if ("province".equals(type)) {
									queryProvinces();
								} else if ("city".equals(type)) {
									queryCities();
								} else if ("county".equals(type)) {
									queryCounties();
								}
							}

						});
					}
				}

				@Override
				public void onError(Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog(); // 关闭进度对话框
							Toast.makeText(ChooseAreaActivity.this, "加载失败",
									Toast.LENGTH_SHORT).show();
						}

					});
				}
			});

		} else {
			Toast.makeText(this, "无可用网络", Toast.LENGTH_SHORT).show();
			closeProgressDialog(); // 关闭对话框
			titleText.setText("无可用网络~");
		}

	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show(); // 显示对话框到屏幕上
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss(); // 释放对话框,并从屏幕上移除
		}
	}

	/**
	 * 捕获Back按键,根据当前的级别来判断,此时应该返回市列表,省列表,还是直接退出
	 */
	@Override
	public void onBackPressed() { // 重写onBackPressed()方法来覆盖默认Back键的行为
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			// 如果是从WeatherActivity跳转过来的,则按下返回键就跳转到WeatherActivity活动中去
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			LocationYourPosition.onDestroy();   //关闭位置监听器
			finish();  //关闭当前Activity
		}
	}

	/**
	 * 用于其他活动启动当前这个活动 这是启动活动的最佳写法
	 * 
	 * @param context
	 *            想启动当前活动的活动context
	 * @param countyCode
	 *            县级代号
	 */
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ChooseAreaActivity.class);
		intent.putExtra("from_weather_activity", true);
		context.startActivity(intent);
	}

	/**
	 * 解析当前位置信息,并放到SQLite中
	 */
	public void saveCitiesId() {
		/*
		 * 思路:根据当前已经获取到的省份名字,通过数据库将当前的省级的id找到,放到
		 * provinceId中,根据这个id查找该所有的城市id(需要存到数据库中),放到cityIdList中,
		 * 再根据当前城市名称找到当前城市id,根据这个id查找所有的县区id(需要存入数据库),根据当前县区名称查找当前县区id
		 */
		
		//根据省份名从数据库查询该省份id
		coolWeatherDB.provinceNameToId(Position.getProvince());
		
		    /*---------------将当前省份的所有城市信息加载到数据库--------------------------------*/
		String cityAddress = "http://www.weather.com.cn/data/list3/city"
				+ Position.getProvinceId() + ".xml";
		// 首先判断一下当前网络是否可以上网 不可以的话,直接不用执行开启线程连接服务器了
		if (HttpUtil.isNetworkAvailable()) {

			// 这个sendHttpRequest()方法向服务器发送"GET"请求并获取到返回的数据 通过java回调机制将数据返回回来
			HttpUtil.sendHttpRequest(cityAddress, new HttpCallbackListener() {
				/*
				 * 这里用到了java的回调机制,sendHttpRequest()方法里面开启线程并接收服务器返回的数据, 这个数据放到
				 * HttpCallbackListener接口的onFinish()参数response中, 在这里即可获取到正确的数据
				 * 而将错误的信息放到onError()方法中
				 */

				@Override
				public void onFinish(String response) { // response:服务器返回的数据

					if (response.equals("当前无可用网络")) {
						return;
					}
					// 解析和处理服务器返回的市级数据
					Utility.handleCitiesResponse(coolWeatherDB, response,
							Integer.parseInt(Position.getProvinceId()));
					
					//根据城市名从数据库查询该城市id,将这个id保存到Position中
					coolWeatherDB.cityNameToId(Position.getCity());
					
					/*-----------------将当前城市的所有县区信息加载到数据库------------------------------------------------*/
					String countyAddress = "http://www.weather.com.cn/data/list3/city"
							+ Position.getCityId() + ".xml";
					// 首先判断一下当前网络是否可以上网 不可以的话,直接不用执行开启线程连接服务器了
					if (HttpUtil.isNetworkAvailable()) {

						// 这个sendHttpRequest()方法向服务器发送"GET"请求并获取到返回的数据 通过java回调机制将数据返回回来
						HttpUtil.sendHttpRequest(countyAddress, new HttpCallbackListener() {
							/*
							 * 这里用到了java的回调机制,sendHttpRequest()方法里面开启线程并接收服务器返回的数据 , 这个数据放到
							 * HttpCallbackListener接口的onFinish()参数response中, 在这里即可获取到正确的数据
							 * 而将错误的信息放到onError()方法中
							 */

							@Override
							public void onFinish(String response) { // response:服务器返回的数据

								if (response.equals("当前无可用网络")) {
									return;
								}
								//解析和处理服务器返回的县级数据
								Utility.handleCountiesResponse(coolWeatherDB, response,
										Integer.parseInt(Position.getCityId()));
								
								//根据县区名从数据库查询该县区id,将这个id保存到Position中
								coolWeatherDB.countyNameToId(Position.getCounty());
								LocationYourPosition.onDestroy();//定位完成  关闭位置监听器
								WeatherActivity.actionStart(ChooseAreaActivity.this, Position.getCountyId());
							}

							@Override
							public void onError(Exception e) {
								e.printStackTrace();
							}
						});
					}
					
				}

				@Override
				public void onError(Exception e) {
					e.printStackTrace();
				}
			});
		}
	
	}

	
}
