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
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.Element;
import org.dom4j.io.SAXContentHandler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A SAXContentHandler that will set the location of an element. As well
 * as being used as a content handler, this class MUST also be set as
 * the only ElementHandler within a SAXReader.
 *
 * @author      Ratnadeep Abrol
 * @version		1.0
 */

public class XMLDiffSAXContentHandler extends SAXContentHandler 
											implements ElementHandler {


	// Attributes.
  	private Locator locator        = null;
  	private Element currentElement = null;


  	/**
   	 * @see org.dom4j.io.SAXContentHandler#SAXContentHandler()
   	 */
  	public XMLDiffSAXContentHandler() {
		super();
  	}


  	/**
   	 * @see org.dom4j.io.SAXContentHandler#SAXContentHandler(org.dom4j.DocumentFactory)
   	 */
  	public XMLDiffSAXContentHandler(DocumentFactory documentFactory) {
		super(documentFactory);
  	}


  	/**
     * @see org.dom4j.io.SAXContentHandler#SAXContentHandler(org.dom4j.DocumentFactory, 
     * 															org.dom4j.ElementHandler)
   	 */
  	public XMLDiffSAXContentHandler(DocumentFactory documentFactory, ElementHandler elementHandler) {
		super(documentFactory, elementHandler);
  	}


  	/**
   	 * @see org.dom4j.ElementHandler#onStart(org.dom4j.ElementPath)
   	 */
  	public void onStart(ElementPath path) {
  		
		/* Remember the current element.*/
		currentElement = path.getCurrent();
  	}


  	/**
   	 * @see org.dom4j.ElementHandler#onEnd(org.dom4j.ElementPath)
   	 */
  	public void onEnd(ElementPath path) {
		// do nothing.
  	}


  	/**
   	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
   	 */
  	public void setDocumentLocator(Locator locator) {
    
	  	super.setDocumentLocator(locator);
	  	
	  	// remember the locator
	  	this.locator = locator;
  	}


  	/**
   	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, 
   	 * 		java.lang.String, java.lang.String, org.xml.sax.Attributes)
   	*/
  	public void startElement(String namespaceURI, String localName, 
  					String qualifiedName, Attributes attributes)
	   													throws SAXException {
         
		// call parent method so that element is created
		super.startElement(namespaceURI, localName, qualifiedName, attributes);

		// set the location of the current element
		if(currentElement != null && currentElement instanceof XMLDiffUserDataElement){
	  		((XMLDiffUserDataElement)currentElement).setLocation(locator);
		}
  	}
}
