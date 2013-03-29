package com.bdoc.interfaces.webservice.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class WebServiceErrorCode {

//	public final static HashMap<Integer,String> errorCodes = new HashMap();
//	public static final String defaultErrorMessage = "Erreur non r�f�renc�e";
//	
//	
//	static 
//	{ 
//		errorCodes.put(-1,  "M�thode non impl�ment�e");
//		errorCodes.put(1,   "Aucune action param�tr�e pour les indexes s�lectionn�e");
//		errorCodes.put(2,   "Erreur lors de la recherche d'un indexe dans le flux XML");
//		errorCodes.put(3,   "Erreur getDocument");
//		errorCodes.put(4,   "Erreur de connexion type R�seau � BdocWeb");
//		errorCodes.put(5,   "Erreur BdocWeb");
//		errorCodes.put(6,   "Erreur de connexion � BdocWeb");
//		errorCodes.put(101, "Erreur de l'action ToBdocEdit");
//		errorCodes.put(102, "Erreur de l'action ToBdocWeb");
//		errorCodes.put(201, "Erreur de l'action ToBdocInteractrive lors de la d�pose du flux ");
//		errorCodes.put(202, "Erreur de l'action ToBdocInteractrive lors de la d�pose du flux ");
//		errorCodes.put(203, "URL de BdocInteractive non valoris�");
//		errorCodes.put(204, "Pas de user BdocInteractive non valoris�");
//		errorCodes.put(205, "bcId non valoris�");
//		errorCodes.put(206, "bcType non valoris�");
//		errorCodes.put(207, "aucun mod�le de document valoris�");
//		errorCodes.put(208, "titre du dossier non valoris�");
//		
//		// Erreur lors de la r�cup�ration d'un document
//		errorCodes.put(301, "Erreur de l'action getDocument");
//	}
//	
//	
//	
//	
//	/**
//	 * Retourne un libell� d'erreur en fonction du code
//	 * @param code
//	 * @return
//	 */
//	public static String getErrorMessage(int code)
//	{
//		String message = errorCodes.get(code);
//		if (message==null)
//		{
//				return defaultErrorMessage;
//		}
//		else
//		{
//			 	return message;
//		}
//	}
//
//	/**
//	 * Retourne un libell� d'erreur en fonction du code
//	 * @param code
//	 * @return
//	 */
//	
//	public static String getErrorMessage(int code,Exception e)
//	{
//		String message = errorCodes.get(code);
//		if (message==null)
//		{
//				return defaultErrorMessage + " : "+e.getLocalizedMessage();
//		}
//		else
//		{
//			 	return message + " : "+e.getLocalizedMessage();
//		}
//	}
//
//	
//	/**
//	 * Retourne un libell� d'erreur en fonction du code
//	 * @param code
//	 * @return
//	 */
//	
//	public static String getErrorMessage(int code,Throwable e)
//	{
//		String message = errorCodes.get(code);
//		if (message==null)
//		{
//				return defaultErrorMessage + " : "+e.getLocalizedMessage();
//		}
//		else
//		{
//			 	return message + " : "+e.getLocalizedMessage();
//		}
//	}	
//	
//}
//
//class MyError 
//{
//	
//	String error;
//	String date;
//	
//	public static LinkedList<MyError> lastErrors = new LinkedList<MyError>();
//
//	public MyError (String error)
//	{
//		date = Calendar.getInstance().toString();
//		this.error = error;
//		lastErrors.add(this);
//	}
//	
//	public static void addError(String error)
//	{
//		lastErrors.add(new MyError(error));
//	}
//	
//	public static String getLastErrorsAsJSON ()
//	{
//		String result = "[";
//		int i=0;
//		for (MyError error : lastErrors) {
//			result+="{lastErrors:{\"date\":\""+error.date+"\", \"error\":\""+error.error+"\"}";			
//			i++;
//			if (i<lastErrors.size()) result+=",";
//		}
//		result+="]";
//		return result;
//		
//	}
//	
//	
	
}

