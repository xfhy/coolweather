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
 * 2016��8��21��11:19:53
 * 
 * �������ڱ���ʡ�������ݵĻ��
 * 
 * @author XFHY
 * 
 */
public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;   //����     �����жϵ�ǰ����������һ������   ʡ,��,��
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;   //���ȶԻ���
	private TextView titleText;   //����
	private ListView listView;    //ListView���
	private ArrayAdapter<String> adapter; //ListView��������
	private CoolWeatherDB coolWeatherDB;  //�����������ݿ��
	private List<String> dataList = new ArrayList<String>();   //ListView��ר�ü�������

	// ʡ�б�
	private List<Province> provinceList;
	// ���б�
	private List<City> cityList;
	// ���б�
	private List<County> countyList;
	// ѡ�е�ʡ��
	private Province selectedProvince;
	//��ǰѡ�е�ʡ����ListView�е�λ��
	private int selecProvinPosition = -1; 
	// ѡ�еĳ���
	private City selectedCity;
	//��ǰѡ�г���ListView�е�λ��
	private int selecCityPosition = -1;
	// ��ǰѡ�еļ���
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_ares);
		titleText = (TextView) findViewById(R.id.title);
		listView = (ListView) findViewById(R.id.list_view);

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
					//Toast.makeText(ChooseAreaActivity.this, "����ʡ����Ϣ:"+selecProvinPosition, 0).show();
					queryCities(); // ���س�������
				} else if (currentLevel == LEVEL_CITY) { // ��ѡ�м����ǳ��� ����ظó����ؼ�����
					selectedCity = cityList.get(position); // ��ȡѡ�еĳ���
					selecCityPosition = position;   //��¼��ǰѡ��ĳ�����ListView�е�λ��
					//Toast.makeText(ChooseAreaActivity.this, "���س�����Ϣ:"+selecCityPosition, 0).show();
					queryCounties(); // �����ؼ�����
				}
			}

		});
		
		queryProvinces(); // ����ʡ������ ����Ǽ��ص�ʡ������
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

			if(selecProvinPosition != -1){
				//Toast.makeText(ChooseAreaActivity.this, "����ʡ����Ϣ:"+selecProvinPosition, 0).show();
				// ���õ�ǰѡ���Item
				listView.setSelection(selecProvinPosition);   //��ListView��λ�����ó���ǰ�����Itemλ��
				selecProvinPosition = -1;
			} else {
				listView.setSelection(0);
			}
			
			titleText.setText("�й�"); // �϶����й�������ʡ����..
			currentLevel = LEVEL_PROVINCE;// ���õ�ǰ��ѡ�񼶱���ʡ��
		} else {
			queryFromServer(null, "province");  //�ӷ�������ѯ���е�ʡ����Ϣ
		}
	}
	
	/**
	 * ��ѯѡ��ʡ�����е���,���ȴ����ݿ��ѯ,���û�в�ѯ����ȥ�������ϲ�ѯ
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
				//Toast.makeText(ChooseAreaActivity.this, "���ó�����Ϣ:"+selecCityPosition, 0).show();
				listView.setSelection(selecCityPosition);
				selecCityPosition = -1;
			} else {
				listView.setSelection(0);
			}
			
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(),"city"); //�ӷ�������ѯ��ʡ�ĳ�����Ϣ
		}
	}

	/**
	 * ��ѯѡ���������е���,���ȴ����ݿ��ѯ,���û�в�ѯ����ȥ�������ϲ�ѯ
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
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ��������
	 * @param code
	 * @param type
	 */
	private void queryFromServer(final String code,final String type) {
		String address;
		if(!TextUtils.isEmpty(code)){  //�л��ؼ�
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		} else { //ʡ��
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		
		showProgressDialog();  //��ʾ���ȶԻ���
		
		//���sendHttpRequest()���������������"GET"���󲢻�ȡ�����ص�����  ͨ��java�ص����ƽ����ݷ��ػ���
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			/*�����õ���java�Ļص�����,sendHttpRequest()�������濪���̲߳����շ��������ص�����,������ݷŵ�
			*HttpCallbackListener�ӿڵ�onFinish()����response��,�����Ｔ�ɻ�ȡ����ȷ������
			*�����������Ϣ�ŵ�onError()������
			**/
			
			@Override
			public void onFinish(String response) {  //response:���������ص�����
				boolean result = false;   //�û��ж�  ����Ľ����¼��Ƿ�ɹ�
				
				   /*----------------��������ȥ������Ӧ��������--------------*/
				if("province".equals(type)){
					//�����ʹ�����������ص�ʡ������(�����������浽���ݿ���)
					result = Utility.handleProvincesResponse(coolWeatherDB, response);
				} else if("city".equals(type)){
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
				} else if("county".equals(type)){
					result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				
				if(result){  //�������Ľ����ɹ�
					//ͨ��Activity�����runOnUiThread()�����ص����̴߳����߼�
					//runOnUiThread():��ָ����UI�߳�Run����
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							closeProgressDialog();   //�رս��ȶԻ���
							
							     /*-----------�������ʹ����ݿ������Ӧ����,������ListView����----------------*/
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
						closeProgressDialog();   //�رս��ȶԻ���
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
					
				});
			}
		});
		
	}
	
	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();  //��ʾ�Ի�����Ļ��
	}
	
	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();  //�ͷŶԻ���,������Ļ���Ƴ�
		}
	}
	
	/**
	 * ����Back����,���ݵ�ǰ�ļ������ж�,��ʱӦ�÷������б�,ʡ�б�,����ֱ���˳�
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
