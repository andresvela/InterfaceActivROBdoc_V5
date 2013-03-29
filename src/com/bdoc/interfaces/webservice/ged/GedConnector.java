package com.bdoc.interfaces.webservice.ged;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.bdoc.interfaces.webservice.utils.IndexValues;

public class GedConnector {

	private IGedConnection gedConnection;
	private ArrayList<GedConnection> gedConnections = null;
	
	private static GedConnector instance;
	
	public static GedConnector getInstance()
	{
		return instance;
	}
	
	public IGedConnection getGedConnection() {
		return gedConnection;
	}

	public void setGedConnection(IGedConnection gedConnection) {
		this.gedConnection = gedConnection;
	}

	public void initialize()
	{
		instance=this;
	}

	public ArrayList<GedConnection> getGedConnections() {
		return gedConnections;
	}

	public void setGedConnections(ArrayList<GedConnection> gedConnections) {
		this.gedConnections = gedConnections;
	}

	
	/**
	 * recherche d'un connecteur GED en fonction du tableau d'index
	 * @param index
	 * @return
	 * @throws BdocWebServiceInterfaceException
	 */
	public GedConnection getGedConnection (IndexValues index)throws BdocWebServiceInterfaceException
	{
		try
		{
			for (GedConnection gc : gedConnections) {
				if (gc.getFilter() == null) return gc;

				if (Expression.get(gc.getFilter()).getBoolean(index))
				{
					Logger.getLogger(this.getClass().getCanonicalName()).info("GedConnection sélectionné : "+gc.getName()+ ": "+gc.getFilter());
					return gc;
				}
				else
				{
					Logger.getLogger(this.getClass().getCanonicalName()).debug("GedConnection non sélectionné : "+gc.getName()+ ": "+gc.getFilter());
				}
			}
		}
		catch (Exception e) {
			throw new BdocWebServiceInterfaceException(407,e);
		}

		throw new BdocWebServiceInterfaceException(406);

	}
	
	/**
	 * Retrouve un connecteur GED en fonction d'un tableau d'indexe
	 * @param index
	 * @return
	 * @throws BdocWebServiceInterfaceException
	 */
	public GedConnection getGedConnection (HashMap<String, String> index)throws BdocWebServiceInterfaceException
	{
		IndexValues gedIndex = new IndexValues();
		gedIndex.put(index);
		return getGedConnection(gedIndex);	
	}
	
}
