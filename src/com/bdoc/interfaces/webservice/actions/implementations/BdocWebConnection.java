package com.bdoc.interfaces.webservice.actions.implementations;

import java.io.IOException;


import org.apache.log4j.Logger;

import com.bdoc.interfaces.webservice.parametres.BdocWebProfile;
import com.bdoc.interfaces.webservice.servlets.TableauDeBord;
import com.bdoc.interfaces.webservice.utils.BdocWebServiceInterfaceException;
import com.bdoc.java.ediweb.api.exception.BDocConnectionException;
import com.bdoc.java.ediweb.api.exception.BDocNetworkException;
import com.bdoc.java.ediweb.api.exception.BDocWebException;
import com.bdoc.java.ediweb.api.JBApplication;
import com.bdoc.java.ediweb.api.JBConnection;


/**
 * Singleton garantissant l'unicité (singleton) de connexion à bdocweb
 */

public class BdocWebConnection {

	private Logger logger = Logger.getLogger(BdocWebConnection.class.getName());

	private String serveur;

	private int port;

	private String login;

	private String password;

	private JBApplication ewSession;

	private JBConnection ewConnect;

	private static BdocWebConnection instance;

	public static BdocWebConnection getInstance() {
		if (instance==null){
			instance = new BdocWebConnection();
		}
		return instance;
	}

	
	public String getServeur() {
		return serveur;
	}

	public void setServeur(String serv) {
		serveur = serv;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String log) {
		login = log;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pass) {
		password = pass;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int portnumber) {
		port = portnumber;
	}

	public JBApplication doConnect() throws BdocWebServiceInterfaceException {

		try {
			ewConnect = new JBConnection(serveur,port);			
			ewSession =  new JBApplication(ewConnect);
			
			if (ewConnect.isConnectedToBdocweb(true)) {
				logger.info("connexion BdocWeb OK : ");
				TableauDeBord.getInstance().addLog("Connexion à Bdoc-Web ["+serveur+":"+port+"] OK ");
			} else {
				TableauDeBord.getInstance().addLog("Erreur de Connexion à Bdoc-Web ["+serveur+":"+port+"] KO ");
				throw new BDocWebException("Erreur de connexion cause inconnue. serveur= "+ serveur +", port="+port);
			}

		} catch (BDocConnectionException e) {
			String errorMessage = e.getMessage() + " - ";
			switch (e.getErrorCode()) {
				case JBConnection.ERROR_NO_MIDDLEWARE :
				   errorMessage += "NO_MIDDLEWARE";
				   break;
				case JBConnection.ERROR_UNKNOWN:
				   errorMessage += "ERROR_UNKNOWN";
				   break;
			default:
					//--------------------------------------------------------------
					// unexpected error
					//--------------------------------------------------------------
					errorMessage += "Unexpected error code [" + e.getErrorCode() + "]";
				   break;
			}
			logger.error(errorMessage);
			throw new BdocWebServiceInterfaceException(6,errorMessage) ;
		}  catch (IOException e) {
			logger.error("BDocNetworkException 		: "+e.getMessage());
			throw new BdocWebServiceInterfaceException(4,e);
		}

		return ewSession;
	}

	public boolean isConnected ()
	{
		return ewConnect.isConnectedToBdocweb();
	}
	
	public synchronized JBApplication getJBApplication(String serveur, int port) throws BdocWebServiceInterfaceException  {
		if (ewSession == null || !ewSession.getConnection().isConnectedToBdocweb(true)) {
			setLogin(login);
			setPassword(password);
			setServeur(serveur);
			setPort(port);
			doConnect();
		}
		return ewSession;
	}

	public synchronized JBApplication getJBApplication() {		
		return ewSession;
	}

	public synchronized JBApplication getJBApplication( BdocWebProfile profile) throws BdocWebServiceInterfaceException {
		
		if (profile!=null)
		{
			return getJBApplication(profile.getHostname(),profile.getPort());
		}
		else 
		{
			return null;
		}
	}
	
	public synchronized boolean initRessources()
	{
		if (ewSession != null) {
			try {
				ewSession.initResources();
			} catch (BDocWebException e) {
				logger.error("Erreur de réinitialisation du cache client de Bdoc-Web : "+e.getLocalizedMessage());
				return false;
			} catch (BDocNetworkException e) {
				logger.error("Erreur réseau lors de la réinitialisation du cache client de Bdoc-Web : "+e.getLocalizedMessage());
				return false;
			}
		}
		logger.info("réinitialisatoin du cache client de Bdoc-Web : OK");
		return true;
	}
	
	public void destroy() {
	}

	public void dispose() {
		if (ewConnect != null) {
			ewConnect.closeConnection();
			logger.info("deconnexion de Bdoc-Web : OK");
		}
	}
}
