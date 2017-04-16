package br.com.lucas.camara.easydtomap;

public class EasyDtoMapFactory {
	
	public static EasyDtoMap create() {
		return new EasyDtoMapImpl();
	}
	
}