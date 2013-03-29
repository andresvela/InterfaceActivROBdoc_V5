package com.bdoc.populate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestMergeXml {

	/**
	 * @param args
	 */
	public byte[] generate(byte[] xmlIn){
	System.out.println("Entrée dans le WS avec le flux de donnée : length = "+xmlIn.length);
	
	byte[] returnedFlow = null;
	String fileName = "";
	String bcType ="";
	String bcId ="";
	String templateName = "";
	Document doc = null;
	Document docTalend = null;

	System.err.println("xmlin= "+xmlIn);

	try {
		//Parse Input as XML
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(new String(xmlIn)));
		is.setSystemId("http:\\\\localhost\\");
		doc = builder.parse(is);
	} catch (SAXException e) {
		System.err.println("Error parsing input : " + e.getMessage());
		return xmlIn;
	} catch (IOException e) {
		System.err.println("Error parsing input : " + e.getMessage());
		return xmlIn;
	} catch (ParserConfigurationException e) {
		System.err.println("Error parsing input : " + e.getMessage());
		return xmlIn;
	}

	//Retrieve BCType from XML
	Node typeNode = doc.getElementsByTagName("BCTYPE").item(0);
	if(typeNode != null){
		bcType = typeNode.getFirstChild().getTextContent();
	}else{
		System.err.println("unfound tag element BCTYPE");
	}

	//Retrieve BCID from XML
	Node idNode = doc.getElementsByTagName("BCID").item(0);
	if(idNode != null){
		bcId = idNode.getFirstChild().getTextContent();
	}else{
		System.err.println("unfound tag element BCID");
	}

	//Retrieve template name from XML
	Element templateNode = (Element)doc.getElementsByTagName("template").item(0);
	if(templateNode != null){
		Node templateNameNode = templateNode.getElementsByTagName("name").item(0);
		templateName = templateNameNode.getFirstChild().getTextContent();
	}else{
		System.err.println("unfound tag element template");
	}    	

	// Appel du WebService TALEND
	
	// Récupération du fichier XML 

	

	//File f = new File( path + fileName );
	File f = new File("c:/temp/RetourTALEND.xml");
	
	if ( f.exists() )
	{
		try {
			System.out.println("Ouverture du fichier "+f.getAbsolutePath());
			DocumentBuilder builder_Talend = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource isTalend = new InputSource(new FileInputStream(f));
			isTalend.setSystemId("http:\\\\localhost\\");
			docTalend = builder_Talend.parse(isTalend);
			Node talendContent = docTalend.getFirstChild();
			System.out.println("Ajout du fichier xml dans le flux retour");
			
			Node importedNode = (Node) doc.importNode(talendContent,true);
			doc.getFirstChild().getFirstChild().appendChild(importedNode);

			System.out.println("Ajout du fichier xml dans le flux retour : OK");
					
			Source source = new DOMSource(doc);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Result result = new StreamResult(out);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer;
			try {
				transformer = factory.newTransformer();
				transformer.transform(source, result);
			} catch (TransformerConfigurationException e) {
				System.err.println("error serializing  document : " + e.getMessage());
			} catch (TransformerException e) {
				System.err.println("error serializing  document : " + e.getMessage());
			}

			returnedFlow = out.toByteArray();

			
		} catch (IOException e) {
			System.err.println("could not read TALEND file : " + e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			System.err.println("Erreur de configuratoin du parseur pour le fichier TALEND: " + e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			System.err.println("Erreur de parsing XML : " + e.getMessage());
			e.printStackTrace();
		}
	}else{
		Element comment = doc.createElement("comment"); 
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		comment.appendChild(doc.createTextNode("Populate-Data-Service: web-service sample modification " + formatter.format(now)));
		doc.getFirstChild().appendChild(comment);
		Source source = new DOMSource(doc);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Result result = new StreamResult(out);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = factory.newTransformer();
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			System.err.println("error serializing  document : " + e.getMessage());
		} catch (TransformerException e) {
			System.err.println("error serializing  document : " + e.getMessage());
		}

		returnedFlow = out.toByteArray();

	}		
	System.out.println("Sortie du WS Populate : Taille Flux = "+returnedFlow.length);
	System.out.println("xmlout= "+new String (returnedFlow));
	return returnedFlow;
}


private byte[] getBytesFromFile(File file) throws IOException {
	InputStream is = new FileInputStream(file);

	// Get the size of the file
	long length = file.length();

	//assume file size is not too large
	byte[] bytes = new byte[(int)length];

	// Read in the bytes
	int offset = 0;
	int numRead = 0;
	while (offset < bytes.length
			&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		offset += numRead;
	}

	// Ensure all the bytes have been read in
	if (offset < bytes.length) {
		throw new IOException("Could not completely read file "+file.getName());
	}

	// Close the input stream and return bytes
	is.close();
	return bytes;
}

	
	public static void main(String[] args) {
			try {
				File fi = new File("C:/Flux_HAB76/BdocI.xml");				
				TestMergeXml mergeXml = new TestMergeXml();			
				byte[] bufin  = mergeXml.getBytesFromFile(fi);
				byte[] bufout = mergeXml.generate(bufin);
				File fo = new File ("c:/temp/_generated.xml");
				FileOutputStream fos = new FileOutputStream(fo);
				fos.write(bufout);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		

	}

}
