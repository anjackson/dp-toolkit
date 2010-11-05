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

import epcc.xmldiff.exception.SetUpException;

/**
 * Processes, stores and provides access to user input.
 * 
 * @author		Lindsay Pottage
 * @version		1.0
 */

public class ArgumentCollection {
	
	/* Declare constant to test for correct minimum user input. */
	private static final int MINIMUM_ARGUMENT_NUMBER = 3;
	
	/* Declare constant error message for wrong number of arguments provided. */
	private static final String SETUPERROR = "SETUP ERROR: ";
	private static final String WRONGARGNMB = "Incorrect number of arguments " +
						"supplied to XMLDiff - please check your argument input";
						
	private static final String FLAGINVALID = "Invalid logging flag " +
						"specified. Recognised flags are '-v' verbose and '-s' silent";
	
	/* Declare a default logfile path to be used if the user does not specify one. */
	private static final String DEFAULT_LOGFILE_PATH = "log";
						
	/* Declare constant command options. */
	private static final String HELP_COMMAND1 = "-h";
	private static final String HELP_COMMAND2 = "-help";
	private static final String VERSION_COMMAND = "-version";
	private static final String METRIC_VALCOMMAND = "-m";
	private static final String FLAG_VERBOSE = "-v";
	private static final String FLAG_SILENT = "-s";
	
	/* Declare constant argument switch cases. */
	private static final int CONTROLDOC_INDEX = 0;
	private static final int CANDIDATEDOC_INDEX = 1;
	private static final int METRICDOC_INDEX = 2;
	private static final int LOGFILE_INDEX = 3;
	private static final int LOGGING_FLAGS = 4;
						
	/* Declare variables to store user input. */
	private String control_doc_path;
	private String candidate_doc_path;
	private String metric_doc_path;
	private String logfile_path;

