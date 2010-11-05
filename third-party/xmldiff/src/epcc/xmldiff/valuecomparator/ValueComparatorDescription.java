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

/**
 * Stores attributes of an {@link Assertion Assertion} instance that 
 * define a {@link ValueComparator ValueComparator} instance.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */
public class ValueComparatorDescription {
	
	/* Declare instance variables used to define/describe a ValueComparator. */
	private String desc_type;
	private String desc_comparisonOp;
	private String desc_tolerance;
	
	
	/**
	 * Initialises instance variables.
	 * 
	 * @param type			Type of {@link ValueComparator ValueComparator}
	 * 						to be generated, must not be <code>null</code>.
	 * @param comparison	Comparison operation to be executed, must not be 
	 * 						<code>null</code>.
	 * @param tolerance		Tolerance level of element value differences allowed.
	 */
	protected ValueComparatorDescription(String type, String comparison, 
											String tolerance){
		
		/* Initialise instance variables. */										
		desc_type = type;
		desc_comparisonOp = comparison;
		desc_tolerance = tolerance;
	}
	
	
	/**
	 * Returns String denoting the type of {@link ValueComparator ValueComparator}
	 * to be generated.
	 * 
	 * @return		Type of {@link ValueComparator ValueComparator} to be generated.
	 */	
	protected String getType(){
		return desc_type;
	}
	
	/**
	 * Returns String denoting the comparison operation to be executed.
	 * 
	 * @return		Comparison operation to be executed.
	 */
	protected String getComparisonOp(){
		return desc_comparisonOp;
	}
	
	/**
	 * Returns String denoting the tolerance level of element value differences allowed.
	 * 
	 * @return		Tolerance level of element value differences allowed.
	 */
	protected String getTolerance(){
		return desc_tolerance;
	}
}
