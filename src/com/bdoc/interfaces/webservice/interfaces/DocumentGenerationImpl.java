package com.bdoc.interfaces.webservice.interfaces;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.actions.IAction;
import com.bdoc.interfaces.webservice.actions.ParserDataFlow;
import com.bdoc.interfaces.webservice.actions.parseXMLdataflow;
import com.bdoc.interfaces.webservice.actions.implementations.BdocCompose;
import com.bdoc.interfaces.webservice.batch.BatchConnector;
import com.bdoc.interfaces.webservice.batch.IBatchConnection;
import com.bdoc.interfaces.webservice.ged.GedConnection;
import com.bdoc.interfaces.webservice.ged.GedConnector;
import com.bdoc.interfaces.webservice.parametres.ActionList;
import com.bdoc.interfaces.webservice.parametres.BdocWebProfile;
import com.bdoc.interfaces.webservice.servlets.TableauDeBord;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;


/**
 * Classe d'expostion des fonctions du WebService
 * @author DERANCOURT
 */
public class DocumentGenerationImpl implements DocumentGeneration {
	
	private Logger logger = Logger.getLogger(this.getClass());
					
	/**
	 * Constructeur.
	 */
	public DocumentGenerationImpl()
	{
	}
	
	/**
	 * Retourne le document généré via BdocWeb en fonction des informations contenues dans le flux.
	 */
	@Override
	/**
	 * génération du document en fonction des indexes paramétrés
	 */
	public WebServiceRetour generate(byte[] xmlFlow) {
		
		logger.debug("Start Generation");
		long start = Calendar.getInstance().getTimeInMillis();
		
		
		// Recherche des indexes dans le flux XML entrant
		HashMap<String, String> docIndex = null;
		try 
		{
			logger.debug("Start Parsing dataflow");
			//TableauDeBord.getInstance().addLog("Start Parsing dataflow");			
			ParserDataFlow parserDataFlow = new parseXMLdataflow();
			docIndex = parserDataFlow.parseDataFlow(xmlFlow);
			logger.debug("End Parsing dataflow");
			//TableauDeBord.getInstance().addLog("End Parsing dataflow");	
		} 
		catch (BdocWebServiceInterfaceException e)
		{
			logger.error (e.getLocalizedMessage());
			return new WebServiceRetour(2,e);
		}
	
		// TODO ajouter le parsing des erreurs		
		List<IAction> actionList =  ActionList.getInstance().getActionList(); 
		
		try {
		
			logger.debug("Start looking for action");
			for (IAction actiondef : actionList) {
				IAction action = actiondef.getnewInstance();
				logger.debug("Action : " + action.getClass().getName());
				//TableauDeBord.getInstance().addLog("Action : " + action.getClass().getName());	
				if (action.selectAction(docIndex)) {
					logger.debug("Action founded : "+action.getClass().getName());
					//TableauDeBord.getInstance().addLog("Action founded : "+action.getClass().getName());	
					WebServiceRetour serviceRetour = action.execute(xmlFlow);
					logger.debug("End action");
					//TableauDeBord.getInstance().addLog("End action");	
					TableauDeBord.getInstance().addOnefunction(action.getName(), Calendar.getInstance().getTimeInMillis() - start);
					return serviceRetour;
				}
			}

		} catch (CloneNotSupportedException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(0,e);
			logger.error(ex.getLocalizedMessage());
			return new WebServiceRetour(ex);		
		}catch (BdocWebServiceInterfaceException e) {
			logger.error(e.libelle());
			return new WebServiceRetour(e);		
		}	
	return new WebServiceRetour(1);
	}

