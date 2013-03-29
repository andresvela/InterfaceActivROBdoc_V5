package com.bdoc.interfaces.webservice.interfaces;

import java.util.HashMap;

public interface DocumentGeneration {


	/**
	 * 
	 * @param xmlFlow
	 * @return WebServiceRetour
	 */
	public WebServiceRetour generate (byte[] xmlFlow);
	
	/**
	 * 
	 * @param xmlFlow
	 * @return WebServiceRetour
	 */
	public WebServiceRetour generateBatch (byte[] xmlFlow);

	
	/**
	 * @param numeroEdition
	 * @return WebServiceRetour
	 */
	public WebServiceRetour getDocument (String numeroEdition);
	
	
	/**
	 * fournis la list des ressources 
	 * @return WebServiceRetour
	 */
	public WebServiceRetour getRessourcesList (String bdocWebProfileName);

	
	/**
	 * @param Liste ordonn�e des param�tres au format Cle=valeur. Dans le cas o� il n'y aurait pas de cl�, une cl� IDXx est creee
	 * @return WebServiceRetour
	 */
	WebServiceRetour getDocumentMultiIndex(String[] indexList);
	
}
