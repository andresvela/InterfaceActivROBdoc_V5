package com.bdoc.interfaces.webservice.actions;

import java.util.HashMap;

import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;

public interface ParserDataFlow {

	public HashMap<String, String> parseDataFlow (byte[] xmlFlow) throws BdocWebServiceInterfaceException;
	
}