	/**
	 * Retourn la liste des modèles de document sous forme de fichier XML
	 */
	@Override
	public WebServiceRetour getRessourcesList(String bdocWebProfileName) {
		try {
			
			//logger.debug("Enter in getRessourcesList");
			long start = Calendar.getInstance().getTimeInMillis();
			BdocCompose bdocCompose = new BdocCompose(BdocWebProfile.getInstance(bdocWebProfileName));		
			WebServiceRetour retour = new WebServiceRetour();
			retour.setByteBuffer(bdocCompose.getTemplateListAsXML().getBytes("UTF-8"));
			TableauDeBord.getInstance().addOnefunction("getRessourceList", Calendar.getInstance().getTimeInMillis() - start);
			return retour;
			
		} catch (BdocWebServiceInterfaceException e) {
			logger.error(e.getMessage());					
			return new WebServiceRetour(6,e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());					
			return new WebServiceRetour(6,e);
		}
	}
	
	/**
	 * génération du document en mode batch : Pas de parsing du flux XML 
	 */
	@Override
	public WebServiceRetour generateBatch(byte[] xmlFlow) {
		logger.debug("démarrage de generateBatch" );
		try {			
			BatchConnector connector  = BatchConnector.getInstance();
			IBatchConnection batch =  connector.getBatchConnection().getnewInstance();
			logger.debug("fin de generateBatch" );
			return batch.execute(xmlFlow);
		} catch (CloneNotSupportedException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(0,e);
			logger.error(ex.getLocalizedMessage());
			return new WebServiceRetour(ex);		
		} catch (BdocWebServiceInterfaceException e) {
			logger.error(e.libelle());
			return new WebServiceRetour(e);		
		}	
	}
	
	/**
	 * récupération du document en fonction d'un seul indexes paramétré
	 */
	@Override
	public WebServiceRetour getDocument(String numeroEdition) {

		GedConnector gedConnector = GedConnector.getInstance();
		WebServiceRetour retour = new WebServiceRetour();
		try {
			//GedConnector connector = GedConnector.getInstance();
			//GedConnection gedConnexion = (GedConnection) connector.getGedConnection().getNewInstance();
			HashMap<String, String> index = new HashMap<String, String>();
			index.put("numeroEdition", numeroEdition);
			//gedConnexion.getDocumentFromGED(index);
			retour.setByteBuffer(gedConnector.getGedConnection().getNewInstance().getDocumentFromGED(index));
		} catch (BdocWebServiceInterfaceException e) {
			return new WebServiceRetour(e);
		} catch (Exception e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(405,e);
			logger.error(ex.getLocalizedMessage());
			return new WebServiceRetour(ex);		
		}
		return retour;		
	}

	
	/**
	 * récupération du document en fonction d'un seul indexes paramétré
	 */
	@Override
	public WebServiceRetour getDocumentMultiIndex (String[] indexList) {

		HashMap<String, String> idxList= new HashMap<String, String>();
		for (int i = 0 ; i<indexList.length; i++) 
		{
			StringTokenizer s = new StringTokenizer(indexList[i],"=");
			if (s.countTokens()==2)
			{
				String key=s.nextToken();
				String value=s.nextToken();
				logger.debug(key+"="+value);
				idxList.put(key, value);
			}
			else
			{
				String key="IDX".concat(new Integer(i).toString());
				String value=s.nextToken();
				idxList.put(key, indexList[i]);				
				logger.debug(key+"="+value);
			}
		}
		
		
		WebServiceRetour retour = new WebServiceRetour();
		try {
			GedConnection gedConnection = GedConnector.getInstance().getGedConnection(idxList);
			//GedConnector connector = GedConnector.getInstance();
			//GedConnection gedConnexion = (GedConnection) connector.getGedConnection().getNewInstance();
			//gedConnexion.getDocumentFromGED(indexList);
			retour.setByteBuffer(gedConnection.getNewInstance().getDocumentFromGED(idxList));
		} catch (BdocWebServiceInterfaceException e) {
			return new WebServiceRetour(e);
		} catch (Exception e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(405,e);
			logger.error(ex.getLocalizedMessage());
			return new WebServiceRetour(ex);		
		}
		return retour;		
	}
	
}
