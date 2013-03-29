
package com.bdoc.bcsearch.multiple.webservice;

public interface MultipleBcQueryExecutorService extends javax.xml.rpc.Service {
	
	public java.lang.String getBcQueryExecutorAddress();

	public com.bdoc.bcsearch.multiple.webservice.BcQueryExecutor getBcQueryExecutor() throws javax.xml.rpc.ServiceException;

	public com.bdoc.bcsearch.multiple.webservice.BcQueryExecutor getBcQueryExecutor(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
