package com.bdoc.interfaces.webservice.batch;

import com.bdoc.interfaces.webservice.actions.IAction;
import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;


public interface IBatchConnection extends Cloneable {

	public WebServiceRetour execute (byte[] fileContent) throws BdocWebServiceInterfaceException;
	
	public IBatchConnection getnewInstance() throws CloneNotSupportedException;
		
}
