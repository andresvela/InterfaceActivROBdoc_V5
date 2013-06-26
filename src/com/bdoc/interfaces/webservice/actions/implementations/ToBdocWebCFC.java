package com.bdoc.interfaces.webservice.actions.implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import javax.imageio.stream.FileImageInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.bdoc.interfaces.webservice.actions.Action;
import com.bdoc.interfaces.webservice.actions.IAction;
import com.bdoc.interfaces.webservice.ged.GedConnection;
import com.bdoc.interfaces.webservice.ged.GedConnector;
import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.parametres.BdocWebProfile;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.bdoc.specific.*;

public class ToBdocWebCFC extends Action {
	
	private String domainName;
	private String templateName;
	private String flowName;
	private String repOutName;
	private String bdocWebProfileName;

	private String canPrint; 
	private String canCopy;
	private String canAnotate;
	private String canModify;
	
	private String doGEDIndex="FALSE";
	private String doPrintIndex="FALSE";
	private String doManualPrintIndex="FALSE";
	private String doCloseDocumentIndex="FALSE";
	private String doPrevisuIndex="FALSE";
	private String exemplaireIndex = "1";
	private String saveFile = "FALSE";
	private String savePath = "";
	
	private HashMap<String, String> templateFindCriteriaIndex=null;
	
	
	private String encryptionOwnerPassWord = String.valueOf(Calendar.getInstance().getTimeInMillis());
	private String encryptionUserPassWord = null;
	
