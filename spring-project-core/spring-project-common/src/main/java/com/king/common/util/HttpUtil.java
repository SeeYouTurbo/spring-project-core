package com.king.common.util;

import okhttp3.*;
import okhttp3.FormBody.Builder;
import okhttp3.MultipartBody.Part;
import okhttp3.internal.http.HttpMethod;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author xuyajie
 * @date 2018年5月30日 上午11:14:46
  * 简单HTTP请求工具
 */
public class HttpUtil {
	
	private static int CONNECT_TIME_OUT = 60; // 连接超时，默认5s
	private static int READ_TIME_OUT = 60;// 读取超时，默认5s
	private static int WRITE_TIME_OUT = 60;// 写入超时，默认5s
	//使用okHttp作为请求客户端，通过单例方式调用
	private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
			.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
			.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
			.build();
	/**
	 * 
	* @date 2018年6月20日 下午3:29:40 
	* @author xuyajie 
	* @return Http请求对象
	* 构造Http请求对象
	 */
	public static HttpRequest createRequest() {
		return new HttpRequest();
	}

	/**
	 * 
	* @author xuyajie 
	* @date 2018年6月20日 下午3:30:03 
	* 参数序列化方式
	 */
	public enum EncodeType {
		FORM, //将params中的参数以表单形式提交（不支持复合参数）
		JSON, //将params的参数转换为json格式提交
		BYTE,  //将body中的内容直接以字节数组的方式提交
		FILE  //文件上传（multipart/form-data）
	}

	/**
	 * 
	* @author xuyajie 
	* @date 2018年6月20日 下午3:30:09 
	* @Description:返回结果解析方式
	 */
	public enum DecodeType {
		JSON
	}
	
	/**
	 * 
	 * @author xuyajie
	 * @date 2018年6月20日 下午3:07:32
	 * Http请求类
	 */
	public static class HttpRequest {
		HttpRequest() {
		}
		
		private final String HEADER_CONTENT_TYPE = "Content-Type";

		private String targetUrl;// 请求地址
		private Map<String, Object> params = new HashMap<>();// 请求参数（key-value类型参数）
		private Map<String, String> headers = new HashMap<>();// 请求头
		private byte[] body = new byte[0];// post请求参数为字节数组时，存放请求的字节数组
		private EncodeType encodeType = EncodeType.FORM;// 参数序列化方式，默认为表单方式
		private DecodeType decodeType = DecodeType.JSON;// 结果反序列化方式，默认为JSON方式

		// 将参数以设定的方式序列化到body中去
		private RequestBody encode() throws IOException {
			switch (encodeType) {
			case FORM: {
				Builder builder = new FormBody.Builder();
				params.forEach((key,value)->{
					builder.add(key, TypeUtil.castToString(value));
				});
				RequestBody requestBody = builder.build();
				return requestBody;
			}
			case JSON: {
				String json = JsonUtil.toJsonString(params);
				RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
				return requestBody;
			}
			case BYTE: {
				//使用byte方式序列化参数时，允许用户修改MediaType
				MediaType mediaType;
				if(getContentType() != null){
					mediaType = MediaType.parse(getContentType());
				}else{
					mediaType = MediaType.parse("application/octet-stream");//默认MediaType
				}
				RequestBody requestBody = RequestBody.create(mediaType, body);
				return requestBody;
			}
			case FILE: {
				okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder();
				params.forEach((key,value)->{
					if(value instanceof Part){
						builder.addPart((Part)value);
					}else{
						builder.addFormDataPart(key, TypeUtil.castToString(value));
					}
				});
				builder.setType(MultipartBody.FORM);
				MultipartBody requestBody = builder.build();
				return requestBody;
			}
			default: {
				throw new IOException("不支持的参数encode方式");
			}
			}
		}

