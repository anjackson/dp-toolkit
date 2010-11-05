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

import java.util.StringTokenizer;

import org.dom4j.Element;

/**
 * Implements the functionality required to execute the appropriate 
 * comparison operations on arrays of type-specified element values.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class ArrayTypeComparator extends ValueComparator {
	
	/* Declare constant error messages. */
	private static final String ARRAYNMBNOTVALID = "Array values not equal in number. " +
												"Comparison operation not executed on ";
	private static final String SUFFIX = "of ";
	
	/* Declare type of ValueComparator to be used across the array. */
	private ValueComparator comparator_type;
	
	/* Declare String variable used to store error messages. */
	private String error_message;	
	
		
	/**
	 * Initialises instance variables.
	 * 
	 * @param comparator		{@link ValueComparator ValueComparator} instance that 
	 * 							will be executing all comparison operations requested 
	 * 							of this instance, must not be <code>null</code>.
	 */
	protected ArrayTypeComparator(ValueComparator comparator) {
		
		comparator_type = comparator;
		error_message = "";
	}
		
		
	/**
	 * Retrieves the values of the Element arguments and executes the requested
	 * comparison operation utilising the specific 
	 * {@link ValueComparator ValueComparator} instance. Returns true if the 
	 * element values agree within tolerance and false if not.
	 * 
	 * @param ctrl_e	Element of control document, must not be <code>null</code>.
	 * @param cand_e	Element of candidate document to be compared to param ctrl_e, 
	 * 					must not be <code>null</code>.
	 * @return			Results of the comparison of all values.
	 */
	public boolean compare(Element ctrl_e, Element cand_e){
		
		boolean elements_compare = true;
		
		/* Check first the these elements are leaf nodes. */
		if(elementsAreLeafNodes(ctrl_e, cand_e)){

			/* Use StringTokenizers to chop up the String values returned from
		 	 * each element. */
			StringTokenizer control_tokenizer = new StringTokenizer(ctrl_e.getText());
			StringTokenizer candidate_tokenizer = new StringTokenizer(cand_e.getText());
		
			/* First, check we have the same number of values in each array. */
			if(control_tokenizer.countTokens() == candidate_tokenizer.countTokens()){
		
				/* Declare and initialise a StringBuffer to log individual array value 
			 	 * comparison errors, an error counter and a boolean to denote individual 
			 	 * array value errors. */
				StringBuffer array_errors = new StringBuffer("");
				int array_error_counter = 0;
				boolean array_compare = true;
			
				String control_token = "";
				String candidate_token = "";
			
				if(!(control_tokenizer.countTokens() == 0 && 
							candidate_tokenizer.countTokens() == 0)){
						
					/* Retrieve each array value and pass to the ValueComparator
				 	 * assigned to this instance for comparison. */
					while(control_tokenizer.hasMoreTokens()){
				
						control_token = control_tokenizer.nextToken();
						candidate_token = candidate_tokenizer.nextToken();
				
						array_compare = compare(control_token, candidate_token);
			
						/* If the value comparison has failed, retrieve the error message 
					 	 * (otherwise will be lost), increment the array error counter and 
					 	 * continue. */
						if(!array_compare){
					
							if(array_error_counter == 0){
								array_errors.append("values: ");
							}
							array_errors.append("[" + control_token + ", " + 
													candidate_token + "] ");		
							array_error_counter++; /* And increment error counter. */
						}
					}
			
					/* If a value comparison amongst the arrays has failed, assign the error
				 	 * message appropriately and the boolean to be returned. */
					if(array_error_counter > 0){
					
						/* If more than 1 error has occurred within the array, simply
					 	 * display a generic failure message. Else, display the specific
					 	 * message. */
						if(array_error_counter > 1){
						
							String op_performed = comparator_type.getComparisonOperation();
						
							/* Firstly, decipher what comparison operation was performed
						 	 * and assign the associated generic error message. */
							if(op_performed.equals(ABSOLUTE_COMPARISON) ||
								op_performed.equals(ABSOLUTE_REAL_COMPARISON) ||
								op_performed.equals(ABSOLUTE_IMAGINARY_COMPARISON)){
								
								error_message = ABSOLUTECOMPERROR;
							
							}else if(op_performed.equals(RELATIVE_COMPARISON) ||
								op_performed.equals(RELATIVE_REAL_COMPARISON) ||
								op_performed.equals(RELATIVE_IMAGINARY_COMPARISON)){
									
								error_message = RELATIVECOMPERROR;
							
							}else{
							
								error_message = EQUALITYCOMPERROR;
							}
						
							/* Include the String listing values that failed. */
							error_message = error_message + array_errors.toString() + 
											SUFFIX;
						}else {
						
							/* Only 1 error occurred, retrieve the specific error 
						 	 * message. */
							error_message = comparator_type.getErrorMessage() +
											array_errors.toString() + SUFFIX;
						}
						elements_compare = false;
					}
				}else{
				
					/* If no values are present in the arrays, pass empty tokens to
				 	 * the ValueComparator to generate the appropriate error message. */					
					array_compare = comparator_type.compare(control_token, 
																candidate_token);
				
					if(!array_compare){
					
						/* Set error message - comparison operation failed. */
						error_message = comparator_type.getErrorMessage();
						elements_compare = false;		
					}			
				}			
			}else {
			
				/* Set error message - number of values in each array different. */
				error_message = ARRAYNMBNOTVALID;
				elements_compare = false;
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
	 * 
	 * @param ctrl_s	String value of control document, must not be <code>null</code>.
	 * @param cand_s	String value of candidate document to be compared to param 
	 * 					ctrl_s, must not be <code>null</code>.
	 * @return			Results of the comparison.
	 */
	protected boolean compare(String ctrl_s, String cand_s){
		return comparator_type.compare(ctrl_s, cand_s);
	}
	
	
	/**
	 * Returns String denoting the comparison operation carried out by the 
	 * implementing {@link ValueComparator ValueComparator} instance.
	 * 
	 * @return		String denoting the comparison operation carried out.
	 */
	protected String getComparisonOperation(){
		return comparator_type.getComparisonOperation();
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