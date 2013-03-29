package com.bdoc.interfaces.webservice.parametres;


import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.actions.implementations.BdocWebConnection;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.java.ediweb.api.JBApplication;

public class BdocWebProfile 
{ 

  private Logger logger = Logger.getLogger(BdocWebProfile.class.getName());

  private static HashMap<String,BdocWebProfile> instance = null;
  
  private String hostname = "localhost";
  private int port=800;
  private String format="PDF";
  private String name="default";
  private String keyWordSerchOperator="ET";

  // connexion associé au profile
  private BdocWebConnection bdocWebConnection = null;
  
  public static BdocWebProfile getInstance (String name) throws BdocWebServiceInterfaceException
  {
	  BdocWebProfile bdocWebProfile = null;
	  
	  if (name != null)
	  {
		  bdocWebProfile = instance.get(name);
	  }
	  else
	  { 
		  bdocWebProfile = instance.get("default");
	  }
	  
	  if (bdocWebProfile==null) throw new BdocWebServiceInterfaceException(6,"pour le profile '"+name+"'");
	  
	  return bdocWebProfile;
  }

  public void initvalues() 
  {
	  	if (instance == null) instance = new HashMap<String, BdocWebProfile>();
	  	
	  	instance.put(name, this);
	  	logger.info("Profile de connexion à BdocWeb : "+this.toString());
	  	try {
			bdocWebConnection = new BdocWebConnection();
			bdocWebConnection.getJBApplication(this);
		} catch (BdocWebServiceInterfaceException e) {
			logger.error(e.libelle());
		}
  }
  
 
  public void finalize()
  {
	  logger.info("Fermeture de la session "+name);
	  if (bdocWebConnection!=null) bdocWebConnection.dispose();
  }
    
  public JBApplication getBdocWebApplication() throws BdocWebServiceInterfaceException
  {
	  return bdocWebConnection.getJBApplication(this);
  }
  
  public void dispose()
  {
	finalize();
  }
  
  public BdocWebProfile()
  {
  }
  
  public String getHostname() {
		return hostname;
  }


  public int getPort() {
		return port;
  }


	/**
	 * Format du document
	 * Valeur par défaut : PDF
	 * @return
	 */
  public String getFormat() {
			if ((format==null)||("".equals(format))) format="PDF";
			return format;
  }

  @Override
  public String toString()
  {
	return "name = "+name+" host="+hostname+":"+port+" default document format = "+getFormat();
	 
  }
public void setHostname(String hostname) {
	this.hostname = hostname;
}

public void setPort(int port) {
	this.port = port;
}

public void setFormat(String format) {
	this.format = format;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public static Set<String> getProfiles()
{
	return instance.keySet();
}

public BdocWebConnection getbdocWebConnection()
{
	return bdocWebConnection;
}

public String getKeyWordSerchOperator() {
	return keyWordSerchOperator;
}

public void setKeyWordSerchOperator(String keyWordSerchoperator) {
	this.keyWordSerchOperator = keyWordSerchoperator;
}



} 
