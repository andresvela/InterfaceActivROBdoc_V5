package com.bdoc.interfaces.webservice.batch;


import sun.security.jca.GetInstance;

public abstract class BatchConnection implements IBatchConnection{

	private static BatchConnection instance ;
	
	
	public static BatchConnection getInstance()
	{
		return instance;
	}
	
}
