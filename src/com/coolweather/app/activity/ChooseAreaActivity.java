package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;   //级别     用于判断当前界面是在哪一个级别   省,市,县
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;   //进度对话框
	private TextView titleText;   //标题
	private ListView listView;    //ListView组件
	private ArrayAdapter<String> adapter; //ListView的适配器
	private CoolWeatherDB coolWeatherDB;  //用来操作数据库的
	private List<String> dataList = new ArrayList<String>();   //ListView的专用集合数据

	// 省列表
	private List<Province> provinceList;
	// 市列表
	private List<City> cityList;
	// 县列表
	private List<County> countyList;
	// 选中的省份
	private Province selectedProvince;
	//当前选中的省份在ListView中的位置
	private int selecProvinPosition = -1; 
	// 选中的城市
	private City selectedCity;
	//当前选中城市ListView中的位置
	private int selecCityPosition = -1;
	// 当前选中的级别
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_ares);
		titleText = (TextView) findViewById(R.id.title);
		listView = (ListView) findViewById(R.id.list_view);

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
					//Toast.makeText(ChooseAreaActivity.this, "加载省级信息:"+selecProvinPosition, 0).show();
					queryCities(); // 加载城市数据
				} else if (currentLevel == LEVEL_CITY) { // 当选中级别是城市 则加载该城市县级数据
					selectedCity = cityList.get(position); // 获取选中的城市
					selecCityPosition = position;   //记录当前选择的城市在ListView中的位置
					//Toast.makeText(ChooseAreaActivity.this, "加载城市信息:"+selecCityPosition, 0).show();
					queryCounties(); // 加载县级数据
				}
			}

		});
		
		queryProvinces(); // 加载省级数据 最初是加载的省级数据
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

			if(selecProvinPosition != -1){
				//Toast.makeText(ChooseAreaActivity.this, "设置省级信息:"+selecProvinPosition, 0).show();
				// 设置当前选择的Item
				listView.setSelection(selecProvinPosition);   //将ListView的位置设置成先前点击的Item位置
				selecProvinPosition = -1;
			} else {
				listView.setSelection(0);
			}
			
			titleText.setText("中国"); // 肯定是中国的所有省份啦..
			currentLevel = LEVEL_PROVINCE;// 设置当前的选择级别是省级
		} else {
			queryFromServer(null, "province");  //从服务器查询所有的省份信息
		}
	}
	
	/**
	 * 查询选中省中所有的市,优先从数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size() > 0){
			dataList.clear();
			
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			
			adapter.notifyDataSetChanged();
			
			if(selecCityPosition != -1){
				//Toast.makeText(ChooseAreaActivity.this, "设置城市信息:"+selecCityPosition, 0).show();
				listView.setSelection(selecCityPosition);
				selecCityPosition = -1;
			} else {
				listView.setSelection(0);
			}
			
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(),"city"); //从服务器查询该省的城市信息
		}
	}

	/**
	 * 查询选中市内所有的县,优先从数据库查询,如果没有查询到再去服务器上查询
	 */
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		if(countyList.size() > 0){
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 * @param code
	 * @param type
	 */
	private void queryFromServer(final String code,final String type) {
		String address;
		if(!TextUtils.isEmpty(code)){  //市或县级
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		} else { //省级
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		
		showProgressDialog();  //显示进度对话框
		
		//这个sendHttpRequest()方法向服务器发送"GET"请求并获取到返回的数据  通过java回调机制将数据返回回来
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			/*这里用到了java的回调机制,sendHttpRequest()方法里面开启线程并接收服务器返回的数据,这个数据放到
			*HttpCallbackListener接口的onFinish()参数response中,在这里即可获取到正确的数据
			*而将错误的信息放到onError()方法中
			**/
			
			@Override
			public void onFinish(String response) {  //response:服务器返回的数据
				boolean result = false;   //用户判断  下面的解析事件是否成功
				
				   /*----------------根据类型去调用相应解析方法--------------*/
				if("province".equals(type)){
					//解析和处理服务器返回的省级数据(解析出来并存到数据库中)
					result = Utility.handleProvincesResponse(coolWeatherDB, response);
				} else if("city".equals(type)){
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
				} else if("county".equals(type)){
					result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				
				if(result){  //如果上面的解析成功
					//通过Activity里面的runOnUiThread()方法回到主线程处理逻辑
					//runOnUiThread():让指定的UI线程Run起来
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							closeProgressDialog();   //关闭进度对话框
							
							     /*-----------根据类型从数据库加载相应数据,并更新ListView界面----------------*/
							if("province".equals(type)){
								queryProvinces();
							} else if("city".equals(type)){
								queryCities();
							} else if("county".equals(type)){
								queryCounties();
							}
						}
						
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						closeProgressDialog();   //关闭进度对话框
						Toast.makeText(ChooseAreaActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
					
				});
			}
		});
		
	}
	
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();  //显示对话框到屏幕上
	}
	
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();  //释放对话框,并从屏幕上移除
		}
	}
	
	/**
	 * 捕获Back按键,根据当前的级别来判断,此时应该返回市列表,省列表,还是直接退出
	 */
	@Override
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTY){
			queryCities();
		} else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		} else {
			finish();
		}
	}
	
}
