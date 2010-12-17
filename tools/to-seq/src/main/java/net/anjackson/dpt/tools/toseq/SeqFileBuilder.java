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

/* From ant.jar, http://ant.apache.org/ */
import org.altlaw.hadoop.LocalSetup;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/** 
 * The output SequenceFile will be stored with BLOCK compression.  Each key (a
 * Text) in the SequenceFile should be the name of the file in the 
 * archive, and its value (a BytesWritable) the contents of the
 * file.
 *
 * @author Stuart Sierra (mail@stuartsierra.com)
 * @see <a href="http://hadoop.apache.org/core/docs/r0.16.3/api/org/apache/hadoop/io/SequenceFile.html">SequenceFile</a>
 * @see <a href="http://hadoop.apache.org/core/docs/r0.16.3/api/org/apache/hadoop/io/Text.html">Text</a>
 * @see <a href="http://hadoop.apache.org/core/docs/r0.16.3/api/org/apache/hadoop/io/BytesWritable.html">BytesWritable</a>
 */
public class SeqFileBuilder {

    private File outputFile;
    private LocalSetup setup;
    private SequenceFile.Writer output = null;
    // Internal buffer, starting at 100KB.
    //private byte[] bytes = new byte[100*1024];

    /** Sets up Configuration and LocalFileSystem instances for
     * Hadoop.  Throws Exception if they fail.  Does not load any
     * Hadoop XML configuration files, just sets the minimum
     * configuration necessary to use the local file system.
     */
    public SeqFileBuilder(File outputFile) throws Exception {
        setup = new LocalSetup();
        this.outputFile = outputFile;
        output = this.openOutputFile();
    }

    /** Performs the conversion. */
    public synchronized void add( String filename, InputStream in, long size ) throws Exception {
    	Text key = new Text(filename);
    	BytesWritable value = new BytesWritable(getBytes(in,size));
    	output.append(key, value);
    }

    private SequenceFile.Writer openOutputFile() throws Exception {
        Path outputPath = new Path(outputFile.getAbsolutePath());
        return SequenceFile.createWriter(setup.getLocalFileSystem(), setup.getConf(),
                                         outputPath,
                                         Text.class, BytesWritable.class,
                                         SequenceFile.CompressionType.BLOCK);
    }
    
    public void close() throws IOException { 
        if (output != null) { output.close(); }
    }

    /** Reads all bytes from the input stream and
     * returns them as a byte array.
     */
    private byte[] getBytes(InputStream input, long size) throws Exception {
        if (size > Integer.MAX_VALUE) {
            throw new Exception("A file in the archive is too large.");
        }
        // Copy:
        BufferedInputStream bufIn=new BufferedInputStream(input);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        IOUtils.copyBytes(bufIn, bytes, (int)size, false);
        bytes.close();
        return bytes.toByteArray();
    }

    
}
