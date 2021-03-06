package com.bdoc.interfaces.webservice.actions.implementations;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;


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
 * Singleton garantissant l'unicit� (singleton) de connexion � bdocweb
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
		}  
		
		//test BdocWeb Network Errors. AVELA.
		this.testSocket();
		
		
		try {
		ewSession =  new JBApplication(ewConnect);
		}
		
		catch(BDocWebException e){
			TableauDeBord.getInstance().addLog("Erreur de Connexion � Bdoc-Web ["+serveur+":"+port+"] KO ");
			throw new BdocWebServiceInterfaceException(8, e) ;			
		}
		catch (BDocNetworkException e){			
			logger.error("BDocNetworkException 		: "+e.getMessage());
			throw new BdocWebServiceInterfaceException(4,e);
		}		
		
		if (ewConnect.isConnectedToBdocweb(true)) {
			logger.info("connexion BdocWeb OK : ");
			TableauDeBord.getInstance().addLog("Connexion � Bdoc-Web ["+serveur+":"+port+"] OK ");
		} 
		return ewSession;
	}

	public boolean isConnected ()
	{
		return ewConnect.isConnectedToBdocweb();
	}
	
	public void testSocket() throws BdocWebServiceInterfaceException{
		try {
			
			   Socket s = null;
	           s = new Socket(serveur, port);
	        } catch (UnknownHostException e) {
	        	// check spelling of hostname
	        	String errorMessage = e.getMessage() + " - ";	        	
	        	throw new BdocWebServiceInterfaceException(4,BdocWebServiceInterfaceException.getErrorLibelle(4) + " " + errorMessage + " - check spelling of hostname") ;
	           
	        } catch (ConnectException e) {
	        	
	        	// connection refused - is server down? Try another port.
	        	String errorMessage = e.getMessage() + " - ";
	        	throw new BdocWebServiceInterfaceException(4,BdocWebServiceInterfaceException.getErrorLibelle(4) + " " + errorMessage + " - connection refused - is server down? Try another port.") ;
	        	
	           
	        } catch (NoRouteToHostException e) {
	        	// The connect attempt timed out.  Try connecting through a proxy
	        	String errorMessage = e.getMessage() + " - ";
	        	throw new BdocWebServiceInterfaceException(4,BdocWebServiceInterfaceException.getErrorLibelle(4) + " " + errorMessage + " - The connect attempt timed out.  Try connecting through a proxy") ;
	        	
	           
	        } catch (IOException e) {
	        	// another error occurred
	        	String errorMessage = e.getMessage() + " - ";
	        	throw new BdocWebServiceInterfaceException(4,BdocWebServiceInterfaceException.getErrorLibelle(4) + " " + errorMessage + " - nother error occurred") ;
	        	           
	        }
		
		
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
				logger.error("Erreur de r�initialisation du cache client de Bdoc-Web : "+e.getLocalizedMessage());
				return false;
			} catch (BDocNetworkException e) {
				logger.error("Erreur r�seau lors de la r�initialisation du cache client de Bdoc-Web : "+e.getLocalizedMessage());
				return false;
			}
		}
		logger.info("r�initialisatoin du cache client de Bdoc-Web : OK");
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
