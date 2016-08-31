package com.coolweather.app.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * 2016��8��28��10:38:07
 * 
 * �����̳���Activity,�����еĻ�඼�̳��������
 * �����и��ô�,���ǿ��Բ鿴��ǰ������һ�����,Ҳ���������
 * @author XFHY
 *
 */
public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("xfhy",getClass().getSimpleName());  //��ӡ��ǰ�������
		//����������������
		ActivityCollector.addActivity(this);  
	}
	
	//����ʱ,���ӻ���������Ƴ���ǰ�
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
}
