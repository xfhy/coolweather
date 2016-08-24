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
 * 2016年8月23日21:08:14
 * ListView的适配器,自定义的适配器,继承自ArrayAdapter<T>  泛型是DayWeather
 * @author XFHY
 *
 */
public class DayWeaAdapter extends ArrayAdapter<DayWeather> {

	private int resourceId;  //ListView子项布局的id
	
	public DayWeaAdapter(Context context, int resource, List<DayWeather> objects) {
		super(context, resource, objects);
		resourceId = resource;  //ListView子项布局的id
	}

	/**
	 * 这个方法在每个子项被滚动到屏幕内的时候会被调用
	 * getView()方法中有一个convertView参数,这个参数用于将之前加载好的布局进行缓存,以便
               之后可以进行重用.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DayWeather dayWeather = getItem(position);  // 首先获得当前项的DayWeather实例
		View view;
		ViewHolder viewHolder;
		if(convertView == null){   //如果是第一次加载    先前没有加载,则就没有缓存View,则需要加载一下
			//加载传入的布局
			view = LayoutInflater.from(getContext()).inflate(resourceId,null);
			viewHolder = new ViewHolder();
			viewHolder.dayDate = (TextView)view.findViewById(R.id.day_date_item);
			viewHolder.weatherType = (TextView)view.findViewById(R.id.weather_type_item);
			viewHolder.lowTemp = (TextView)view.findViewById(R.id.low_temp_item);
			viewHolder.highTemp = (TextView)view.findViewById(R.id.high_temp_item);
			view.setTag(viewHolder);    //将这个内部类的实例存到view里面缓存
		} else {//第二次记载,则需要加载之前的缓存数据
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		
		//设置布局文件中控件的数据
		viewHolder.dayDate.setText(dayWeather.getDayDate());
		viewHolder.weatherType.setText(dayWeather.getWeatherType());
		viewHolder.lowTemp.setText(dayWeather.getLowTemp());
		viewHolder.highTemp.setText(dayWeather.getHighTemp());
		
		return view;
	}
	
	//这是一个内部类,用于缓存已经加载过了的布局上的id
	class ViewHolder{
		TextView dayDate;     // 日期:号数
		TextView weatherType; // 天气类型
		TextView lowTemp;     // 最低温度
		TextView highTemp;    // 最高温度
	}
	
}

