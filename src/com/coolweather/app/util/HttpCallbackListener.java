package com.coolweather.app.util;

/**
 * 2016��8��21��8:48:26
 * ����ӿ������ص����񷵻صĽ��
 * @author XFHY
 * 
 * ������һ���ӿ�,onFinish()������ʾ�������ɹ���Ӧ���������ʱ�����,onError()������ʾ����������������ִ����ʱ��
 * ����.���������������в���,onFinish()�����еĲ��������ŷ��������ص�����,��onError()�еĲ�����¼�Ŵ������ϸ��Ϣ.
 * 
 */
public interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}
