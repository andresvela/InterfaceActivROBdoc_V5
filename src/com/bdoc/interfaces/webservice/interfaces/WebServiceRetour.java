package com.bdoc.interfaces.webservice.interfaces;

import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;

/**
 * 
 * Classe retour de génération 
 * 
 * returnType = 0 : aucun retour
 * returnType = 1 : byteBuffer
 * returnType = 2 : StringBuffer
 * 
 * errorCode = 0 : Pas d'anomalies
 * errorCode > 0 : Code Erreur + Message d'erreur
 * 
 * @author DERANCOURT
 *
 */

public class WebServiceRetour {

	public final static int RETOUR_BUFFER = 1;
	public final static int RETOUR_STRING = 2;
	public final static int RETOUR_VIDE = 0;
	

	private int returnType=RETOUR_VIDE;
	private byte[] byteBuffer=null;
	private String stringBuffer=null;

	private int errorCode=0;
	private String errorMessage=null;

	
	public WebServiceRetour()
	{
		
	}
	
	public WebServiceRetour(int code, String message)
	{
		this.errorCode = code;		
		setErrorMessage(message);
	}

	public WebServiceRetour(int code)
	{
		setErrorCode(code);		
		setErrorMessage(BdocWebServiceInterfaceException.getErrorLibelle(code));
	}

	public WebServiceRetour(int code, Exception e)
	{
		setErrorCode(code);		
		setErrorMessage(BdocWebServiceInterfaceException.getErrorLibelle(code)+" : "+e.getLocalizedMessage());
	}

	public WebServiceRetour(int code, Throwable e)
	{
		setErrorCode(code);		
		setErrorMessage(BdocWebServiceInterfaceException.getErrorLibelle(code)+" : "+e.getLocalizedMessage());
	}
	
	public WebServiceRetour(BdocWebServiceInterfaceException e)
	{
		setErrorCode(e.getCode());		
		setErrorMessage(e.getMessage());
	}
	
	public byte[] getByteBuffer() {
		return byteBuffer;
	}
	
	public void setByteBuffer(byte[] byteBuffer) {
		returnType = RETOUR_BUFFER;
		this.byteBuffer = byteBuffer;
	}
	
	public String getStringBuffer() {
		return stringBuffer;
	}
	
	public void setStringBuffer(String stringBuffer) {
		returnType = RETOUR_STRING;
		this.stringBuffer = stringBuffer;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getReturnType() {
		return returnType;
	}
	
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}