package com.coolweather.app.activity;

import com.coolweather.app.R;

import com.coolweather.app.model.Configure;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

/**
 * 2016年9月11日10:18:56
 * 天气预报设置
 * @author XFHY
 *
 */
public class SystemSettings extends Activity {

	/**
	 * 配置是否更新天气
	 */
	ToggleButton weatherToUpdate;
	RelativeLayout update_rate_layout;
	Spinner spinner_hours;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_settings);
		
		/*初始化控件*/
		weatherToUpdate = (ToggleButton)findViewById(R.id.mTogBtn);
		update_rate_layout = (RelativeLayout)findViewById(R.id.update_rate_layout);
		spinner_hours = (Spinner)findViewById(R.id.update_hours);
		
		
		
		//获取SharedPreferences文件中是否需要更新   再将值设置到ToggleButton中
		if(Configure.getWhetherToUpdate()){
			weatherToUpdate.setChecked(true);  //设置更新状态是true 
			update_rate_layout.setVisibility(View.VISIBLE);  //设置下面的   设置更新频率(小时)  为可见
			switch(Configure.getUpdateFrequency()){    //设置 选择器是 之前设置的值()从SharedPerfences文件中读取
			case 2:
				spinner_hours.setSelection(0,true);
				break;
			case 4:
				spinner_hours.setSelection(1,true);
				break;
			case 6:
				spinner_hours.setSelection(2,true);
				break;
			case 8:
				spinner_hours.setSelection(3,true);
				break;
			case 10:
				spinner_hours.setSelection(4,true);
				break;
			case 12:
				spinner_hours.setSelection(5,true);
				break;
			}
		} else {
			weatherToUpdate.setChecked(false);   //设置更新状态是false
			update_rate_layout.setVisibility(View.GONE);   //设置下面的   设置更新频率(小时)  为不可见
		}
		
		//ToggleButton的监听器   是否更新
		weatherToUpdate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//如果原来是false,则把它变成true 否则相反
				if(isChecked){
					Configure.setWhetherToUpdate(true);
					update_rate_layout.setVisibility(View.VISIBLE);
				} else {
					Configure.setWhetherToUpdate(false);
					update_rate_layout.setVisibility(View.GONE);
				}
			}
		});
		
		//更新频率(小时)   选择器
		spinner_hours.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Configure.setUpdateFrequency(2);
					break;
				case 1:
					Configure.setUpdateFrequency(4);
					break;
				case 2:
					Configure.setUpdateFrequency(6);
					break;
				case 3:
					Configure.setUpdateFrequency(8);
					break;
				case 4:
					Configure.setUpdateFrequency(10);
					break;
				case 5:
					Configure.setUpdateFrequency(12);
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
	}
	
}
