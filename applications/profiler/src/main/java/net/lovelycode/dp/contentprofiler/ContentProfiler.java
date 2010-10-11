/**
 * 
 */
package net.lovelycode.dp.contentprofiler;

import java.io.File;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import net.lovelycode.dp.contentprofiler.metadata.FileSummarizeEvent;
import net.lovelycode.dp.contentprofiler.metadata.FileSummary;
import net.lovelycode.dp.contentprofiler.profilers.FitsFileProfiler;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;

/**
 * Also
 * http://www.hyperic.com/support/docs/sigar/org/hyperic/sigar/FileSystem.html
 * 
 * @author AnJackson
 *
 */
public class ContentProfiler {
    static private Logger log = Logger.getLogger(ContentProfiler.class);

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception  {
        profileWizard();
    }

    public static FileSummarizeEvent profile( String[] files ) {
        FileSummarizeEvent fse = new FileSummarizeEvent(null);
        for( String f : files ) {
            FileSummary fs = profileOneItem(new File(f));
            if( fs != null ) {
                fse.getSummaries().add(fs);
            }
        }
        return fse;
    }

    public static FileSummarizeEvent profile( File file, File output ) {
        FileSummarizeEvent fse = new FileSummarizeEvent(output);
        profile(fse, file);
        return fse;
    }

    public static void profileWizard() {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Please choose a file or directory to analyse...");
        chooser.isDirectorySelectionEnabled();
        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
        // Note: source for ExampleFileFilter can be found in FileChooserDemo,
        // under the demo/jfc directory in the JDK.
        //        ExampleFileFilter filter = new ExampleFileFilter();
        //        filter.addExtension("jpg");
        //        filter.addExtension("gif");
        //        filter.setDescription("JPG & GIF Images");
        //        chooser.setFileFilter(filter);
        //        int returnVal = chooser.showOpenDialog(parent);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            log.info("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
        } else {
            log.warn("No file chosen!");
            return;
        }

        // Input and output:
        File in = chooser.getSelectedFile();

        // Now select output file:
        chooser.setDialogTitle("Please choose a file to put the output in...");
        chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
        chooser.addChoosableFileFilter( new CSVFilter() );
        chooser.setSelectedFile(new File("output.csv"));

        returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            log.info("You chose to write to this file: " +
                    chooser.getSelectedFile().getName());
        } else {
            log.warn("No file chosen!");
            return;
        }

        // Input and output:
        File out = chooser.getSelectedFile();
        if( out.exists() ) {
            log.warn("File exists!");
            return;
        }

        // Run
        FileSummarizeEvent profile = profile(in, out);

        profile.finishFile();

    }

    public static void profile( FileSummarizeEvent fse, File file ) {
        if( file.isFile() ) {
            FileSummary fs = profileOneItem( file );
            if( fs != null ) {
                fse.addSummary(fs);
            }
        } else if( file.isDirectory() ) {
            for( File lf : file.listFiles() ) {
                profile( fse, lf);
            }
        }
    }

    private static FileSummary profileOneItem( File in ) {
        log.info("Profiling "+in.getAbsolutePath()+"...");
        long start = System.currentTimeMillis();
        FileSummary fs = FitsFileProfiler.profile(in);
        long elapsed = System.currentTimeMillis() - start;
        if( fs == null ) {
            log.warn("Got NULL for "+in.getAbsolutePath());
        }
        log.info("Profiling "+in.getName()+" took "+(elapsed/1000.0)+"s");
        return fs;
    }


    /**
     * http://www.drewnoakes.com/code/exif/sampleUsage.html
     * 
     * @throws ImageProcessingException
     */
    private static void imageQuery() throws ImageProcessingException {
        File jpegFile = new File("myImage.jpg");
        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
        /* Segments manually */
        /*
        JpegSegmentReader segmentReader = new JpegSegmentReader(jpegFile); 
        byte[] exifSegment = segmentReader.readSegment(JpegSegmentReader.SEGMENT_APP1); 
        byte[] iptcSegment = segmentReader.readSegment(JpegSegmentReader.SEGMENT_APPD); 
        Metadata metadata = new Metadata(); 
        new ExifReader(exifSegment).extract(metadata);
        new IptcReader(iptcSegment).extract(metadata);
         */

        /* SLowest, but also has the image */
        /*
        JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(new FileInputStream(jpegFile)); 
        BufferedImage image = jpegDecoder.decodeAsBufferedImage(); 
        // now you can use the image 
        JPEGDecodeParam decodeParam = jpegDecoder.getJPEGDecodeParam(); 
        Metadata metadata = JpegMetadataReader.readMetadata(decodeParam);
         */

        // iterate through metadata directories 
        Iterator directories = metadata.getDirectoryIterator(); 
        while (directories.hasNext()) { 
            Directory directory = (Directory)directories.next(); 
            // iterate through tags and print to System.out  
            Iterator tags = directory.getTagIterator(); 
            while (tags.hasNext()) { 
                Tag tag = (Tag)tags.next(); 
                // use Tag.toString()  
                System.out.println(tag); 
                int tagint = tag.getTagType();
                String hex = tag.getTagTypeHex();
                String name = tag.getTagName();
                try {
                    // the tag's value
                    String value = tag.getDescription();
                } catch (MetadataException e) {
                    e.printStackTrace();
                }
            } 
        }
        /*
        Directory exifDirectory = metadata.getDirectory(ExifDirectory.class); int exifDirectory.getInt(tag.getTagType()); double exifDirectory.getDouble(tag); float exifDirectory.getFloat(tag); long exifDirectory.getLong(tag); Rational exifDirectory.getRational(tag); java.util.Date exifDirectory.getDate(tag); 
         */
        /*
        Directory exifDirectory = metadata.getDirectory(ExifDirectory.class); String cameraMake = exifDirectory.getString(ExifDirectory.TAG_MAKE); String cameraModel = exifDirectory.getString(ExifDirectory.TAG_MODEL); Directory iptcDirectory = metadata.getDirectory(IptcDirectory.class); String caption = iptcDirectory.getString(IptcDirectory.TAG_CAPTION); 
         */
    }
}
