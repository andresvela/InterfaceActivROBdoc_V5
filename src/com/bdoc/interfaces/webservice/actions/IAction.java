package com.bdoc.interfaces.webservice.actions;

import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;

import java.util.HashMap;

public interface IAction extends Cloneable{
	
	
	/**
	 * Vérifie l'execution de l'action
	 * @param indexes
	 * @return
	 */
	public boolean selectAction (HashMap<String,String> indexes);	

	
	/**
	 * Execute l'action 
	 * 
	 * @param xmlflow
	 * @param indexes
	 * @return
	 * @throws BdocWebServiceInterfaceException 
	 */
	public WebServiceRetour execute (byte[] xmlflow) throws BdocWebServiceInterfaceException;	
		
	public IAction getnewInstance() throws CloneNotSupportedException;
	
	public String getName();
	
}
