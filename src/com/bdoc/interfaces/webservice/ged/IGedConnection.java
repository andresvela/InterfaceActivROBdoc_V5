package com.bdoc.interfaces.webservice.ged;

import java.util.HashMap;

import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.IndexValues;

public interface IGedConnection extends Cloneable {

	public void sendDocumentToGED (IndexValues indexValues , byte[] fileContent, byte[] indexcontent) throws BdocWebServiceInterfaceException;
	public byte[] getDocumentFromGED (HashMap<String, String> Index) throws BdocWebServiceInterfaceException;
	public IGedConnection getNewInstance() throws BdocWebServiceInterfaceException;

}
