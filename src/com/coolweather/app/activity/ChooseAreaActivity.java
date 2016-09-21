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
 * 2016��8��21��11:19:53
 * 
 * �������ڱ���ʡ�������ݵĻ��
 * 
 * @author XFHY
 * 
 */
public class ChooseAreaActivity extends BaseActivity {

	public static final int LEVEL_PROVINCE = 0; // ���� �����жϵ�ǰ����������һ������ ʡ,��,��
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	/**
	 * �Ƿ��WeatherActivity����ת����
	 */
	private boolean isFromWeatherActivity;
	/**
	 * ���ȶԻ���
	 */
	private ProgressDialog progressDialog;
	/**
	 * ����
	 */
	private TextView titleText;
	/**
	 * �������õ�Button
	 */
	private Button settingBtn;
	/**
	 * ���ڶ�λ��Button
	 */
	private Button locationBtn;
	/**
	 * ListView���
	 */
	private ListView listView;
	/**
	 * ListView��������
	 */
	private ArrayAdapter<String> adapter;
	/**
	 * �����������ݿ��
	 */
	private CoolWeatherDB coolWeatherDB;
	/**
	 * ListView��ר�ü�������
	 */
	private List<String> dataList = new ArrayList<String>();

	/**
	 * ʡ�б�
	 */
	private List<Province> provinceList;
	/**
	 * ���б�
	 */
	private List<City> cityList;
	/**
	 * ���б�
	 */
	private List<County> countyList;
	/**
	 * ѡ�е�ʡ��
	 */
	private Province selectedProvince;
	/**
	 * ��ǰѡ�е�ʡ����ListView�е�λ��
	 */
	private int selecProvinPosition = -1;
	/**
	 * ѡ�еĳ���
	 */
	private City selectedCity;
	/**
	 * ��ǰѡ�г���ListView�е�λ��
	 */
	private int selecCityPosition = -1;
	/**
	 * ��ǰѡ�еļ���
	 */
	private int currentLevel;
	/**
	 * ��ǰ�Լ���λ��
	 */
	LocationYourPosition myPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * ����˵���� appId �� appSecret �ֱ�ΪӦ�õķ��� ID ����Կ�������׺�̨�Զ����ɣ�ͨ�������׺�̨ > Ӧ����ϸ��Ϣ
		 * ���Ի�á� isTestModel : �Ƿ�������ģʽ��true Ϊ�ǣ�false Ϊ�񡣣��ϴ�������˼��������г��汾��������Ϊ
		 * false�� isEnableYoumiLog: �Ƿ������׵�Log�����Ĭ��Ϊ����״̬
		 * �ϴ���������վ�������ʱ����ؿ������׵�Log���������ܱ�֤ͨ����� �����߷���apk�������г���ʱ��ǿ�ҽ���ر����׵�Log
		 */
		//AdManager.getInstance(getBaseContext()).init("89a962f77ebf6c0c",
				//"7fab8966d2360133", false, true);
		
		//�����ж��Ƿ��Ǵ�WeatherActivity��ת������   Ĭ��ֵ��false
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		
		//�ȶ�ȡ�����ļ�
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//���֮ǰѡ��������Ҳ��Ǵ�WeatherActivity��ת������,��ֱ����������   ֱ�ӵ���ʾ������Ϣ�Ļ
		if(prefs.getBoolean("city_selected", false) && !isFromWeatherActivity){   
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return ;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);   //����ȫ��
		setContentView(R.layout.choose_ares);
		
		settingBtn = (Button)findViewById(R.id.setting);
		titleText = (TextView) findViewById(R.id.title);
		locationBtn = (Button)findViewById(R.id.location_btn);
		listView = (ListView) findViewById(R.id.list_view);

		settingBtn.setOnClickListener(new SettingListener());
		locationBtn.setOnClickListener(new LocationMyPositionListener());  //���ö�λ��ť������
		
