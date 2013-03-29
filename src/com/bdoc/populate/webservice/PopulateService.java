

package com.bdoc.populate.webservice;

public interface PopulateService extends javax.xml.rpc.Service {
	
	public java.lang.String getPopulateAddress();

	public Populate getPopulate() throws javax.xml.rpc.ServiceException;

	public Populate getPopulate(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;

}
