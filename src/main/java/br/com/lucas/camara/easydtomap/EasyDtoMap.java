package br.com.lucas.camara.easydtomap;

import java.util.Collection;
import java.util.Map;

public interface EasyDtoMap {
	
	Map<String, Object> fromObject(Object object) throws Exception;
	
	Object[] fromCollection(Collection<? extends Object> collection) throws Exception;
	
}