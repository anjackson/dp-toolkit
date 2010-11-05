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

import org.xml.sax.Locator;

/**
 * Encapsulates a location with in an XML document. Note this is
 * different from a org.xml.sax.Locator in that this contains
 * static location data, whereas org.xml.sax.Locator contains
 * dynamic data that changes during parsing of an XML document.
 * This class is immutable.
 *
 * @author      Ratnadeep Abrol
 * @version     1.0
 */

public class XMLDiffXMLLocation {
  
  	/** value to which location components are set if they are not set */ 
  	public static final int UNSET = -1;


  	// Attributes.
  	private int lineNumber_   = UNSET; // line number of this location.
  	private int columnNumber_ = UNSET; // column number of this location.


  	/**
     * Constructor.
     * 
     * @param locator A locator describing a location within an XML
     * document. Must not be <code>null</code>.
     * @throws NullPointerException if locator is <code>null</code>
     */
  	public XMLDiffXMLLocation(Locator locator) {
    	init(locator);
  	}


  	/**
   	 * Sets a location within an XML document.
     * 
     * @param locator A locator describing the location within an XML
     * document. Must not be <code>null</code>.
     * @throws NullPointerException if locator is <code>null</code>
     */
  	private void init(Locator locator) {
    	lineNumber_   = locator.getLineNumber();
    	columnNumber_ = locator.getColumnNumber();
  	}
  

  	/**
   	 * Returns the line number of this location.
   	 * 
   	 * @return The line number of this location. If this is not set this
   	 * may be <code>UNSET</code>.
     */
  	public int getLineNumber() {
    	return lineNumber_;
  	}
  
  
  	/**
   	 * Returns the column number of this location. This may be unset as
   	 * some XML parsers do not set this property within their locators
   	 * due to performance reasons.
   	 * 
   	 * @return The column number of this location. If this is not set this
   	 * may be <code>UNSET</code>.
     */
  	public int getColumnNumber() {
    	return columnNumber_;
  	}


  	/**
   	 * Returns a string containing the location information. This is in
   	 * the format of "Line: lineno, Column: colno".
   	 * 
   	 * @return A string containing the location information.
   	 */
  	public String toString() {
    	return "Line: " + lineNumber_ + ", Column: " + columnNumber_;
  	}
}