	/* Declare variables to denote what type of output has been requested. */
	private boolean help_request;
	private boolean version_request;
	private boolean metric_validation;
	private boolean logging_set;
	private boolean verbose_logging;
	private boolean silent_logging;
	
	 
	/**
	 * Initialises instance variables.
	 * 
	 * @param args	Array of String arguments as provided by the user, 
	 * 				must not be <code>null</code>.
	 * @throws SetUpException If an error occurred while processing the 
	 * user-specified arguments.
	 */
	protected ArgumentCollection(String[] args) throws SetUpException {
		
		/* Initialise instance variables. */
		control_doc_path = "";
		candidate_doc_path = "";
		metric_doc_path = "";
		logfile_path = DEFAULT_LOGFILE_PATH;
		
		help_request = false;
		version_request = false;
		metric_validation = false;
		
		logging_set = false;
		verbose_logging = false;
		silent_logging = false;
		
		/* If an error occurred whilst retrieving the user-specified
		 * arguments, a SetUpException will be thrown. */
		setArguments(args);	
	}
	
	
	/**
	 * Assigns each user-provided argument to the appropriate instance 
	 * variables.
	 * 
	 * @param args	Array of String arguments as provided by the user via
	 * 				the command line, must not be <code>null</code>.
	 * @return		Whether arguments were successfully assigned to instance
	 * 				variables.
	 * @throws SetUpException If an error occurred while processing the 
	 * user-specified arguments.
	 */
	private void setArguments(String[] args) throws SetUpException {
	
		if(args.length == 0 || (args.length == 1 && (args[0].equals(HELP_COMMAND1) ||
														args[0].equals(HELP_COMMAND2)))){
			
			/* If the user has entered no arguments or '-h', display the help 
			 * information. */
			help_request = true;
			
		}else if(args.length == 1 && args[0].equals(VERSION_COMMAND)){
			
			/* If the user has entered '-version', display the version information. */
			version_request = true;
	
		}else if(args[0].equals(METRIC_VALCOMMAND)){
			
			/* Special case. If the user has entered '-m', metric validation 
			 * should take place. */
			metric_validation = true;
			
			/* Next, retrieve the metric file and log file if specified. */
			if(args.length > 1){
				
				/* Assign the metric document. */
				metric_doc_path = args[1];
				
				if(args.length > 2){
					
					/* The user has specified a log file to log output. */
					logfile_path = args[2];
				}
			}else {
				
				/* The user has not provided the appropriate input - metric file
				 * is minimum. */
				throw new SetUpException(SETUPERROR + WRONGARGNMB);
			}				
		}else {
		
			if(args.length >= MINIMUM_ARGUMENT_NUMBER){
			
				/* Loop over args and assign instance variables appropriately -
			 	 * will only handle first 5 arguments, if any more are specified
			 	 * they will be ignored. */
				for(int i=0; i<args.length; i++){
				
					String arg = args[i];
			
					switch(i){
						case CONTROLDOC_INDEX:
							control_doc_path = arg;
							break;
						case CANDIDATEDOC_INDEX:
							candidate_doc_path = arg;
							break;
						case METRICDOC_INDEX:
							metric_doc_path = arg;
							break;
						case LOGFILE_INDEX:
						
							/* First check if logging flags have been specified.
							 * If this is the case, assign them and use the default 
							 * logfile as set previously. Else, use the logfile path
							 * as specified by the user.  */
							if(arg.equalsIgnoreCase(FLAG_VERBOSE)){
								
								verbose_logging = true;
								logging_set = true;
							
							}else if(arg.equalsIgnoreCase(FLAG_SILENT)){
								
								silent_logging = true;
								logging_set = true;
								
							}else {
								
								/* Assign the logfile as specified. */
								logfile_path = arg;
							}
							break;
						case LOGGING_FLAGS:
						
							/* A logging flag has been specified - update the
						 	 * logging_set variable and decipher which flag has
						 	 * been requested. */
							logging_set = true;
							if(arg.equalsIgnoreCase(FLAG_VERBOSE)){
								
								verbose_logging = true;
							
							}else if(arg.equalsIgnoreCase(FLAG_SILENT)){
								
								silent_logging = true;
							
							}else{
							
								/* If an unrecognised flag has been specified, throw
								 * a SetUpException to detail the error. */
								logging_set = false;// Not necessary but just incase!
								throw new SetUpException(SETUPERROR + FLAGINVALID);								
							}
					}
				}
			}else{
				
				/* Catch incorrect user input. */
				throw new SetUpException(SETUPERROR + WRONGARGNMB); 
			}				
		}
	}
	
	
	/**
	 * Returns String representing path of control document as provided by user.
	 * 
	 * @return	String representing path of control document as provided by user.
	 */
	protected String getControlDocumentPath(){
		return control_doc_path;
	}
	
	
	/**
	 * Returns String representing path of candidate document as provided by user.
	 * 
	 * @return	String representing path of candidate document as provided by user.
	 */
	protected String getCandidateDocumentPath(){
		return candidate_doc_path;
	}
	
	
	/**
	 * Returns String representing path of metric document as provided by user.
	 * 
	 * @return	String representing path of metric document as provided by user.
	 */
	protected String getMetricDocumentPath(){
		return metric_doc_path;
	}
	
	
	/**
	 * Returns String representing path of either default log file or the requested 
	 * log file if provided by user.
	 * 
	 * @return	String representing path of either default log file or the requested 
	 * 			log file if provided by user.
	 */
	protected String getLogfilePath(){
		return logfile_path;
	}
	
	
	/**
	 * Returns whether a help request has been made by the user.
	 * 
	 * @return	Whether a help request has been made by the user.
	 */
	protected boolean isHelpRequest(){
		return help_request;
	}
	
	
	/**
	 * Returns whether a request for product version information has been made by the user.
	 * 
	 * @return	Whether a request for product version information has been made by the 
	 * 			user.
	 */
	protected boolean isVersionRequest(){
		return version_request;
	}
	
	
	/**
	 * Returns whether a request to validate a metric document against a schema has 
	 * been made by the user.
	 * 
	 * @return	Whether a request to validate a metric document against a 
	 * 			schema has been made by the user.
	 */
	protected boolean isMetricValFunction(){
		return metric_validation;
	}
	
	
	/**
	 * Returns whether logging flags have been specified by the user.
	 * 
	 * @return	Whether logging flags have been specified by the user.
	 */
	protected boolean isLoggingSet(){
		return logging_set;
	}
	
	
	/**
	 * Returns whether the verbose logging flag has been specified by the user.
	 * 
	 * @return	Whether the verbose logging flag has been specified by the user.
	 */
	protected boolean isLoggingVerbose(){
		return verbose_logging;
	}
	
	
	/**
	 * Returns whether the silent logging flag has been specified by the user.
	 * 
	 * @return	Whether the silent logging flag has been specified by the user.
	 */
	protected boolean isLoggingSilent(){
		return silent_logging;
	}
}

