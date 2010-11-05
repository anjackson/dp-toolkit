/*
 * Created on May 4, 2004
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
package epcc.xmldiff.valuecomparator;

import epcc.xmldiff.exception.SetUpException;

/**
 * Stores attributes of an 'Assertion' element contained within the metric document 
 * and provides access to an appropriate {@link ValueComparator ValueComparator} 
 * class instance.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class Assertion {
	
	/* Declare constant error messages. */
	private static final String SETUPERROR = "SETUP ERROR ";
			
	/* Declare instance variables used to define an Assertion. */
	private String assertion_xpath;
	private String assertion_type;
	private String assertion_comparison;
	private String assertion_tolerance;
	private String parent_doc;
	private int assertion_linenumber;
	
	/**
	 * Initialises instance variables.
	 * 
	 * @param xpath			XPath of document elements to be compared, must not be 
	 * 						<code>null</code>.
	 * @param type			Type of data to be compared, must not be 
	 * 						<code>null</code>.
	 * @param comparison_op	Comparison operation to be executed, must not be 
	 * 						<code>null</code>.
	 * @param tolerance		Tolerance level of data differences allowed.
	 * @param line_no		Location of this metric assertion within the metric 
	 * 						document.
	 */
	protected Assertion(String xpath, String type, String comparison_op, 
						String tolerance, String doc_name, int line_no){
							
		assertion_xpath = xpath;
		assertion_type = type;
		assertion_comparison = comparison_op;
		assertion_tolerance = tolerance;
		parent_doc = doc_name;
		assertion_linenumber = line_no;
	}
	
	
	/**
	 * Returns String of XPath to locate document elements.
	 * 
	 * @return		String referencing XPath to locate document elements.
	 */
	public String getXPath(){
		return assertion_xpath;
	}
	
	
	/**
	 * Returns int denoting the line number location of the 'Assertion' element
	 * within the metric document that an instance of this class represents.
	 * 
	 * @return	Integer denoting the location of this Assertion within the metric
	 * 			document.
	 */
	public int getAssertionLineNumber(){
		return assertion_linenumber;
	}
	
	
	/**
	 * Uses {@link ValueComparatorFactory ValueComparatorFactory} instance to 
	 * retrieve the appropriate {@link ValueComparator ValueComparator} instance (as
	 * defined by an instance of this class).
	 * 
	 * @return		{@link ValueComparator ValueComparator} of the type defined 
	 * 				by an instance of this class.
	 * @throws SetUpException Detailing the error that occurred while attempting
	 * to retrieve a {@link ValueComparator ValueComparator} instance.
	 */
	public ValueComparator getValueComparator() throws SetUpException {
		
		ValueComparator comparator = null;
		
		/* Next, retrieve the single instance of ValueComparatorFactory and call its
		 * getValueComparator method to retrieve the appropriate ValueComparator 
		 * instance. */
		ValueComparatorFactory factory = ValueComparatorFactory.getInstance();
		
		try {
			
			comparator = 
				factory.getValueComparator(new ValueComparatorDescription(assertion_type, 
																		assertion_comparison,
																		assertion_tolerance));
		}catch(SetUpException sue){
			
			/* Catch an exception if thrown and assign the line number of this
			 * metric Assertion to the output message to aid the user. */
			throw new SetUpException(SETUPERROR + "[" + parent_doc + " line " +
								assertion_linenumber + "]: " + sue.getMessage(),
								sue.getCause());
		}
		return comparator;
	}
}
