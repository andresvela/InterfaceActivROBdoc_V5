package com.bdoc.interfaces.webservice.actions.implementations;

import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;

public interface GetDocumentInGed {

	public WebServiceRetour execute (String uniqueIndex) throws BdocWebServiceInterfaceException;
	
}
