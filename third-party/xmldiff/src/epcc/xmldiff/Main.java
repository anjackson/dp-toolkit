/*
 * Created on Apr 30, 2004
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

import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import epcc.xmldiff.io.XMLDiffSimpleFormatter;

import epcc.xmldiff.exception.SetUpException;
import epcc.xmldiff.exception.StructuralException;
import epcc.xmldiff.exception.ElementValueException;
import epcc.xmldiff.exception.WarningException;

/**
 * Entry point to XMLDiff. 
 * Retrieves and processes user input and initiates comparison or metric
 * validation processes via instance of the {@link XMLDiff XMLDiff} class.
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class Main {
	
	/* Declare Logger instance to be used by this class only. */
	private static final Logger main_logger_ = Logger.getLogger(Main.class.getName());
								
	/* Declare handler classes intended for logging of application-generated 
	 * messages to user. */
	private static Handler file_handler_;
	private static Handler console_handler_;

	/* Declare class used to process, store and provide access to 
	 * argument input provided by user. */
	private static ArgumentCollection arg_collection_;
	
	/* Declare constant exit values. */
	private static final int EXIT_PASS = 0;
	private static final int EXIT_WARNING = 1;
	private static final int EXIT_SETUP = 2;
	private static final int EXIT_STRUCTURAL = 3;
	private static final int EXIT_ELEMENTVALUE = 4;
	
	/* Declare Strings containing usage instructions for XMLDiff command-line 
	 * operation. */
	private static final String XMLDIFF_USAGE1 = "Usage: xmldiff [-h][-version]"; 
	private static final String XMLDIFF_USAGE2 = "or     xmldiff " +
					"controlfile candidatefile metricfile [logfile] [-v] [-s]";
	private static final String XMLDIFF_USAGE3 = "or     xmldiff -m metricfile [logfile]";
	
	private static final String HELP = "-h -help  Display this help message";
 	private static final String VERSION = "-version  Display product version information";
 	private static final String VERBOSE = "-v        Enable verbose output";
  	private static final String SILENT = "-s        Enable silent output";
	private static final String METRIC_VAL = "-m        Enable validation of metric";
	
	private static final String XMLDIFF_USAGE = XMLDIFF_USAGE1 + 
								System.getProperty("line.separator") + XMLDIFF_USAGE2 + 
								System.getProperty("line.separator") + XMLDIFF_USAGE3 + 
								System.getProperty("line.separator") + 
								System.getProperty("line.separator") + HELP + 
								System.getProperty("line.separator") + VERSION + 
								System.getProperty("line.separator") + VERBOSE + 
								System.getProperty("line.separator") + SILENT + 
								System.getProperty("line.separator") + METRIC_VAL;
							
	private static final String XMLDIFF_VERSION = "XMLDiff version 1.0";
	
	/* Declare constant error messages. */	
	private static final String SETUPERROR_LOGFILEEXCP = "SETUP ERROR: Error occurred " +
						"while attempting to generate the log file";
		
	
	/**
	 * Sets up application logging functionality and using the run method
	 * of an instance of the {@link XMLDiff XMLDiff} class,
	 * initiates parsing and either document comparison or metric
	 * validation processes.
	 * 
	 * @param args	Array of String arguments input by user.
	 */							
	public static void main(String[] args) {
		
		try{
			/* Set up user-specified functionality first. */
			init(args);
			
			/* If the user simply requested syntax information, 
			 * display and exit. */
			if(arg_collection_.isHelpRequest()){
				
				main_logger_.severe(XMLDIFF_USAGE);
				exit(EXIT_PASS, false);
				
			}else if(arg_collection_.isVersionRequest()){
				
				/* If the user simply requested version information, 
				 * display and exit. */
				main_logger_.severe(XMLDIFF_VERSION);
				exit(EXIT_PASS, false);
				
			}else{
				
				/* Call run method to parse user input and initiate comparison 
				 * operations. */
				XMLDiff main_application = null;
				
				if(!arg_collection_.isMetricValFunction()){
					
					main_application = new XMLDiff(arg_collection_.getControlDocumentPath(),
												arg_collection_.getCandidateDocumentPath(),
												arg_collection_.getMetricDocumentPath());
				}else {
					
					/* Utilise separate contructor if the user simply wishes to validate 
					 * a metric against a schema. */
					main_application = 
								new XMLDiff(arg_collection_.getMetricDocumentPath());
				}
				
				main_application.run();
			
				/* If no exceptions have been thrown, exit the application cleanly. */
				exit(EXIT_PASS, true);
			}
		}catch(SetUpException sue){
		
			/* Application should exit as an error occurred whilst processing
			 * user-specified input. */
			String message = sue.getMessage();
			
			if(sue.getCause() != null){
				
				message = message + System.getProperty("line.separator") + 
							sue.getCause().toString();
			}	
			main_logger_.severe(message);
			exit(EXIT_SETUP, true);
			
		}catch(StructuralException se){
		
			/* Application should exit as the control and candidate documents
			 * do not equate structurally. */
			main_logger_.severe(se.getMessage());
			exit(EXIT_STRUCTURAL, true);
			
		}catch(ElementValueException eve){
		
			/* Application should exit as the control and candidate documents
			 * contain element value errors. */
			main_logger_.severe(eve.getMessage());
			exit(EXIT_ELEMENTVALUE, true);
			
		}catch(WarningException we){
		
			/* Application should exit as comparison completed but warnings
			 * were logged. */
			main_logger_.severe(we.getMessage());
			exit(EXIT_WARNING, true);
		}		
	}
	
	
	/**
	 * Sets up application logging functionality based on user-specified 
	 * arguments.
	 * 
	 * @param args	Array of String arguments provided by user, must not be 
	 * 				<code>null</code>.
	 * @throws SetUpException If an error occurred during processing of
	 * user-specified arguments.
	 */
	private static void init(String[] args) throws SetUpException {
			
		/* Retrieve a 'root' Logger instance and set the default level for 
		 * logging - set to SEVERE as haven't retrieved user-specified flags 
		 * yet. */
		Logger xmldiff_logger = Logger.getLogger("");
		xmldiff_logger.setLevel(Level.SEVERE);
		
		/* Retrieve all Handlers and remove. */
		Handler[] parent_handlers = xmldiff_logger.getHandlers();
		for(int i=0; i<parent_handlers.length; i++) {
			xmldiff_logger.removeHandler(parent_handlers[i]);
		}
		
		/* Create a custom Formatter for this application. */
		XMLDiffSimpleFormatter xmldiff_formatter = new XMLDiffSimpleFormatter();
			
		/* Initialise and create ConsoleHandler instance by default - this enables
		 * output to be displayed to the user if an error occurred creating
		 * the FileHandler instance. */
		console_handler_ = new ConsoleHandler();
		console_handler_.setFormatter(xmldiff_formatter);
		xmldiff_logger.addHandler(console_handler_);
		
		/* Pass arguments to ArgumentCollection instance for initial 
		 * processing - a SetUpException will be thrown should any errors
		 * occur. */
		arg_collection_ = new ArgumentCollection(args);
	
		/* If all arguments have been received and logging is required, attempt 
		 * to create a FileHandler instance using the logfile argument provided by 
		 * the user (or default as appropriate). */
		if(!arg_collection_.isHelpRequest() && !arg_collection_.isVersionRequest()){
		
			try{
			
				/* Create a FileHandler instance and specify a SimpleFormatter to
			 	 * format the messages for display. */
				file_handler_ = new FileHandler(arg_collection_.getLogfilePath());
				file_handler_.setFormatter(xmldiff_formatter);
				
				xmldiff_logger.addHandler(file_handler_);
			
			}catch(Exception e){
			
				/* Throw a SetUpException - can't create/locate log file. */
				throw new SetUpException(SETUPERROR_LOGFILEEXCP, e);
			}
			
			/* If metric validation has been requested, leave the logging as it
			 * is (with output going to both console and file handlers). 
			 * Else, customise the logging functionality as the user has requested. */
			if(!arg_collection_.isMetricValFunction()){
		 
				if(arg_collection_.isLoggingSet()){
			
					if(arg_collection_.isLoggingVerbose()){
				
						/* If the user has specified logging to be verbose, 
				 		 * keep the ConsoleHandler instance and
				 		 * set the level of error reporting to Level.INFO to allow
				 		 * more information to be logged. */
						xmldiff_logger.setLevel(Level.INFO);
				
					}else if(arg_collection_.isLoggingSilent()){
				
						/* If the user has specified logging to be silent, 
						 * remove the ConsoleHandler instance and
						 * set the level of error reporting to Level.OFF. */
						xmldiff_logger.removeHandler(console_handler_);
						console_handler_ = null;
						
						xmldiff_logger.setLevel(Level.OFF);
					}
				}else {
			
					/* If no logging flags have been specified, set up default logging
					 * capabilities (leaving the Level at SEVERE) via only the FileHandler 
					 * instance. */
					xmldiff_logger.removeHandler(console_handler_);
					console_handler_ = null;
				}
			}
		}
	}
	
	
	/**
	 * Called when a SetUpException, StructuralComparatorException,
	 * ElementValueComparatorException or WarningException has been thrown 
	 * and thus requires the application to exit safely.
	 * 
	 * @param exit_value 		Exit value to be displayed, must not be 
	 * 							<code>null</code>.
	 * @param display_exitvalue	Whether to display the descriptive exit 
	 * 							value or not. 
	 */
	private static void exit(int exit_value, boolean display_exitvalue) {
		
		/* If requested to do so, display the exit value to the user. */
		if(display_exitvalue){
			
			main_logger_.setLevel(Level.INFO);
			main_logger_.info(String.valueOf(exit_value));
		}
		
		/* Shut down logging. */
		if(console_handler_ != null){
			console_handler_.close();
		}
		if(file_handler_ != null){
			file_handler_.close();
		}
		
		/* Shut down application. */
		System.exit(exit_value);
	}
}
