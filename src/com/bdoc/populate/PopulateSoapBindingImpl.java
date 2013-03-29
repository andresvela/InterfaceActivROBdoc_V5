

package com.bdoc.populate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.bdoc.interfaces.webservice.actions.implementations.ToBdocInteractive;
import com.bdoc.populate.webservice.Populate;

public class PopulateSoapBindingImpl implements Populate{
	private Logger logger = Logger.getLogger(this.getClass());

	protected String path="";
	protected boolean useResourceName = false;

	private static Logger log = Logger.getLogger(PopulateSoapBindingImpl.class);

		
	public PopulateSoapBindingImpl() {
		super();
		ResourceBundle props = ResourceBundle.getBundle("com.bdoc.populate.config", LocaleContextHolder.getLocale());
		try{
			String useResource = props.getString("populateDataService.external.xml.useTemplateName");
			useResourceName = Boolean.parseBoolean(useResource);			
		}catch (MissingResourceException e) {
		}
		try{
			String filePath = props.getString("populateDataService.external.xml.filepath");
			path = filePath;			
		}catch (MissingResourceException e) {
		}
	}


	public byte[] generate(byte[] xmlIn) throws java.rmi.RemoteException {

		String path= ((ToBdocInteractive)ToBdocInteractive.instance).getSpool();
		logger.debug ("spool = "+path);
		
		logger.debug("=========== Entrée dans le WS 'populate' avec le flux de donnée : length = "+xmlIn.length);
		
		byte[] returnedFlow = null;
		String bcId ="";
		Document doc = null;
	
		// Parsing du flux xlm entrant pour récupérer le bcid et le bctype
		try {
			//Parse Input as XML
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(new String(xmlIn)));
			is.setSystemId("http:\\\\localhost\\");
			doc = builder.parse(is);

			//Retrieve BCID from XML
			Node idNode = doc.getElementsByTagName("BCID").item(0);
			if(idNode != null){
				bcId = idNode.getFirstChild().getTextContent();
			}else{
				log.warn("unfound tag element BCID");
			}
			
		} catch (SAXException e) {
			log.error("Error parsing input : " + e.getMessage());
			return xmlIn;
		} catch (IOException e) {
			log.error("Error parsing input : " + e.getMessage());
			return xmlIn;
		} catch (ParserConfigurationException e) {
			log.error("Error parsing input : " + e.getMessage());
			return xmlIn;
		}
		
		// Récupération du fichier XML stocké dans "path" ayant le nom "bcid"

			File f = new File(path,bcId);
			logger.debug("============ Ouverture du fichier "+f.getAbsolutePath());
			FileInputStream fis=null;
			try {
				System.out.println();
				fis = new FileInputStream(f);
				returnedFlow = new byte[(int)f.length()];
				fis.read(returnedFlow);
				fis.close();
				
			} catch (FileNotFoundException e1) {
				logger.error("could not read file : " + f.getAbsolutePath()+ " - "+e1.getMessage());
				e1.printStackTrace();
				throw new java.rmi.RemoteException ("could not read file : " + f.getAbsolutePath()+ " - "+e1.getMessage());
			} catch (IOException e) {
				logger.error("Erreur I/O : " + f.getAbsolutePath()+ " - " + e.getMessage());
				throw new java.rmi.RemoteException ("Erreur I/O : " + f.getAbsolutePath()+ " - " + e.getMessage());
			}
			
			logger.info("Sortie du WS Populate : Taille Flux = "+returnedFlow.length);
//			logger.debug("xmlout= "+new String (returnedFlow));
			return returnedFlow;
	}
}
