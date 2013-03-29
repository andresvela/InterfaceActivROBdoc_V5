package com.bdoc.populate;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MergeXml {

	/**
	 * @param args
	 */
		  public static void main(String[] args) throws Exception { 
		    // proper error/exception handling omitted for brevity 
		    File file1 = new File("c:/temp/BdocI.xml"); 
		    File file2 = new File("c:/temp/RetourTALEND.xml"); 
		    Document doc = merge("/Root/Document","/doc" , file1, file2); 
		    print(doc); 
		  } 
		 
		  private static Document merge(String expression,String expression2, File file1, File file2) throws Exception { 
		    XPathFactory xPathFactory = XPathFactory.newInstance(); 
		    XPath xpath = xPathFactory.newXPath(); 
		    XPathExpression compiledExpression = xpath.compile(expression);
		    XPath xpath2 = xPathFactory.newXPath(); 
		    XPathExpression compiledExpression2 = xpath2.compile(expression2);
		    
		    return merge(compiledExpression,compiledExpression2,file1,file2); 
		  } 
		 
		  private static Document merge(XPathExpression expression,XPathExpression expression2, File file1, File file2) throws Exception { 
		    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance(); 
		    docBuilderFactory.setIgnoringElementContentWhitespace(true); 
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder(); 
		    Document base = docBuilder.parse(file1); 
		 
		    Node results = (Node) expression.evaluate(base,XPathConstants.NODE); 
		    if (results == null) { 
		      throw new IOException(file1    + ": expression does not evaluate to node"); 
		    } 
		 
		    Document merge = docBuilder.parse(file2);
		    Node nextResults = (Node) expression2.evaluate(merge,XPathConstants.NODE); 
		    while (nextResults.hasChildNodes()) { 
			        Node kid = (Node) nextResults.getFirstChild(); 
			        nextResults.removeChild(kid); 
			        kid = (Node) base.importNode(kid, true); 
			        results.appendChild(kid); 
			} 
	    
	 
		    return base; 
		  } 
		 
		  private static void print(Document doc) throws Exception { 
		    TransformerFactory transformerFactory = TransformerFactory 
		        .newInstance(); 
		    Transformer transformer = transformerFactory 
		        .newTransformer(); 
		    DOMSource source = new DOMSource(doc); 
		    Result result = new StreamResult(System.out); 
		    transformer.transform(source, result); 
		  } 
		 	
}
