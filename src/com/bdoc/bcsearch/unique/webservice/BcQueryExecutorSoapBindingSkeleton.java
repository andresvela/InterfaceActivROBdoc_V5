
package com.bdoc.bcsearch.unique.webservice;

public class BcQueryExecutorSoapBindingSkeleton implements com.bdoc.bcsearch.unique.webservice.BcQueryExecutor, org.apache.axis.wsdl.Skeleton {
	private com.bdoc.bcsearch.unique.webservice.BcQueryExecutor impl;
	private static java.util.Map _myOperations = new java.util.Hashtable();
	private static java.util.Collection _myOperationsList = new java.util.ArrayList();

	/**
	 * Returns List of OperationDesc objects with this name
	 */
	public static java.util.List getOperationDescByName(java.lang.String methodName) {
		return (java.util.List)_myOperations.get(methodName);
	}

	/**
	 * Returns Collection of OperationDescs
	 */
	public static java.util.Collection getOperationDescs() {
		return _myOperationsList;
	}

	static {
		org.apache.axis.description.OperationDesc _oper;
		org.apache.axis.description.FaultDesc _fault;
		org.apache.axis.description.ParameterDesc [] _params;
		_params = new org.apache.axis.description.ParameterDesc [] {
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://unique.search.bc.bdoc.com", "id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://unique.search.bc.bdoc.com", "type"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
		};
		_oper = new org.apache.axis.description.OperationDesc("executeBCQuery", _params, new javax.xml.namespace.QName("http://unique.search.bc.bdoc.com", "executeBCQueryReturn"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://unique.search.bc.bdoc.com", "executeBCQuery"));
		_oper.setSoapAction("");
		_myOperationsList.add(_oper);
		if (_myOperations.get("executeBCQuery") == null) {
			_myOperations.put("executeBCQuery", new java.util.ArrayList());
		}
		((java.util.List)_myOperations.get("executeBCQuery")).add(_oper);
	}

	public BcQueryExecutorSoapBindingSkeleton() {
		this.impl = new com.bdoc.bcsearch.unique.BcQueryExecutorSoapBindingImpl();
	}

	public BcQueryExecutorSoapBindingSkeleton(com.bdoc.bcsearch.unique.webservice.BcQueryExecutor impl) {
		this.impl = impl;
	}
	public java.util.HashMap executeBCQuery(java.lang.String id, java.lang.String type) throws java.rmi.RemoteException
	{
		java.util.HashMap ret = impl.executeBCQuery(id, type);
		return ret;
	}

}
