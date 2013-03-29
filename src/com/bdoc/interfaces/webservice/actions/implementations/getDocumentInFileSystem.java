package com.bdoc.interfaces.webservice.actions.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.ws.WebServiceException;

import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.WebServiceErrorCode;
import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;

public class getDocumentInFileSystem implements GetDocumentInGed {

	
	private String directory;

	
	
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) throws FileNotFoundException, BdocWebServiceInterfaceException {
		this.directory = directory;
		File d = new File (directory);
		if (!d.exists()) throw new BdocWebServiceInterfaceException(401,"Le répertoire "+d.getAbsolutePath()+" est introuvable");
		if (!d.isDirectory()) throw new BdocWebServiceInterfaceException(402,d.getAbsolutePath()+" n'est pas un répertoire");
		if (!d.canWrite()) throw new BdocWebServiceInterfaceException(403,d.getAbsolutePath()+" n'est pas accessible en écriture");
		if (!d.canRead()) throw new BdocWebServiceInterfaceException(404,d.getAbsolutePath()+" n'est pas accessible en lecture");
	}

	
	private File calculeFileAdress (String uniqueIndex) throws BdocWebServiceInterfaceException 
	{
		File f = new File(directory, uniqueIndex+".PDF");
		if (!f.exists()) throw new BdocWebServiceInterfaceException(401,"Le fichier "+f.getAbsolutePath()+" est introuvable");
		if (!f.isFile()) throw new BdocWebServiceInterfaceException(402,f.getAbsolutePath()+" n'est pas un fichier");
		if (!f.canWrite()) throw new BdocWebServiceInterfaceException(403,f.getAbsolutePath()+" n'est pas accessible en écriture");
		if (!f.canRead()) throw new BdocWebServiceInterfaceException(404,f.getAbsolutePath()+" n'est pas accessible en lecture");
		
		return f;
	}
	
	@Override
	public WebServiceRetour execute(String uniqueIndex) throws BdocWebServiceInterfaceException {
		
		// Calcul et vérification que le document est présent et accessible
		File document = null;
		
		document = calculeFileAdress (uniqueIndex);
		
		
		WebServiceRetour result = null;
		
		// Récupération du document et retour
		try 
		{
			FileInputStream documentStream = new FileInputStream(document);
			byte[] documentContent = new byte[(int) document.length()];
			documentStream.read(documentContent);
			result = new WebServiceRetour();
			result.setByteBuffer(documentContent);
			
		} catch (FileNotFoundException e) {
			throw new BdocWebServiceInterfaceException(3,e);
		} catch (IOException e) {
			throw new BdocWebServiceInterfaceException(3,e);
		}
		
		return result;
	}


}
