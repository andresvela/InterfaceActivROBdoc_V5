
package com.bdoc.populate.webservice;

import com.bdoc.populate.PopulateSoapBindingImpl;

public class PopulateSoapBindingSkeleton implements Populate, org.apache.axis.wsdl.Skeleton {
	private Populate impl;
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
				new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://business", "xmlIn"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false), 
		};
		_oper = new org.apache.axis.description.OperationDesc("generate", _params, new javax.xml.namespace.QName("http://business", "generateReturn"));
		_oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		_oper.setElementQName(new javax.xml.namespace.QName("http://business", "generate"));
		_oper.setSoapAction("");
		_myOperationsList.add(_oper);
		if (_myOperations.get("generate") == null) {
			_myOperations.put("generate", new java.util.ArrayList());
		}
		((java.util.List)_myOperations.get("generate")).add(_oper);
	}

	public PopulateSoapBindingSkeleton() {
		this.impl = new PopulateSoapBindingImpl();
	}

	public PopulateSoapBindingSkeleton(Populate impl) {
		this.impl = impl;
	}
	public byte[] generate(byte[] xmlIn) throws java.rmi.RemoteException
	{
		byte[] ret = impl.generate(xmlIn);
		return ret;
	}

}
