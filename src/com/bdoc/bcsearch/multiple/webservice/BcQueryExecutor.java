

package com.bdoc.bcsearch.multiple.webservice;

public interface BcQueryExecutor extends java.rmi.Remote {
    
	public java.util.HashMap[] executeBCQuery(java.util.HashMap map, java.lang.String type) throws java.rmi.RemoteException;

}
