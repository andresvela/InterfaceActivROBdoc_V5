package com.bdoc.interfaces.webservice.actions.implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.bdoc.interfaces.webservice.actions.Action;
import com.bdoc.interfaces.webservice.actions.IAction;
import com.bdoc.interfaces.webservice.ged.GedConnector;
import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.parametres.BdocWebProfile;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.bdoc.java.ediweb.api.JBTemplate;
import com.bdoc.java.ediweb.api.exception.BDocNetworkException;
import com.bdoc.java.ediweb.api.exception.BDocWebException;


/**
 * Construction d'une URL d'accès à bdocInteractive avec création du dossier.
 * @author DERANCOURT
 *
 */
public class ToBdocInteractive extends Action {

	private String spool;		// répertoire dans lequel sera déposé le flux
	private String url  ;       // url de BdocInteractive
	private String user ;     	//nom utilisateur Bdoc Interactive oui
	private String password ; 	//mot de passe utilisateur oui
	private String bcId ;     	//identifiant du client à afficher oui
	private String bcType ;   	//type du contexte métier oui
	private ArrayList <String> templates ; 	// nom du modèle Bdoc Design. Ce paramètre est
	private String title ;		//nom du dossier dans Bdoc Interactive oui
	private HashMap<String,String> metaInfoMap ;//méta-information (voir page 119) non	

	private String ged = null ;		    //Création du domaine 
	private String gedDomaineName  = null  ;		//domaine contenant le modèle 
	private String gedTemplateName = null  ;		//modèle de génération du flux d'indexe 

	
	private HashMap<String,String> templateFindCriteriaIndex ;//mots clés de recherche du modèle 	
	private String bdocWebProfileName;

	
	@Override
	public WebServiceRetour execute(byte[] dataFlow) {
		
		// Calcul des valeures
		String urlValue=Expression.get(url).getValue(indexValues);
		String userValue=Expression.get(user).getValue(indexValues);		
		String passwordValue=Expression.get(password).getValue(indexValues);
		String bcIdValue=Expression.get(bcId).getValue(indexValues);
		String bcTypeValue=Expression.get(bcType).getValue(indexValues);
		String titleValue=Expression.get(title).getValue(indexValues);

		boolean doGedindex = "TRUE".equals(Expression.get(ged).getValue(indexValues).trim().toUpperCase());
		
		// dépose du flux
		if (spool!=null) 
		{
			File fo = new File(spool,bcIdValue);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(fo);
				fos.write(dataFlow);
				fos.flush();
				fos.close();
		
			} catch (FileNotFoundException e) {
				logger.error(e.getLocalizedMessage());
				return new WebServiceRetour(201,e);
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
				return new WebServiceRetour(202,e);
			}
		}
		
		
		

		// Calcul des template en cas de recherche du mot clé
		ArrayList<String> tempaltevalues= new ArrayList<String>();		
		HashMap<String, String> templateFindCriteria=new HashMap<String, String>();
		if (templateFindCriteriaIndex!=null)
		{
			logger.debug("Mode mot clé  	: ");
			logger.debug("Critères de recherche du modèle  	: ");
			for (String key : templateFindCriteriaIndex.keySet()) {
				templateFindCriteria.put(key, Expression.get(templateFindCriteriaIndex.get(key)).getValue(indexValues));
				logger.debug(key + "	: "+templateFindCriteria.get(key));
			}

			try {
				BdocCompose bdocCompose = new BdocCompose(BdocWebProfile.getInstance(bdocWebProfileName));
				JBTemplate jbtemplate = bdocCompose.getTemplateByKeyWord(templateFindCriteria);
				tempaltevalues.add(jbtemplate.getName());
				
			} catch (BdocWebServiceInterfaceException e) {
				logger.error(e.getLocalizedMessage());
				return new WebServiceRetour(e);
			} catch (BDocWebException e) {
				logger.error("BDocWebException : "+e.getLocalizedMessage());
				return new WebServiceRetour(5,e);
			} catch (BDocNetworkException e) {
				logger.error("BDocNetworkException : "+e.getLocalizedMessage());
				return new WebServiceRetour(6,e);
			}

		}
		else
		{
			logger.debug("Mode classic");
			for (String template : templates){
				String templateValue = Expression.get(template).getValue(indexValues);
				if (templateValue!=null && !"".equals(templateValue)) tempaltevalues.add(templateValue);
			}
			
		}
		
		if (tempaltevalues.size()==0) return new WebServiceRetour(207);
		
		HashMap<String,String> metaInfoMapValues= new HashMap<String,String>();
		for (String metaInfo : metaInfoMap.keySet()) {
			String metainfoValue = Expression.get(metaInfoMap.get(metaInfo)).getValue(indexValues);
			if (metainfoValue!=null && !"".equals(metainfoValue)) metaInfoMapValues.put(metaInfo,metainfoValue);
		}
		// MetainfoMap n'est pas obligatoire
			
