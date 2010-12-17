/* TarToSeqFile.java - Convert tar files into Hadoop SequenceFiles.
 *
 * Copyright (C) 2008 Stuart Sierra
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * http:www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package net.anjackson.dpt.tools.toseq;

import java.io.File;


/** Utility to convert zip or tar files into Hadoop SequenceFiles.  The tar
 * files may be compressed with GZip or BZip2.  The output
 * SequenceFile will be stored with BLOCK compression.  Each key (a
 * Text) in the SequenceFile is the name of the file in the original
 * archive, and its value (a BytesWritable) is the contents of the
 * file.
 *
 * <p>This class can be run at the command line; run without
 * arguments to get usage instructions.
 *
 * @author Stuart Sierra (mail@stuartsierra.com)
 * @author Andrew Jackson (anj@anjackson.net)
 */
public class ToSeq {

    /** Runs the converter at the command line. */
    public static void main(String[] args) {
        if (args.length != 2) {
            exitWithHelp();
        }

        try {
        	File inputFile = new File(args[0]);
        	File outputFile = new File(args[1]);
        	if( outputFile.exists() ) {
        		System.err.println("Output file already exists! Will not overwrite.");
                exitWithHelp();
        	}
        	// ZIP or TAR/TAR.* ?
        	if( inputFile.getName().endsWith(".zip" ) ) {
        		ZipToSeq.execute(inputFile, outputFile);
        	} else {
        		TarToSeq.execute(inputFile, outputFile);
        	}
        } catch (Exception e) {
            e.printStackTrace();
            exitWithHelp();
        }
    }

    public static void exitWithHelp() {
        System.err.println("Usage: java net.anjackson.dpt.tools.toseq.ToSeq <inputfile> <output>\n\n" +
                           "<inputfile> may be ZIP or GZIP or BZIP2 compressed TAR, must have a\n" +
                           "recognizable extension .zip, .tar, .tar.gz, .tgz, .tar.bz2, or .tbz2.");
        System.exit(1);
    }
}