	@Override
	/**
	 * Fonction d'execution du traitement de composition + Insertion des commande d'impression
	 */
	public WebServiceRetour execute(byte[] xmlflow) throws BdocWebServiceInterfaceException{

		WebServiceRetour retour = new WebServiceRetour(); 
		try {  
			
			String domainValue = domainName!=null ? Expression.get(domainName).getValue(indexValues):"";
			String templateValue = templateName!=null ? Expression.get(templateName).getValue(indexValues):"";
			String flowValue = flowName!=null ? Expression.get(flowName).getValue(indexValues):"";
			String repOutValue = repOutName!=null ? Expression.get(repOutName).getValue(indexValues):"";
			
			
			boolean doGED =  "TRUE".equals(Expression.get(doGEDIndex).getValue(indexValues));
			boolean doPrint = "TRUE".equals(Expression.get(doPrintIndex).getValue(indexValues));
			boolean doManualPrint= "TRUE".equals(Expression.get(doManualPrintIndex).getValue(indexValues));
			boolean doCloseDocument= "TRUE".equals(Expression.get(doCloseDocumentIndex).getValue(indexValues));
			boolean doPrevisu = "TRUE".equals(Expression.get(doPrevisuIndex).getValue(indexValues));

			String stencryptionOwnerPassWord = Expression.get(encryptionOwnerPassWord).getValue(indexValues);
			String stencryptionUserPassWord = Expression.get(encryptionUserPassWord).getValue(indexValues);

			boolean doSaveFile = "TRUE".equals(Expression.get(saveFile).getValue(indexValues));
			String stSavePath = Expression.get(savePath).getValue(indexValues);
		
			HashMap<String, String> templateFindCriteria=new HashMap<String, String>();
			if (templateFindCriteriaIndex!=null)
			{
				logger.debug("Critères de recherche du modèle  	: "+stSavePath);
				for (String key : templateFindCriteriaIndex.keySet()) {
					templateFindCriteria.put(key, Expression.get(templateFindCriteriaIndex.get(key)).getValue(indexValues));
					logger.debug(key + "	: "+templateFindCriteria.get(key));
				}
			}

			
			logger.debug("execution GED : "+doGEDIndex+" : "+doGED);
			logger.debug("execution Print : "+doPrintIndex+" : "+doPrint);
			logger.debug("execution Print Manuel : "+doManualPrintIndex+" : "+doManualPrint);
			logger.debug("execution Fermeture Document : "+doCloseDocumentIndex+" : "+doCloseDocument);
			logger.debug("execution Prévisualisation : "+doPrevisuIndex+" : "+doPrevisu);
			logger.debug("execution ownerPasswd : "+stencryptionOwnerPassWord);
			logger.debug("execution userPasswd  : "+stencryptionUserPassWord);
			logger.debug("execution saveFile 	: "+doSaveFile);
			logger.debug("execution savePath  	: "+stSavePath);
			
			int exemplaire = 1;
			try
			{
				exemplaire = Integer.valueOf(Expression.get(exemplaireIndex).getValue(indexValues));
			}
			catch (NumberFormatException e) {
				logger.warn("Nombre de copies mal formé : "+exemplaireIndex+"="+Expression.get(exemplaireIndex).getValue(indexValues)+" : "+e.getLocalizedMessage());	
			}
			
			BdocWebProfile bdocWebProfile = BdocWebProfile.getInstance(bdocWebProfileName);
			if (bdocWebProfile==null) 
			{
				throw new BdocWebServiceInterfaceException(102,"Erreur la connexion BdocWeb ["+bdocWebProfileName+"] n'est pas paramétrée");
			}
			BdocCompose bdocCompose = new BdocCompose(bdocWebProfile);
							
			// on autorise tout par défaut
			BdocPDFSecurityOption pdfSecurityOption = new BdocPDFSecurityOption();
			pdfSecurityOption.setCanAnotate(!"FALSE".equals(Expression.get(canAnotate).getValue(indexValues)));
			pdfSecurityOption.setCanModify(!"FALSE".equals(Expression.get(canModify).getValue(indexValues)));
			pdfSecurityOption.setCanCopy(!"FALSE".equals(Expression.get(canCopy).getValue(indexValues)));
			pdfSecurityOption.setCanPrint(!"FALSE".equals(Expression.get(canPrint).getValue(indexValues)));
			pdfSecurityOption.setOwnerPasswd(stencryptionOwnerPassWord);
			pdfSecurityOption.setUserPasswsd(stencryptionUserPassWord);
			logger.debug("ged Owner Pass : "+stencryptionOwnerPassWord);
			logger.debug("ged User Pass  : "+stencryptionUserPassWord);
			
			byte[] documentContent = null;
			byte[][] document = null;
			byte[] indexcontent = null;				
			byte[] b = null;
			
					
			//XMLLize file if templateName contains "_XML"
			if (templateValue.contains("_XML")){
				XmlizerFlatFile xmlCassiopeParser = new XmlizerFlatFile(flowValue);
				xmlCassiopeParser.processLineByLine();
				b = xmlCassiopeParser.writeXMLStream();				
			}
			
			//Passes the flat file Non XMLized
			else
			{
				File F = new File(flowValue);
				FileImageInputStream fis = new FileImageInputStream(F);
				b = new byte[(int) F.length() ];
				fis.read(b);
				
			}
			
			if (templateFindCriteriaIndex==null)
			{
				document = bdocCompose.BdocComposeDocument(domainValue, templateValue, b,pdfSecurityOption);
			}
			else
			{
				document = bdocCompose.BdocComposeDocument(templateFindCriteria, b,pdfSecurityOption);				
			}
			
			documentContent = document[0];
			indexcontent = document[1];
			
			if (documentContent==null) return new WebServiceRetour(102,"Erreur lors de l'assemblage du document, le buffer retour est null");

			// copie du fichier de données dans un répertoire.
			if (doSaveFile){
				logger.debug("sauvegarde du fichier de données : "+stSavePath);				
				//logger.debug("sauvegarde du fichier de données : "+repOutValue);
				
				FileOutputStream fileoutput = new FileOutputStream(new File (stSavePath));	
				//FileOutputStream fileoutput = new FileOutputStream(new File (repOutValue));
				//fileoutput.write(xmlflow);
				fileoutput.write(documentContent);
				
				fileoutput.flush();
				fileoutput.close();
			}			
			
			// Archive du document
			if (doGED) {
				GedConnector gedConnector = GedConnector.getInstance();
				GedConnection gedConnection = gedConnector.getGedConnection(indexValues);
				if (gedConnection!=null)
				{
					gedConnection.sendDocumentToGED(indexValues, documentContent, indexcontent);
					logger.info("Dépose du document sur le connecteur GED sélectionné : "+gedConnection.getName()+ ": "+ gedConnection.getFilter());
				}
				else
				{
					GedConnector.getInstance().getGedConnection().getNewInstance().sendDocumentToGED(indexValues, documentContent, indexcontent);
					logger.info("Dépose du document sur le connecteur GED par défaut: ");
				}
			}
			
			// Ajout des commandes d'impression
			if (doPrint || doManualPrint)
			{
				documentContent = InsertPrintingCommand(doManualPrint,doCloseDocument, documentContent,pdfSecurityOption, exemplaire, stencryptionOwnerPassWord, stencryptionUserPassWord);
			}

			logger.debug("Previsualisation du document = "+doPrevisu);
			
			// On force le retour du document si Print ou ManualPrint est sélectionné
			if (doPrevisu || doPrint || doManualPrint)
			{
				retour.setByteBuffer(documentContent);
			}
			else
			{
				retour.setErrorCode(0);
			}

		
		} catch (FileNotFoundException e){
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(102,e);
			logger.error(ex.libelle(e));
			return new WebServiceRetour(ex);
		} catch (IOException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(110,e);
			logger.error(e.getMessage());
			return new WebServiceRetour(ex); 
		} catch (DocumentException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(110,e);
			logger.error(e.getMessage());
			return new WebServiceRetour(ex);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return retour;
	}

	
	
	private String JavaScriptCalculation (boolean manual,boolean doCloseDocument, int copies)
	{
		String manualjs	=	manual ? "full" : "automatic";
		String closejs = doCloseDocument ? "this.closeDoc(true);":"";
		
		String js = "var pp = this.getPrintParams();pp.interactive = pp.constants.interactionLevel."+manualjs+";";
		if (copies > 1) {
			js+="pp.NumCopies = "+copies+";";
		}
		js+="this.print(pp);";
		js+=closejs;
		logger.debug("Javascript ajouté : "+js);
		return js;
	}
	
	
	/**
	 * Ajoute les commande d'impression JavaScript dans le document PDF avec propagation des options de sécurité
	 * @param manual : Impression manuelles
	 * @param doCloseDocument : Fermeture automatique du document après impression
	 * @param documentContent : tableau de byte contenant le document PDF
	 * @param pdfSecurityOption : Options de sécurité du PDF
	 * @param copies : Nombre de copies à imprimer
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	private byte[] InsertPrintingCommand (boolean manual, boolean doCloseDocument, byte[] documentContent,BdocPDFSecurityOption pdfSecurityOption, int copies, String ownerPassWd, String userPasswd) throws IOException, DocumentException 
	{
		PdfReader reader = null;
//		if (ownerPassWd==null || ownerPassWd=="")
//		{
//			reader = new PdfReader(documentContent);
//			
//		}
//		else
//		{
//			reader = new PdfReader(documentContent,ownerPassWd.getBytes());
//		}

		reader = new PdfReader(documentContent);
		
		
		ByteOutputStream outputPDF = new ByteOutputStream();
		PdfStamper stamper = new PdfStamper(reader, outputPDF);		
		
		
		if (pdfSecurityOption.isActivate())
		{
			 int SecurityInt = 	(pdfSecurityOption.isCanAnotate()	? PdfWriter.ALLOW_MODIFY_ANNOTATIONS  : 0 )
			 				   +(pdfSecurityOption.isCanModify()  	? PdfWriter.ALLOW_MODIFY_CONTENTS	  :	0 )
			 				   +(pdfSecurityOption.isCanPrint() 	? PdfWriter.ALLOW_PRINTING  		  :	0 )
			 				   +(pdfSecurityOption.isCanCopy() 		? PdfWriter.ALLOW_COPY  			  :	0 );
			
			 
			 byte[] btUserPassWd = (userPasswd == null || userPasswd==null ? null : userPasswd.getBytes());
			 byte[] btOwnerPassWd = (ownerPassWd == null ? null : ownerPassWd.getBytes());
			 
			 logger.debug("return Owner Pass : "+btOwnerPassWd);
			 logger.debug("return User Pass  : "+btUserPassWd);
			 logger.debug("PDF Security value : "+SecurityInt);
			 //if (!pdfSecurityOption.isCanAnotate()) 
				 stamper.setEncryption(null, btOwnerPassWd ,SecurityInt,PdfWriter.ENCRYPTION_AES_128);
		}
		
		stamper.addJavaScript(JavaScriptCalculation(manual,doCloseDocument,copies));
		
		stamper.close();
		return outputPDF.getBytes();
	}
	
	
	
	
	
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public void setCanPrint(String canPrint) {
		this.canPrint = canPrint;
	}

	public void setCanCopy(String canCopy) {
		this.canCopy = canCopy;
	}

	public void setCanAnotate(String canAnotate) {
		this.canAnotate = canAnotate;
	}

	public void setCanModify(String canModify) {
		this.canModify = canModify;
	}

	
	@Override
	public IAction getnewInstance() throws CloneNotSupportedException {
		
		return (IAction) this.clone();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}



	public String getDoGEDIndex() {
		return doGEDIndex;
	}



	public void setDoGEDIndex(String doGEDIndex) {
		this.doGEDIndex = doGEDIndex;
	}



	public String getDoPrintIndex() {
		return doPrintIndex;
	}



	public void setDoPrintIndex(String doPrintIndex) {
		this.doPrintIndex = doPrintIndex;
	}



	public String getDoManualPrintIndex() {
		return doManualPrintIndex;
	}



	public void setDoManualPrintIndex(String doManualPrintIndex) {
		this.doManualPrintIndex = doManualPrintIndex;
	}



	public String getDoPrevisuIndex() {
		return doPrevisuIndex;
	}



	public void setDoPrevisuIndex(String doPrevisuIndex) {
		this.doPrevisuIndex = doPrevisuIndex;
	}



	public String getBdocWebProfileName() {
		return bdocWebProfileName;
	}



	public void setBdocWebProfileName(String bdocWebProfileName) {
		this.bdocWebProfileName = bdocWebProfileName;
	}



	public String getEncryptionOwnerPassWord() {
		return encryptionOwnerPassWord;
	}



	public void setEncryptionOwnerPassWord(String encryptionOwnerPassWord) {
		this.encryptionOwnerPassWord = encryptionOwnerPassWord;
	}



	public String getEncryptionUserPassWord() {
		return encryptionUserPassWord;
	}



	public void setEncryptionUserPassWord(String encryptionUserPassWord) {
		this.encryptionUserPassWord = encryptionUserPassWord;
	}



	public String getSaveFile() {
		return saveFile;
	}



	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}



	public String getSavePath() {
		return savePath;
	}



	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}



	public String getExemplaireIndex() {
		return exemplaireIndex;
	}



	public void setExemplaireIndex(String exemplaireIndex) {
		this.exemplaireIndex = exemplaireIndex;
	}



	public String getDoCloseDocumentIndex() {
		return doCloseDocumentIndex;
	}



	public void setDoCloseDocumentIndex(String doCloseDocumentIndex) {
		this.doCloseDocumentIndex = doCloseDocumentIndex;
	}



	public HashMap<String, String> getTemplateFindCriteriaIndex() {
		return templateFindCriteriaIndex;
	}



	public void setTemplateFindCriteriaIndex(
			HashMap<String, String> templateFindCriteriaIndex) {
		this.templateFindCriteriaIndex = templateFindCriteriaIndex;
	}



	public String getFlowName() {
		return flowName;
	}



	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}



	public String getRepOutName() {
		return repOutName;
	}



	public void setRepOutName(String repOutName) {
		this.repOutName = repOutName;
	}


}
