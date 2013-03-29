package com.bdoc.interfaces.webservice.utils;

import java.util.HashMap;

public class testExpression {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HashMap<String, String> id = new HashMap<String, String>();
		IndexValues ids = new IndexValues();
		ids.put(id);
		
		
		id.put("id1", "Hello");
		id.put("id2", " World");
		
		String filter = "{id1}=Hello";
		
		Expression exp = Expression.get(filter);
		System.out.println (exp.getValue(ids));

	}

}
