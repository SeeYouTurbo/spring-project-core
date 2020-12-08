package com.king.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.king.common.exception.JsonParseException;
import com.king.common.support.CustomJavaTimeModule;
import com.king.common.support.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
* @author xuyajie 
* @date 2018年7月18日 上午11:29:49 
* json转换工具
 */
public class JsonUtil {
	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	/**
	 * 
	* @date 2018年7月18日 上午11:33:41 
	* @author xuyajie 
	* @param object 目标对象
	* @return json字符串
	* @throws com.king.common.exception.JsonParseException json转换异常
* 将对象转成json字符串
	 */
	public static String toJsonString(Object object) throws JsonParseException {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}
	/**
	 * 
	* @date 2018年7月18日 上午11:34:07 
	* @author xuyajie 
	* @param json json字符串
	* @param t 目标类型
	* @return 对象
	* @throws JsonParseException json转换异常
* 将json字符串转成对应类型的对象
	 */
	public static <T> T toObject(String json, Class<T> t) throws JsonParseException {
		try {
			return new ObjectMapper().readValue(json, t);
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}

	/**
	 * 转为Json字符串
	 *
	 * @param value :
	 * @return java.lang.String
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:36
	 */
	public static <T> String toJson(T value) {
		try {
			return getInstance().writeValueAsString(value);
		} catch (Exception var2) {
			log.error(var2.getMessage(), var2);
			throw Exceptions.unchecked(var2);
		}
	}

	/**
	 * 对象转为JSON byte[]
	 *
	 * @param object :
	 * @return byte[]
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:37
	 */
	public static byte[] toJsonAsBytes(Object object) {
		try {
			return getInstance().writeValueAsBytes(object);
		} catch (JsonProcessingException var2) {
			throw Exceptions.unchecked(var2);
		}
	}

	/**
	 * 解析JSON字符串为指定类
	 *
	 * @param content :
	 * @param valueType :
	 * @return T
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:37
	 */
	public static <T> T parse(String content, Class<T> valueType) {
		try {
			return getInstance().readValue(content, valueType);
		} catch (Exception var3) {
			throw Exceptions.unchecked(var3);
		}
	}

	/**
	 * 解析JSON字符串为指定类
	 *
	 * @param content :
	 * @param typeReference :
	 * @return T
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:37
	 */
	public static <T> T parse(String content, TypeReference<?> typeReference) {
		try {
			return getInstance().readValue(content, typeReference);
		} catch (IOException var3) {
			throw Exceptions.unchecked(var3);
		}
	}

	/**
	 * 解析JSON字符串字节 为指定类
	 *
	 * @param bytes :
	 * @param valueType :
	 * @return T
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:37
	 */
	public static <T> T parse(byte[] bytes, Class<T> valueType) {
		try {
			return getInstance().readValue(bytes, valueType);
		} catch (IOException var3) {
			throw Exceptions.unchecked(var3);
		}
	}
	/**
	 * 解析JSON字符串字节 为指定类
	 *
	 * @param bytes :
	 * @param typeReference :
	 * @return T
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:37
	 */
	public static <T> T parse(byte[] bytes, TypeReference<?> typeReference) {
		try {
			return getInstance().readValue(bytes, typeReference);
		} catch (IOException var3) {
			throw Exceptions.unchecked(var3);
		}
	}
	/**
	 * 解析JSON流 为指定类
	 *
	 * @param in :
	 * @param valueType :
	 * @return T
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:38
	 */
	public static <T> T parse(InputStream in, Class<T> valueType) {
		try {
			return getInstance().readValue(in, valueType);
		} catch (IOException var3) {
			throw Exceptions.unchecked(var3);
		}
	}
	/**
	 * 解析JSON流 为指定类
	 *
	 * @param in :
	 * @param typeReference :
	 * @return T
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:38
	 */
	public static <T> T parse(InputStream in, TypeReference<?> typeReference) {
		try {
			return getInstance().readValue(in, typeReference);
		} catch (IOException var3) {
			throw Exceptions.unchecked(var3);
		}
	}
	/**
	 * 通过jsonString读取tree数据
	 *
	 * @param jsonString :
	 * @return com.fasterxml.jackson.databind.JsonNode
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:38
	 */
	public static JsonNode readTree(String jsonString) {
		try {
			return getInstance().readTree(jsonString);
		} catch (IOException var2) {
			throw Exceptions.unchecked(var2);
		}
	}
	/**
	 * 通过InputStream读取tree数据
	 *
	 * @param in :
	 * @return com.fasterxml.jackson.databind.JsonNode
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:39
	 */
	public static JsonNode readTree(InputStream in) {
		try {
			return getInstance().readTree(in);
		} catch (IOException var2) {
			throw Exceptions.unchecked(var2);
		}
	}
	/**
	 * 通过byte[]读取tree数据
	 *
	 * @param content :
	 * @return com.fasterxml.jackson.databind.JsonNode
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:39
	 */
	public static JsonNode readTree(byte[] content) {
		try {
			return getInstance().readTree(content);
		} catch (IOException var2) {
			throw Exceptions.unchecked(var2);
		}
	}
	/**
	 * 通过JsonParser读取tree数据
	 *
	 * @param jsonParser :
	 * @return com.fasterxml.jackson.databind.JsonNode
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:39
	 */
	public static JsonNode readTree(JsonParser jsonParser) {
		try {
			return (JsonNode)getInstance().readTree(jsonParser);
		} catch (IOException var2) {
			throw Exceptions.unchecked(var2);
		}
	}
	/**
	 * 获取ObjectMapper实例
	 *
	 * @return com.fasterxml.jackson.databind.ObjectMapper
	 * @author jinfeng.wu
	 * @date 2020/8/17 13:39
	 */
	public static ObjectMapper getInstance() {
		return JsonUtil.JacksonHolder.INSTANCE;
	}

	public static class JacksonObjectMapper extends ObjectMapper {
		private static final long serialVersionUID = 4288193147502386170L;
		private static final Locale CHINA;

		public JacksonObjectMapper() {
			super.setLocale(CHINA);
			super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			super.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
			super.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			super.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
			super.findAndRegisterModules();
			super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			super.getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
			super.registerModule(new CustomJavaTimeModule());
			super.findAndRegisterModules();
		}
		static {
			CHINA = Locale.CHINA;
		}
	}

	private static class JacksonHolder {
		private static ObjectMapper INSTANCE = new JsonUtil.JacksonObjectMapper();

		private JacksonHolder() {
		}
	}

}