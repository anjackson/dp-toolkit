/*
 * Created on May 3, 2004
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

import org.dom4j.Document;

import epcc.xmldiff.io.XMLDiffUserDataElement;

import epcc.xmldiff.exception.SetUpException;
import epcc.xmldiff.exception.StructuralException;

/**
 * Compares two documents structurally.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class StructuralComparator {
	
	/* Declare Logger instance to be used by this class only. */
	private static final Logger structural_logger_ = 
								Logger.getLogger(StructuralComparator.class.getName());
	
	/* Declare constant error messages. */
	private static final String SETUPERROR_INVALIDINPUT = "SETUP ERROR: Exception " +
							"thrown whilst retrieving XML document(s)";
	private static final String STRUCT_ERROR = "STRUCTURAL ERROR ";
	private static final String EXTRA_ELEMENT = "Candidate document contains extra elements within ";
	private static final String MISSING_ELEMENT = "Candidate document is missing elements within ";
	private static final String ELEMENT_TAGS = "Control and candidate element node tags do not equate ";
	
	/* Declare constant information messages. */
	private static final String INFO_STRUCTURALPASS = "STRUCTURAL COMPARISON: Structural " +
													"comparison completed without errors";
											
	/* Declare Document instances. */
	private Document control_doc;
	private Document candidate_doc;
	
	/* Declare Strings representing the names of each respective Document instance. */
	private String controlDoc_name;
	private String candidateDoc_name;
	
						 
	/**
	 * Initialises instance variables.
	 * 
	 * @param control	Document used as comparison control, must not be
	 * 					<code>null</code>.
	 * @param candidate Document to be compared to comparison control, must
	 * 					not be <code>null</code>.
	 * @throws SetUpException If the argument input is invalid i.e. <code>null</code>.
	 */
	public StructuralComparator(Document control, Document candidate) 
														throws SetUpException {
		
		/* Check that the input is valid. */
		if(control != null && candidate != null){
			
			control_doc = control;
			controlDoc_name = control_doc.getName();

			candidate_doc = candidate;
			candidateDoc_name = candidate_doc.getName();
			
		}else {
			
			/* Throw a SetUpException to inform of invalid input. */
			throw new SetUpException(SETUPERROR_INVALIDINPUT);
		}
	}
	
	
	/**
	 * Iterates through control and candidate documents comparing them on a structural
	 * basis. 
	 * 
	 * @throws StructuralException If a structural error was found.
	 */
	public void compare() throws StructuralException {
		
		/* Retrieve the root element from both documents and initiate the 
		 * recursive comparison method structureCompare to iterate through each. */
		compareStructure((XMLDiffUserDataElement)control_doc.getRootElement(),
								(XMLDiffUserDataElement)candidate_doc.getRootElement());
									
		/* If no structural errors occurred - log an INFO message for the user. */
		structural_logger_.info(INFO_STRUCTURALPASS);
	}
	
	
	/**
	 * Retrieves child elements of each {@link XMLDiffUserDataElement XMLDiffUserDataElement}
	 * argument received and compares the number and tag name of each child element. 
	 * Any inconsistencies result in throwing of a {@link StructuralException StructuralException}, 
	 * otherwise this method is called recursively to iterate through the two documents. 
	 * 
	 * @param control_element		Element of the control document, must not be 
	 * 								<code>null</code>.
	 * @param candidate_element 	Element of the candidate document, must not be 
	 * 								<code>null</code>.
	 * @throws StructuralException If a structural error was found.
	 */
	private void compareStructure(XMLDiffUserDataElement control_element, 
									XMLDiffUserDataElement candidate_element) 
															throws StructuralException {
		
		/* Check that the tag names of these elements match. */
		if(nodeTagsCompare(control_element.getName(), candidate_element.getName())){
		
			/* If they do, retrieve child element nodes (if present) and call 
			 * recursively. */
			List control_elem_kids = control_element.elements();
			List candidate_elem_kids = candidate_element.elements();
		
			if(control_elem_kids.size() > 0) {	
			
				/* First compare the number of child elements. */
				int controlKids_number = control_elem_kids.size();
				int candidateKids_number = candidate_elem_kids.size(); 
			
				/* If they equate, for each control element, get the equivalent 
			 	 * candidate element then make a recursive call if appropriate. */
				if(controlKids_number == candidateKids_number) {
				
					for(int i=0; i<control_elem_kids.size(); i++) {
					
						XMLDiffUserDataElement control_kid = 
										(XMLDiffUserDataElement)control_elem_kids.get(i);
						XMLDiffUserDataElement candidate_kid = 
									(XMLDiffUserDataElement)candidate_elem_kids.get(i);
					
						/* Make recursive call to compare these two child elements. */
						compareStructure(control_kid, candidate_kid);
					}
				}else {
						
					/* If there is either a missing candidate child element(s) or
				 	 * extra candidate child element(s), throw a new StructuralException 
				 	 * to note the error. */
					if(controlKids_number > candidateKids_number){
					
						/* Throw a StructuralException instance to notify of
				 	 	 * missing equivalent candidate element. */
						throw new StructuralException(STRUCT_ERROR + "[" + 
							candidateDoc_name + " line " + 
							candidate_element.getLocation().getLineNumber() + "]: " + 
							MISSING_ELEMENT + "'" + candidate_element.getUniquePath() + 
							"' (control: " + controlKids_number + ", candidate: " + 
							candidateKids_number + ")");
					
					}else {
					
						/* Throw a StructuralException instance to notify of
					 	 * an extra candidate element. */
						throw new StructuralException(STRUCT_ERROR + "[" + 
							candidateDoc_name + " line " + 
							candidate_element.getLocation().getLineNumber() + "]: " + 
							EXTRA_ELEMENT + "'" + candidate_element.getUniquePath() + 
							"' (control: " + controlKids_number + ", candidate: " + 
							candidateKids_number + ")");
					}
				}
			}else {
			
				/* The control element has no child elements to compare - confirm that 
				 * the candidate element also has no child elements before continuing. */
				if(candidate_elem_kids.size() != 0){
				
					/* Throw a StructuralException instance to notify of an extra 
					 * candidate element. */
					throw new StructuralException(STRUCT_ERROR + "[" + candidateDoc_name +
						" line " + candidate_element.getLocation().getLineNumber() + 
						"]: " + EXTRA_ELEMENT + "'" + candidate_element.getUniquePath() + 
						"' (control: " + control_elem_kids.size() + ", candidate: " + 
						candidate_elem_kids.size() + ")");
				}
			}
		}else{
			
			/* Throw a StructuralException instance to notify of erroneous 
			 * non-matching tag names. */
			throw new StructuralException(STRUCT_ERROR + "[" + controlDoc_name + " line " + 
									control_element.getLocation().getLineNumber() + 
									", " + candidateDoc_name + " line " +
									candidate_element.getLocation().getLineNumber() + 
									"]: " + ELEMENT_TAGS + 
									"(control '" + control_element.getUniquePath() + 
									"', candidate '" + candidate_element.getUniquePath() + 
									"')");
		}
	}
	
	
	/**
	 * Returns true if arguments ctrl_tag and cand_tag equate, false if not.
	 * 
	 * @param ctrl_tag 	String to be compared to param cand_tag, must not
	 * 					be <code>null</code>.
	 * @param cand_tag	String to be compared to param ctrl_tag, must not
	 * 					be <code>null</code>.
	 * @return			Whether the two String arguments equate.
	 */
	private boolean nodeTagsCompare(String ctrl_tag, String cand_tag){
		if(ctrl_tag.equals(cand_tag)){return true;}else {return false;}
	}
}
