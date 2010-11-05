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
package epcc.xmldiff.comparator;

import java.util.List;
import java.util.logging.Logger;

import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.XPath;
import org.dom4j.InvalidXPathException;

import epcc.xmldiff.io.XMLDiffUserDataElement;

import epcc.xmldiff.exception.SetUpException;
import epcc.xmldiff.exception.StructuralException;
import epcc.xmldiff.exception.ElementValueException;
import epcc.xmldiff.exception.WarningException;

import epcc.xmldiff.valuecomparator.Assertions;
import epcc.xmldiff.valuecomparator.Assertion;
import epcc.xmldiff.valuecomparator.ValueComparator;

/**
 * Compares the data of two documents as deciphered by a third 'metric' document.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class ElementValueComparator {
	
	/* Declare Logger instance to be used by this class only. */
	private static final Logger elementvalue_logger_ = 
										Logger.getLogger(ElementValueComparator.class.getName());
											
	/* Declare Assertions instance. */
	private static Assertions assertions_;
	
	/* Declare constant error messages. */
	private static final String SETUPERROR_INVALIDINPUT = "SETUP ERROR: Exception " +
								"thrown whilst retrieving XML document(s)";
	private static final String SETUPERROR = "SETUP ERROR ";
	private static final String ELEMENTVALUE_ERROR = "ELEMENT-VALUE ERROR ";
	private static final String ELEMENTVALUE_WARNING = "ELEMENT-VALUE WARNING ";
	
	private static final String XPATHNOTVALID = "Non-valid value for assertion attribute 'xpath' has been specified - ";					
	private static final String STRUCTURALERROR = "STRUCTURAL ERROR: Control and candidate documents do not equate structurally. " +
													"Element value comparisons terminated";					
	private static final String ELEMENTSNOTLEAF = "Elements returned by the following XPath expression are not leaf node elements - ";
	private static final String NOELEMENTS = "No elements were returned for XPath expression - ";								
	private static final String COMPLETION_RESULT = "Element value comparison completed with ";
								
	/* Declare constant information messages. */
	private static final String ELEMENTVALUE_COMPARISON = "ELEMENT-VALUE COMPARISON: ";				
	private static final String INFO_NOELEMENTSLEFT = "Reached end of control and candidate documents, no elements left to compare";						
	private static final String INFO_ELEMVALUEPASS = "Element value comparison completed without errors";
	
	/* Declare Document instances. */
	private Document control_doc;
	private Document candidate_doc;
	
	/* Declare Strings representing the names of each respective Document instance. */
	private String controlDoc_name;
	private String candidateDoc_name;
	private String metricDoc_name;
	
	private boolean no_elements_left;
	
	/* Declare integers to hold number of element value errors and warnings
	 * recorded. */
	private int error_counter;
	private int warning_counter;
		
	
	/**
	 * Initialises instance variables and initiates creation of 
	 * {@link Assertion Assertion} instances using the metric document.
	 * 
	 * @param control	Document used as comparison control, must not be
	 * 					<code>null</code>.
	 * @param candidate Document to be compared to comparison control, must 
	 * 					not be <code>null</code>.
	 * @param metric	Document containing instructions on how to compare
	 * 					the control and candidate documents, must not be
	 * 					<code>null</code>.
	 * @throws SetUpException If an error occurred during processing
	 * of metric document 'assertions' or the argument input is invalid 
	 * i.e. <code>null</code>.
	 */
	public ElementValueComparator(Document control, Document candidate, 
									Document metric) throws SetUpException {
		
		/* Check that the input is valid. */
		if(control != null && candidate != null && metric != null){
			
			control_doc = control;
			controlDoc_name = control_doc.getName();
		
			candidate_doc = candidate;
			candidateDoc_name = candidate_doc.getName();
		
			metricDoc_name = metric.getName();
		
			no_elements_left = false;
			error_counter = 0;
			warning_counter = 0;
		
			/* Create an Assertions instance and pass on the metric Document to 
		 	 * generate Assertion instances. */
			assertions_ = new Assertions(metric);
			
		}else {
			
			/* Throw a SetUpException to inform of invalid input. */
			throw new SetUpException(SETUPERROR_INVALIDINPUT);
		}
	}
	
	
	/**
	 * Iterates through control and candidate documents and compares the data of each
	 * as instructed by {@link Assertion Assertion} instances generated from the
	 * metric document. 
	 * @throws SetUpException If an error occurred attempting to retrieve data for comparison.
	 * @throws StructuralException If the amount of data retrieved from control and 
	 * candidate documents did not equate.
	 * @throws ElementValueException If any data comparison errors occurred.
	 * @throws WarningException If any data comparison warnings occurred.
	 */
	public void compare() throws SetUpException, StructuralException, 
									ElementValueException, WarningException {
		 
		/* Initialise Assertion and ValueComparator instances which will 
		 * be retrieved. */
		Assertion assertion = null; 			 
		ValueComparator value_comp = null; 
		
		/* Loop across all Assertion instances. Using each one, retrieve the 
		 * associated ValueComparator and pass to the compareElementValues method.*/
		for(int i=0; i<assertions_.size(); i++){
		 	
		 	assertion = (Assertion)assertions_.get(i);
		 	
		 	value_comp = assertion.getValueComparator();
				
			/* Next compare element values for all elements returned by the
			 * assertion XPath expression. */
			compareElementValues(assertion.getXPath(), 
										assertion.getAssertionLineNumber(), value_comp);
				
			if(no_elements_left) {
				
				/* If no elements are left to compare - inform the user and exit
				 * the loop. */
				elementvalue_logger_.info(ELEMENTVALUE_COMPARISON + INFO_NOELEMENTSLEFT);
				break; 
			}
		}
		
		/* If element value errors (and/or warnings) occurred during the comparison 
		 * process, retrieve these now and notify the user. */
		if(error_counter > 0){
			
			String message = ELEMENTVALUE_COMPARISON + COMPLETION_RESULT + 
							error_counter + " error(s)";
				
			/* Check that no warnings were logged. */
			if(warning_counter > 0){
				message = message + " and " + warning_counter + " warning(s)";
			}
			
			/* Throw an Exception to notify the error. */
			throw new ElementValueException(message);
				
		}else {
				
			/* If no element value errors occurred, check than no warnings 
			 * were logged. */
			if(warning_counter == 0){
					
				/* If no element value warnings occurred - log an INFO message 
				 * for the user that the comparsion completed cleanly. */
				elementvalue_logger_.info(ELEMENTVALUE_COMPARISON + INFO_ELEMVALUEPASS);
				
			}else {
					
				/* Warnings occurred so throw an Exception to notify the warnings. */				
				throw new WarningException(ELEMENTVALUE_COMPARISON + COMPLETION_RESULT + 
											warning_counter + " warning(s)");
			}
		}
	}
	
	
	/**
	 * Retrieves control and candidate document elements and compares their values
	 * using the {@link ValueComparator ValueComparator} argument. Any errors or warnings 
	 * are logged but element value comparison DOES NOT STOP if these occur.
	 * 
	 * @param xpath				String denoting XPath of the Elements for comparison,
	 * 							must not be <code>null</code>.
	 * @param assertion_lineNo	Integer denoting location of assertion from within the
	 * 							metric document.
	 * @param vc				{@link ValueComparator ValueComparator} instance to 
	 * 							execute the appropriate comparison operation, must not
	 * 							be <code>null</code>.
	 * @throws SetUpException If, for example, the XPath expression is invalid.
	 * @throws StructuralException If elements returned by XPath expressions are 
	 * not equal in number.
	 */
	private void compareElementValues(String xpath, int assertion_lineNo, ValueComparator vc) 
										throws SetUpException, StructuralException {
		
		/* First, attempt to generate XPaths using the control and candidate 
		 * documents and the XPath String specified by the user. */
		XPath control_xpath = null;
		XPath candidate_xpath = null;
		
		try{
			control_xpath = control_doc.createXPath(xpath); 
			candidate_xpath = candidate_doc.createXPath(xpath);	
			
		}catch(InvalidXPathException xpe){
			
			/* Throw a SetUpException - XPath expression is invalid. */
			throw new SetUpException(SETUPERROR + "[" + metricDoc_name + " line " +
									assertion_lineNo + "]: " + XPATHNOTVALID + xpath,
									xpe);
		}
		
		/* Next, retrieve all nodes returned by the XPath. */
		List control_nodes = control_xpath.selectNodes(control_doc);
		List candidate_nodes = candidate_xpath.selectNodes(candidate_doc);
			
		/* Catch potential errors - 
		 * (i)  If there have been no elements returned, warn the user and continue,
		 * (ii) If the numbers of elements returned do not equate, inform the user
		 *      and exit the application as a structural error has gone unnoticed. */
		if(control_nodes.size() == 0 || candidate_nodes.size() == 0){
				
			/* Log WARNING message for the user - no element nodes were retrieved. */
			elementvalue_logger_.warning(ELEMENTVALUE_WARNING + "[" + metricDoc_name + 
								" line " + assertion_lineNo + "]: " + NOELEMENTS + xpath);
			
			warning_counter++;// And increment warning counter.	
		}
		
		if(control_nodes.size() != candidate_nodes.size()){
				
			/* Throw a StructuralException - structural error has gone unnoticed. */
			throw new StructuralException(STRUCTURALERROR);
			
		}else {				
				 
			/* From each List, retrieve each element in turn and compare using the
			 * ValueComparator instance provided. */
			XMLDiffUserDataElement control_elem = null;
			Element control_parent = null;
				
			XMLDiffUserDataElement candidate_elem = null;
			Element candidate_parent = null;
				
			for(int i=0; i<control_nodes.size(); i++){
				
				try{
					
					control_elem = (XMLDiffUserDataElement)control_nodes.get(i);
					candidate_elem = (XMLDiffUserDataElement)candidate_nodes.get(i);
					
				}catch(ClassCastException ce){
				
					/* Throw a SetUpException - XPath expression is invalid if
					 * anything other than XMLDiffUserDataElements are returned. */
					throw new SetUpException(SETUPERROR + "[" + metricDoc_name + 
								" line " + assertion_lineNo + "]: " + 
								XPATHNOTVALID + xpath, ce);
				}
				
				boolean detach_nodes = true;
					
				/* Execute the compare operation on the elements. */
				if(!vc.compare(control_elem, candidate_elem)){
					
					String compare_errormsg = vc.getErrorMessage();
					
					/* If comparison failed because these elements were not leaf node
					 * elements - log a WARNING message and continue. */
					if(compare_errormsg.equals(vc.ELEMENTSNOTLEAF)){
						
						elementvalue_logger_.warning(ELEMENTVALUE_WARNING + "[" + 
									metricDoc_name + " line " + assertion_lineNo + 
									"]: " + ELEMENTSNOTLEAF + xpath);
									
						warning_counter++; 	 // And increment warning counter.
						detach_nodes = false;// Do not detach these nodes as not leaf.
						
					}else {
					
						
						/* An error occurred during comparison of element values, log 
					 	 * as a SEVERE message and continue. */
						elementvalue_logger_.severe(ELEMENTVALUE_ERROR + "[" +
								controlDoc_name + " line " + 
								control_elem.getLocation().getLineNumber() + ", " +
								candidateDoc_name + " line " +
								candidate_elem.getLocation().getLineNumber() + "]: " +
								compare_errormsg + "elements " + 
								control_elem.getPath() + ", " + candidate_elem.getPath());	
														
						error_counter++; // And increment error counter.
					}
				}
					
				/* To ensure elements are only compared by the assertion of
				 * highest precedence, remove leaf node elements following 
				 * comparison operation (whether they agreed or disagreed). */
				if(detach_nodes){
				
					detachElement(control_elem);
					detachElement(candidate_elem);
				}
			}
		}
	}
	
	
	/**
	 * Detaches argument doc_element from its location within the document 
	 * tree. Parent elements that in turn become leaf nodes through this 
	 * detachment are also removed in a recursive manner. 
	 * 
	 * @param doc_element 	Element to be detched from its location within the
	 * 						document tree.
	 */
	private void detachElement(Element doc_element){
		
		/* Retrieve the parent of this element first. */
		Element parent = doc_element.getParent();
		
		doc_element.detach();
		
		/* Next, check if the parent element has more child elements. 
		 * If not, remove the parent element as this is now a leaf node. 
		 * Continue test for all parent leaf nodes that result from each 
		 * element removal. 
		 */
		List element_siblings = parent.elements();
		
		if(element_siblings.size() == 0){
			
			/* This parent element is now a leaf node, call recursively
			 * UNLESS we have reached the root element in which case we have
			 * reached the end of the document. */
			if(!parent.isRootElement()){
				detachElement(parent);
			}else{
				no_elements_left = true;
			}
		}		
	}
}
