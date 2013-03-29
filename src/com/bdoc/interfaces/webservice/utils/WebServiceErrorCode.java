package com.bdoc.interfaces.webservice.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class WebServiceErrorCode {

//	public final static HashMap<Integer,String> errorCodes = new HashMap();
//	public static final String defaultErrorMessage = "Erreur non référencée";
//	
//	
//	static 
//	{ 
//		errorCodes.put(-1,  "Méthode non implémentée");
//		errorCodes.put(1,   "Aucune action paramétrée pour les indexes sélectionnée");
//		errorCodes.put(2,   "Erreur lors de la recherche d'un indexe dans le flux XML");
//		errorCodes.put(3,   "Erreur getDocument");
//		errorCodes.put(4,   "Erreur de connexion type Réseau à BdocWeb");
//		errorCodes.put(5,   "Erreur BdocWeb");
//		errorCodes.put(6,   "Erreur de connexion à BdocWeb");
//		errorCodes.put(101, "Erreur de l'action ToBdocEdit");
//		errorCodes.put(102, "Erreur de l'action ToBdocWeb");
//		errorCodes.put(201, "Erreur de l'action ToBdocInteractrive lors de la dépose du flux ");
//		errorCodes.put(202, "Erreur de l'action ToBdocInteractrive lors de la dépose du flux ");
//		errorCodes.put(203, "URL de BdocInteractive non valorisé");
//		errorCodes.put(204, "Pas de user BdocInteractive non valorisé");
//		errorCodes.put(205, "bcId non valorisé");
//		errorCodes.put(206, "bcType non valorisé");
//		errorCodes.put(207, "aucun modèle de document valorisé");
//		errorCodes.put(208, "titre du dossier non valorisé");
//		
//		// Erreur lors de la récupération d'un document
//		errorCodes.put(301, "Erreur de l'action getDocument");
//	}
//	
//	
//	
//	
//	/**
//	 * Retourne un libellé d'erreur en fonction du code
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
//	 * Retourne un libellé d'erreur en fonction du code
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
//	 * Retourne un libellé d'erreur en fonction du code
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

