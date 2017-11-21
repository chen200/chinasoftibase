package com.huateng.weixin.user.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import net.sf.json.JSONObject;


public class HttpUtil {
	
	/**
	 * 将一个对象转化成Json请求体
	 * @param source
	 * @return
	 */
	public final static HttpEntity<String> toJsonBody(Object source) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		String json = JSONObject.fromObject(source).toString();
		return new HttpEntity<String>(json, headers);
	}
	
	public final static HttpEntity<String> makeBody(String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		return new HttpEntity<String>(json, headers);
	}

	private final static HttpHeaders jsonHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return headers;
	}
	
	/*public final static HttpEntity<String> emptyBody() {
		HttpHeaders headers = new HttpHeaders();
	}*/
}
