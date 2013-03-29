package com.bdoc.interfaces.webservice.parametres;

import java.util.List;

import sun.security.jca.GetInstance;

import com.bdoc.interfaces.webservice.actions.IAction;

public class ActionList {

	private static ActionList instance = null;
	
	private List<IAction> actionList;
	
	
	public static ActionList getInstance ()
	{
		return instance;
	}
	
	public ActionList()
	{
		
	}

	public void initialize ()
	{
		instance=this;
	}
	

	public List<IAction> getActionList() {
		return actionList;
	}


	public void setActionList(List<IAction> actionList) {
		this.actionList = actionList;
	}
	

	
}
