package com.coolweather.app.model;

import java.util.List;
import com.coolweather.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 2016��8��23��21:08:14
 * ListView��������,�Զ����������,�̳���ArrayAdapter<T>  ������DayWeather
 * @author XFHY
 *
 */
public class DayWeaAdapter extends ArrayAdapter<DayWeather> {

	private int resourceId;  //ListView����ֵ�id
	
	public DayWeaAdapter(Context context, int resource, List<DayWeather> objects) {
		super(context, resource, objects);
		resourceId = resource;  //ListView����ֵ�id
	}

	/**
	 * ���������ÿ�������������Ļ�ڵ�ʱ��ᱻ����
	 * getView()��������һ��convertView����,����������ڽ�֮ǰ���غõĲ��ֽ��л���,�Ա�
               ֮����Խ�������.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DayWeather dayWeather = getItem(position);  // ���Ȼ�õ�ǰ���DayWeatherʵ��
		View view;
		ViewHolder viewHolder;
		if(convertView == null){   //����ǵ�һ�μ���    ��ǰû�м���,���û�л���View,����Ҫ����һ��
			//���ش���Ĳ���
			view = LayoutInflater.from(getContext()).inflate(resourceId,null);
			viewHolder = new ViewHolder();
			viewHolder.dayDate = (TextView)view.findViewById(R.id.day_date_item);
			viewHolder.weatherType = (TextView)view.findViewById(R.id.weather_type_item);
			viewHolder.lowTemp = (TextView)view.findViewById(R.id.low_temp_item);
			viewHolder.highTemp = (TextView)view.findViewById(R.id.high_temp_item);
			view.setTag(viewHolder);    //������ڲ����ʵ���浽view���滺��
		} else {//�ڶ��μ���,����Ҫ����֮ǰ�Ļ�������
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		
		//���ò����ļ��пؼ�������
		viewHolder.dayDate.setText(dayWeather.getDayDate());
		viewHolder.weatherType.setText(dayWeather.getWeatherType());
		viewHolder.lowTemp.setText(dayWeather.getLowTemp());
		viewHolder.highTemp.setText(dayWeather.getHighTemp());
		
		return view;
	}
	
	//����һ���ڲ���,���ڻ����Ѿ����ع��˵Ĳ����ϵ�id
	class ViewHolder{
		TextView dayDate;     // ����:����
		TextView weatherType; // ��������
		TextView lowTemp;     // ����¶�
		TextView highTemp;    // ����¶�
	}
	
}

