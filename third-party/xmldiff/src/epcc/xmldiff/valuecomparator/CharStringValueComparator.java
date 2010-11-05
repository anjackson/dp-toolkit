/*
 * Created on May 5, 2004
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

import org.dom4j.Element;

import epcc.xmldiff.exception.SetUpException;

/**
 * Implements the functionality required to execute the appropriate 
 * comparison operations on String type element values.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class CharStringValueComparator extends ValueComparator {
	
	/* Declare constant error messages. */
	private static final String COMPARISONOPNOTVALID = "Non-valid value for " +
								"assertion attribute 'comparison' has been set for " +
								"the String type - ";
								
	private final String VALIDOPERATIONS = "[Supported comparison operations for the " +
									"string type are:- " + EXACT_EQUALITY + 
									" and " + IGNORE + "]";																
	
	/* Declare String denoting what comparison operation has been specified. */							
	private String comparison_todo;
	
	/* Declare String variable used to store error messages. */
	private String error_message;	
		
		
	/**
	 * Initialises instance variables.
	 * 
	 * @param comparison_op		Comparison operation as specified by user, must not
	 * 							be <code>null</code>.
	 * @throws SetUpException Detailing error that occurred during initialisation of an
	 * instance of this class.
	 */
	protected CharStringValueComparator(String comparison_op) throws SetUpException {
		
		init(comparison_op);
		error_message = "";	
	}
	
	
	/**
	 * Validates the comparison_op argument against comparison operations allowed for 
	 * the String type. 
	 * 
	 * @param comparison_op		Comparison operation as specified by user, must not
	 * 							be <code>null</code>.
	 * @throws SetUpException Detailing error that occurred during initialisation of an
	 * instance of this class.
	 */
	private void init(String comparison_op) throws SetUpException {
		
		/* Call parent isComparisonValid method first for general checking - 
		 * if an erroneous comparison operation has been set for this type, 
		 * throw exception and provide list of valid comparison operations. */
		try {
			
			isComparisonValid(comparison_op);
			
		}catch(SetUpException sue) {
			
			throw new SetUpException(COMPARISONOPNOTVALID + comparison_op +
							System.getProperty("line.separator") + VALIDOPERATIONS);
		}
			
		/* If valid, next check comparison_op against operations allowed for the 
		 * String type.*/
		if(comparison_op.equals(EXACT_EQUALITY) ||
			comparison_op.equals(IGNORE)){
					
			comparison_todo = comparison_op;
				
		}else {
				
			/* Throw new SetUpException - comparison operation specified is not 
			 * valid for this type. */
			 throw new SetUpException(COMPARISONOPNOTVALID + comparison_op +
							 System.getProperty("line.separator") + VALIDOPERATIONS);
		}
	}
	
	
	/**
	 * Retrieves the values of the Element arguments and executes the requested
	 * comparison operation. Returns true if the element values agree within 
	 * tolerance and false if not.
	 * 
	 * @param ctrl_e	Element of control document, must not be <code>null</code>.
	 * @param cand_e	Element of candidate document to be compared to param ctrl_e, 
	 * 					must not be <code>null</code>.
	 * @return			Results of the comparison.
	 */
	public boolean compare(Element ctrl_e, Element cand_e){
		
		boolean elements_compare = true;
		
		/* Check first the these elements are leaf nodes. */
		if(elementsAreLeafNodes(ctrl_e, cand_e)){
		
			String ctrl_string = ctrl_e.getText();
			String cand_string = cand_e.getText();
			
			/* Depending on what comparison operation has been specified, 
		 	 * pass the values to the appropriate comparison method. */
			if(comparison_todo.equals(EXACT_EQUALITY)){
				
				elements_compare = equalityComparison(ctrl_string, cand_string);
				
			}else{
				
				elements_compare = ignore();
			}		
		}else {
			
			/* Elements are not leaf nodes, comparison not executed. */
			error_message = ELEMENTSNOTLEAF;
			elements_compare = false;
		}
		return elements_compare;
	}
	
	
	/**
	 * Executes the requested comparison operation on the arguments passed. 
	 * Returns true if the values agree within tolerance and false if not.
	 * Not implemented in this class.
	 * 
	 * @param ctrl_s	String value of control document, must not be <code>null</code>.
	 * @param cand_s	String value of candidate document to be compared to param 
	 * 					ctrl_s, must not be <code>null</code>.
	 * @return			Results of the comparison.
	 */
	protected boolean compare(String ctrl_s, String cand_s){
		return true;
	}
	
	
	/**
	 * Returns true if the control and candidate element values are in agreement
	 * and false if not.
	 * 
	 * @param ctrl	Integer value of control element.
	 * @param cand	Integer value of candidate element to be compared to value of
	 * 				control element.
	 * @return		Result of exact equality comparison operation.
	 */
	private boolean equalityComparison(String ctrl, String cand){
	
		boolean equality_result = true;
		
		/* Return whether the two values are exactly equal. */  
		if(!ctrl.equals(cand)){
				
			/* Set error message - equality comparison operation failed. */
			error_message = EQUALITYCOMPERROR;
			equality_result = false;
		}
		return equality_result;
	}
	
	
	/**
	 * Returns String denoting the comparison operation carried out by an
	 * instance of this class.
	 * 
	 * @return		String denoting the comparison operation carried out.
	 */
	protected String getComparisonOperation(){
		return comparison_todo;
	}
	
	
	/**
	 * Returns String message detailing error(s) that occurred during comparison
	 * operations.
	 * 
	 * @return	String detailing error(s) that occurred during comparison 
	 * 			operations.
	 */
	public String getErrorMessage(){
		return error_message;		
	}
}
