/**
 * 
 */
package net.lovelycode.dp.contentprofiler.metadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author AnJackson
 *
 */
public class FileSummarizeEvent {
    static private Logger log = Logger.getLogger(FileSummarizeEvent.class);

    List<FileSummary> summaries = new ArrayList<FileSummary>();
    TechnicalEnvironment environment = new TechnicalEnvironment();
    
    File output = null;

    private CSVWriter writer = null;
    
    public FileSummarizeEvent( File output ) {
        this.output = output;
        if( this.output == null ) {
            try {
                this.output = File.createTempFile("contentprofiler", ".tmp.csv");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        try {
             writer = new CSVWriter(new FileWriter(this.output), ',');
        } catch (IOException e) {
            e.printStackTrace();
            writer = null;
        }
    }
    
    public void addSummary(FileSummary fs) {
        if( fs != null ) {
            summaries.add(fs);
            String[] entries = { 
                    fs.filename, fs.fullpath, fs.device.volumeLabel, fs.device.volumeType, 
                    ""+fs.size, fs.md5sum, fs.format, fs.formatUri, ""+fs.valid, ""+fs.wellFormed 
                    };
            writer.writeNext(entries);
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("Attempted to add a NULL object.");
        }
    }

    /**
     * @return the summaries
     */
    public final List<FileSummary> getSummaries() {
        return summaries;
    }
    
    /**
     * FIXME This is toast.
     * @return
     */
    public File finishFile(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.output;
    }
}
