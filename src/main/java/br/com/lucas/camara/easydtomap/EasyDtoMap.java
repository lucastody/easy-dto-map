package br.com.lucas.camara.easydtomap;

import java.util.Collection;
import java.util.Map;

public interface EasyDtoMap {
	
	Map<String, Object> toDtoMap(Object object) throws Exception;
	
	Object[] toDtoArray(Collection<? extends Object> collection) throws Exception;
	
}