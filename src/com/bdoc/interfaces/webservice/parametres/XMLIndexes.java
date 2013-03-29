package com.bdoc.interfaces.webservice.parametres;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.actions.ParserDataFlow;
import com.bdoc.interfaces.webservice.actions.parseXMLdataflow;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.bdoc.interfaces.webservice.utils.IndexValues;


public class XMLIndexes {

	private Logger logger = Logger.getLogger(this.getClass());
	private static XMLIndexes instance = null;
		
	/**
	 * Liste des défintions des indexes à parser
	 */
	private HashMap<String, String> indexesDefinition;
	private HashMap<String,String> computedIndexes;
	private HashMap<String,String> ConstantIndexes;
	
	
	public XMLIndexes () 
	{
		instance = this;
	}

	
	public static XMLIndexes getinstance ()
	{
		return instance;
	}
	
	public static XMLIndexes getnewinstance () throws CloneNotSupportedException
	{
		return (XMLIndexes) instance.clone();
	}

	
	/**
	 * 
	 */
	public void initialize ()
	{
		instance=this;
		logger.info("Chargement de la définition des indexes \n"+toString());
		//testExpression();
	}
	
	
	/**
	 * fourni une liste de défintion d'indexes
	 * @return
	 */
	public HashMap<String, String> getIndexesDefinition() {
		return indexesDefinition;
	}


	/**
	 * Ajoute une liste de défintion d'indexes
	 * @return
	 */

	public void setIndexesDefinition(HashMap<String, String> indexesDefinition) {
		this.indexesDefinition = indexesDefinition;
	}


	public String toString()
	{
		String result = "Index Parsés: \n";
		for (Entry<String, String> entry : indexesDefinition.entrySet()) {
			String name = entry.getKey();
			String path = entry.getValue();
			result += name + " : " + path + "\n";
		}
		result += "\nConstantes : \n";
		for (Entry<String, String> entry : ConstantIndexes.entrySet()) {
			String name = entry.getKey();
			String path = entry.getValue();
			result += name + " : " + path + "\n";
		}
		return result;	
	}
	
	public ParserDataFlow getparser()
	{
		return new parseXMLdataflow();
	}


	public HashMap<String, String> getComputedIndexes() {
		return computedIndexes;
	}


	public void setComputedIndexes(HashMap<String, String> computedIndexes) {
		this.computedIndexes = computedIndexes;
	}


	public HashMap<String, String> getConstantIndexes() {
		return ConstantIndexes;
	}


	public void setConstantIndexes(HashMap<String, String> constantIndexes) {
		ConstantIndexes = constantIndexes;
	}
	

	private void testExpression ()
	{

	}
	
	
}
