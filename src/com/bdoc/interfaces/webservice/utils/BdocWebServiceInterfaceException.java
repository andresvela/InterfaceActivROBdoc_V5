package com.bdoc.interfaces.webservice.utils;

import java.util.HashMap;

import com.bdoc.interfaces.webservice.servlets.TableauDeBord;

public class BdocWebServiceInterfaceException extends Exception {

	public  static HashMap<Integer,String> errorCodes = new HashMap();
	private static final String defaultErrorMessage = "Erreur non r�f�renc�e";
	private String message;
	private int code=-1;
	
	
	static 
	{ 
		
		// Erreurs d'initialisation
		errorCodes.put(-1,  "M�thode non impl�ment�e");
		errorCodes.put(-2,  "Les codes erreurs \"users\" doivent �tre sup�rieurs � 1000");
		
		// Erreurs g�n�riques 0-99
		errorCodes.put(1,   "Aucune action param�tr�e pour les indexes s�lectionn�e");
		errorCodes.put(2,   "Erreur lors de la recherche d'un indexe dans le flux XML");
		errorCodes.put(3,   "Erreur getDocument");
		errorCodes.put(4,   "Erreur de connexion type R�seau � BdocWeb");
		errorCodes.put(5,   "Erreur BdocWeb");
		errorCodes.put(6,   "Erreur de connexion � BdocWeb");
		errorCodes.put(7,   "Erreur aucune connexion � BdocWeb n'est param�tr�e");
		errorCodes.put(8,   "Erreur interne non document�e");
		
		
		// Erreur des actions 100-199
		errorCodes.put(100, "Erreur lors de la recherche d'indexes dans le flux xml");
		errorCodes.put(101, "Erreur de l'action ToBdocEdit");
		errorCodes.put(102, "Erreur de l'action ToBdocWeb");
		errorCodes.put(103, "Domaine inexistant");
		errorCodes.put(104, "mod�le de document inexistant");
		errorCodes.put(105, "Mod�le de document interactif");
		errorCodes.put(106, "Mod�le de document interactif");
		errorCodes.put(107, "Erreur lors de la lecture du retour de BdocWeb");
		errorCodes.put(108, "Retour null de BdocWeb");
		errorCodes.put(109, "Document de taille nulle");
		errorCodes.put(110, "Erreur de traitment Itext");
		errorCodes.put(111, "Aucun mod�le de document retrouv�");
		errorCodes.put(112, "Doublon dans la liste des mod�les");
				
		// Erreur de l'action BdocInteractive 200-299
		errorCodes.put(201, "R�pertoire non trouv� lors de la d�pose du flux ");
		errorCodes.put(202, "Erreur d'�criture du fichier lors de la d�pose du flux ");
		errorCodes.put(203, "URL de BdocInteractive non valoris�");
		errorCodes.put(204, "user BdocInteractive non valoris�");
		errorCodes.put(205, "bcId non valoris�");
		errorCodes.put(206, "bcType non valoris�");
		errorCodes.put(207, "aucun mod�le de document valoris�");
		errorCodes.put(208, "titre du dossier non valoris�");
		
		
		// Erreur de r�cup�ration de la GED 400-499
		errorCodes.put(301, "Erreur de l'action getDocument");
		errorCodes.put(302, "Erreur de l'action getDocument - Acc�s au fichier : ");
		
		// Erreur d'acc�s � la GED
		errorCodes.put(400, "Erreur d'acc�s au r�pertoir de GED");
		errorCodes.put(401, "Erreur d'acc�s au r�pertoir de GED");
		errorCodes.put(402, "Erreur d'acc�s au r�pertoir de GED");
		errorCodes.put(403, "Erreur d'acc�s au r�pertoir de GED");
		errorCodes.put(404, "Erreur d'acc�s en �criture � la GED : ");
		errorCodes.put(405, "Erreur d'acc�s en lecture � la GED : ");
		errorCodes.put(406, "Erreur aucun connecteur GED ne correspond");
		errorCodes.put(407, "Erreur lors de la recherche du connecteur GED");

		// Erreur de transmission vers BdocEdit 500-599
		errorCodes.put(501, "Erreur r�pertoir de d�pot des flux batch");
		errorCodes.put(502, "Erreur d'�criture du flux batch");

		
		// Plage d'erreur actions ajout�es � partir de 1000
		
		
	}

	
	public BdocWebServiceInterfaceException(int code)
	{
		message = getErrorMessage(code);
		this.code = code;
		TableauDeBord.getInstance().addLog("ERREUR : "+code+":"+message);
	}
	
	public BdocWebServiceInterfaceException(int code, String message)
	{
		this.message = getErrorMessage(code)+" : "+message;
		this.code = code;
		TableauDeBord.getInstance().addLog("ERREUR : "+code+":"+message);
	}

	public BdocWebServiceInterfaceException(int code, Exception e)
	{
		super (e);
		message = getErrorMessage(code)+" : "+e.getMessage();
		this.code = code;
		TableauDeBord.getInstance().addLog("ERREUR : "+code+":"+message);
	}

	public BdocWebServiceInterfaceException(int code, Throwable e)
	{
		super (e);
		message = getErrorMessage(code)+" : "+e.getMessage();
		this.code = code;
		TableauDeBord.getInstance().addLog("ERREUR : "+code+":"+message);
	}
	
	/**
	 * Retourne un libell� d'erreur en fonction du code
	 * @param code
	 * @return
	 */
	private String getErrorMessage(int code)
	{
		String message = errorCodes.get(code);
		if (message==null)
		{
				message = defaultErrorMessage;
		}

		return message;
	}

	/**
	 * Retourne un libell� d'erreur en fonction du code
	 * @param code
	 * @return
	 */
	
	private String getErrorMessage(int code,Exception e)
	{
		String message = errorCodes.get(code);
		if (message==null)
		{
				return defaultErrorMessage + " : "+e.getLocalizedMessage();
		}
		else
		{
			 	return message + " : "+e.getLocalizedMessage();
		}
	}

	
	/**
	 * Retourne un libell� d'erreur en fonction du code
	 * @param code
	 * @return
	 */
	
	private String getErrorMessage(int code,Throwable e)
	{
		String message = errorCodes.get(code);
		if (message==null)
		{
				return defaultErrorMessage + " : "+e.getLocalizedMessage();
		}
		else
		{
			 	return message + " : "+e.getLocalizedMessage();
		}
	}	

	/**
	 * Ajout un code erreur suppl�mentaire. le code doit �tre supperieur � 1000
	 * @param code
	 * @param message
	 * @throws BdocWebServiceInterfaceException
	 */
	public static void addErrorCode (int code, String message) throws BdocWebServiceInterfaceException
	{
		if (code<1000) throw new BdocWebServiceInterfaceException(-2);
			errorCodes.put(code, message);
	}
	
	public static String getErrorLibelle (int code) 
	{
		String message = errorCodes.get(code);
		if (message==null)
		{
				return defaultErrorMessage;
		}
		else
		{
			 	return message;
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String libelle()
	{
		return code+" : "+message;
	}

	public String libelle(Throwable e)
	{
		return ""+code+" : "+message+ " : "+e.getLocalizedMessage();
	}
	
}
