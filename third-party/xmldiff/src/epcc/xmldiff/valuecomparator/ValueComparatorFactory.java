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

import epcc.xmldiff.exception.SetUpException;

/**
 * Generates appropriate {@link ValueComparator ValueComparator} implementation 
 * classes according to the {@link ValueComparatorDescription ValueComparatorDescription} 
 * instance received.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0 
 */

public class ValueComparatorFactory {
	
	/* Create a private single instance of this class. */
	private static ValueComparatorFactory factory_ = new ValueComparatorFactory();
	
	/* Declare String constants used to validate ValueComparatorDescription 'types'. */
	private static final String REAL_TYPE = "real";
	private static final String DOUBLE_TYPE = "double";
	private static final String INTEGER_TYPE = "integer";
	private static final String COMPLEX_TYPE = "complex";
	private static final String CHAR_STRING_TYPE = "string";
	private static final String REALARRAY_TYPE = "real_array";
	private static final String DOUBLEARRAY_TYPE = "double_array";
	private static final String INTEGERARRAY_TYPE = "integer_array";
	private static final String COMPLEXARRAY_TYPE = "complex_array";

	/* Declare constant error messages. */
	private static final String TYPENOTVALID = "Non-valid value for assertion attribute " +
												"'type' has been specified - ";
	private static final String VALIDTYPES = "[Supported data types:- " + REAL_TYPE + ", " + 
						DOUBLE_TYPE + ", " + INTEGER_TYPE + 
						", " + COMPLEX_TYPE + ", " + CHAR_STRING_TYPE + ", " +
						REALARRAY_TYPE + ", " + DOUBLEARRAY_TYPE + ", " + 
						INTEGERARRAY_TYPE + " and " + COMPLEXARRAY_TYPE +"]";	
	
	
	/**
	 * private Constructor.
	 */
	private ValueComparatorFactory(){
	}
	
	
	/**
	 * Returns single instance of this class.
	 * 
	 * @return		Single instance of this class.
	 */
	protected static ValueComparatorFactory getInstance(){
		return factory_;
	}
	
	
	/**
	 * Returns a {@link ValueComparator ValueComparator} instance.
	 * 
	 * @param desc		{@link ValueComparatorDescription ValueComparatorDescription}
	 * 					instance containing all information required to generate a
	 * 					{@link ValueComparator ValueComparator} instance, must not
	 * 					be <code>null</code>.
	 * @return			{@link ValueComparator ValueComparator} instance.
	 * @throws SetUpException If an error occurred whilst generating a
	 * {@link ValueComparator ValueComparator} instance.
	 */
	protected ValueComparator getValueComparator(ValueComparatorDescription desc) 
													throws SetUpException {
		
		/* Retrieve the 'type' of ValueComparator requested. */
		String type_requested = desc.getType();
		
		/* Initialise 2 ValueComparator instances - one to be returned by this method
		 * and the second to be passed to an ArrayTypeComparator instance. */
		ValueComparator type_comparator = null;
		ValueComparator value_comparator = null; 
		
		/* Decipher the type of ValueComparator requested and create the
		 * appropriate concrete implementation class. */
		if(type_requested.equals(REAL_TYPE)){
		
			// Real type ValueComparator.
			value_comparator = new RealValueComparator(desc.getComparisonOp(), 
														desc.getTolerance());	
														
		}else if(type_requested.equals(DOUBLE_TYPE)){
		
			// Double type ValueComparator.
			value_comparator = new DoubleValueComparator(desc.getComparisonOp(), 
															desc.getTolerance());
															
		}else if(type_requested.equals(INTEGER_TYPE)){
		
			// Integer type ValueComparator.
			value_comparator = new IntegerValueComparator(desc.getComparisonOp(), 
															desc.getTolerance());
															
		}else if(type_requested.equals(COMPLEX_TYPE) || 
					type_requested.equals(COMPLEXARRAY_TYPE)){
		
			// Complex type and Complex Array type ValueComparator.
			value_comparator = new ComplexValueComparator(desc.getComparisonOp(), 
															desc.getTolerance());
															
		}else if(type_requested.equals(CHAR_STRING_TYPE)){
		
			// String type ValueComparator.
			value_comparator = new CharStringValueComparator(desc.getComparisonOp());	
		
		}else if(type_requested.equals(REALARRAY_TYPE)){
		
			// Real Array type ValueComparator.
			type_comparator = new RealValueComparator(desc.getComparisonOp(), 
														desc.getTolerance());
			value_comparator = new ArrayTypeComparator(type_comparator);
			
		}else if(type_requested.equals(DOUBLEARRAY_TYPE)){
		
			// Double Array type ValueComparator.
			type_comparator = new DoubleValueComparator(desc.getComparisonOp(), 
														desc.getTolerance());
			value_comparator = new ArrayTypeComparator(type_comparator);
			
		}else if(type_requested.equals(INTEGERARRAY_TYPE)){
		
			// Integer Array type ValueComparator.
			type_comparator = new IntegerValueComparator(desc.getComparisonOp(), 
															desc.getTolerance());
			value_comparator = new ArrayTypeComparator(type_comparator);	
			
		}else{
			
			/* Throw a SetUpException - ValueComparatorDescription has provided 
			 * a 'type' that is not valid.*/
			throw new SetUpException(TYPENOTVALID + type_requested + 
									System.getProperty("line.separator") + VALIDTYPES);
		}
		return value_comparator;
	}
}
