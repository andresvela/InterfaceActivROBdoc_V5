package com.bdoc.interfaces.webservice.batch;

public class BatchConnector {

	private static BatchConnector instance;
	private BatchConnection batchConnection;
	
	public static BatchConnector getInstance()
	{
		return instance;
	}

	public void initialize ()
	{
		instance = this;
	}

	public BatchConnection getBatchConnection() {
		return batchConnection;
	}

	public void setBatchConnection(BatchConnection batchConnection) {
		this.batchConnection = batchConnection;
	}

	
	
	
}
