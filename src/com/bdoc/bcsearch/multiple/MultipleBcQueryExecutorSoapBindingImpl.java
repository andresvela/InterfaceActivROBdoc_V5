
package com.bdoc.bcsearch.multiple;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MultipleBcQueryExecutorSoapBindingImpl implements com.bdoc.bcsearch.multiple.webservice.BcQueryExecutor{

	@SuppressWarnings("unchecked")
	public java.util.HashMap[] executeBCQuery(java.util.HashMap map, java.lang.String type) throws java.rmi.RemoteException {

		Set keylist = map.keySet();
		for (Object object : keylist) {
			System.out.println((String)object+ " = "+ (String)map.get((String)object));
		}
		HashMap[] bcs = null;
		
		String query = (String)map.get("BCTYPE");
		System.out.println("Type : "+query);
		
		ArrayList<HashMap<String, String>> listbc = new ArrayList<HashMap<String,String>>(1);

		String ID = null;
			ID = (String)map.get("BCID");
			if (ID!=null)
			{
				System.out.println("==> Recherche contexte : "+query);
				HashMap<String, String> bc = new HashMap<String, String>();
				bc.put("BCID",ID);
				bc.put("Type", type);
				listbc.add(bc);
				bcs = listbc.toArray(new HashMap[listbc.size()]);
			}
			else
			{
				bcs=null;
			}
					
			return bcs;
		}
	
}
