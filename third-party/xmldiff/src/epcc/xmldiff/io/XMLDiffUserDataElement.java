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

import org.dom4j.QName;
import org.dom4j.util.UserDataElement;

import org.xml.sax.Locator;

/**
 * Class describing a UserDataElement with location information on its
 * position within an XML document.
 *
 * @author      Ratnadeep Abrol
 * @version		1.0
 */

public class XMLDiffUserDataElement extends UserDataElement {


  	// Attributes.
  	private XMLDiffXMLLocation location_ = null;

  	/**
   	 * @see org.dom4j.util.UserDataElement#UserDataElement(String)
   	 */
  	public XMLDiffUserDataElement(String name) {
    	super(name);
  	}


  	/**
   	 * @see org.dom4j.util.UserDataElement#UserDataElement(QName)
   	 */
  	public XMLDiffUserDataElement(QName qname) {
    	super(qname);
  	}
  
  
 	/**
   	 * Sets the location of this element from a XML parser location object.
   	 * 
   	 * @param locator A locator object from an XML parser
   	 */
	public void setLocation(Locator locator) {
    	location_ = new XMLDiffXMLLocation(locator);    
  	}
  
  
 	/**
   	 * Returns the location of this element within its XML document.
   	 * 
   	 * @return Location of this element within its XML document. This
   	 * may be <code>null</code>.
   	 */
  	public XMLDiffXMLLocation getLocation() {
    	return location_;
  	}
  
  
  	/**
   	 * Returns whether the location of this element has been set.
   	 * 
   	 * @return Whether the location of this element has been set.
   	 */
  	public boolean isLocationSet() {
    	return location_ != null;
  	}
}
