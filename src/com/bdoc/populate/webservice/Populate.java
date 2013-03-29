
package com.bdoc.populate.webservice;

public interface Populate extends java.rmi.Remote {

	public byte[] generate(byte[] xmlIn) throws java.rmi.RemoteException;

}
