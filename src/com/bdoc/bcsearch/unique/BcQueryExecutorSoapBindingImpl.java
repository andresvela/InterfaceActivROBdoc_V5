

package com.bdoc.bcsearch.unique;

import java.util.HashMap;

public class BcQueryExecutorSoapBindingImpl implements com.bdoc.bcsearch.unique.webservice.BcQueryExecutor{

	public java.util.HashMap executeBCQuery(java.lang.String id, java.lang.String type) throws java.rmi.RemoteException {
		
    	System.out.println("bdociws BcQueryExecutor : Hello \n ID="+id+" \n type="+type);
    	HashMap<String, String> bc = null;
    	System.out.println("Type DIRECT : Ajout des id");
    	bc = new HashMap<String, String>();
		bc.put("DIRECT_ID",id);
		bc.put("Type", type);

  		return bc;
	}

}
