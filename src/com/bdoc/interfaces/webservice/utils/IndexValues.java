package com.bdoc.interfaces.webservice.utils;

import java.util.HashMap;
import java.util.Map;


public class IndexValues implements ExpressionValues {

	private String indexes;

	private Map<String,String> cache;

	
	public String get(String name) {

		String v = cache.get(name);
		if(v != null) return v;

		return "";
	}

	
	public void put (HashMap<String, String> map)
	{
		cache = map;
	}

	public String toString() {
		return "IndexValues["+indexes+"]";
	}

	public String toSimpleString(){
		return indexes;
	}

	public void setSimpleindexes(String indexe){
		this.indexes = indexe;
	}

	public void replaceIndex(String code, String newValue){

	}

}
