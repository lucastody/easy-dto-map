package br.com.lucas.camara.easydtomap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EasyDtoMapImpl implements EasyDtoMap {
	
	EasyDtoMapImpl() {
		super();
	}

	@Override
	public Map<String, Object> fromObject(Object object) throws Exception {
		return map(object, null);
	}

	@Override
	public Object[] fromCollection(Collection<? extends Object> collection) throws Exception {
		return mapList(collection);
	}
	
	private Object[] mapList(Collection<? extends Object> list) throws Exception {
		Map<Integer, Object> tmp = new HashMap<Integer, Object>();
		int cont = 0;
		
		for(Object obj : list) {
			Map<String, Object> map = map(obj, null);
			tmp.put(cont++, map);
		}
		
		Set<Entry<Integer, Object>> entrySet = tmp.entrySet();
		Object[] listTmp = new Object[entrySet.size()];
		cont = 0;
		
		for(Entry<Integer, Object> entry : tmp.entrySet()) {
			Object value = entry.getValue();
			listTmp[cont++] = value;
		}
		
		return listTmp;
	}
	
	private Map<String, Object> map(Object obj, Object parent) throws Exception {
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
					mapping.put(name, getCollectionAttributeValue(attributeValue, obj));
				} else {
					mapping.put(name, map(attributeValue, obj));
				}
			} else {
				mapping.put(name, attributeValue);
			}
		}
		return mapping;
	}
	
	private List<Object> getCollectionAttributeValue(Object attributeValue, Object obj) throws Exception {
		Collection<?> collection = (Collection<?>) attributeValue;
		List<Object> listMapping = new ArrayList<>();
		
		for(Object collectionItem : collection) {
			listMapping.add(map(collectionItem, obj));
		}
		
		return listMapping;
	}
	
	private Object getAttributeValue(Field field, Class<? extends Object> klass, Object obj, Object parent) {
		Method method = null;
		Object result = null;
		
		try {
			method = klass.getMethod(getMethodName(field));
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
		
		
		try {
			result = method.invoke(obj);
		} catch(Exception ex) {
			return null;
		}
		
		if(result == parent) {
			return null;
		}
		
		return result;
	}
	
	private String getMethodName(Field field) {
		String name = field.getName();
		Character firstChar = Character.toUpperCase(name.charAt(0));
		String methodName = name.substring(1, name.length());
		return "get" + firstChar + methodName;
	}

	private Set<Class<?>> getWrapperTypes() {
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