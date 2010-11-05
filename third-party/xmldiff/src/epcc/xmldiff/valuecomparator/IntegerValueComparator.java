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
 * comparison operations on integer type element values.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class IntegerValueComparator extends ValueComparator {
	
	/* Declare constant error messages. */
	private static final String COMPARISONOPNOTVALID = "Non-valid value for " +
								"assertion attribute 'comparison' has been set for " +
								"the integer type - ";

	private static final String VALUENOTINT = "Element value(s) are not of the correct " +
									"'integer' type. Comparison not executed on ";
										
	private final String VALIDOPERATIONS = "[Supported comparison operations for the " +
									"integer type are:- " + EXACT_EQUALITY + ", " +
									ABSOLUTE_COMPARISON + ", " +
									RELATIVE_COMPARISON + " and " + 
									IGNORE + "]";
									
	/* Declare String denoting what comparison operation has been specified. */							
	private String comparison_todo;
	
	/* Declare Number denoting what level of tolerance has been specified (could be
	 * double or could be integer). */
	private Number comparison_tolerance;
	
	/* Declare String variable used to store error messages. */
	private String error_message;	
	
		
	/**
	 * Initialises instance variables.
	 * 
	 * @param comparison_op		Comparison operation as specified by user, must not
	 * 							be <code>null</code>.
	 * @param tolerance			Tolerance as specified by user.
	 * @throws SetUpException Detailing error that occurred during initialisation of an
	 * instance of this class.
	 */
	protected IntegerValueComparator(String comparison_op, String tolerance) 
														throws SetUpException {
		init(comparison_op, tolerance);
		error_message = "";
	}
	
	
	/**
	 * Validates the comparison_op argument against comparison operations allowed for 
	 * the integer type and confirms the tolerance argument is of an integer type.
	 * 
	 * @param comparison_op		Comparison operation as specified by user, must not
	 * 							be <code>null</code>.
	 * @param tolerance			Tolerance as specified by user.
	 * @throws SetUpException Detailing error that occurred during initialisation of an
	 * instance of this class.
	 */
	private void init(String comparison_op, String tolerance) throws SetUpException {
		
		/* Call parent isComparisonValid method first for general checking - 
		 * if an erroneous comparison operation has been set for this type, 
		 * throw exception and provide list of valid comparison operations. */
		try {
			
			isComparisonValid(comparison_op);
			
		}catch(SetUpException sue) {
			
			throw new SetUpException(COMPARISONOPNOTVALID + comparison_op +
							System.getProperty("line.separator") + VALIDOPERATIONS);
		}
			
		/* If valid, next check comparison_op against operations 
		 * allowed for the integer type.*/
		if(comparison_op.equals(EXACT_EQUALITY) ||
			comparison_op.equals(ABSOLUTE_COMPARISON) ||
			comparison_op.equals(RELATIVE_COMPARISON) ||
			comparison_op.equals(IGNORE)){
			
			comparison_todo = comparison_op;
				
			/* Next, unless the 'ignore' or 'equality' comparison operations have
			 * been requested, test that the tolerance provided is of the correct type
			 * for the comparison specified. */
			if(!comparison_todo.equals(IGNORE) && 
				!comparison_todo.equals(EXACT_EQUALITY)){
					
				try {
					/* If a relative comparison operation has been specified,
					 * the tolerance must be a double type, else it is an
					 * integer. */
					if(comparison_todo.equals(RELATIVE_COMPARISON)){
						comparison_tolerance = new Double(tolerance);
					}else {
						comparison_tolerance = new Integer(tolerance);
					}	
				}catch(NumberFormatException nfe){
						
					/* Throw new SetUpException - tolerance specified is not valid 
					 * for this type. */
					throw new SetUpException(TOLERANCENOTVALID + tolerance, nfe);		
				}
			}
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
		
		/* Check first the these Elements are leaf nodes. */
		if(elementsAreLeafNodes(ctrl_e, cand_e)){
		
			String ctrl_string = ctrl_e.getText();
			String cand_string = cand_e.getText();
			
			elements_compare = compare(ctrl_string, cand_string);
		
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
	 * 
	 * @param ctrl_s	String value of control document, must not be <code>null</code>.
	 * @param cand_s	String value of candidate document to be compared to param 
	 * 					ctrl_s,	must not be <code>null</code>.
	 * @return			Results of the comparison.
	 */
	protected boolean compare(String ctrl_s, String cand_s){
		
		boolean elements_compare = true;
		 
		int ctrl_int = 0; 
		int cand_int = 0; 
		
		try{			
			ctrl_int = valueValid(ctrl_s);
			cand_int = valueValid(cand_s);
			
		}catch(NumberFormatException nfe){
			
			error_message = nfe.getMessage();
			elements_compare = false;
		}
		
		/* Continue if values are of an accepted int type. */
		if(elements_compare){
			
			/* Depending on what comparison operation has been specified, 
			 * call the appropriate comparison method. */
			if(comparison_todo.equals(EXACT_EQUALITY)){
				
				elements_compare = equalityComparison(ctrl_int, cand_int);
				
			}else if(comparison_todo.equals(ABSOLUTE_COMPARISON)){
				
				elements_compare = absoluteComparison(ctrl_int, cand_int);
				
			}else if(comparison_todo.equals(RELATIVE_COMPARISON)){
				
				elements_compare = relativeComparison(ctrl_int, cand_int);
				
			}else{
				
				elements_compare = ignore();
			}
		}
		return elements_compare;
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
	private boolean equalityComparison(int ctrl, int cand){
		
		boolean equality_result = true;
		
		/* Decipher whether two values equate exactly. */
		if(ctrl != cand){
			
			/* Set error message - equality comparison operation failed. */
			error_message = EQUALITYCOMPERROR;
			equality_result = false;
		}
		return equality_result;
	}
	
	
	/**
	 * Returns true if the control and candidate element values are in agreement
	 * and false if not.
	 * 
	 * @param ctrl	Integer value of control element.
	 * @param cand	Integer value of candidate element to be compared to value of
	 * 				control element.
	 * @return		Result of absolute comparison operation.
	 */
	private boolean absoluteComparison(int ctrl, int cand){
		
		boolean absolute_result = true;

		/* Retrieve the absolute difference between two values. */		
		int comparison_result = StrictMath.abs(ctrl - cand);
		
		/* Cast the tolerance Number and retrieve the (non-negative)
		 * int value for comparison. */
		int absolute_tolerance = StrictMath.abs(comparison_tolerance.intValue());
		
		/* Return whether the difference is within the tolerance allowance. */  
		if(comparison_result >= absolute_tolerance){
			
			/* Set error message - absolute comparison operation failed. */
			error_message = ABSOLUTECOMPERROR;
			absolute_result = false;
		}
		return absolute_result;
	}
	
	
	/**
	 * Returns true if the control and candidate element values are in agreement
	 * and false if not.
	 * 
	 * @param ctrl	Integer value of control element.
	 * @param cand	Integer value of candidate element to be compared to value of
	 * 				control element.
	 * @return		Result of relative comparison operation.
	 */
	private boolean relativeComparison(int ctrl, int cand){
		
		boolean relative_result = true;
		
		/* Retrieve the relative difference between two values. */
		double ctrl_double = (double)ctrl;
		double cand_double = (double)cand;
	
		/* Check that both values are not zero, if they are treat
		 * as a special case. */
		double zero = (double)ABSOLUTE_ZERO;
		if(!(Double.compare(StrictMath.abs(ctrl_double), zero) == 0 && 
				Double.compare(StrictMath.abs(cand_double), zero) == 0)){
		
			/* Check if the double numbers generated are either NaN or
			 * Infinite values - if they are, comparison should not take
			 * place. */
			if(Double.isNaN(ctrl_double) || Double.isNaN(cand_double)){
				
				error_message = NANVALUE;
				relative_result = false; 
				
			}else if(Double.isInfinite(ctrl_double) || Double.isInfinite(cand_double)){
				
				error_message = INFINITEVALUE;
				relative_result = false;
				
			}else {
			
			
				double comparison_result = StrictMath.abs(ctrl_double - cand_double) / 
										StrictMath.max(StrictMath.abs(ctrl_double),
														StrictMath.abs(cand_double));
		
				/* Cast the tolerance Number and retrieve the (non-negative)
		 	 	 * double value for comparison. */
				double relative_tolerance = 
									StrictMath.abs(comparison_tolerance.doubleValue());
							
				/* Return whether the difference is within the tolerance allowance. */  
				if(Double.isNaN(comparison_result) || Double.isInfinite(comparison_result) ||
					comparison_result >= relative_tolerance){
				
					/* If i) a NaN has resulted from the relative comparison operation,
				 	* or ii) the values failed in the relative comparison - notify the user. */
					error_message = RELATIVECOMPERROR;
					relative_result = false; 		
				}
			}
		}
		return relative_result;
	}
	
	
	/**
	 * Confirms that the value argument passed is of an accepted integer number type.
	 * 
	 * @param value		String value to be parsed to an integer.
	 * @return			Integer value of the string argument.
	 * @throws NumberFormatException If any errors occurred during parsing of the
	 * string argument.
	 */
	private int valueValid(String value) throws NumberFormatException {

		int int_value = 0; /* To be returned. */
	
		try{
			
			int_value = Integer.parseInt(value);
		
		}catch(NumberFormatException nfe){
			
			/* Unless the 'ignore' comparison operation has been requested, check that 
			 * the value is actually present and display the appropriate error message 
			 * if it is not, else the value are of an invalid Integer format. */
			if(!comparison_todo.equals(IGNORE)){
				
				if(value.equals("")){
					
					/* Throw a NumberFormatException to notify the calling method. */
					throw new NumberFormatException(NOVALUE);
		
				}else {
					
					/* Throw a NumberFormatException to notify the calling method. */
					throw new NumberFormatException(VALUENOTINT);
				}
			}
		}
		return int_value;
	}
	
		
	/**
	 * Returns String denoting the comparison operation carried out by an instance 
	 * of this class.
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
