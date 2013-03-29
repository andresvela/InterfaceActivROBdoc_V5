package com.bdoc.interfaces.webservice.actions.implementations;

import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;

public class GetDocumentInGedImpl implements GetDocumentInGed,Cloneable {

	private static GetDocumentInGedImpl instance ;
	 
	
	
	public GetDocumentInGedImpl getInstance()
	{
		return instance;
	}
	
	
	@Override
	public WebServiceRetour execute(String uniqueIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
