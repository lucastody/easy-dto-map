package br.com.lucas.camara.easydtomap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EasyDtoMap {
	
	public static Map<String, Object> toDtoMap(Object obj) throws Exception {
		return map(obj, null);
	}
	
	private static Map<String, Object> map(Object obj, Object parent) throws Exception {
		if(obj == null) {
			return null;
		}
		
		Class<? extends Object> klass = obj.getClass();
		Map<String, Object> mapping = new HashMap<>();
		Field[] declaredFields = klass.getDeclaredFields();
		
		for(Field field : declaredFields) {
			String name = field.getName();
			Object attributeValue = getAttributeValue(field, klass, obj, parent);
			
			if(attributeValue != null && !getWrapperTypes().contains(attributeValue.getClass())) {
				if(attributeValue instanceof Collection) {
					Collection<?> collection = (Collection<?>) attributeValue;
					List<Object> listMapping = new ArrayList<>();
					
					for(Object collectionItem : collection) {
						listMapping.add(map(collectionItem, obj));
					}
					
					mapping.put(name, listMapping);
				} else {
					mapping.put(name, map(attributeValue, obj));
				}
			} else {
				mapping.put(name, attributeValue);
			}
		}
		return mapping;
	}
	
	private static Object getAttributeValue(Field field, Class<? extends Object> klass, Object obj, Object parent) throws Exception {
		Method method = klass.getMethod(getMethodName(field));
		Object result = method.invoke(obj);
		
		if(result == parent) {
			return null;
		}
		
		return result;
	}
	
	private static String getMethodName(Field field) {
		String name = field.getName();
		Character firstChar = Character.toUpperCase(name.charAt(0));
		String methodName = name.substring(1, name.length());
		return "get" + firstChar + methodName;
	}

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		ret.add(String.class);
		
		return ret;
	}
}