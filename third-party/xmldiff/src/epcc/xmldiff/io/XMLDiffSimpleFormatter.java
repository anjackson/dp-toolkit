/*
 * Created on May 27, 2004
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
package epcc.xmldiff.io;

import java.util.logging.SimpleFormatter;
import java.util.logging.LogRecord;

/**
 * Formats messages received from Logger instances within this 
 * application to only display the raw message (no localized information 
 * is required).
 * 
 * @author 		Lindsay Pottage
 * @version 	1.0
 */

public class XMLDiffSimpleFormatter extends SimpleFormatter {
	
	/**
	 * Calls parent constructor.
	 */
	public XMLDiffSimpleFormatter(){
		super();
	}
	
	/**
	 * Retrieves the raw message from the record argument and returns it.
	 * 
	 * @param record	{@link java.util.logging.LogRecord LogRecord} generated 
	 * 					within the application by a 
	 * 					{@link java.util.logging.Logger Logger} instance, must not 
	 * 					be <code>null</code>.
	 * @return			Raw message String to be logged.
	 */
	public String format(LogRecord record){
		
		/* Simply return the raw log message (without localized info). */
		String log_message = record.getMessage() + System.getProperty("line.separator");
		return log_message;
	}
}
