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

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import java.io.File;


/** Utility to convert zip files into Hadoop SequenceFiles.
 *
 * @author Andrew Jackson <anj@anjackson.net>
 */
public class ZipToSeq {

    /** Performs the conversion. */
    public static void execute(File inputFile, File outputFile ) throws Exception {
        ZipFile input = null;
        SeqFileBuilder sfb = new SeqFileBuilder(outputFile);
        try {
            input = new ZipFile(inputFile);
            Enumeration <? extends ZipEntry> entries = input.entries();
            ZipEntry entry = null;
            while( entries.hasMoreElements() ) {
            	entry = entries.nextElement();
                if (entry.isDirectory()) { continue; }
                sfb.add(entry.getName(), input.getInputStream(entry), entry.getSize());
            }
        } finally {
            if (input != null) { input.close(); }
            if (sfb != null) { sfb.close(); }
        }
    }

}
