package com.bdoc.interfaces.webservice.actions.implementations;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;


import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.parametres.BdocWebProfile;
import com.bdoc.interfaces.webservice.servlets.TableauDeBord;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.java.ediweb.api.exception.*;
import com.bdoc.java.ediweb.api.BDocDocument;
import com.bdoc.java.ediweb.api.GenerationOptions;
import com.bdoc.java.ediweb.api.JBGeneration;
import com.bdoc.java.ediweb.api.JBApplication;
import com.bdoc.java.ediweb.api.JBDomain;
import com.bdoc.java.ediweb.api.JBKeyword;
import com.bdoc.java.ediweb.api.JBKeywordManager;
import com.bdoc.java.ediweb.api.JBTemplate;


import com.bdoc.java.ediweb.api.PdfAssemblyOptions;


public class BdocCompose {
	
private java.text.NumberFormat nf = java.text.NumberFormat.getInstance(Locale.FRENCH);
private Logger logger = Logger.getLogger(BdocCompose.class.getName());

private BdocWebProfile profile;
private String ressourcesAsXML = null;
private String ressourcesAsJSON = null;
private BdocWebProfile bdocWebProfile = null;
private static int StaticSessionNumber=0;
private String SessionName = "BWS_".concat(Integer.toString(StaticSessionNumber++));





public BdocCompose(BdocWebProfile profile)
{
	this.profile = profile;
}


/**
 * Composition d'un document 
 * @param domainName
 * @param templateName
 * @param versionName
 * @param dataContent
 * @param bdocPDFSecurityOption
 * @return
 * @throws BDocWebException 
 * @throws BDocNetworkException 
 * @throws BDocResourceException 
 * @throws BdocWebServiceInterfaceException
 */




private byte[][] BdocComposeDocument(JBTemplate jbTemplate, byte[] dataContent, BdocPDFSecurityOption bdocPDFSecurityOption,JBApplication bdocWebAppli) throws BDocResourceException, BDocNetworkException, BDocWebException, BdocWebServiceInterfaceException
{
	byte[] ewResult = null;
	byte[] ewIndex = null;
	byte[][] result = new byte[2][];
	
	long datestart = Calendar.getInstance().getTimeInMillis();

	JBGeneration ewGeneration = bdocWebAppli.setTemplateToGenerate(SessionName,jbTemplate);
	
	GenerationOptions genOption = new GenerationOptions();
	logger.debug("Entrée dans la génération des options d'assemblage");
	
	// Utilisation du moteur "BDOC-PDF"
	genOption.setProductionFormat(GenerationOptions.FORMAT_PDF);
	
	PdfAssemblyOptions pdfOptions = new PdfAssemblyOptions();
	pdfOptions.setCanAnnotate(bdocPDFSecurityOption.isCanAnotate());
	pdfOptions.setCanCopy(bdocPDFSecurityOption.isCanCopy());
	pdfOptions.setCanModify(bdocPDFSecurityOption.isCanModify());
	pdfOptions.setCanPrint(bdocPDFSecurityOption.isCanPrint());
	genOption.setPdfoptions(pdfOptions);
	logger.debug("Flag de sécurité BdocPDF : "+bdocPDFSecurityOption.toString());
	
	ewGeneration.setOptions(genOption);
	ewGeneration.setDataStream(dataContent);
	
	if (jbTemplate.isInteractive()) throw new BdocWebServiceInterfaceException(105,"Le modèle "+jbTemplate.getName()+" : "+jbTemplate.getDescription()+" est interactif et ne peut être traité");
 
	logger.debug("démarrage de l'assemblage");
	BDocDocument bd = ewGeneration.generateToBDocDocument();

	if (bd==null) throw new BdocWebServiceInterfaceException(107);
	
	ewResult = bd.getDocument();
	if (ewResult==null) throw new BdocWebServiceInterfaceException(108);
	logger.debug("Assemblage Ok" );
	
	// Les index moteur ne sont plus dispo en 5.0 ==> on attend la 5.1
	//ewIndex = bd.getIndex();
	//if (ewIndex==null) logger.debug("index KO"); else logger.debug("index OK : "+ewIndex.length+" octets");

	result [0] = ewResult;
	result [1] = ewIndex;
	
	logger.info("fin composition du modèle ["+profile.getName()+"]: "+jbTemplate.getDescription()+" en " +nf.format(System.currentTimeMillis()-datestart)+ " ms, Taille (o): "+ewResult.length);
	if (ewResult.length==0) throw new BdocWebServiceInterfaceException(109);

	TableauDeBord.getInstance().addOneTemplate(jbTemplate.getName(), Calendar.getInstance().getTimeInMillis()-datestart);

	return result;
}


private JBApplication getbdocwebAppli () throws BdocWebServiceInterfaceException
{
	if (profile==null) {//Fournis par un constructeur
		if (bdocWebProfile==null){ // fourni par Spring
			throw new BdocWebServiceInterfaceException(7);
		}
		else
		{
			// mise à jour du profile avec le profile de spring
			profile = bdocWebProfile;
		}
	}
	logger.debug("Connexion à BdocWeb ["+profile.getName()+"]: serveur "+profile.getHostname()+":"+ profile.getPort());
	return BdocWebConnection.getInstance().getJBApplication(profile.getHostname(), profile.getPort());
	
}


public byte[][] BdocComposeDocument (String domainName,String templateName, byte[] dataContent, BdocPDFSecurityOption bdocPDFSecurityOption) throws BdocWebServiceInterfaceException 
{

		logger.info("Assemblage du modèle :"+domainName+"."+templateName);
	

		byte[][] result;
		
		JBApplication bdocWebAppli = getbdocwebAppli();
	
		try {

			JBDomain domain;
			domain = (JBDomain) bdocWebAppli.getDomain(domainName);
			if (domain==null) throw new BdocWebServiceInterfaceException(103,"le domain "+domainName+" n'est pas reconnu par BdocWeb");
		
			JBTemplate jbTemplate;
			jbTemplate= (JBTemplate) domain.getTemplateByLogicalName(templateName);
			if (jbTemplate==null) throw new BdocWebServiceInterfaceException(104,"le modèle "+templateName +" n'est pas reconnu par BdocWeb");
					
			result = BdocComposeDocument(jbTemplate,dataContent,bdocPDFSecurityOption,bdocWebAppli);
			
		
		} catch (BDocWebException e) {
			throw new BdocWebServiceInterfaceException(5,e.getLocalizedMessage());
		} catch (BDocNetworkException e) {
			throw new BdocWebServiceInterfaceException(4,e.getLocalizedMessage());
		} catch (BDocResourceException e) {
			throw new BdocWebServiceInterfaceException(103,e.getLocalizedMessage());
		}

		
		return result;

}


public byte[][] BdocComposeDocument (HashMap<String, String> keywords, byte[] dataContent, BdocPDFSecurityOption bdocPDFSecurityOption) throws BdocWebServiceInterfaceException, BDocWebException, BDocNetworkException 
{

		JBTemplate template = getTemplateByKeyWord(keywords);
	
		logger.info("Assemblage du modèle :"+template.getParentDomain().getName()+"."+template.getName());
	
		byte[][] result = null;

		JBApplication bdocWebAppli = getbdocwebAppli();
	
		try {
					
			result = BdocComposeDocument(template,dataContent,bdocPDFSecurityOption,bdocWebAppli);
			
			
		} catch (BDocWebException e) {
			throw new BdocWebServiceInterfaceException(5,e.getLocalizedMessage());
		} catch (BDocNetworkException e) {
			throw new BdocWebServiceInterfaceException(4,e.getLocalizedMessage());
		} catch (BDocResourceException e) {
			throw new BdocWebServiceInterfaceException(103,e.getLocalizedMessage());
		}

		
		return result;

}





/**
 * Fourni la liste des domaine, thème et modeles et versions de modèle sous la forme d'un arbre XML
 * @return
 * @throws BDocWebException 
 * @throws BDocNetworkException 
 * @throws BDocConnectionException 
 * @throws BdocWebServiceInterfaceException 
 * @throws Throwable
 */
public String getTemplateListAsXML() throws  BdocWebServiceInterfaceException {
	
	logger.debug("récupération de la liste des modèles comme XML");
		
	JBApplication bdocWebAppli= profile.getBdocWebApplication();
	logger.debug("profile.getBdocWebApplication() OK"); 
	Collection<JBDomain> domains = bdocWebAppli.getDomains();
	logger.debug("bdocWebAppli.getDomains() OK");
	
	ressourcesAsXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<root bdocWebProfile=\""+profile.getName()+"\">";
	
	//try {
		for (JBDomain domain : domains ) {
			//Collection<JBTopic> topics = domain.getTopicsCollection();
			
			ressourcesAsXML+="<domain name=\""+domain.getName()+"\" description=\""+domain.getDescription()+"\">";
			//for (JBTopic topic : topics) {
				//ressourcesAsXML+="<topic name=\""+topic.getName()+"\" description=\""+topic.getDescription()+"\">";
			    ressourcesAsXML+="<topic name=\"default\" description=\"default domain\">";
				Collection<JBTemplate> templates = domain.getTemplates();
				for (JBTemplate template : templates) {
					ressourcesAsXML+="<template name=\""+template.getName()+"\" description=\""+template.getDescription()+"\">";
					//Collection <JBVersionTemplate> versionTemplates = template.getVersionsCollection();
					// récupération des mots clés
					Collection <JBKeyword> keywordList = template.getKeywords();
					for (JBKeyword keyword : keywordList) {
						ressourcesAsXML+="<keyWords name=\""+keyword.getName()+"\">";
						Collection <String> valueCollection = keyword.getValues();
						for (String value : valueCollection) {
							ressourcesAsXML+="<value>"+value+"</value>";
						}
						ressourcesAsXML+="</keyWords>";						
					}
					// récupération des versions
					//for (JBVersionTemplate versionTemplate : versionTemplates) {
					//	ressourcesAsXML+="<version name=\""+versionTemplate.getName()+"\" description=\""+versionTemplate.getDescription()+"\">";
					//	ressourcesAsXML+="</version>";
					//}
					ressourcesAsXML+="</template>";
				}
				ressourcesAsXML+="</topic>";	
			//}//fin des topics (mais en v5 il n'y en a plus qu'un seul.
			ressourcesAsXML+="</domain>";	
		}
//	} catch (BDocNetworkException e) {
//		BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(4,e);
//		logger.error(e.getMessage());
//		throw ex;
//	} catch (BDocWebException e) {
//		BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(5,e);
//		logger.error(e.getMessage());
//		throw ex;
//	}	 	
	ressourcesAsXML += "</root>";
	
	ressourcesAsXML = ressourcesAsXML.replaceAll("\"null\"", "\"\"");
	
	logger.debug(ressourcesAsXML);
	return ressourcesAsXML;
}


/**
 * Fournis la liste des modèles de documents au format JSON pour EXT-JS
 * @param forceReinit
 * @return
 * @throws BdocWebServiceInterfaceException
 */
public String getTemplateListAsJSON(String name) throws BdocWebServiceInterfaceException {
	
		logger.debug("récupération de la liste des modèles");
	
		BdocWebProfile profile = BdocWebProfile.getInstance(name);
		if (profile==null) throw new BdocWebServiceInterfaceException(7);
	
		JBApplication bdocWebAppli= profile.getBdocWebApplication();
		Collection<JBDomain> domains = bdocWebAppli.getDomains();
	
		ressourcesAsJSON = "";
	
		for (JBDomain domain : domains ) {
			//Collection<JBTopic> topics = domain.getTopicsCollection();
			ressourcesAsJSON += "{text: '"+domain.getName()+"', id: '"+domain.getName()+"', iconCls: 'folder', leaf: false";
			ressourcesAsJSON += ", children: [";
			//for (JBTopic topic : topics) {
				ressourcesAsJSON += "{text: 'default', id: 'default', iconCls: 'folder', leaf: false";
				ressourcesAsJSON += ", children: [";
				Collection<JBTemplate> templates = domain.getTemplates();
				for (JBTemplate template : templates) {
					ressourcesAsJSON += "{text: '"+template.getName()+"', id: '"+template.getName()+"', iconCls: 'folder', leaf: false";
					ressourcesAsJSON += ", children: [";
//					
//					Collection <> versionTemplates = template.getVersionsCollection();
//					for (JBVersionTemplate versionTemplate : versionTemplates) {
//						ressourcesAsJSON += "{text: '"+versionTemplate.getName()+"', id: '"+versionTemplate.getName()+"', iconCls: 'folder', leaf: false";
//					}
					ressourcesAsJSON+="]}";
				}
				ressourcesAsJSON+="]}";	
			//}
			//ressourcesAsJSON+="]}";	
		}	 	
		ressourcesAsJSON += "}";
	
		ressourcesAsJSON = ressourcesAsJSON.replaceAll("\"null\"", "\"\"");

		return ressourcesAsJSON;
}





public JBTemplate getTemplateByKeyWord (HashMap<String, String> keywords) throws BdocWebServiceInterfaceException, BDocWebException, BDocNetworkException
{
			
	long datestart = Calendar.getInstance().getTimeInMillis();

	logger.info("Recherche du modèle");

	if (profile==null) {//Fournis par un constructeur
		if (bdocWebProfile==null){ // fourni par Spring
			throw new BdocWebServiceInterfaceException(7);
		}
		else
		{
			// mise à jour du profile avec le profile de spring
			profile = bdocWebProfile;
		}		
	}

		
	JBApplication bdocWebAppli;
	logger.debug("Connexion à BdocWeb ["+profile.getName()+"]: serveur "+profile.getHostname()+":"+ profile.getPort());
	bdocWebAppli = BdocWebConnection.getInstance().getJBApplication(profile.getHostname(), profile.getPort());
	JBKeywordManager keywordManager = bdocWebAppli.getKeywordManager();
    String criteria = "=\n";
    int i=0;
    for (String key : keywords.keySet()) {
		criteria+=key+"\n"+keywords.get(key);
		i++;
		if (i<keywords.size()) criteria+="\n"+profile.getKeyWordSerchOperator()+"\n";
	}
    
    logger.debug("Critere de recherche = \n"+criteria);
    

	Collection<JBTemplate> templates = keywordManager.findKeywordBDocResourcesCollection(criteria.getBytes());
	
    
	logger.info(String.format("Fin de Recherche du modèle par mot clé : %d ms", datestart-Calendar.getInstance().getTimeInMillis()));
	
    // Génération d'un erreur (111, 112) si il n'y a pas un et un seul modèle en retour
    if (templates.isEmpty()) {
    	throw new BdocWebServiceInterfaceException(111);
    }
    else if (templates.size()>1) {
		logger.error("Liste des modèles retrouvé avec les critères"+criteria);
    	for (JBTemplate template : templates) {
			logger.error(template.getParentDomain().getName()+"."+template.getName()+" : "+template.getDescription());
		}
//    	// Je vérifie qu'on à bien un doublon.
//    	String nomPhy=null;
//    	for (JBTemplate jbTemplate : templates) 
//    	{
//    		if (nomPhy==null) nomPhy=jbTemplate.getPhysicalName();
//    		if (nomPhy!=jbTemplate.getPhysicalName()) throw new BdocWebServiceInterfaceException(112);
//    	}
//    	for (JBTemplate jbTemplate : templates) return jbTemplate;
    }
    else for (JBTemplate jbTemplate : templates) return jbTemplate;
    return null;
    
  }   
}
