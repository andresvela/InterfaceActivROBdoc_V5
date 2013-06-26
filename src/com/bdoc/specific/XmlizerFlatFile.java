package com.bdoc.specific;

import java.io.*;
import java.util.Scanner;


import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlizerFlatFile {

	
	private DocumentBuilder docBuilder;
	private Document doc ;
	private Element rootElement;
	private final File fFile;
	protected Logger logger = Logger.getLogger(this.getClass());
	private Scanner scanner;

  public  void main(String... aArgs) throws FileNotFoundException, ParserConfigurationException, TransformerException {
	XmlizerFlatFile parser = new XmlizerFlatFile("C:\\Temp\\Syn BN 01 (Amaral) Funding Notices_1.txt");
    parser.processLineByLine();
    writeXMLFile("C:\\Temp\\out.xml");
    logger.debug("Xmlization Done.");
  }
  
  /**
   Constructor.
   @param aFileName full name of an existing, readable file.
  */
  public XmlizerFlatFile(String aFileName){
    fFile = new File(aFileName);     
    //Note that FileReader is used, not File, since File is not Closeable
    try {
		Scanner scanner = new Scanner(new FileReader(fFile));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  /** Template method that calls {@link #processLine(String)}.  
 * @throws ParserConfigurationException 
 * @throws TransformerException */
  
  public final void processLineByLine() throws ParserConfigurationException, TransformerException {
	  
	 //create XMLM
	 this.createXML();
	 //create root
	 this.createRootXML("CASSIOPEROOT");	  
    
	 try {
		 //first use a Scanner to get each line
		 while ( scanner.hasNextLine() ){
			 processLine( scanner.nextLine() );
		 }     
    }
    catch (Exception e){    	
    	logger.error(e.getMessage());
    }
    finally {
      //ensure the underlying stream is always closed
      //this only has any effect if the item passed to the Scanner
      //constructor implements Closeable (which it does in this case).
      scanner.close();      
    }
  }
  
  /** 
   Overridable method for processing lines in different ways.
    
   <P>This simple default implementation expects simple name-value pairs, separated by an 
   '=' sign. Examples of valid input : 
   <tt>height = 167cm</tt>
   <tt>mass =  65kg</tt>
   <tt>disposition =  "grumpy"</tt>
   <tt>this is the name = this is the value</tt>
  */
  protected void processLine(String aLine){
    //use a second Scanner to parse the content of each line 
    Scanner scanner2 = new Scanner(aLine);
    scanner2.useDelimiter("#");  
    String ID = null;
    Element staff = null;
    try {
    	 if (scanner2.hasNext() ){
    	    	ID = scanner2.next();    
    	    	
    	    	// staff elements
    	    	staff = doc.createElement(ID);
    			rootElement.appendChild(staff);		
    	    	//log("ID is : " + quote(ID.trim()));
    	    	int i= 0;
    	   	    while (scanner2.hasNext()){
    	   	    	i++;
    	   	    	String value = scanner2.next();
    	   		  
    	   	    	// sons elements
    	   			Element dataElement = doc.createElement("DATA"+i);
    	   			dataElement.appendChild(doc.createTextNode(value));
    	   			staff.appendChild(dataElement);
    	   		  
    	   		  //log("Value is : " + quote(value.trim()) );
    	   	  }  
    	    	
    	    }  
    	    else {
    	    	logger.warn("Empty or invalid line. Unable to process.");
    	        //no need to call scanner.close(), since the source is a String
    	      }
    }
    catch (Exception e){
    	logger.error("- Line "+ ID + " error: " + e.getMessage());
    }
   
  }
  
      
  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
  
  private void createXML() throws ParserConfigurationException{
  	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	this.docBuilder = docFactory.newDocumentBuilder();		
	  
  }
  
  private void createRootXML(String rootString){
	// root elements
		this.doc = docBuilder.newDocument();
		this.rootElement = doc.createElement(rootString);
		doc.appendChild(rootElement);
	  
  }
  
  public  void writeXMLFile(String fileName) throws TransformerException{
	  
// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileName));
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		logger.debug("File saved!");
  }
  
  public  byte[] writeXMLStream() throws TransformerException{
	  
		// write the content into xml file
		
		DOMSource source = new DOMSource(doc);
						
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
        StringWriter stringWriter = new StringWriter();  
        StreamResult result = new StreamResult(out); 
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(source, result); 
		
		return out.toByteArray(); 
	  }  

} 
