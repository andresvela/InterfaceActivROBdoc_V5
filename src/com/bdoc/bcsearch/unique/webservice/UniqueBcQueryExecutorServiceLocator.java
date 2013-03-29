package com.bdoc.bcsearch.unique.webservice;

public class UniqueBcQueryExecutorServiceLocator extends org.apache.axis.client.Service implements com.bdoc.bcsearch.unique.webservice.UniqueBcQueryExecutorService {

	public UniqueBcQueryExecutorServiceLocator() {
	}


	public UniqueBcQueryExecutorServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public UniqueBcQueryExecutorServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for BcQueryExecutor
	private java.lang.String BcQueryExecutor_address = "http://localhost:8080/BCSearchWebService/services/UniqueBcQueryExecutor";

	public java.lang.String getBcQueryExecutorAddress() {
		return BcQueryExecutor_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String BcQueryExecutorWSDDServiceName = "BcQueryExecutor";

	public java.lang.String getBcQueryExecutorWSDDServiceName() {
		return BcQueryExecutorWSDDServiceName;
	}

	public void setBcQueryExecutorWSDDServiceName(java.lang.String name) {
		BcQueryExecutorWSDDServiceName = name;
	}

	public com.bdoc.bcsearch.unique.webservice.BcQueryExecutor getBcQueryExecutor() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(BcQueryExecutor_address);
		}
		catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getBcQueryExecutor(endpoint);
	}

	public com.bdoc.bcsearch.unique.webservice.BcQueryExecutor getBcQueryExecutor(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			com.bdoc.bcsearch.unique.webservice.BcQueryExecutorSoapBindingStub _stub = new com.bdoc.bcsearch.unique.webservice.BcQueryExecutorSoapBindingStub(portAddress, this);
			_stub.setPortName(getBcQueryExecutorWSDDServiceName());
			return _stub;
		}
		catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setBcQueryExecutorEndpointAddress(java.lang.String address) {
		BcQueryExecutor_address = address;
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (com.bdoc.bcsearch.unique.webservice.BcQueryExecutor.class.isAssignableFrom(serviceEndpointInterface)) {
				com.bdoc.bcsearch.unique.webservice.BcQueryExecutorSoapBindingStub _stub = new com.bdoc.bcsearch.unique.webservice.BcQueryExecutorSoapBindingStub(new java.net.URL(BcQueryExecutor_address), this);
				_stub.setPortName(getBcQueryExecutorWSDDServiceName());
				return _stub;
			}
		}
		catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("BcQueryExecutor".equals(inputPortName)) {
			return getBcQueryExecutor();
		}
		else  {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://unique.search.bc.bdoc.com", "UniqueBcQueryExecutorService");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://unique.search.bc.bdoc.com", "BcQueryExecutor"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("BcQueryExecutor".equals(portName)) {
			setBcQueryExecutorEndpointAddress(address);
		}
		else 
		{ // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
