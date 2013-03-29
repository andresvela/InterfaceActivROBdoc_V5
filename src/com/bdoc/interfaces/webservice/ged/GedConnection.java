package com.bdoc.interfaces.webservice.ged;



public abstract class GedConnection implements IGedConnection {

	protected static IGedConnection instance;
	private String filter = "TRUE";
	private String name = "";
	
	public void initialize()
	{
		instance = (GedFileSystem) this;
	}
	
	public IGedConnection getInstance()
	{
		return instance;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
