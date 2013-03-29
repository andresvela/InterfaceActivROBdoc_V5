package com.bdoc.interfaces.webservice.actions;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.bdoc.interfaces.webservice.parametres.XMLIndexes;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.bdoc.interfaces.webservice.utils.IndexValues;


public class parseXMLdataflow implements ParserDataFlow{

private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Parse le flux xml et valorise les indexes.
	 * @param xmlFlow
	 * @param indexesDefinition (HashMap : Index | Xpath)
	 * @return (HashMap : Index | valeur)
	 * @throws DocumentException 
	 */
	@Override
	public HashMap<String, String> parseDataFlow (byte[] xmlFlow) throws BdocWebServiceInterfaceException 
	{

		logger.debug("Démarrage du parsing XML : ");
		HashMap<String, String> indexesDefinition = XMLIndexes.getinstance().getIndexesDefinition();
		HashMap<String, String> constantDefinition = XMLIndexes.getinstance().getConstantIndexes();
		HashMap<String, String> computedDefinition = XMLIndexes.getinstance().getComputedIndexes();

		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(xmlFlow);
		HashMap<String, String> result = new HashMap<String, String>();
		
		SAXReader reader = new SAXReader();
		reader.setIgnoreComments(true);
		reader.setMergeAdjacentText(true);
		reader.setStripWhitespaceText(true);

		Element root;
		try {
			root = reader.read(arrayInputStream).getRootElement();
		} catch (DocumentException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(100,e);
			logger.error(ex.libelle());
			throw ex;
		}

		// Ajout des constantes 
		result.putAll(constantDefinition);
		
		logger.debug("Index Constant");
		
		for (Entry<String, String> entry : constantDefinition.entrySet()) {
			logger.debug("lecture  : " + entry.getKey()+ " : " + entry.getValue());
		}

		logger.debug("Index parsés");
			
			for (Entry<String, String> entry : indexesDefinition.entrySet()) {
			String name = entry.getKey();
			String path = entry.getValue();
			String value = getValues(root.selectNodes(path));
			result.put(name, value);
			logger.debug("lecture  : " + name + " : " + path + " = " + value);
		}		

		
		logger.debug("Index Calculés");

		IndexValues indexValues = new IndexValues();
		indexValues.put(result);
		
		for (Entry<String, String> entry : computedDefinition.entrySet()) {
			String name = entry.getKey();
			String path = entry.getValue();
			String value = (String) Expression.get(path).getValue(indexValues);
			result.put(name, value);
			indexValues.put(result);
			logger.debug("lecture  : " + name + " : " + path + " = " + value);
		}

		return result;

	}

	private String getValues(List<?> list){
		// prise en compte des éléments itératifs
		String value="";
		for (Iterator<?> iter = list.iterator(); iter.hasNext(); ) {
			if (value.length()>0) {
				break;
			}
			Object o = iter.next();
			if (o instanceof Attribute) {
				value += ((Attribute) o).getValue();
			}
			else if (o instanceof Node) {
				value += ((Node) o).getText();
			}
        }
		return value;
	}

	
	
	
}
