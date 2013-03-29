package com.bdoc.interfaces.webservice.ged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.actions.implementations.GetDocumentInGed;
import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
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

public class GedFileSystem extends GedConnection {

	private Logger logger = Logger.getLogger(this.getClass());
	
	private String rootGedSpool;
	private String fileNameIndex;
	private String Doc_Relative_Path;
	private String Idx_Relative_Path;
	private String saveIndexIfExists;
	private String Idx_Extention;
	
	// Attention, dans cette methode on ne recupere que le document
	private File getGedFile (String path, String filename, boolean doCreatePath) throws BdocWebServiceInterfaceException 
	{

		if (path == null || "".equals(path))
		{
			throw new BdocWebServiceInterfaceException(301,"Répertoire de GED indéterminé");
		}

		if (filename == null || "".equals(filename))
		{
			throw new BdocWebServiceInterfaceException(301,"nom du fichier indéterminé");
		}

		//if (filename.length()==1) filename="0"+filename;
		//File directory = new File (path,filename.toUpperCase().substring(filename.length()-2));
		File directory = new File (path);

		File file = null;
		
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
			
			file = new File(directory,filename);

		return file;

	}
	
		
	@Override
	public void sendDocumentToGED(IndexValues indexValues, byte[] fileContent, byte [] indexcontent) throws BdocWebServiceInterfaceException {

		String path =  Expression.get(rootGedSpool).getValue(indexValues);
		String filename =  Expression.get(fileNameIndex).getValue(indexValues);
		String indexFilename = filename+Expression.get(getIdx_Extention()).getValue(indexValues);
		
		String docPath = Expression.get(Doc_Relative_Path).getValue(indexValues);
		String idxPath = Expression.get(Idx_Relative_Path).getValue(indexValues);
		
		if (docPath!=null && !"".equals(docPath) && !docPath.startsWith("/")) docPath = "/".concat(docPath);
		if (idxPath!=null && !"".equals(idxPath) && !idxPath.startsWith("/")) idxPath = "/".concat(idxPath);
		
		boolean saveIndex = "TRUE".equals(Expression.get(getSaveIndexIfExists()).getValue(indexValues).trim().toUpperCase());
		
		// enregistrement du document
		if (fileContent != null) 
		try
		{
			
			File file = getGedFile(path+docPath,filename,true);
			logger.debug("Sauvegarde document : "+file.getAbsolutePath());
			FileOutputStream fo = new FileOutputStream(file);
			fo.write(fileContent);
			fo.flush();
			fo.close();
		}
		catch (IOException e) {
			String msg=("Erreur d'enregistrement du docucment répertoire "+path+docPath+"- fichier="+filename+" : "+e);
			logger.error(msg);
			throw new BdocWebServiceInterfaceException(404,msg);
		
		}

		// enregistrement du fichier d'indexe
		if (saveIndex && indexcontent!=null)
		{
			try
			{
				File file = getGedFile(path+idxPath,indexFilename,true);
				logger.debug("Sauvegarde Index : "+file.getAbsolutePath());
				FileOutputStream fo = new FileOutputStream(file);
				fo.write(indexcontent);
				fo.flush();
				fo.close();
			}
			catch (IOException e) {
				String msg=("Erreur d'enregistrement du docucment répertoire "+path+idxPath+"- fichier="+filename+" : "+e);
				logger.error(msg);
				throw new BdocWebServiceInterfaceException(404,msg);	
			}			
		}
	}


	
	public String getRootGedSpool() {
		return rootGedSpool;
	}

	public void setRootGedSpool(String rootGedSpool) {
		this.rootGedSpool = rootGedSpool;
	}

	public String getFileNameIndex() {
		return fileNameIndex;
	}

	public void setFileNameIndex(String fileNameIndex) {
		this.fileNameIndex = fileNameIndex;
	}
	
	

	@Override
	public IGedConnection getNewInstance() throws BdocWebServiceInterfaceException 
	{
		try {
			return (GedFileSystem) this.clone();
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


	public String getDoc_Relative_Path() {
		return Doc_Relative_Path;
	}


	public void setDoc_Relative_Path(String doc_Relative_Path) {
		Doc_Relative_Path = doc_Relative_Path;
	}


	public String getIdx_Relative_Path() {
		return Idx_Relative_Path;
	}


	public void setIdx_Relative_Path(String idx_Relative_Path) {
		Idx_Relative_Path = idx_Relative_Path;
	}


	public String getSaveIndexIfExists() {
		return saveIndexIfExists;
	}


	public void setSaveIndexIfExists(String saveIndexIfExists) {
		this.saveIndexIfExists = saveIndexIfExists;
	}


	public String getIdx_Extention() {
		return Idx_Extention;
	}


	public void setIdx_Extention(String idx_Extention) {
		Idx_Extention = idx_Extention;
	}


	@Override
	public byte[] getDocumentFromGED(HashMap<String, String> index)	throws BdocWebServiceInterfaceException {

		IndexValues indexValues = new IndexValues();
		indexValues.put(index);
		
		String rootPath = Expression.get(rootGedSpool).getValue(indexValues);
		String fileName = Expression.get(fileNameIndex).getValue(indexValues);

		String doc_Path = Expression.get(Doc_Relative_Path).getValue(indexValues);
		//String idx_Path = Expression.get(Idx_Relative_Path).getValue(indexValues);
		//String indexIfExists;
		String index_Extention = Expression.get(Doc_Relative_Path).getValue(indexValues);

		File dir ;
		if (doc_Path!=null && !"".equals(doc_Path)) dir = new File (rootPath,doc_Path); else dir = new File (rootPath) ;
		
		File gedFile = getGedFile(dir.getAbsolutePath(), fileName, false);
		byte[] b = new byte[(int) gedFile.length()];
		try {
			FileInputStream fis = new FileInputStream(gedFile);
			fis.read(b);
			fis.close();
		} catch (FileNotFoundException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(302,e);
			logger.error(ex.getMessage());
			throw ex;
		} catch (IOException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(302,e);
			logger.error(ex.getMessage());
			throw ex;
		}
		
		
		return b;
	}


}