		// 将结果以设定的方式反序列化成对象
		private <T> T decode(String result, Class<T> clazz) throws IOException {
			switch (decodeType) {
			case JSON: {
				return JsonUtil.toObject(result, clazz);
			}
			default: {
				throw new IOException("不支持的参数decode方式");
			}
			}
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:10:58
		 * @author xuyajie
		 * @return 请求返回结果
		 * @throws IOException
		 *             请求失败异常
* 发送get请求
		 */
		public String get() throws IOException {
			Call call = okHttpClient.newCall(getRequest());
			Response response = call.execute();
			if (response.isSuccessful()) {
				String resultStr = response.body().string();
				return resultStr;
			} else {
				throw new IOException("请求失败：" + response.code() + "," + response.message());
			}
		}
		
		/**
		 * 
		* @date 2018年7月30日 上午9:39:50 
		* @author xuyajie 
		* @return
		* @throws IOException
* get获取完整的响应对象
		 */
		public Response getForEntireResponse() throws IOException{
			Call call = okHttpClient.newCall(getRequest());
			Response response = call.execute();
			return response;
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:11:56
		 * @author xuyajie
		 * @param callback
		 *            回调函数
* 异步发送get请求
		 */
		public void asynGet(Callback callback) {
			Call call = okHttpClient.newCall(getRequest());
			call.enqueue(callback);
		}

		// 生成get请求的Request对象
		private Request getRequest() {
			String targetUrl = addParamsToUrl();
			Request req = new Request.Builder().url(targetUrl).get().headers(Headers.of(headers)).build();
			return req;
		}

		// 将参数拼接到url中
		private String addParamsToUrl() {
			StringBuilder builder = new StringBuilder(targetUrl);
			if (!params.isEmpty()) {
				if (!targetUrl.contains("?")) {
					builder.append("?");
				}else{
					builder.append("&");
				}
				params.forEach((key, value) -> {
					builder.append(key).append("=").append(value).append("&");
				});
				builder.deleteCharAt(builder.length() - 1);
			}
			return builder.toString();
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:12:32
		 * @author xuyajie
		 * @return 请求返回结果
		 * @throws IOException
		 *             请求失败异常
* 发送post请求
		 */
		public String post() throws IOException {
			Response response = postForEntireResponse();
			if (response.isSuccessful()) {
				String resultStr = response.body().string();
				return resultStr;
			} else {
				throw new IOException("请求失败：" + response.code() + "," + response.message());
			}
		}
		
		/**
		 * 
		* @date 2018年7月30日 上午9:39:50 
		* @author xuyajie 
		* @return
		* @throws IOException
* post获取完整的响应对象
		 */
		public Response postForEntireResponse() throws IOException{
			Call call = okHttpClient.newCall(postRequest());
			Response response = call.execute();
			return response;
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:12:57
		 * @author xuyajie
		 * @param callback
		 *            回调函数
		 * @throws IOException
		 *             序列化失败异常
* 异步发送post请求
		 */
		public void asynPost(Callback callback) throws IOException {
			Call call = okHttpClient.newCall(postRequest());
			call.enqueue(callback);
		}

		// 生成post的请求
		private Request postRequest() throws IOException {
			RequestBody requestBody = encode();
			Request req = new Request.Builder().
					url(targetUrl).post(requestBody).
					headers(Headers.of(headers)).
					header("Accept-Encoding", "")//显示指定以取消okhttp默认值，
					.build();
			return req;
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:14:03
		 * @author xuyajie
		 * @param <T>
		 *            返回结果类型
		 * @param t
		 *            返回结果类型
		 * @return 返回结果对象
		 * @throws IOException
		 *             请求失败异常
* 发送post请求，并将返回结果解析成对应对象
		 */
		public <T> T postForObject(Class<T> t) throws IOException {
			String str = post();
			return decode(str, t);
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:17:51
		 * @author xuyajie
		 * @param <T>
		 *            返回结果类型
		 * @param t
		 *            返回结果类型
		 * @return 返回结果对象
		 * @throws IOException
		 *             请求失败异常
* 发送get请求，并将返回结果解析成对应对象
		 */
		public <T> T getForObject(Class<T> t) throws IOException {
			String str = get();
			return decode(str, t);
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:19:08
		 * @author xuyajie
		 * @return 返回的结果对象
		 * @throws IOException
		 *             请求失败异常
* 发送post请求，并将返回结果解析成对应对象
		 */
		public Result postForResult() throws IOException {
			return postForObject(Result.class);
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:19:17
		 * @author xuyajie
		 * @return 返回的结果对象
		 * @throws IOException
		 *             请求失败异常
* 发送get请求，并将返回结果解析成结果对象
		 */
		public Result getForResult() throws IOException {
			return getForObject(Result.class);
		}
		
		/***
		 * 
		 * @date 2018年6月20日 下午3:20:44
		 * @author xuyajie
		 * @param method
		 *            请求的方法
		 * @return 返回的结果
		 * @throws IOException
		 *             请求失败异常
* 使用指定的Http方法请求
		 */
		public String request(String method) throws IOException {
			RequestBody requestBody = encode();
			Request req;
			if(!HttpMethod.requiresRequestBody(method)){
				req = new Request.Builder().url(addParamsToUrl()).method(method, null).headers(Headers.of(headers))
						.build();
			}else{
				req = new Request.Builder().url(targetUrl).method(method, requestBody).headers(Headers.of(headers))
						.build();
			}
			Call call = okHttpClient.newCall(req);
			Response response = call.execute();
			if (response.isSuccessful()) {
				String resultStr = response.body().string();
				return resultStr;
			} else {
				throw new IOException("请求失败：" + response.code() + "," + response.message());
			}
		}
		
		/**
		 * 
		 * @date 2018年6月20日 下午3:23:11
		 * @author xuyajie
		 * @param targetUrl
		 *            请求地址
		 * @return 请求对象
* 设置请求地址
		 */
		public HttpRequest url(String targetUrl) {
			this.targetUrl = targetUrl;
			return this;
		}
		
		/**
		 * 
		 * @date 2018年6月20日 下午3:23:32
		 * @author xuyajie
		 * @param params
		 *            请求参数集合
		 * @return 请求对象
* 设置请求参数
		 */
		public HttpRequest setParams(Map<String, Object> params) {
			if(params == null){
				params = new HashMap<>();
			}
			this.params = params;
			return this;
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:22:12
		 * @author xuyajie
		 * @param key
		 *            参数名
		 * @param value
		 *            参数值
		 * @return 请求对象
* 添加请求参数
		 */
		public HttpRequest addParam(String key, Object value) {
			this.getParams().put(key, value);
			return this;
		}

		public HttpRequest setParam(String key, Object value){
			return addParam(key, value);
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:23:57
		 * @author xuyajie
		 * @param headers
		 *            请求头集合
		 * @return 请求对象
* 设置请求头
		 */
		public HttpRequest setHeaders(Map<String, String> headers) {
			this.headers = headers;
			return this;
		}
		
		public HttpRequest setHeader(String key, String value) {
			return addHeader(key, value);
		}
		
		/**
		 * 
		 * @date 2018年6月20日 下午3:22:47
		 * @author xuyajie
		 * @param key
		 *            请求头名
		 * @param value
		 *            请求头值
		 * @return 请求对象
		 * 添加请求头
		 */
		public HttpRequest addHeader(String key, String value) {
			this.getHeaders().put(key, value);
			return this;
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:24:24
		 * @author xuyajie
		 * @param body
		 *            字节数组
		 * @return 请求对象
* 设置post提交时body中的字节数组
		 */
		public HttpRequest setBody(byte[] body) {
			this.body = body;
			setEncodeType(EncodeType.BYTE);
			return this;
		}
		public HttpRequest setBody(byte[] body, String contentType) {
			this.body = body;
			setEncodeType(EncodeType.BYTE);
			this.setHeader(HEADER_CONTENT_TYPE, contentType);
			return this;
		}
		
		/**
		 * 
		 * @date 2018年6月20日 下午3:24:56
		 * @author xuyajie
		 * @param encodeType
		 *            序列化方式
		 * @return 请求对象
* 设置参数序列化方式
		 */
		public HttpRequest setEncodeType(EncodeType encodeType) {
			this.encodeType = encodeType;
			return this;
		}

		/**
		 * 
		 * @date 2018年6月20日 下午3:26:33
		 * @author xuyajie
		 * @param decodeType
		 *            反序列化方式
		 * @return 请求对象
* 设置参数反序列化方式
		 */
		public HttpRequest setDecodeType(DecodeType decodeType) {
			this.decodeType = decodeType;
			return this;
		}
		/**
		 * 
		* @date 2018年6月20日 下午4:49:39 
		* @author xuyajie 
		* @param contentType
		* @return
* 设置请求内容的媒体类型
		 */
		public HttpRequest setContentType(String contentType) {
			this.setHeader(HEADER_CONTENT_TYPE, contentType);
			return this;
		}
		
		/**
		 * 
		* @date 2018年11月8日 下午5:07:14 
		* @author xuyajie 
		* @param key 
		* @param filename 文件名
		* @param fileInputStream 文件输入流
		* @return
		 * @throws IOException 
* 设置要上传的文件
		 */
		public HttpRequest setFile(String key, String filename, InputStream fileInputStream) throws IOException{
			byte[] filebyte = copyToByteArray(fileInputStream);
			RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), filebyte);
			Part part = Part.createFormData(key, filename, fileBody);
			this.getParams().put(key, part);
			return this;
		}
		//将输入流中的数据读取成字节数组
		private byte[] copyToByteArray(InputStream in) throws IOException {
			int BUFFER_SIZE = 4096;
			if (in == null) {
				return new byte[0];
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
			return out.toByteArray();
		}
		
		/**
		 * 
		* @date 2018年11月8日 下午5:48:22 
		* @author xuyajie 
		* @param key
		* @param filename 文件名
		* @param filebyte 文件字节数组
		* @return
* 设置要上传的文件
		 */
		public HttpRequest setFile(String key, String filename, byte[] filebyte){
			RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), filebyte);
			Part part = Part.createFormData(key, filename, fileBody);
			this.getParams().put(key, part);
			return this;
		}
		/**
		 * 
		* @date 2018年11月8日 下午5:48:49 
		* @author xuyajie 
		* @param key
		* @param filename 文件名
		* @param file 文件
		* @return
* 设置要上传的文件
		 */
		public HttpRequest setFile(String key, String filename, File file){
			RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
			Part part = Part.createFormData(key, filename, fileBody);
			this.getParams().put(key, part);
			return this;
		}
		
		/**
		 * 
		* @date 2018年6月20日 下午4:53:15 
		* @author xuyajie 
		* @return
		* @Description:返回请求内容的媒体类型
		 */
		public String getContentType(){
			return this.getHeaders().get(HEADER_CONTENT_TYPE);
		}
		
		public String getTargetUrl() {
			return targetUrl;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public Map<String, Object> getParams() {
			return params;
		}

		public EncodeType getEncodeType() {
			return encodeType;
		}

		public byte[] getBody() {
			return body;
		}

		public DecodeType getDecodeType() {
			return decodeType;
		}
		
	}

	public static int getCONNECT_TIME_OUT() {
		return CONNECT_TIME_OUT;
	}

	public static void setCONNECT_TIME_OUT(int cONNECT_TIME_OUT) {
		CONNECT_TIME_OUT = cONNECT_TIME_OUT;
	}

	public static int getREAD_TIME_OUT() {
		return READ_TIME_OUT;
	}

	public static void setREAD_TIME_OUT(int rEAD_TIME_OUT) {
		READ_TIME_OUT = rEAD_TIME_OUT;
	}

	public static int getWRITE_TIME_OUT() {
		return WRITE_TIME_OUT;
	}

	public static void setWRITE_TIME_OUT(int wRITE_TIME_OUT) {
		WRITE_TIME_OUT = wRITE_TIME_OUT;
	}
	
}