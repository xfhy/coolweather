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
 * 2016��9��11��10:18:56
 * ����Ԥ������
 * @author XFHY
 *
 */
public class SystemSettings extends Activity {

	/**
	 * �����Ƿ��������
	 */
	ToggleButton weatherToUpdate;
	RelativeLayout update_rate_layout;
	Spinner spinner_hours;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_settings);
		
		/*��ʼ���ؼ�*/
		weatherToUpdate = (ToggleButton)findViewById(R.id.mTogBtn);
		update_rate_layout = (RelativeLayout)findViewById(R.id.update_rate_layout);
		spinner_hours = (Spinner)findViewById(R.id.update_hours);
		
		
		
		//��ȡSharedPreferences�ļ����Ƿ���Ҫ����   �ٽ�ֵ���õ�ToggleButton��
		if(Configure.getWhetherToUpdate()){
			weatherToUpdate.setChecked(true);  //���ø���״̬��true 
			update_rate_layout.setVisibility(View.VISIBLE);  //���������   ���ø���Ƶ��(Сʱ)  Ϊ�ɼ�
			switch(Configure.getUpdateFrequency()){    //���� ѡ������ ֮ǰ���õ�ֵ()��SharedPerfences�ļ��ж�ȡ
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
			weatherToUpdate.setChecked(false);   //���ø���״̬��false
			update_rate_layout.setVisibility(View.GONE);   //���������   ���ø���Ƶ��(Сʱ)  Ϊ���ɼ�
		}
		
		//ToggleButton�ļ�����   �Ƿ����
		weatherToUpdate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//���ԭ����false,��������true �����෴
				if(isChecked){
					Configure.setWhetherToUpdate(true);
					update_rate_layout.setVisibility(View.VISIBLE);
				} else {
					Configure.setWhetherToUpdate(false);
					update_rate_layout.setVisibility(View.GONE);
				}
			}
		});
		
		//����Ƶ��(Сʱ)   ѡ����
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
