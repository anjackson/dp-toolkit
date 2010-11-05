/*
 * Created on Jun 7, 2004
 *
 * XMLDiff Software Distribution
 * 
 * Copyright (c) 2004 The University of Edinburgh. All rights reserved.
 * 
 * Redistribution and use of this software, in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the
 *    distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE, ARE DISCLAIMED. IN NO EVENT SHALL THE
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING BUT
 * NOT LIMITED TO PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF THE AUTHOR HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package epcc.xmldiff.io;

import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.SAXReader;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Creates a SAXReader that creates Elements with location information.
 * This class uses its own SAXContentHandler and also uses its own
 * default handler. No element handlers must be set on this class. 
 *
 * @author      Ratnadeep Abrol
 * @version     1.0
 */

public class XMLDiffSAXReader extends SAXReader {
	
	/**
	 * @see org.dom4j.io.SAXReader#SAXReader()
	 */
	public XMLDiffSAXReader() {
		super();
	}
            

	/**
	 * Supports XML Schema-based validation.
	 * @see org.dom4j.io.SAXReader#SAXReader(boolean)
	 * @throws SAXException If an error occurred setting up schema
	 * based validation.
	 */
	public XMLDiffSAXReader(boolean validating) throws SAXException {
		
		super(validating);
		
		/* Configure the XMLReader for schema-based validation if this has
		 * been requested. */
		if(validating){
			
			XMLReader xml_reader = this.getXMLReader();

			xml_reader.setFeature("http://xml.org/sax/features/validation", true);
			xml_reader.setFeature("http://apache.org/xml/features/validation/schema", true);
			xml_reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
			xml_reader.setFeature("http://apache.org/xml/features/validation/dynamic", true);
			
			/* Include an EntityResolver to read the schema data. */
			xml_reader.setEntityResolver(new XMLDiffEntityResolver());
		}
	}


	/**
	 * @see org.dom4j.io.SAXReader#SAXReader(org.dom4j.DocumentFactory)
	 */
	XMLDiffSAXReader(DocumentFactory factory) {
		super(factory);
	}


	/**
	 * @see org.dom4j.io.SAXReader#SAXReader(org.dom4j.DocumentFactory, boolean)
	 */
	XMLDiffSAXReader(DocumentFactory factory, boolean validating) {
		super(factory, validating);
	}
            

	/**
	 * @see org.dom4j.io.SAXReader#SAXReader(java.lang.String)
	 */
	XMLDiffSAXReader(String xmlReaderClassName) throws SAXException {
		super(xmlReaderClassName);
	}
       
            
	/**
	 * @see org.dom4j.io.SAXReader#SAXReader(java.lang.String, boolean)
	 */
	XMLDiffSAXReader(String xmlReaderClassName, boolean validating) throws SAXException {
		super(xmlReaderClassName, validating);
	}
            
	/**
	 * @see org.dom4j.io.SAXReader#SAXReader(org.xml.sax.XMLReader)
	 */
	XMLDiffSAXReader(XMLReader xmlReader) {
		super(xmlReader);
	}
            
	/**
	 * @see org.dom4j.io.SAXReader#SAXReader(org.xml.sax.XMLReader, boolean)
	 */
	XMLDiffSAXReader(XMLReader xmlReader, boolean validating) {
		super(xmlReader, validating); 
	}


	/**
 	 * Sets up and returns a content handler that sets the location of the
	 * elements.
	 * 
	 * @return a content handler that sets the location of the elements.
	 */
	protected SAXContentHandler createContentHandler(XMLReader reader) {
    
		XMLDiffSAXContentHandler contentHandler
				= new XMLDiffSAXContentHandler(getDocumentFactory(), getDispatchHandler());

		/* The XMLDiffSAXContentHandler must be set up as the default
		 * element handler on this reader. This is hacky, but gets around a
		 * limitaiton of dom4j in that it doesn't allow a content handler
		 * access to its element stack. */
		setDefaultHandler(contentHandler);
		return contentHandler;
	}
}