		// ����ListView������,����ΪdataList�����ṩ
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);

		// ���CoolWeatherDBʵ��
		coolWeatherDB = CoolWeatherDB.getInstance(this);

		// ����ListView Itemѡ����������
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) { // ��ǰѡ�еļ�����ʡ�� ����ظ�ʡ��������
					selectedProvince = provinceList.get(position); // ��ȡѡ�е�ʡ��
					selecProvinPosition = position;  //��¼��ǰѡ���ʡ����ListView�е�λ��
					queryCities(); // ���س�������
				} else if (currentLevel == LEVEL_CITY) { // ��ѡ�м����ǳ��� ����ظó����ؼ�����
					selectedCity = cityList.get(position); // ��ȡѡ�еĳ���
					selecCityPosition = position;   //��¼��ǰѡ��ĳ�����ListView�е�λ��
					queryCounties(); // �����ؼ�����
				} else if(currentLevel == LEVEL_COUNTY){  // ��ѡ�м�������   ��������ʾ������Ϣ�Ļ
					 String countyCode = countyList.get(position).getCountyCode();
					 WeatherActivity.actionStart(ChooseAreaActivity.this, countyCode);  //����������д��
					 LocationYourPosition.onDestroy();   //�ر�λ�ü�����
				}
			}

		});
		
		queryProvinces(); // ����ʡ������ ����Ǽ��ص�ʡ������
		
	}

	/**
	 * ���ò˵�������
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
	 * ��λ��ť������
	 * Sep 21, 2016 8:04:46 PM
	 * @author XFHY
	 *
	 */
	class LocationMyPositionListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			LocationYourPosition.getLocationXY();  //�ȶ�λ,��ȡ��γ��
			LocationYourPosition.getRequest(new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) {
					//��ȡ��ǰ��ʡ��,������,������ �ŵ�Location������ �������������ص�json����
					LocationYourPosition.getDistrict(response);
					if( Position.getCounty() != null ){
						//WeatherActivity.actionStart(ChooseAreaActivity.this, myDir);
						LogUtil.d("position","ChooseAreaActivity ��ǰ����  ->   "+Position.getCounty());
						saveCitiesId();
						
					}
				}
				
				@Override
				public void onError(Exception e) {
					//Toast.makeText(MyApplication.getContext(), "��ȡ��ǰλ��ʧ��~", Toast.LENGTH_LONG).show();
				}
				
			});
			
		}
	}
	
	/**
	 * ��ѯȫ�����е�ʡ,���ȴ����ݿ��ѯ,���û�в�ѯ��,��ȥ��������ѯ
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces(); // ��ȡ���е�ʡ������
		if (provinceList.size() > 0) {
			dataList.clear(); // ���֮ǰ��ListView

			// ����provinceList����,�������е�ʡ����Ϣ��ListView����
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}

			// Notifies the attached View that the underlying data has been
			// changed and it should refresh itself.
			// ֪ͨ��ǰ�ø���������View,���������Ѿ��ı�,֪ͨ��ˢ���Լ�(��ʽ����....)
			adapter.notifyDataSetChanged();

			if (selecProvinPosition != -1) {
				// Toast.makeText(ChooseAreaActivity.this,
				// "����ʡ����Ϣ:"+selecProvinPosition, 0).show();
				// ���õ�ǰѡ���Item
				listView.setSelection(selecProvinPosition); // ��ListView��λ�����ó���ǰ�����Itemλ��
				selecProvinPosition = -1;
			} else {
				listView.setSelection(0);
			}

			titleText.setText("�й�"); // �϶����й�������ʡ����..
			currentLevel = LEVEL_PROVINCE;// ���õ�ǰ��ѡ�񼶱���ʡ��
		} else {
			queryFromServer(null, "province"); // �ӷ�������ѯ���е�ʡ����Ϣ
		}
	}

	/**
	 * ��ѯѡ��ʡ�����е���,���ȴ����ݿ��ѯ,���û�в�ѯ����ȥ�������ϲ�ѯ
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
				// "���ó�����Ϣ:"+selecCityPosition, 0).show();
				listView.setSelection(selecCityPosition);
				selecCityPosition = -1;
			} else {
				listView.setSelection(0);
			}

			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city"); // �ӷ�������ѯ��ʡ�ĳ�����Ϣ
		}
	}

	/**
	 * ��ѯѡ���������е���,���ȴ����ݿ��ѯ,���û�в�ѯ����ȥ�������ϲ�ѯ
	 */
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}

			adapter.notifyDataSetChanged();   //ListView�������Ѹı�
			listView.setSelection(0);    //Ĭ��ѡ��ListView�ĵ�1��
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ��������
	 * 
	 * @param code
	 * @param type
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) { // �л��ؼ�
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else { // ʡ��
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}

		showProgressDialog(); // ��ʾ���ȶԻ���

		// �����ж�һ�µ�ǰ�����Ƿ�������� �����ԵĻ�,ֱ�Ӳ���ִ�п����߳����ӷ�������
		if (HttpUtil.isNetworkAvailable()) {

			// ���sendHttpRequest()���������������"GET"���󲢻�ȡ�����ص����� ͨ��java�ص����ƽ����ݷ��ػ���
			HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
				/*
				 * �����õ���java�Ļص�����,sendHttpRequest()�������濪���̲߳����շ��������ص�����,������ݷŵ�
				 * HttpCallbackListener�ӿڵ�onFinish()����response��,�����Ｔ�ɻ�ȡ����ȷ������
				 * �����������Ϣ�ŵ�onError()������
				 */

				@Override
				public void onFinish(String response) { // response:���������ص�����

					if (response.equals("��ǰ�޿�������")) {
						closeProgressDialog(); // �رս��ȶԻ���
						return;
					}

					boolean result = false; // �û��ж� ����Ľ����¼��Ƿ�ɹ�

					/*----------------��������ȥ������Ӧ��������--------------*/
					if ("province".equals(type)) {
						// �����ʹ�����������ص�ʡ������(�����������浽���ݿ���)
						result = Utility.handleProvincesResponse(coolWeatherDB,
								response);
					} else if ("city".equals(type)) {
						result = Utility.handleCitiesResponse(coolWeatherDB,
								response, selectedProvince.getId());
					} else if ("county".equals(type)) {
						result = Utility.handleCountiesResponse(coolWeatherDB,
								response, selectedCity.getId());
					}

					if (result) { // �������Ľ����ɹ�
						// ͨ��Activity�����runOnUiThread()�����ص����̴߳����߼�
						// ʵ�ִ����߳��л������߳�.ԭ������첽��Ϣ�������
						// runOnUiThread():��ָ����UI�߳�Run����
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								closeProgressDialog(); // �رս��ȶԻ���

								/*-----------�������ʹ����ݿ������Ӧ����,������ListView����----------------*/
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
							closeProgressDialog(); // �رս��ȶԻ���
							Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
									Toast.LENGTH_SHORT).show();
						}

					});
				}
			});

		} else {
			Toast.makeText(this, "�޿�������", Toast.LENGTH_SHORT).show();
			closeProgressDialog(); // �رնԻ���
			titleText.setText("�޿�������~");
		}

	}

	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show(); // ��ʾ�Ի�����Ļ��
	}

	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss(); // �ͷŶԻ���,������Ļ���Ƴ�
		}
	}

	/**
	 * ����Back����,���ݵ�ǰ�ļ������ж�,��ʱӦ�÷������б�,ʡ�б�,����ֱ���˳�
	 */
	@Override
	public void onBackPressed() { // ��дonBackPressed()����������Ĭ��Back������Ϊ
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			// ����Ǵ�WeatherActivity��ת������,���·��ؼ�����ת��WeatherActivity���ȥ
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			LocationYourPosition.onDestroy();   //�ر�λ�ü�����
			finish();  //�رյ�ǰActivity
		}
	}

	/**
	 * ���������������ǰ���� ��������������д��
	 * 
	 * @param context
	 *            ��������ǰ��Ļcontext
	 * @param countyCode
	 *            �ؼ�����
	 */
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ChooseAreaActivity.class);
		intent.putExtra("from_weather_activity", true);
		context.startActivity(intent);
	}

	/**
	 * ������ǰλ����Ϣ,���ŵ�SQLite��
	 */
	public void saveCitiesId() {
		/*
		 * ˼·:���ݵ�ǰ�Ѿ���ȡ����ʡ������,ͨ�����ݿ⽫��ǰ��ʡ����id�ҵ�,�ŵ�
		 * provinceId��,�������id���Ҹ����еĳ���id(��Ҫ�浽���ݿ���),�ŵ�cityIdList��,
		 * �ٸ��ݵ�ǰ���������ҵ���ǰ����id,�������id�������е�����id(��Ҫ�������ݿ�),���ݵ�ǰ�������Ʋ��ҵ�ǰ����id
		 */
		
		//����ʡ���������ݿ��ѯ��ʡ��id
		coolWeatherDB.provinceNameToId(Position.getProvince());
		
		    /*---------------����ǰʡ�ݵ����г�����Ϣ���ص����ݿ�--------------------------------*/
		String cityAddress = "http://www.weather.com.cn/data/list3/city"
				+ Position.getProvinceId() + ".xml";
		// �����ж�һ�µ�ǰ�����Ƿ�������� �����ԵĻ�,ֱ�Ӳ���ִ�п����߳����ӷ�������
		if (HttpUtil.isNetworkAvailable()) {

			// ���sendHttpRequest()���������������"GET"���󲢻�ȡ�����ص����� ͨ��java�ص����ƽ����ݷ��ػ���
			HttpUtil.sendHttpRequest(cityAddress, new HttpCallbackListener() {
				/*
				 * �����õ���java�Ļص�����,sendHttpRequest()�������濪���̲߳����շ��������ص�����, ������ݷŵ�
				 * HttpCallbackListener�ӿڵ�onFinish()����response��, �����Ｔ�ɻ�ȡ����ȷ������
				 * �����������Ϣ�ŵ�onError()������
				 */

				@Override
				public void onFinish(String response) { // response:���������ص�����

					if (response.equals("��ǰ�޿�������")) {
						return;
					}
					// �����ʹ�����������ص��м�����
					Utility.handleCitiesResponse(coolWeatherDB, response,
							Integer.parseInt(Position.getProvinceId()));
					
					//���ݳ����������ݿ��ѯ�ó���id,�����id���浽Position��
					coolWeatherDB.cityNameToId(Position.getCity());
					
					/*-----------------����ǰ���е�����������Ϣ���ص����ݿ�------------------------------------------------*/
					String countyAddress = "http://www.weather.com.cn/data/list3/city"
							+ Position.getCityId() + ".xml";
					// �����ж�һ�µ�ǰ�����Ƿ�������� �����ԵĻ�,ֱ�Ӳ���ִ�п����߳����ӷ�������
					if (HttpUtil.isNetworkAvailable()) {

						// ���sendHttpRequest()���������������"GET"���󲢻�ȡ�����ص����� ͨ��java�ص����ƽ����ݷ��ػ���
						HttpUtil.sendHttpRequest(countyAddress, new HttpCallbackListener() {
							/*
							 * �����õ���java�Ļص�����,sendHttpRequest()�������濪���̲߳����շ��������ص����� , ������ݷŵ�
							 * HttpCallbackListener�ӿڵ�onFinish()����response��, �����Ｔ�ɻ�ȡ����ȷ������
							 * �����������Ϣ�ŵ�onError()������
							 */

							@Override
							public void onFinish(String response) { // response:���������ص�����

								if (response.equals("��ǰ�޿�������")) {
									return;
								}
								//�����ʹ�����������ص��ؼ�����
								Utility.handleCountiesResponse(coolWeatherDB, response,
										Integer.parseInt(Position.getCityId()));
								
								//���������������ݿ��ѯ������id,�����id���浽Position��
								coolWeatherDB.countyNameToId(Position.getCounty());
								LocationYourPosition.onDestroy();//��λ���  �ر�λ�ü�����
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
