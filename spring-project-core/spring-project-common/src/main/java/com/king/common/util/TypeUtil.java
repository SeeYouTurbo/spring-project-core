package com.king.common.util;


import com.king.common.exception.TypeCastException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类型转换工具
 * @author xuyajie
 * @date 2017年4月7日 下午12:03:56
 */
public class TypeUtil {
	/**
	 * 将对象转换为字符串
	 * @date 2017年4月11日 下午5:44:07
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的字符串
	 */
	public static String castToString(Object value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	/**
	 * 将对象转化为整型
	 * @date 2017年4月12日 上午11:57:01
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的整型
	 */
	public static Integer castToInt(Object value) {
		
		if (value == null) {
			return null;
		}

		if ((value instanceof Integer)) {
			return (Integer) value;
		}

		if ((value instanceof Number)) {
			return Integer.valueOf(((Number) value).intValue());
		}

		if ((value instanceof String)) {
			String strVal = (String) value;
			// 如果是带","的数字字符串，去掉","
			if (strVal.contains(",")) {
				strVal = strVal.replaceAll(",", "");
			}
			try {
				return Integer.valueOf(Integer.parseInt(strVal));
			} catch (NumberFormatException e) {
				throw new TypeCastException("无法将字符串" + value + "转换为整型。");
			}
		}
		throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为整型。对象：" + value);
	}

	/**
	 * 将对象转化为长整型
	 * @date 2017年4月12日 上午9:55:02
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的长整型
	 */
	public static Long castToLong(Object value) {
		if (value == null) {
			return null;
		}

		if ((value instanceof Number)) {
			return Long.valueOf(((Number) value).longValue());
		}

		if ((value instanceof String)) {
			String strVal = (String) value;
			// 如果是带","的数字字符串，去掉","
			if (strVal.contains(",")) {
				strVal = strVal.replaceAll(",", "");
			}
			try {
				return Long.valueOf(Long.parseLong(strVal));
			} catch (NumberFormatException e) {
				throw new TypeCastException("无法将字符串" + value + "转换为长整型。");
			}
		}
		throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为长整型。对象：" + value);
	}

	/**
	 * 将对象转化为短整型
	 * @date 2017年4月12日 上午11:55:46
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的短整型
	 */
	public static Short castToShort(Object value) {
		if (value == null) {
			return null;
		}

		if ((value instanceof Number)) {
			return Short.valueOf(((Number) value).shortValue());
		}

		if ((value instanceof String)) {
			String strVal = (String) value;
			// 如果是带","的数字字符串，去掉","
			if (strVal.contains(",")) {
				strVal = strVal.replaceAll(",", "");
			}
			try {
				return Short.valueOf(Short.parseShort(strVal));
			} catch (NumberFormatException e) {
				throw new TypeCastException("无法将字符串" + value + "转换为短整型。");
			}
		}

		throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为短整型。对象：" + value);
	}

	/**
	 * 将对象转化为大数字
	 * @date 2017年4月12日 上午11:55:57
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的大数字
	 */
	public static BigDecimal castToBigDecimal(Object value) {
		if (value == null) {
			return null;
		}

		if ((value instanceof BigDecimal)) {
			return (BigDecimal) value;
		}

		if ((value instanceof BigInteger)) {
			return new BigDecimal((BigInteger) value);
		}

		String strVal = value.toString();
		try {
			return new BigDecimal(strVal);
		} catch (NumberFormatException e) {
			throw new TypeCastException("无法将对象 " + value + " 转换为大数字类型。");
		}
	}

	/**
	 * 将对象转化为大整型
	 * @date 2017年4月12日 上午11:56:05
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的大整型
	 */
	public static BigInteger castToBigInteger(Object value) {
		if (value == null) {
			return null;
		}

		if ((value instanceof BigInteger)) {
			return (BigInteger) value;
		}

		if (((value instanceof Float)) || ((value instanceof Double))) {
			return BigInteger.valueOf(((Number) value).longValue());
		}

		String strVal = value.toString();
		try {
			return new BigInteger(strVal);
		} catch (NumberFormatException e) {
			throw new TypeCastException("无法将对象 " + value + " 转换为大整型。");
		}

	}

	/**
	 * 将对象转化为浮点型
	 * @date 2017年4月12日 上午11:56:18
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的浮点型
	 */
	public static Float castToFloat(Object value) {
		if (value == null) {
			return null;
		}

		if ((value instanceof Number)) {
			return Float.valueOf(((Number) value).floatValue());
		}

		if ((value instanceof String)) {
			String strVal = value.toString();
			// 如果是带","的数字字符串，去掉","
			if (strVal.contains(",")) {
				strVal = strVal.replaceAll(",", "");
			}
			try {
				return Float.valueOf(Float.parseFloat(strVal));
			} catch (NumberFormatException e) {
				throw new TypeCastException("无法将字符串" + value + "转换为浮点型。");
			}
		}

		throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为浮点型。对象：" + value);
	}

	/**
	 * 将对象转化为双精度浮点型
	 * @date 2017年4月12日 上午11:56:28
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的双精度浮点型
	 */
	public static Double castToDouble(Object value) {
		if (value == null) {
			return null;
		}

		if ((value instanceof Number)) {
			return Double.valueOf(((Number) value).doubleValue());
		}

		if ((value instanceof String)) {
			String strVal = value.toString();
			// 如果是带","的数字字符串，去掉","
			if (strVal.contains(",")) {
				strVal = strVal.replaceAll(",", "");
			}
			try {
				return Double.valueOf(Double.parseDouble(strVal));
			} catch (NumberFormatException e) {
				throw new TypeCastException("无法将字符串" + value + "转换为双精度浮点型。");
			}
		}
		throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为双精度浮点型。对象：" + value);
	}

	/**
	 * 将对象转化为日期类型
	 * @date 2017年4月12日 上午11:56:50
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的日期类型
	 */
	public static Date castToDate(Object value) {
		if (value == null) {
			return null;
		}

		if ((value instanceof Date)) {
			return (Date) value;
		}

		if ((value instanceof Calendar)) {
			return ((Calendar) value).getTime();
		}

		long longValue = -1L;

		if ((value instanceof Number)) {
			longValue = ((Number) value).longValue();
			return new Date(longValue);
		}

		if ((value instanceof String)) {
			String strVal = (String) value;
			if (strVal.length() == 0) {
				return null;
			}
			String format = null;
			if (strVal.indexOf('-') != -1) {
				if (strVal.length() == "yyyy-MM-dd HH:mm:ss".length()) {
					format = "yyyy-MM-dd HH:mm:ss";
				} else if (strVal.length() == "yyyy-MM-dd".length()) {
					format = "yyyy-MM-dd";
				} else if (strVal.length() == "yyyy-MM-dd HH:mm:ss.SSS".length()) {
					format = "yyyy-MM-dd HH:mm:ss.SSS";
				}
			} else if (strVal.length() == "yyyyMMdd".length()) {
				format = "yyyyMMdd";
			} else if (strVal.length() == "yyyyMMddHHmmss".length()) {
				format = "yyyyMMddHHmmss";
			}
			if (format != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
				try {
					return dateFormat.parse(strVal);
				} catch (ParseException e) {
					throw new TypeCastException("无法将字符串" + value + "转换为日期类型。");
				}
			}

			longValue = Long.parseLong(strVal);
		}

		if (longValue < 0L) {
			throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为日期类型。对象：" + value);
		}
		return new Date(longValue);
	}

	/**
	 * 将对象转化为字节数组
	 * @date 2017年4月12日 上午11:57:09
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的字节数组
	 */
	public static byte[] castToBytes(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof byte[])) {
			return (byte[]) value;
		}
		if ((value instanceof String)) {
			return ((String) value).getBytes();
		}
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(output);
			objectOut.writeObject(value);
			objectOut.close();
			output.close();
			return output.toByteArray();
		} catch (IOException e) {
			throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为字节数组。对象：" + value);
		}
	}

	/**
	 * 将对象转化为布尔型
	 * @date 2017年6月12日 下午6:23:14
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的布尔型
	 */
	public static Boolean castToBoolean(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Boolean)) {
			return (Boolean) value;
		}
		if ((value instanceof Number)) {
			return Boolean.valueOf(((Number) value).intValue() == 1);
		}
		if ((value instanceof String)) {
			String strVal = (String) value;

			if ((strVal.length() == 0) || ("null".equals(strVal)) || ("NULL".equals(strVal))) {
				return null;
			}

			if (("true".equalsIgnoreCase(strVal)) || ("1".equals(strVal))) {
				return Boolean.TRUE;
			}

			if (("false".equalsIgnoreCase(strVal)) || ("0".equals(strVal))) {
				return Boolean.FALSE;
			}

			if (("Y".equalsIgnoreCase(strVal)) || ("T".equals(strVal))) {
				return Boolean.TRUE;
			}

			if (("F".equalsIgnoreCase(strVal)) || ("N".equals(strVal))) {
				return Boolean.FALSE;
			}
		}
		throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为Boolean。对象：" + value);
	}

	/**
	 * 将javabean和map对象转化为Map&lt;String,Object&gt;
	 * @date 2017年4月12日 下午2:33:58
	 * @author xuyajie
	 * @param value
	 *            目标对象
	 * @return 转换后的amp
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> castToMap(Object value) {
		if (value == null) {
			return null;
		}
		// 如果入参是Map
		if (value instanceof Map) {
			Map<?, ?> valueMap = (Map<?, ?>) value;
			Set<?> keySet = valueMap.keySet();
			Iterator<?> iterator = keySet.iterator();
			// 如果是空的map或者本身支持Map<String, Object>直接强转
			if (!iterator.hasNext() || iterator.next() instanceof String) {
				return (Map<String, Object>) value;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (Object key : keySet) {
				map.put(castToString(key), valueMap.get(key));
			}
			return map;
		}
		// 如果是javaBean
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Class<?> clazz = value.getClass();
			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String key = descriptor.getName();
				// 将每个对象都有的getClass方法排除在外
				if ("class".equals(key)) {
					continue;
				}
				Object attrVal = descriptor.getReadMethod().invoke(value);
				if(attrVal != null){
					//如果该属性值不为空才put到map中去
					map.put(key, attrVal);
				}
			}
			return map;
		} catch (IntrospectionException e) {
			throw new TypeCastException("无法将类型为" + value.getClass() + "的对象转换为Map。对象：" + value);
			// 以下异常为Method.invoke方法抛出，按逻辑是不会抛出的，所以不做处理
		} catch (IllegalAccessException e) {
			throw new TypeCastException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new TypeCastException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new TypeCastException(e.getMessage());
		}
	}
	
	/**
	 * 将目标转换为List
	* @date 2018年3月22日 下午1:56:44 
	* @author xuyajie 
	* @param value 目标对象
	* @return 转换后的List
	 */
	public static List<?> castToList(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			return (List<?>) value;
		}
		if (value instanceof Iterable) {
			List<Object> list = new ArrayList<Object>();
			Iterable<?> iterables = (Iterable<?>) value;
			for (Object obj : iterables) {
				list.add(obj);
			}
			return list;
		}
		if (value.getClass().isArray()) {
			List<Object> list = new ArrayList<Object>();
			Object[] objs = (Object[]) value;
			for (Object obj : objs) {
				list.add(obj);
			}
			return list;
		}
		List<Object> list = new ArrayList<Object>();
		list.add(value);
		return list;
	}
	/**
	 * 将目标转换为List
	* @date 2018年3月22日 下午2:14:20 
	* @author xuyajie 
	* @param value 目标对象
	* @param t 泛型类型
	* @param <T> t的类型
	* @return 转换后的List
	 */
	public static <T> List<T> castToList(Object value, Class<T> t) {
		if (value == null) {
			return null;
		}
		if (value instanceof Collection) {
			List<T> list = new ArrayList<T>();
			Collection<?> conlections = (Collection<?>) value;
			for (Object obj : conlections) {
				list.add(castToClass(obj, t));
			}
			return list;
		}
		List<T> list = new ArrayList<T>();
		list.add(castToClass(value, t));
		return list;
	}
	
	
	/**
	 * 将目标转换为Set
	* @date 2018年3月22日 下午1:57:20 
	* @author xuyajie 
	* @param value 目标对象
	* @return 转换后的Set
	 */
	public static Set<?> castToSet(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Set) {
			return (Set<?>) value;
		}
		if (value instanceof Collection) {
			Set<Object> set = new HashSet<Object>();
			Collection<?> conlections = (Collection<?>) value;
			for (Object obj : conlections) {
				set.add(obj);
			}
			return set;
		}
		if (value.getClass().isArray()) {
			Set<Object> set = new HashSet<Object>();
			Object[] objs = (Object[]) value;
			for (Object obj : objs) {
				set.add(obj);
			}
			return set;
		}
		Set<Object> set = new HashSet<Object>();
		set.add(value);
		return set;
	}
	/**
	 * 将目标转换为Set
	* @date 2018年3月22日 下午2:13:51 
	* @author xuyajie 
	* @param value 目标对象
	* @param t 泛型类型
	* @param <T> t的类型
	* @return 转换后的Set
	 */
	public static <T> Set<T> castToSet(Object value, Class<T> t) {
		if (value == null) {
			return null;
		}
		if (value instanceof Collection) {
			Set<T> set = new HashSet<T>();
			Collection<?> conlections = (Collection<?>) value;
			for (Object obj : conlections) {
				set.add(castToClass(obj, t));
			}
			return set;
		}
		Set<T> set = new HashSet<T>();
		set.add(castToClass(value, t));
		return set;
	}
	

	/**
	 * 将源对象转为目标对象
	 * @date 2017年12月24日 上午3:14:44
	 * @author xuyajie
	 * @param object
	 *            源对象
	 * @param t
	 *            转换后的bean的类型的class对象
	 * @param <T>
	 *            bean的类型
	 * @return 转换后的bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T castToBean(Object object, Class<T> t) {
		if(object == null) {
			return null;
		}
		if (object instanceof Map) {
			return castMapToBean((Map<String, Object>) object, t);
		} else {
			return castBeanToBean(object, t);
		}
	}

	/**
	 * 将Map转换为java bean
	 * @date 2017年4月12日 下午5:57:48
	 * @author xuyajie
	 * @param map
	 *            源Map
	 * @param t
	 *            转换后的bean的类型的class对象
	 * @param <T>
	 *            bean的类型
	 * @return 转换后的bean
	 */
	public static <T> T castMapToBean(Map<String, Object> map, Class<T> t) {
		try {
			T object = t.newInstance();
			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(t).getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {

				String key = descriptor.getName();
				Object value = map.get(key);
				if (value == null) {
					continue;
				}
				if (map.containsKey(key)) {
					try {
						Method writeMethod = descriptor.getWriteMethod();
						Class<?> paramClass = writeMethod.getParameterTypes()[0];
						if (paramClass.isAssignableFrom(value.getClass())) {
							// 如果类型刚好匹配，则直接赋值
							descriptor.getWriteMethod().invoke(object, value);
						}else {
							descriptor.getWriteMethod().invoke(object, castToClass(value, paramClass));
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
			return object;
		} catch (InstantiationException e) {
			throw new TypeCastException("转换失败，" + t.getName() + "不是合法的javaBean:" + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new TypeCastException("转换失败，" + t.getName() + "不是合法的javaBean:" + e.getMessage());
		} catch (IntrospectionException e) {
			throw new TypeCastException("转换失败，" + t.getName() + "不是合法的javaBean:" + e.getMessage());
		}
	}

	/**
	 * 将一个javabean转为另一个javabean
	 * @date 2017年12月24日 上午2:48:35
	 * @author xuyajie
	 * @param bean
	 *            原bean
	 * @param t
	 *            目标bean的类型
	 * @param <T>
	 *            bean的类型
	 * @return 目标bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T castBeanToBean(Object bean, Class<T> t) {
		if (bean == null || t == null) {
			return null;
		}

		if (t.isAssignableFrom(bean.getClass())) {
			return (T) bean;
		}

		try {
			T target = t.newInstance();
			PropertyDescriptor[] targetDescriptors = Introspector.getBeanInfo(t).getPropertyDescriptors();
			PropertyDescriptor[] sourceDescriptors = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
			for (PropertyDescriptor targetDescriptor : targetDescriptors) {
				String key = targetDescriptor.getName();
				for (PropertyDescriptor sourceDescriptor : sourceDescriptors) {
					String sourceKey = sourceDescriptor.getName();
					if (key.equals(sourceKey)) {
						try {
							targetDescriptor.getWriteMethod().invoke(target,
									sourceDescriptor.getReadMethod().invoke(bean));
						} catch (Exception e) {
							continue;
						}
					}
				}
			}
			return target;
		} catch (InstantiationException e) {
			throw new TypeCastException("转换失败，" + t.getName() + "不是合法的javaBean:" + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new TypeCastException("转换失败，" + t.getName() + "不是合法的javaBean:" + e.getMessage());
		} catch (IntrospectionException e) {
			throw new TypeCastException("转换失败，" + t.getName() + "不是合法的javaBean:" + e.getMessage());
		}
	}
	
	/**
	 * 强对应目标对象转换成特定的类型的对象（对象类型先定为常规类型和javaBean
	* @date 2018年3月22日 下午2:36:17 
	* @author xuyajie 
	* @param value 目标对象
	* @param t 目标类型
	* @param <T> t的类型
	* @return 特定的类型的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T castToClass(Object value, Class<T> t) {
		String targetClassName = t.getName();
		if ("java.lang.Long".equals(targetClassName) || "long".equals(targetClassName)) {
			return (T) castToLong(value);
		} else if ("java.lang.Integer".equals(targetClassName) || "int".equals(targetClassName)) {
			return (T) castToInt(value);
		} else if ("java.lang.Short".equals(targetClassName) || "short".equals(targetClassName)) {
			return (T) castToShort(value);
		} else if ("java.lang.Double".equals(targetClassName) || "double".equals(targetClassName)) {
			return (T) castToDouble(value);
		} else if ("java.lang.Float".equals(targetClassName) || "float".equals(targetClassName)) {
			return (T) castToFloat(value);
		} else if ("java.math.BigDecimal".equals(targetClassName)) {
			return (T) castToBigDecimal(value);
		} else if ("java.math.BigInteger".equals(targetClassName)) {
			return (T) castToBigInteger(value);
		}else if("java.lang.String".equals(targetClassName)){
			return (T) castToString(value);
		}else if("java.util.Date".equals(targetClassName)){
			return (T) castToDate(value);
		}else if("java.lang.Boolean".equals(targetClassName) || "boolean".equals(targetClassName)){
			return (T) castToBoolean(value);
		}else if("java.util.Map".equals(targetClassName)){
			return (T) castToMap(value);
		}else if("java.util.List".equals(targetClassName)){
			return (T) castToList(value);
		}else if("java.util.Set".equals(targetClassName)){
			return (T) castToSet(value);
		}
		return castToBean(value, t);
	}
	
}