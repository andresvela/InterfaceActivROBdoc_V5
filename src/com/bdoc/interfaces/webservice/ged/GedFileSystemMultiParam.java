package com.bdoc.interfaces.webservice.ged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.bdoc.interfaces.webservice.utils.IndexValues;

/**
 * 
 * Classe permettant de retourner un document en fonction de son index.  
 * 
 * @author DERANCOURT
 *
 */

public class GedFileSystemMultiParam extends GedConnection {

	private Logger logger = Logger.getLogger(this.getClass());
	
	private String storeDocPath="c:/temp/ged2.pdf";
	private String storeIdxPath="c:/temp/ged2.idx";

	private String getDocPath="c:/temp/ged2.pdf";
	private String getIdxPath="c:/temp/ged2.idx";
	
	private String createPath;
	private String saveIndexIfExists;
	
	
	// Attention, dans cette methode on ne recupere que le document
	private File getGedFile (String pathIndex, HashMap<String, String> index, boolean doCreatePath) throws BdocWebServiceInterfaceException 
	{
		IndexValues gedIndex = new IndexValues();
		gedIndex.put(index);		
		return getGedFile(pathIndex, gedIndex, doCreatePath); 
	}
	
	private File getGedFile (String pathIndex, IndexValues gedIndex, boolean doCreatePath) throws BdocWebServiceInterfaceException 
	{

		String docPath = Expression.get(pathIndex).getValue(gedIndex);
		logger.debug("path index  : "+pathIndex);
		logger.info("récupération du fichier : "+docPath);

		// Traitement du fichier d'index
		if (docPath == null || "".equals(docPath))
		{
			throw new BdocWebServiceInterfaceException(301,"Répertoire de GED indéterminé");
		}

		File file = new File (docPath);
		
		File directory = file.getParentFile();
		
		if (!directory.exists())
			{
				if (doCreatePath) directory.mkdirs();
			}
			else
			{
				if (directory.isFile()) throw new BdocWebServiceInterfaceException(301,"Le répertoire cible "+directory.getAbsolutePath()+" existe déjà en tant que fichier");
				if (!directory.canWrite()) throw new BdocWebServiceInterfaceException(301,directory.getAbsolutePath()+" accès en écriture refusée");
				if (!directory.canRead()) throw new BdocWebServiceInterfaceException(301,directory.getAbsolutePath()+" accès en lecture refusée");
			}
			
		return file;

	}
	
		
	@Override
	public void sendDocumentToGED(IndexValues indexValues, byte[] fileContent, byte [] indexcontent) throws BdocWebServiceInterfaceException {

		// L'idée c'est de calculer un chemin de fichier d'index et un chemin de fichier d'index
		
		//String path =  Expression.get(rootGedSpool).getValue(indexValues);
		String filename =  Expression.get(storeDocPath).getValue(indexValues);
		String indexFilename = Expression.get(storeIdxPath).getValue(indexValues);
		boolean saveIndex = "TRUE".equals(Expression.get(getSaveIndexIfExists()).getValue(indexValues).trim().toUpperCase());

		if (filename!=null && !"".equals(filename))
		{
			//TODO Traitement de la sauvegarde du documenten GED
			if (fileContent != null) 
				try
				{
					File file = new File(filename);
					logger.debug("Sauvegarde document : "+file.getAbsolutePath());
					FileOutputStream fo = new FileOutputStream(file);
					fo.write(fileContent);
					fo.flush();
					fo.close();
				}
				catch (IOException e) {
					String msg=("Erreur d'enregistrement du docucment : "+filename+" : "+e);
					logger.error(msg);
					throw new BdocWebServiceInterfaceException(404,msg);
				}
		}
		
		
		if (indexFilename!=null && !"".equals(indexFilename))
		{
			if (saveIndex && indexcontent!=null)
			{
				try
				{
					File file = new File(indexFilename);
					logger.debug("Sauvegarde Index : "+file.getAbsolutePath());
					FileOutputStream fo = new FileOutputStream(file);
					fo.write(indexcontent);
					fo.flush();
					fo.close();
				}
				catch (IOException e) {
					String msg=("Erreur d'enregistrement du docucment : "+indexFilename+" : "+e);
					logger.error(msg);
					throw new BdocWebServiceInterfaceException(404,msg);	
				}			
			}
		}
		
	}

	@Override
	public byte[] getDocumentFromGED(HashMap<String, String> index) throws BdocWebServiceInterfaceException  {
	
		IndexValues gedIndex = new IndexValues();
		gedIndex.put(index);
		//Boolean doCreatePath = Expression.get(createPath).getBoolean(gedIndex);
		File file = getGedFile(getDocPath,gedIndex,false);
		
		byte[] filecontent = new byte[(int)file.length()];
		
		try
		{
			FileInputStream fi = new FileInputStream(file);
			fi.read(filecontent);
			fi.close();
		}
		catch (IOException e) {
			String msg=("Erreur de lecture du docucment "+file.getAbsolutePath()+" : "+e);
			logger.error(msg);
			throw new BdocWebServiceInterfaceException(405,msg);
		}

		return filecontent;
	
	}


		

	@Override
	public IGedConnection getNewInstance() throws BdocWebServiceInterfaceException 
	{
		try {
			return (GedFileSystemMultiParam) this.clone();
		} catch (CloneNotSupportedException e) {
			logger.error(e.getLocalizedMessage());
			throw new BdocWebServiceInterfaceException(8,e.getLocalizedMessage());
		}
		
		
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}


	public String getSaveIndexIfExists() {
		return saveIndexIfExists;
	}


	public void setSaveIndexIfExists(String saveIndexIfExists) {
		this.saveIndexIfExists = saveIndexIfExists;
	}



	public String getCreatePath() {
		return createPath;
	}

	public void setCreatePath(String createPath) {
		this.createPath = createPath;
	}

	public String getStoreDocPath() {
		return storeDocPath;
	}

	public void setStoreDocPath(String storeDocPath) {
		this.storeDocPath = storeDocPath;
	}

	public String getStoreIdxPath() {
		return storeIdxPath;
	}

	public void setStoreIdxPath(String storeIdxPath) {
		this.storeIdxPath = storeIdxPath;
	}

	public String getGetDocPath() {
		return getDocPath;
	}

	public void setGetDocPath(String getDocPath) {
		this.getDocPath = getDocPath;
	}

	public String getGetIdxPath() {
		return getIdxPath;
	}

	public void setGetIdxPath(String getIdxPath) {
		this.getIdxPath = getIdxPath;
	}


}
