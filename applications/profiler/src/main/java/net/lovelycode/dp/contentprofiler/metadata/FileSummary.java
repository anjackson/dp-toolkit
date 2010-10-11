/**
 * 
 */
package net.lovelycode.dp.contentprofiler.metadata;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import net.lovelycode.dp.contentprofiler.profilers.FitsFileProfiler;

/**
 * @author AnJackson
 *
 */
public class FileSummary {
    static private Logger log = Logger.getLogger(FileSummary.class);
    
    String fullpath;
    String filename;
    StorageDevice device;
    
    Date dateSeen;
    Date dateModified;
    Date dateCreated;

    Long size;
    String md5sum;
    String format;
    String formatUri;
    Boolean wellFormed;
    Boolean valid;
    
    public FileSummary( File file ) throws IOException {
        this.device = new StorageDevice(file);
        
        this.fullpath = file.getCanonicalPath();
        this.filename = file.getName();
        
        this.dateSeen = Calendar.getInstance().getTime();
        
    }
    
    

    /**
     * @return the dateSeen
     */
    public Date getDateSeen() {
        return dateSeen;
    }



    /**
     * @return the size
     */
    public Long getSize() {
        return size;
    }



    /**
     * @param size the size to set
     */
    public void setSize(Long size) {
        log.debug("size:"+this.size+" -> "+size);
        this.size = size;
    }



    /**
     * @return the md5sum
     */
    public String getMd5sum() {
        return md5sum;
    }



    /**
     * @param md5sum the md5sum to set
     */
    public void setMd5sum(String md5sum) {
        log.debug("md5:"+this.md5sum+" -> "+md5sum);
        this.md5sum = md5sum;
    }



    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }



    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        log.debug("format:"+this.format+" -> "+format);
        this.format = format;
    }



    /**
     * @return the formatUri
     */
    public String getFormatUri() {
        return formatUri;
    }



    /**
     * @param formatUri the formatUri to set
     */
    public void setFormatUri(String formatUri) {
        log.debug("formatUri:"+this.formatUri+" -> "+formatUri);
        this.formatUri = formatUri;
    }



    /**
     * @return the wellFormed
     */
    public Boolean getWellFormed() {
        return wellFormed;
    }



    /**
     * @param wellFormed the wellFormed to set
     */
    public void setWellFormed(Boolean wellFormed) {
        log.debug("wellFormed:"+this.wellFormed+" -> "+wellFormed);
        this.wellFormed = wellFormed;
    }



    /**
     * @return the valid
     */
    public Boolean getValid() {
        return valid;
    }



    /**
     * @param valid the valid to set
     */
    public void setValid(Boolean valid) {
        log.debug("valid:"+this.valid+" -> "+valid);
        this.valid = valid;
    }



    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FileSummary [dateCreated=" + dateCreated + ", dateModified="
                + dateModified + ", dateSeen=" + dateSeen + ", device="
                + device + ", format=" + format + ", filename=" + filename
                + ", formatUri=" + formatUri + ", fullpath="
                + fullpath + ", md5sum=" + md5sum + ", size=" + size
                + ", valid=" + valid + ", wellFormed=" + wellFormed + "]";
    }
    
}
