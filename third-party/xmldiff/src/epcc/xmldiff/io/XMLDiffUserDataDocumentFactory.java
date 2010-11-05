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
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.util.UserDataDocumentFactory;

/**
 * Singleton document factory implementation that produces LocatedUserDataElements.
 *
 * @author      Ratnadeep Abrol
 * @version     1.0
 */

public class XMLDiffUserDataDocumentFactory extends UserDataDocumentFactory {

  	/** single instance of this class - uses lazy instance creation */
  	private static XMLDiffUserDataDocumentFactory factory_ = null;


  	/**
   	 * Returns the single instance of this factory.
   	 * 
   	 * @return The single instance of this factory.
   	 */
  	public static DocumentFactory getInstance() {

    	if(factory_ == null ) {
			factory_ = new XMLDiffUserDataDocumentFactory();
    	}
    	return factory_;
 	}


  	/**
     * Creates an instance of a {@link XMLDiffUserDataElement XMLDiffUserDataElement}
   	 * 
   	 * @return {@link org.dom4j.Element Element} interface to a newly created
   	 * {@link XMLDiffUserDataElement XMLDiffUserDataElement} instance.
   	 */
  	public Element createElement(QName qname) {
    	return new XMLDiffUserDataElement(qname);
  	}
}
