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

import java.util.List;
import java.util.ArrayList;

import org.dom4j.Document;

import epcc.xmldiff.io.XMLDiffUserDataElement;
import epcc.xmldiff.exception.SetUpException;

/**
 * Creates and provides access to {@link Assertion Assertion} class instances.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class Assertions {
	
	/* Declare String constants used to retrieve assertion element attributes. */
	private static final String XPATH_ATTRIBUTE = "xpath";
	private static final String TYPE_ATTRIBUTE = "type";
	private static final String COMPARISON_ATTRIBUTE = "comparison";
	private static final String TOLERANCE_ATTRIBUTE = "tolerance";
	
	/* Declare constant error messages. */
	private static final String SETUPERROR = "SETUP ERROR ";
	private static final String MISSINGATTRIBUTE = "Assertion statement is missing the " +
											"following essential attribute(s) - ";
	private static final String NOASSERTIONS = "Metric document contains " +
							"no assertions, please provide to enable document comparison";
	
	/* Declare container for Assertion instances. */
	private List assertion_list;	
						
	/**
	 * Generates {@link Assertion Assertion} instances from the information 
	 * contained within the metric document argument.
	 * 
	 * @param metric	Metric document from which {@link Assertion Assertion}
	 * 					instances will be generated, must not be <code>null</code>.
	 * @throws SetUpException To notify of an error encountered while
	 * processing the metric document. 
	 */
	public Assertions(Document metric) throws SetUpException {
	
		processMetricDocument(metric);
	}
	
	
	/**
	 * Retrieves each 'assertion' element listed within the metric document
	 * and creates a new {@link Assertion Assertion} instance for each.
	 * 
	 * @param metric	Document from which {@link Assertion Assertion}
	 * 					instances will be generated, must not be <code>null</code>.
	 * @throws SetUpException To notify of an error encountered while
	 * processing the metric document.
	 */
	private void processMetricDocument(Document metric) throws SetUpException {
		
		/* Initialise the container for the Assertion instances. */
		assertion_list = new ArrayList();
		
		/* Retrieve the root element (should never be null as would
		 * have been caught during parsing). */
		XMLDiffUserDataElement metric_root = 
						(XMLDiffUserDataElement)metric.getRootElement();
		
		/* Retrieve all Assertion elements. */
		List metric_assertions = metric_root.elements();
		
		if(metric_assertions.size() > 0){	
			
			/* Retrieve each assertion element and use its attribute values
			 * to create Assertion instances. */
			XMLDiffUserDataElement assertion_element = null;
			Assertion assertion = null;
			
			Object xpath_attribute = null;
			Object type_attribute = null;
			Object comparison_attribute = null;
			Object tolerance_attribute = null;
			String metricDoc_name = metric.getName();
			int line_number = 0;
			
			for(int i=0; i<metric_assertions.size(); i++){
				
				assertion_element = (XMLDiffUserDataElement)metric_assertions.get(i);
				
				/* Retrieve this element's attribute values.*/
				xpath_attribute = assertion_element.attributeValue(XPATH_ATTRIBUTE);
				type_attribute = assertion_element.attributeValue(TYPE_ATTRIBUTE);
				comparison_attribute = assertion_element.attributeValue(COMPARISON_ATTRIBUTE);
				tolerance_attribute = assertion_element.attributeValue(TOLERANCE_ATTRIBUTE);
				line_number = assertion_element.getLocation().getLineNumber();
				
				/* Test that no attributes are missing. */
				if(xpath_attribute != null && type_attribute != null && 
					comparison_attribute != null && tolerance_attribute != null){
						
					/* Create new Assertion instance using data retrieved. */
					assertion = new Assertion((String)xpath_attribute, 
												(String)type_attribute, 
												(String)comparison_attribute, 
												(String)tolerance_attribute,
												metricDoc_name, line_number);
					
					/* Add new Assertion instance to the List container. */
					assertion_list.add(assertion);	
									
				}else {
					
					/* Generate an error message to display all attributes
					 * that are missing from the assertion statement. */
					String error_message = SETUPERROR + "[" + metric.getName() + 
											" line " + line_number + "]: " + 
											MISSINGATTRIBUTE;
											
					if(xpath_attribute == null){
						error_message = error_message + XPATH_ATTRIBUTE + " ";
					}
					if(type_attribute == null) {
						error_message = error_message + TYPE_ATTRIBUTE + " ";
					}
					if(comparison_attribute == null) {
						error_message = error_message + COMPARISON_ATTRIBUTE + " ";
					}
					if(tolerance_attribute == null) {
						error_message = error_message + TOLERANCE_ATTRIBUTE + " ";
					}
					
					/* Throw a SetUpException instance - metric document assertion 
			  		 * missing a required attribute. */
					throw new SetUpException(error_message);
				}				
			}
		}else{

			/* Throw a SetUpException instance - no assertions listed in 
			 * metric document. */
			throw new SetUpException(SETUPERROR + "[" + metric.getName() + 
										"]: " + NOASSERTIONS);
		}
	}
	
	
	/**
	 * Returns the number of {@link Assertion Assertion} instances contained 
	 * within an instance of this class.
	 * 
	 * @return	Number of {@link Assertion Assertion} instances contained 
	 * within an instance of this class.
	 */
	public int size(){
		return assertion_list.size();
	}
	
	
	/**
	 * Returns an {@link Assertion Assertion} instance contained within an instance
	 * of this class at (inverted) index 'index'.
	 * 
	 * @param index	Index of java.util.List from where an {@link Assertion Assertion}
	 * 				instance should be retrieved.
	 * @return		{@link Assertion Assertion} contained within a java.util.List 
	 * 				at (inverted) index 'index'.
	 */
	public Assertion get(int index){
		int start_index = assertion_list.size() - 1;
		return (Assertion)assertion_list.get(start_index - index);
	}
}
