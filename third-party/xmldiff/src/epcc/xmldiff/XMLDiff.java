/*
 * Created on May 14, 2004
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
package epcc.xmldiff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import org.xml.sax.SAXException;

import epcc.xmldiff.io.XMLDiffSAXReader;
import epcc.xmldiff.io.XMLDiffUserDataDocumentFactory;

import epcc.xmldiff.comparator.StructuralComparator;
import epcc.xmldiff.comparator.ElementValueComparator;

import epcc.xmldiff.exception.SetUpException;
import epcc.xmldiff.exception.StructuralException;
import epcc.xmldiff.exception.ElementValueException;
import epcc.xmldiff.exception.WarningException;

/**
 * Executes comparison process or metric schema-based validation. 
 * This class controls the flow of the application.
 * 
 * @author		Lindsay Pottage
 * @version		1.0
 */

public class XMLDiff {
	
	/* Declare constant error messages. */
	private static final String SETUPERROR = "SETUP ERROR: ";
	private static final String FILENOTFNDEXCP = "Error occurred while attempting to " +
													"retrieve input files";
						
	private static final String IOEXCP = "Error occurred while attempting to close InputStreams";				
	private static final String SAXEXCP = "Error occurred while attempting to set up " +
											"schema-based validation";
	private static final String DOCEXCP = "Error occurred during parsing of XML document(s)";
	
	/* Declare String variables to store path information of input files. */
	private String controlfile_path;
	private String candidatefile_path;
	private String metricfile_path;
	
	/* Declare boolean to denote whether metric schma_based validation has been
	 * requested. */
	private boolean metric_schemavalidation = false;
	
	
	/**
	 * Initialises instance variables for normal XML document comparison.
	 * 
	 * @param controlfile	String representing path of control document as 
	 * 						provided by user, must not be <code>null</code>.
	 * @param candidatefile	String representing path of candidate document as 
	 * 						provided by user, must not be <code>null</code>.
	 * @param metricfile	String representing path of metric document as 
	 * 						provided by user, must not be <code>null</code>.
	 */
	public XMLDiff(String controlfile, String candidatefile, String metricfile){
		
		controlfile_path = controlfile;
		candidatefile_path = candidatefile;
		metricfile_path = metricfile;
	}
	
	
	/**
	 * Initialises instance variables for metric schema-based validation.
	 * 
	 * @param metricfile	String representing path of metric document as 
	 * 						provided by user, must not be <code>null</code>.
	 */
	public XMLDiff(String metricfile){

		metricfile_path = metricfile;
		metric_schemavalidation = true;
	}
	
	
	/**
	 * Executes comparison process or metric schema-based validation. 
	 * This method controls the flow of the application.
	 * 
	 * @throws SetUpException If an error occurred within the application
	 * during processing of user-provided input.
	 * @throws StructuralException If an error occurred during the structural
	 * comparison phase of this method.
	 * @throws ElementValueException If an error occurred during the document
	 * comparison phase of this method.
	 * @throws WarningException If any warning messages were logged during the
	 * document comparison phase of this method.
	 */
	public void run() throws SetUpException, StructuralException, 
									ElementValueException, WarningException {
		
		Document control_document = null;
		Document candidate_document = null;
		Document metric_document = null;
		
		/* Using user arguments, create FileInputStreams to be passed to and
		 * parsed by an XMLDiffSAXReader instance. */
		try {
			
			FileInputStream control_input = null;
			FileInputStream candidate_input = null;
			FileInputStream metric_input = null;
			
			if(!metric_schemavalidation){
				
				/* If we are executing a normal XML document comparison,
				 * create FileInputStreams of the control and candidate documents. 
				 * Else, only the metric document need be placed into a 
				 * FileInputStream. */
				control_input = getInputStream(controlfile_path);
				candidate_input = getInputStream(candidatefile_path);
			}
				
			metric_input = getInputStream(metricfile_path);
			
			/* Initialise the SAXReader instance which will parse the documents to
			 * creat Document instances. If metric schema-based validation has been
			 * requested, this reader needs to be validating. */
			SAXReader xmldiff_reader = null;
			
			if(!metric_schemavalidation){
			
				/* Create XMLDiffSAXReader object and set a document factory that will 
			 	 * create locatable xml objects. */
				xmldiff_reader = new XMLDiffSAXReader();
				xmldiff_reader.setDocumentFactory(XMLDiffUserDataDocumentFactory.getInstance());
		
				control_document = xmldiff_reader.read(control_input);
				control_document.setName(controlfile_path);
			
				candidate_document = xmldiff_reader.read(candidate_input);
				candidate_document.setName(candidatefile_path);
				
				metric_document = xmldiff_reader.read(metric_input);
				metric_document.setName(metricfile_path);
				
				/* Close FileInputStreams. */
				control_input.close();
				candidate_input.close();								
			}else {
				
				/* Create XMLDiffSAXReader object that validates the document received
				 * against a schema. */
				xmldiff_reader = new XMLDiffSAXReader(true);
				metric_document = xmldiff_reader.read(metric_input);
			}
									
			metric_input.close();
				
		}catch(FileNotFoundException fe){
			
			/* Throw new SetUpException - can't locate file. */
			throw new SetUpException(SETUPERROR + FILENOTFNDEXCP, fe);	
				
		}catch(SAXException saxe){
			
			/* Throw new SetUpException - validation setup failed. */
			throw new SetUpException(SETUPERROR + SAXEXCP, saxe);
			
		}catch(DocumentException de){
		
			/* Throw new SetUpException - parsing failed. */
			throw new SetUpException(SETUPERROR + DOCEXCP, de);
			
		}catch(IOException io){
			
			/* Throw new SetUpException - FileInputStream closing issue. */
			throw new SetUpException(SETUPERROR + IOEXCP, io);
		}
		
		/* If no exceptions have been thrown during parsing of the user-specified
		 * XML documents, continue..*/
		if(!metric_schemavalidation){
		
			/* Initiate Pass 1 STRUCTURAL comparison operation. */
			StructuralComparator structural_comparator = 
					new StructuralComparator(control_document, candidate_document);
				
			/* If control and candidate documents compare structurally (i.e. no
			 * StructuralExceptions were thrown), continue.. */					
			structural_comparator.compare();	
					
			/* Initiate Pass 2 ELEMENT VALUE comparison operation - a SetUpException
			 * will be thrown here if any errors occurred during the processing of the
			 * metric document. */
			ElementValueComparator elementval_comparator =
									new ElementValueComparator(control_document, 
																candidate_document, 
																metric_document);
				
			/* If control and candidate documents don't compare based on their
			 * element values, appropriate Exceptions will be thrown here. */
			elementval_comparator.compare();	
		}
	}
	
	
	/**
	 * Generates {@link java.io.FileInputStream FileInputStream} instances using 
	 * the file_path argument.
	 * 
	 * @param 	file_path	String file path from which a 
	 * 						{@link java.io.FileInputStream FileInputStream} should be 
	 * 						created, must not be <code>null</code>.
	 * @return	{@link java.io.FileInputStream FileInputStream} generated from file 
	 * 			path String provided.
	 * @throws {@link java.io.FileNotFoundException FileNotFoundException} If an 
	 * error occurred retrieving the user-specified file referenced by the 
	 * file_path argument.
	 */
	private FileInputStream getInputStream(String file_path) 
											throws FileNotFoundException {
		
		return new FileInputStream(new File(file_path));
	}
}
