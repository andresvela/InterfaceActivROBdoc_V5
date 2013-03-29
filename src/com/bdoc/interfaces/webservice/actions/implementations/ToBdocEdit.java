package com.bdoc.interfaces.webservice.actions.implementations;

import com.bdoc.interfaces.webservice.actions.Action;
import com.bdoc.interfaces.webservice.actions.IAction;
import com.bdoc.interfaces.webservice.interfaces.WebServiceRetour;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.interfaces.webservice.utils.Expression;
import com.bdoc.interfaces.webservice.utils.IndexValues;
import com.bdoc.interfaces.webservice.utils.WebServiceErrorCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.log4j.Logger;

public class ToBdocEdit extends Action {

	
	private String bdocEditInputdirectory=null;
	private String outputFilename=null;
		
	@Override
	public WebServiceRetour execute(byte[] xmlflow) {
		logger.debug ("Action");
		String filename = Expression.get(outputFilename).getValue(indexValues);
		File outputFile = new File (bdocEditInputdirectory,filename);
		logger.debug("fichier de sortie : "+outputFile);
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(outputFile,false);
			fout.write(xmlflow);
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			logger.error("Erreur 101 : "+BdocWebServiceInterfaceException.getErrorLibelle(101));
			return new WebServiceRetour(101, e);
		} catch (IOException e) {
			logger.error("Erreur 101 : "+BdocWebServiceInterfaceException.getErrorLibelle(101));
			return new WebServiceRetour(101, e);
		}
		return new WebServiceRetour(0, "done");
	}

	public String getBdocEditInputdirectory() {
		return bdocEditInputdirectory;
	}

	public void setBdocEditInputdirectory(String bdocEditInputdirectory) {
		this.bdocEditInputdirectory = bdocEditInputdirectory;
	}

	public String getOutputFilename() {
		return outputFilename;
	}

	public void setOutputFilename(String outputFilename) {
		this.outputFilename = outputFilename;
	}

	@Override
	public IAction getnewInstance() throws CloneNotSupportedException {
	
			return (IAction) this.clone();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}


}
