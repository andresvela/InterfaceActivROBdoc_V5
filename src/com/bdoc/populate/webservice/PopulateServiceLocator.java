

package com.bdoc.populate.webservice;

public class PopulateServiceLocator extends org.apache.axis.client.Service implements PopulateService {

	public PopulateServiceLocator() {
	}


	public PopulateServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public PopulateServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for Populate
	private java.lang.String Populate_address = "http://localhost:8080/GEDSearchWebService/services/Populate";

	public java.lang.String getPopulateAddress() {
		return Populate_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String PopulateWSDDServiceName = "Populate";

	public java.lang.String getPopulateWSDDServiceName() {
		return PopulateWSDDServiceName;
	}

	public void setPopulateWSDDServiceName(java.lang.String name) {
		PopulateWSDDServiceName = name;
	}

	public Populate getPopulate() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(Populate_address);
		}
		catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getPopulate(endpoint);
	}

	public Populate getPopulate(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			PopulateSoapBindingStub _stub = new PopulateSoapBindingStub(portAddress, this);
			_stub.setPortName(getPopulateWSDDServiceName());
			return _stub;
		}
		catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setPopulateEndpointAddress(java.lang.String address) {
		Populate_address = address;
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (Populate.class.isAssignableFrom(serviceEndpointInterface)) {
				PopulateSoapBindingStub _stub = new PopulateSoapBindingStub(new java.net.URL(Populate_address), this);
				_stub.setPortName(getPopulateWSDDServiceName());
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
		if ("Populate".equals(inputPortName)) {
			return getPopulate();
		}
		else  {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://business", "PopulateService");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://business", "Populate"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("Populate".equals(portName)) {
			setPopulateEndpointAddress(address);
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
