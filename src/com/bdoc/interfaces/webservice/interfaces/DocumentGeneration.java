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
	 * @param Liste ordonnée des paramètres au format Cle=valeur. Dans le cas où il n'y aurait pas de clé, une clé IDXx est creee
	 * @return WebServiceRetour
	 */
	WebServiceRetour getDocumentMultiIndex(String[] indexList);
	
}
