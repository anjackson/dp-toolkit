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
 * Implements the functionality required to execute the appropriate 
 * comparison operations on complex type element values.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class ComplexValueComparator extends ValueComparator {
	
	/* Declare constant error messages. */
	private static final String COMPARISONOPNOTVALID = "Non-valid value for " +
								"assertion attribute 'comparison' has been set for " +
								"the complex type - ";

	private static final String VALUENOTDOUBLE = "Element value(s) are not of the correct " +
									"'double' type. Comparison not executed on ";
																	
	private final String VALIDOPERATIONS = "[Supported comparison operations for the " +
									"complex type are:- " + ABSOLUTE_COMPARISON + 
									", " + ABSOLUTE_REAL_COMPARISON + ", " +
									ABSOLUTE_IMAGINARY_COMPARISON + ", " +
									RELATIVE_COMPARISON + ", " +
									RELATIVE_REAL_COMPARISON + ", " +
									RELATIVE_IMAGINARY_COMPARISON + " and " + 
									IGNORE +"]";
																											
	/* Declare String denoting what comparison operation has been specified. */							
	private String comparison_todo;
	
	/* Declare double denoting what level of tolerance has been specified. */
	private double comparison_tolerance; 
	
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
	protected ComplexValueComparator(String comparison_op, String tolerance) 
											throws SetUpException {	
		init(comparison_op, tolerance);
		error_message = "";
	}
	
	
	/**
	 * Validates the comparison_op argument against comparison operations allowed for 
	 * the complex type and confirms the tolerance argument is of a double type. 
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
		 * allowed for the complex type.*/
		if(comparison_op.equals(ABSOLUTE_COMPARISON) ||
			comparison_op.equals(ABSOLUTE_REAL_COMPARISON) ||
			comparison_op.equals(ABSOLUTE_IMAGINARY_COMPARISON) ||
			comparison_op.equals(RELATIVE_COMPARISON) ||
			comparison_op.equals(RELATIVE_REAL_COMPARISON) ||
			comparison_op.equals(RELATIVE_IMAGINARY_COMPARISON) ||
			comparison_op.equals(IGNORE)){
					
			comparison_todo = comparison_op;
					
			/* Next, unless the 'ignore' comparison operation has been requested, test
			 * that the tolerance provided is of a double type. */
			if(!comparison_todo.equals(IGNORE)){
						
				try{
					comparison_tolerance = StrictMath.abs(Double.parseDouble(tolerance));
				
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
	 * SPECIAL CASE: This method will return true only if there are exactly 2 child 
	 * leaf node elements for each Element argument.
	 * 
	 * @param ctrl_e	Element of control document, must not be <code>null</code>.
	 * @param cand_e	Element of candidate document, must not be <code>null</code>.
	 * @return			Whether these Elements are leaf nodes or not.
	 */
	protected boolean elementsAreLeafNodes(Element ctrl_e, Element cand_e){
		
		boolean leaf_nodes = true;	
				
		/* Attempt to retrieve child elements of the arguments passed. */
		List ctrl_e_kids = ctrl_e.elements();
		List cand_e_kids = cand_e.elements();
		
		/* First check that these elements have exactly 2 child elements - 
		 * as denoted for the special complex type. */
		if(ctrl_e_kids.size() == 2 && cand_e_kids.size() == 2){
			
			/* Next check that each child element is itself a leaf node. */			
			for(int i=0; i<ctrl_e_kids.size(); i++) {
				
				Element ctrl_e_kid = (Element)ctrl_e_kids.get(i);
				Element cand_e_kid = (Element)cand_e_kids.get(i);
				
				leaf_nodes = super.elementsAreLeafNodes(ctrl_e_kid, cand_e_kid);
			}
		}else {
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
	public boolean compare(Element ctrl_e, Element cand_e){
		
		boolean elements_compare = true;
		
		/* Check first the these Elements are leaf nodes. */
		if(elementsAreLeafNodes(ctrl_e, cand_e)){
			
			/* First, retrieve the child elements of these elements. */
			List ctrl_e_kids = ctrl_e.elements();
			List cand_e_kids = cand_e.elements();
			
			/* Retrieve first the real and imaginary child elements (we know
			 * there are exactly 2 child elements for each through use of the
			 * elementsAreLeafNodes method), then their values and pass these 
			 * to the appropriate method for comparison. */
			Element ctrl_r = (Element)ctrl_e_kids.get(0);
			Element ctrl_i = (Element)ctrl_e_kids.get(1);
			
			String ctrl_r_string = ctrl_r.getText();/* Ctrl real String value. */
			String ctrl_i_string = ctrl_i.getText();/* Ctrl imaginary String value. */
			
			Element cand_r = (Element)cand_e_kids.get(0);
			Element cand_i = (Element)cand_e_kids.get(1);
			
			String cand_r_string = cand_r.getText();/* Cand real String value. */
			String cand_i_string = cand_i.getText();/* Cand imaginary String value. */
			
			elements_compare = compare(ctrl_r_string, ctrl_i_string,
											cand_r_string, cand_i_string);
		
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
	 * Redundant in this class.
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
	 * Executes the requested comparison operation on the arguments passed. 
	 * Returns true if the values agree within tolerance and false if not.
	 * 
	 * @param ctrl_r	String value of control document 'real' element, must not 
	 * 					be <code>null</code>.
	 * @param ctrl_i	String value of control document 'imaginary' element, 
	 * 					must not be <code>null</code>.
	 * @param cand_r	String value of candidate document 'real' element, must 
	 * 					not be <code>null</code>. 
	 * @param cand_i	String value of candidate document 'imaginary' element, 
	 * 					must not be <code>null</code>. 
	 * @return			Results of the comparison.
	 */
	private boolean compare(String ctrl_r, String ctrl_i, String cand_r, 
							String cand_i){
		
		boolean elements_compare = true;
		 
		double ctrl_re = 0.0; 	/* Real value (control). */
		double ctrl_img = 0.0; 	/* Imaginary value (control). */
			
		double cand_re = 0.0; 	/* Real value (candidate). */
		double cand_img = 0.0; 	/* Imaginary value (candidate). */
				
		try{
			
			ctrl_re = valueValid(ctrl_r);
			ctrl_img = valueValid(ctrl_i);
			
			cand_re = valueValid(cand_r);
			cand_img = valueValid(cand_i); 
		
		}catch(NumberFormatException nfe){
			
			error_message = nfe.getMessage();
			elements_compare = false;
		}
		
		/* Continue if values are of the double type. */
		if(elements_compare){
			
			/* Depending on what comparison operation has been specified, 
			 * call the appropriate comparison method. */
			if(comparison_todo.equals(ABSOLUTE_COMPARISON)){
				
				elements_compare = absoluteComparison(ctrl_re, ctrl_img, cand_re, cand_img);
															
			}else if(comparison_todo.equals(ABSOLUTE_REAL_COMPARISON)){
		
				elements_compare = absoluteTypeComparison(ctrl_re, cand_re);	
																
			}else if(comparison_todo.equals(ABSOLUTE_IMAGINARY_COMPARISON)){
		
				elements_compare = absoluteTypeComparison(ctrl_img, cand_img);
																	
			}else if(comparison_todo.equals(RELATIVE_COMPARISON)){
		
				elements_compare = relativeComparison(ctrl_re, ctrl_img, cand_re, cand_img);
												
			}else if(comparison_todo.equals(RELATIVE_REAL_COMPARISON)){
		
				elements_compare = relativeTypeComparison(ctrl_re, cand_re);
				
																
			}else if(comparison_todo.equals(RELATIVE_IMAGINARY_COMPARISON)){
		
				elements_compare = relativeTypeComparison(ctrl_img,	cand_img);
				
			}else{
				
				elements_compare = ignore();
			}
		}		
		return elements_compare;
	}
	
	
	/**
	 * Returns true if the control and candidate element values are in agreement
	 * and false if not.
	 * @param ctrl_r	Double value of control document 'real' element.
	 * @param ctrl_i	Double value of control document 'imaginary' element.
	 * @param cand_r	Double value of candidate document 'real' element.
	 * @param cand_i	Double value of candidate document 'imaginary' element.
	 * @return			Result of absolute comparison operation.
	 */
	private boolean absoluteComparison(double ctrl_r, double ctrl_i, double cand_r, 
										double cand_i){
		
		boolean absolute_result = true;
				
		/* Retrieve the absolute difference between two values. */
		double real_squared = (ctrl_r - cand_r) * 
									(ctrl_r - cand_r);
		
		double imag_squared = (ctrl_i - cand_i) * 
									(ctrl_i - cand_i);
		
		double comparison_result = real_squared + imag_squared;
		comparison_result = StrictMath.sqrt(comparison_result);
		
		/* Return whether the difference is within the tolerance allowance. */  
		if(Double.isNaN(comparison_result) || Double.isInfinite(comparison_result) ||
				comparison_result >= comparison_tolerance){
		
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
	 * @param ctrl		Double value of control document 'real' or 'imaginary' 
	 * 					element.
	 * @param cand		Double value of candidate document 'real' or 'imaginary'
	 * 					element.
	 * @return			Result of absolute comparison operation.
	 */
	private boolean absoluteTypeComparison(double ctrl, double cand){
		
		boolean absolute_result = true;
		
		/* Retrieve the absolute difference between the two values. */
		double comparison_result = StrictMath.abs(ctrl - cand);
		
		/* Return whether the difference is within the tolerance allowance. */  
		if(Double.isNaN(comparison_result) || Double.isInfinite(comparison_result) ||
				comparison_result >= comparison_tolerance){
	
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
	 * @param ctrl_r	Double value of control document 'real' element.
	 * @param ctrl_i	Double value of control document 'imaginary' element.
	 * @param cand_r	Double value of candidate document 'real' element.
	 * @param cand_i	Double value of candidate document 'imaginary' element.
	 * @return			Result of relative comparison operation.
	 */
	private boolean relativeComparison(double ctrl_r, double ctrl_i, double cand_r, 
											double cand_i){
			
		boolean relative_result = true;
		
		/* Check that values are not 'zero', if they are treat as a special case. */
		double zero = (double)ABSOLUTE_ZERO;
		if(!(Double.compare(StrictMath.abs(ctrl_r), zero) == 0 && 
				Double.compare(StrictMath.abs(cand_r), zero) == 0 &&
				Double.compare(StrictMath.abs(ctrl_i), zero) == 0 && 
				Double.compare(StrictMath.abs(cand_i), zero) == 0)){
		
			/* Retrieve the relative difference between two values. */
			double real_squared = (ctrl_r - cand_r) * 
									(ctrl_r - cand_r);
		
			double imag_squared = (ctrl_i - cand_i) * 
									(ctrl_i - cand_i);
		
			double top_equation = real_squared + imag_squared;
			top_equation = StrictMath.sqrt(top_equation);
		
			double control_bottom_equation = (ctrl_r * ctrl_r) + 
												(ctrl_i * ctrl_i);
			control_bottom_equation = StrictMath.sqrt(control_bottom_equation);
	
			double candidate_bottom_equation = (cand_r * cand_r) +
													(cand_i * cand_i);
			candidate_bottom_equation = StrictMath.sqrt(candidate_bottom_equation);
		
		
			double comparison_result = top_equation / 
										StrictMath.max(control_bottom_equation, 
														candidate_bottom_equation);							  
							  
			/* Return whether the difference is within the tolerance allowance. */  
			if(Double.isNaN(comparison_result) || Double.isInfinite(comparison_result) ||
				comparison_result >= comparison_tolerance){
		
				/* Set error message - relative comparison operation failed. */
				error_message = RELATIVECOMPERROR;
				relative_result = false;
			}
		}
		return relative_result;
	}
	
	
	/**
	 * Returns true if the control and candidate element values are in agreement
	 * and false if not.
	 * 
	 * @param ctrl		Double value of control document 'real' or 'imaginary' 
	 * 					element.
	 * @param cand		Double value of candidate document 'real' or 'imaginary'
	 * 					element.
	 * @return			Result of relative comparison operation.
	 */
	private boolean relativeTypeComparison(double ctrl, double cand) {
		
		boolean relative_result = true;
		
		/* Check that both values are not 'zero', if they are treat as a special case. */
		double zero = (double)ABSOLUTE_ZERO;
		if(!(Double.compare(StrictMath.abs(ctrl), zero) == 0 && 
				Double.compare(StrictMath.abs(cand), zero) == 0)){
				
			/* Retrieve the relative difference between the two values. */
			double comparison_result = StrictMath.abs(ctrl - cand) / 
											StrictMath.max(StrictMath.abs(ctrl),
														StrictMath.abs(cand));
							  
			/* Return whether the difference is within the tolerance allowance. */  
			if(Double.isNaN(comparison_result) || Double.isInfinite(comparison_result) ||
				comparison_result >= comparison_tolerance){
		
				/* Set error message - relative comparison operation failed. */
				error_message = RELATIVECOMPERROR;
				relative_result = false;
			}
		}
		return relative_result;
	}
	
	
	/**
	 * Confirms that the value argument passed is of an accepted double number type.
	 * 
	 * @param value		String value to be parsed to a double.
	 * @return			Double value of the string argument.
	 * @throws NumberFormatException If any errors occurred during parsing of the
	 * string argument.
	 */
	private double valueValid(String value) throws NumberFormatException {

		double double_value = 0; /* To be returned. */
	
		try{
			
			double_value = Double.parseDouble(value);
		
		}catch(NumberFormatException nfe){
			
			/* Unless the 'ignore' comparison operation has been requested, check that 
			 * the value is actually present and display the appropriate error message 
			 * if it is not, else the value are of an invalid Double format. */
			if(!comparison_todo.equals(IGNORE)){
				
				if(value.equals("")){
					
					/* Throw a NumberFormatException to notify the calling method. */
					throw new NumberFormatException(NOVALUE);
		
				}else {
					
					/* Throw a NumberFormatException to notify the calling method. */
					throw new NumberFormatException(VALUENOTDOUBLE);
				}
			}
		}
		
		/* Unless the 'ignore' comparison operation has been requested, check
		 * that this double value is neither a NaN nor an Infinite number. */
		if(!comparison_todo.equals(IGNORE)){
				
			if(Double.isNaN(double_value)){
					
				/* Throw a NumberFormatException to notify the calling method. */
				throw new NumberFormatException(NANVALUE);
					
			}else if(Double.isInfinite(double_value)){
					
				/* Throw a NumberFormatException to notify the calling method. */
				throw new NumberFormatException(INFINITEVALUE);
			}
		}
		return double_value;
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
