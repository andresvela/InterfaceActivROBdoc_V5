
package com.bdoc.bcsearch.unique.webservice;

public interface BcQueryExecutor extends java.rmi.Remote {
	public java.util.HashMap executeBCQuery(java.lang.String id, java.lang.String type) throws java.rmi.RemoteException;
}
