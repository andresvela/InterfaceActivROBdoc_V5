package com.bdoc.interfaces.webservice.actions;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.bdoc.interfaces.webservice.utils.IndexValues;

public abstract class Action implements IAction {

	protected Logger logger = Logger.getLogger(this.getClass());
	protected String filter = "FALSE";
	protected IndexValues indexValues;
	protected String name = this.getClass().getSimpleName();
	
	/**
	 * Test si l'action doit être executée
	 * @param expression
	 * @param documentIndex
	 * @return
	 */
	
	@Override
	public boolean selectAction(HashMap<String, String> indexes) {
		boolean result = false;
		indexValues = new IndexValues();
		indexValues.put(indexes);
		Expression exp = Expression.get(filter);
		result = exp.getValue(indexValues).equals("TRUE");
		logger.debug("filter = " + filter + " ==> result=" + result);
		return result;
	}

	@Override
	public WebServiceRetour execute(byte[] xmlflow) throws BdocWebServiceInterfaceException {
		return null;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
		
	public static IAction instance; 
	
	public void initialize ()
	{
		instance = this;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	

}
