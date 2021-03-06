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

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;


/** Utility to convert tar files into Hadoop SequenceFiles.  The tar
 * files may be compressed with GZip or BZip2.  
 *
 * @author Stuart Sierra (mail@stuartsierra.com)
 */
public class TarToSeq {

    /** Performs the conversion. */
    public static void execute(File inputFile, File outputFile ) throws Exception {
        TarInputStream input = null;
        SeqFileBuilder sfb = new SeqFileBuilder(outputFile);
        try {
            input = openInputFile(inputFile);
            TarEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                if (entry.isDirectory()) { continue; }
                sfb.add(entry.getName(), input, entry.getSize());
            }
        } finally {
            if (input != null) { input.close(); }
            if (sfb != null) { sfb.close(); }
        }
    }

    private static TarInputStream openInputFile(File inputFile) throws Exception {
        InputStream fileStream = new FileInputStream(inputFile);
        String name = inputFile.getName();
        InputStream theStream = null;
        if (name.endsWith(".tar.gz") || name.endsWith(".tgz")) {
            theStream = new GZIPInputStream(fileStream);
        } else if (name.endsWith(".tar.bz2") || name.endsWith(".tbz2")) {
            /* Skip the "BZ" header added by bzip2. */
            fileStream.skip(2);
            theStream = new CBZip2InputStream(fileStream);
        } else {
            /* Assume uncompressed tar file. */
            theStream = fileStream;
        }
        return new TarInputStream(theStream);
    }


}
