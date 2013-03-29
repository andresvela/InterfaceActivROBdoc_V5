
package com.bdoc.bcsearch.unique.webservice;

public interface UniqueBcQueryExecutorService extends javax.xml.rpc.Service {
	public java.lang.String getBcQueryExecutorAddress();

	public com.bdoc.bcsearch.unique.webservice.BcQueryExecutor getBcQueryExecutor() throws javax.xml.rpc.ServiceException;

	public com.bdoc.bcsearch.unique.webservice.BcQueryExecutor getBcQueryExecutor(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
