package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.widget.Toast;


/**
 * 2016年8月21日8:41:26
 * 
 * 这个类服务和服务器进行交互
 * 
 * @author XFHY
 * 
 *  一个应用程序很可能会在很多地方都使用到网络功能,而发生HTTP请求的代码基本都是相同的
 * 所以,通常情况下,我们都应该将这些通用的网络操作提取到一个公共的类里面,并提供一个静态方法,当想
 * 要发起网络请求的时候只需简单地调用一下这个方法即可.
 * 
 * 在获取服务器响应的数据后我们就可以对它进行解析和处理了.但是需要注意,网络请求通常是属于耗时操作,而sendHttpRequest()方法的内部
 * 并没有开始线程的话,这样就有可能导致在调用sendHttpRequest()方法的时候使得主线程被阻塞住.
 * 
 * 于是需要用到子线程,子线程去执行具体的网络操作,注意子线程是无法用return语句来返回数据的.因此这里我们将服务器响应的数据传入了
 * 自己写的HttpCallbackListener的onFinish()方法当中,如果出现了异常就传入onError()方法中.
 * 并使用java的回调机制就可以了.
 * 
 * 当在线程中使用Toast的时候,报Can't create handler inside thread that has not called Looper.prepare()错误时,
 * 需要在Toast前后加上Looper.prepare();和Looper.loop();
 * 
 */
public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener){
		
		//首先判断一下当前网络是否可以上网  不可以的话,直接不用执行下面的代码(开启线程连接服务器)了
		if( !isNetworkAvailable() ){
			Looper.prepare();  //需要调用Looper.prepare()来给线程创建一个消息循环
			Toast.makeText(MyApplication.getContext(), "无可用网络", Toast.LENGTH_SHORT).show();
			Looper.loop();     //让Looper开始工作，从消息队列里取消息，处理消息。
			return ;
		}
		
		//启动线程
		new Thread(new Runnable(){

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try{
					URL url = new URL(address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");   //设置 URL 请求的方法
					connection.setConnectTimeout(8000);   //设置一个连接指定的超时值（以毫秒为单位）
					connection.setReadTimeout(8000);      //将读超时设置为指定的超时值，以毫秒为单位
					
					InputStream in = connection.getInputStream();   //服务器返回的数据是以输入流的方式
					//将字节流转换成字符流
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuffer response = new StringBuffer();  //用于存放服务器返回的数据
					String line;
					while( (line=reader.readLine()) != null ){  //读取服务器的数据,一行一行地读
						response.append(line);
					}
					
					//将服务器返回的数据放到了HttpCallbackListener实例的onFinish()中,java的回调机制会让调用这个方法的类获取到数据
					if( listener != null ){   
						listener.onFinish(response.toString());
					}
				} catch(Exception e){
					//将连接服务器时的错误信息放到HttpCallbackListener的实例的onError()中,
					//java的回调机制会让调用sendHttpRequest()这个方法的类获取到数据
					if( listener != null ){
						listener.onError(e);
					}
				} finally {
					if( connection != null ){
						connection.disconnect();   //最后,记得断开连接
					} 
				}
			}
			
		}).start();
	}
	
	/**
	 * 判断网络是否连接   
	 * 简单地判断网络是否连接,对于一些特殊情况不能判断,不如 连接上移动网络但无法上网,连接上wifi但无法上网等.
	 * @return 有连接则返回true,否则返回false(无可用网络)
	 */
	public static boolean isNetworkAvailable() {
		
		//这里需要加入权限
		//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
		//得到网络连接
		ConnectivityManager manager = (ConnectivityManager)
				MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		//去判断网络是否连接
		if(manager.getActiveNetworkInfo() != null){
			return manager.getActiveNetworkInfo().isAvailable();  //表明网络连接是否可能
		}
		return false;
	}
	
}
