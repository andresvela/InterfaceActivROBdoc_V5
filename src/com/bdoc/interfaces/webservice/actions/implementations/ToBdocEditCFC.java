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

import javax.imageio.stream.FileImageInputStream;


import org.apache.log4j.Logger;

public class ToBdocEditCFC extends Action {

	
	private String bdocEditInputdirectory=null;
	private String outputFilename=null;
	private String repInName=null;
	private String templateName=null;
	private String channelName=null;



	private String flowName=null;
		
	@Override
	public WebServiceRetour execute(byte[] xmlflow) {
		logger.debug ("Action");
		//String filename = flowName!=null ? Expression.get(flowName).getValue(indexValues):"";
		String filename = Expression.get(outputFilename).getValue(indexValues);
		String repInValue = Expression.get(repInName).getValue(indexValues);
		String templateValue = templateName!=null ? Expression.get(templateName).getValue(indexValues):"";
		String channelValue = channelName!=null ? Expression.get(channelName).getValue(indexValues):"";
		//File outputFile = new File (bdocEditInputdirectory,filename);
		
		
		try {
			
		byte[] b = null;
		File inputFile = new File (filename);
		FileImageInputStream fis = null;		
		fis = new FileImageInputStream(inputFile);
		
		b = new byte[(int) inputFile.length() ];
		fis.read(b);		
		String outputFile = inputFile.getName();
		
		
		logger.debug("fichier de sortie : "+ outputFile);
		FileOutputStream fout;
	
			String bdocEditFileOut=null;
			if (channelValue.equals("Batch")){
				bdocEditFileOut = repInValue + outputFile + "." + templateValue + ".BTC";;
			}
			if (channelValue.equals("Email")){
				bdocEditFileOut = repInValue + outputFile + "." + templateValue + ".MEL";
			}
			
			fout = new FileOutputStream(bdocEditFileOut,false);
			fout.write(b);
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
	
	public String getFlowName() {
		return flowName;
	}



	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getRepInName() {
		return repInName;
	}

	public void setRepInName(String repInFilename) {
		this.repInName = repInFilename;
	}
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}


}
