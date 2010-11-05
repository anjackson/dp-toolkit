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

import java.util.List;

import org.dom4j.Element;

import epcc.xmldiff.exception.SetUpException;

/**
 * Implements the common functionality shared among all ValueComparator 
 * implementation classes.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public abstract class ValueComparator {
	
	/* Declare String constants used to validate comparison operations as specified. */
	protected static final String ABSOLUTE_COMPARISON = "absolute";
	protected static final String RELATIVE_COMPARISON = "relative";
	protected static final String EXACT_EQUALITY = "equality";
	protected static final String ABSOLUTE_REAL_COMPARISON = "absolute_real";
	protected static final String ABSOLUTE_IMAGINARY_COMPARISON = "absolute_imaginary";
	protected static final String RELATIVE_REAL_COMPARISON = "relative_real";
	protected static final String RELATIVE_IMAGINARY_COMPARISON = "relative_imaginary";
	protected static final String IGNORE = "ignore";
	
	/* Declare variables used to compare values against for 'zero' special case. */
	protected static final int ABSOLUTE_ZERO = 0;
	
	/* Declare constant error messages. */	
	protected static final String NANVALUE = "NaN element value(s) located. Comparison not executed on ";					
	protected static final String INFINITEVALUE = "Infinite element value(s) located. Comparison not executed on ";
	protected static final String NOVALUE = "Element value(s) missing. Comparison not executed on ";
	protected static final String TOLERANCENOTVALID = "Non-valid value for assertion " +
										"attribute 'tolerance' has been specified - ";
										
	protected static final String ABSOLUTECOMPERROR = "Absolute comparison failed for ";							
	protected static final String RELATIVECOMPERROR = "Relative comparison failed for ";
	protected static final String EQUALITYCOMPERROR = "Equality comparison failed for ";
	
	public final String ELEMENTSNOTLEAF = "Elements are not leaf node elements";
	
	
	/**
	 * Empty constructor.
	 */
	protected ValueComparator(){
	}
	
	
	/**
	 * Validates the comparison_operation argument against all allowed comparison
	 * operations as defined within an instance of this class. 
	 * 
	 * @param comparison_operation		Comparison operation as specified by user,
	 * 									must not be <code>null</code>.
	 * @throws SetUpException If the comparison_operation argument is not recognised.
	 */
	protected void isComparisonValid(String comparison_operation) throws SetUpException {
		
		/* Test whether the comparison operation provided is valid - if its not,
		 * throw a new SetUpException. */
		if(!comparison_operation.equals(ABSOLUTE_COMPARISON) &&
			!comparison_operation.equals(RELATIVE_COMPARISON) && 
			!comparison_operation.equals(EXACT_EQUALITY) && 
			!comparison_operation.equals(ABSOLUTE_REAL_COMPARISON) && 
			!comparison_operation.equals(ABSOLUTE_IMAGINARY_COMPARISON) &&
			!comparison_operation.equals(RELATIVE_REAL_COMPARISON) && 
			!comparison_operation.equals(RELATIVE_IMAGINARY_COMPARISON) &&
			!comparison_operation.equals(IGNORE)){
				
				throw new SetUpException(comparison_operation);
		}
	}
	
	
	/**
	 * This comparison operation will always return true.
	 * 
	 * @return	True always.
	 */
	protected boolean ignore(){
		return true;
	}
	
	
	/**
	 * Returns true if the element arguments are leaf nodes and false if not.
	 * 
	 * @param ctrl_e	Element of control document, must not be <code>null</code>.
	 * @param cand_e	Element of candidate document, must not be <code>null</code>.
	 * @return			Whether these elements are leaf nodes or not.
	 */
	protected boolean elementsAreLeafNodes(Element ctrl_e, Element cand_e){
		
		boolean leaf_nodes = true;
		
		/* Attempt to retrieve child elements of the arguments passed
		 * and depending on what (if anything) is returned, return 
		 * appropriately. */
		List ctrl_e_kids = ctrl_e.elements();
		List cand_e_kids = cand_e.elements();
		
		if(ctrl_e_kids.size() != 0 || cand_e_kids.size() != 0){
			
			leaf_nodes = false;
		}
		return leaf_nodes;
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
	public abstract boolean compare(Element ctrl_e, Element cand_e);
	
	
	/**
	 * Executes the requested comparison operation on the arguments passed. 
	 * Returns true if the values agree within tolerance and false if not.
	 * 
	 * @param ctrl_s	String value of control document, must not be <code>null</code>.
	 * @param cand_s	String value of candidate document to be compared to 
	 * 					param ctrl_s, must not be <code>null</code>.
	 * @return			Results of the comparison.
	 */
	protected abstract boolean compare(String ctrl_s, String cand_s);
	
	
	/**
	 * Returns String denoting the comparison operation carried out by the
	 * implementing ValueComparator instance.
	 * 
	 * @return		String denoting the comparison operation carried out.
	 */
	protected abstract String getComparisonOperation();
	
	
	/**
	 * Returns String message detailing error(s) that occurred during comparison
	 * operations.
	 * 
	 * @return	String detailing error(s) that occurred during comparison 
	 * 			operations.
	 */
	public abstract String getErrorMessage();
}