		// construction de l'url
			String result  = "";
			
			   if (urlValue!=null && !"".equals(urlValue)) {
				   result  = urlValue+"/authentication_to_bdoci_without_login_page.action?";
			   }
			   else
			   {
				   return new WebServiceRetour(203);
			   }
			   
			   if (userValue!=null && !"".equals(userValue)) {
				   result += "user="+userValue;
			   }
			   else
			   {
				   return new WebServiceRetour(204);
			   }
			   
			   
		       if (passwordValue!=null && !"".equals(passwordValue)) {
		    	   result += "&password="+passwordValue;
		       }
		       
		       if (bcIdValue!=null && !"".equals(bcIdValue)){
		    	   result += "&bcId="+bcIdValue;
		       }
			   else
			   {
				   return new WebServiceRetour(205);
			   }
		       
		       if (bcTypeValue!=null && !"".equals(bcTypeValue)){
		    	   result += "&bcType="+bcTypeValue;
		       }
			   else
			   {
				   return new WebServiceRetour(206);
			   }
		       
		       for (String template : tempaltevalues) {
		    	   result += "&docs="+template;
		       }
		       
		       if (titleValue!=null && !"".equals(titleValue))
		       {
		    	   result += "&title="+titleValue;
		       }
		       else
			   {
				   return new WebServiceRetour(208);
			   }

		       for (String metaInfo : metaInfoMapValues.keySet()) {
		    	   result += "&metaInfoMap="+metaInfo+","+metaInfoMapValues.get(metaInfo);
		       }
			
		       logger.info("URL = >"+result+"<");
		
		       WebServiceRetour retour = new WebServiceRetour();
		       retour.setStringBuffer(result);

		       if (doGedindex)
		       {
		   			String indexDomaineName = Expression.get(gedDomaineName).getValue(indexValues);
		   			String indexTemplateName = Expression.get(gedTemplateName).getValue(indexValues);
					try {
						BdocCompose bdocCompose = new BdocCompose(BdocWebProfile.getInstance(bdocWebProfileName));
						BdocPDFSecurityOption pdfSecurityOption = new BdocPDFSecurityOption();
						pdfSecurityOption.setCanAnotate(true);
						pdfSecurityOption.setCanModify(true);
						pdfSecurityOption.setCanCopy(true);
						pdfSecurityOption.setCanPrint(true);
						byte[][] document = bdocCompose.BdocComposeDocument(indexDomaineName, indexTemplateName, dataFlow, pdfSecurityOption);
						GedConnector.getInstance().getGedConnection().getNewInstance().sendDocumentToGED(indexValues, document[0], document[1]); 
					} catch (BdocWebServiceInterfaceException e) {
						return new WebServiceRetour(e);
					}
		       }
		       // http://serveur-bdoc:8080/bdoci-4.1/authentication_to_bdoci_without_login_page.action?user=achrist&bcId=1&bcType=client&docs=MODELE_SIMPLE&docs=MODELE_PAGE_A4&title=MonDossier&metaInfoMap=authorId,achrist
		return retour;

	}
	
	@Override
	public IAction getnewInstance() throws CloneNotSupportedException {
		
			return (IAction) this.clone();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBcId() {
		return bcId;
	}

	public void setBcId(String bcId) {
		this.bcId = bcId;
	}

	public String getBcType() {
		return bcType;
	}

	public void setBcType(String bcType) {
		this.bcType = bcType;
	}

	public ArrayList<String> getTemplates() {
		return templates;
	}

	public void setTemplates(ArrayList<String> templates) {
		this.templates = templates;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public HashMap<String, String> getMetaInfoMap() {
		return metaInfoMap;
	}

	public void setMetaInfoMap(HashMap<String, String> metaInfoMap) {
		this.metaInfoMap = metaInfoMap;
	}

	public String getSpool() {
		return spool;
	}

	public void setSpool(String spool) {
		this.spool = spool;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, String> getTemplateFindCriteriaIndex() {
		return templateFindCriteriaIndex;
	}

	public void setTemplateFindCriteriaIndex(
			HashMap<String, String> templateFindCriteriaIndex) {
		this.templateFindCriteriaIndex = templateFindCriteriaIndex;
	}

	public String getBdocWebProfileName() {
		return bdocWebProfileName;
	}

	public void setBdocWebProfileName(String bdocWebProfileName) {
		this.bdocWebProfileName = bdocWebProfileName;
	}

	public String getGed() {
		return ged;
	}

	public void setGed(String ged) {
		this.ged = ged;
	}

	public String getGedDomaineName() {
		return gedDomaineName;
	}

	public void setGedDomaineName(String gedDomaineName) {
		this.gedDomaineName = gedDomaineName;
	}

	public String getGedTemplateName() {
		return gedTemplateName;
	}

	public void setGedTemplateName(String gedTemplateName) {
		this.gedTemplateName = gedTemplateName;
	}


}
