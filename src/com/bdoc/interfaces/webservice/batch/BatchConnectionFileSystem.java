package com.bdoc.interfaces.webservice.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;

public class BatchConnectionFileSystem extends BatchConnection {

	private Logger logger = Logger.getLogger(this.getClass());
	private String directory;
	private String filename;
	private String extention;
	private static volatile Integer iteration = new Integer(0);
	
	
	String getUniqueFileName()
	{	int i; 
		synchronized (iteration) {
			
			i = iteration++;
		}
		
		String timeHex = Long.toHexString(Calendar.getInstance().getTimeInMillis())+Long.toHexString(i%16);
		if (timeHex.length()>8) timeHex=timeHex.substring(timeHex.length()-8);
		timeHex.toUpperCase();
		logger.debug(timeHex+" - "+i);
		return timeHex;
	}
	
	@Override
	public WebServiceRetour execute(byte[] fileContent) throws BdocWebServiceInterfaceException {
		String outputFilename = "";
		WebServiceRetour retour = new WebServiceRetour(); 
		
		if (filename==null || "".equals(filename)) outputFilename = getUniqueFileName(); 
		
		outputFilename += extention;
		
		try {
			File fo = new File(directory,outputFilename);
			FileOutputStream fos;
			fos = new FileOutputStream(fo);
			fos.write(fileContent);
			fos.flush();
			fos.close();
			retour.setStringBuffer(outputFilename);
		} catch (FileNotFoundException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(501, e);
			logger.error(ex.libelle());
			throw ex;
		} catch (IOException e) {
			BdocWebServiceInterfaceException ex = new BdocWebServiceInterfaceException(502, e);
			logger.error(ex.libelle());
			throw ex;
		}
		
		return retour;
	}


	public String getDirectory() {
		return directory;
	}


	public void setDirectory(String directory) {
		this.directory = directory;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public String getExtention() {
		return extention;
	}


	public void setExtention(String extention) {
		this.extention = extention;
	}

	@Override
	public IBatchConnection getnewInstance() throws CloneNotSupportedException {
		return (IBatchConnection) this.clone();		
	}

}
