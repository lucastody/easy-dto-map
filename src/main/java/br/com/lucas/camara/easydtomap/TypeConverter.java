package br.com.lucas.camara.easydtomap;

public interface TypeConverter<T> {
	
	Object convertNew(T value);
	
}