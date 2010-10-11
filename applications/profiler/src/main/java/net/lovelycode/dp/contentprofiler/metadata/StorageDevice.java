/**
 * 
 */
package net.lovelycode.dp.contentprofiler.metadata;

import java.io.BufferedReader;


import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

/**
 * @author AnJackson
 *
 */
public class StorageDevice {
    
    String volumeLabel;
    String volumeType;
    Long totalSpace;
    
    public StorageDevice( File in ) {
        // disk information
        //System.out.println(""+in.getTotalSpace()+":"+in.getUsableSpace()+":"+in.getFreeSpace());
        //
        this.totalSpace = in.getTotalSpace();
        // find the root level:
        FileSystemView v = FileSystemView.getFileSystemView();
        File drive = in.getAbsoluteFile();
        while(!(drive == null) && !v.isDrive(drive = drive.getParentFile()) ) { }
        this.volumeLabel = v.getSystemDisplayName(drive);
        this.volumeType = v.getSystemTypeDescription(drive);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return volumeLabel + " [" + volumeType + "]";
    }



    // pattern to search for _(X:)
    static final Pattern p = Pattern.compile( " \\([A-Za-z]:\\)" );

    /**
     * 
     */
    public static void fileSystem()
    {
        FileSystemView v = FileSystemView.getFileSystemView();
        for ( File f : File.listRoots() )
        {
            final String full = v.getSystemDisplayName( f );
            System.out.print(full+","+v.getSystemTypeDescription(f));
            final int length = full.length();
            // Remove the trailing _(X:)
            final String chopped;
            final String drive;

            final Matcher m = p.matcher( full );
            if ( m.find() )
            {
                chopped = full.substring( 0, m.start() ).trim();
                drive = full.substring(m.start()+2, m.end()-1);
            }
            else
            {
                chopped = full.trim();
                drive = chopped;
            }
            System.out.println( ", full:[" + full + "]     chopped:[" + chopped + "]" );
            //
        }

    }

    /**
     * 
$ sudo vol_id /dev/sdax
or
$ sudo blkid /dev/sdax
But, still got to map file to device, is not predictable, req root, and only works on local volumes.
     * @param drive
     * @return
     */
    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            //File file = File.createTempFile("realhowto",".vbs");
            File file = File.createTempFile("tmp",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                +"Set colDrives = objFSO.Drives\n"
                +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                +"Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                new BufferedReader
                (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch(Exception e){
            System.out.println("Foo bar "+e);

            return "";
        }
        if(result.trim().length() < 1){
            System.out.println("Could not find "+drive);
            return "";
        }
        result = result.trim();

        try {
            System.out.println("ID: "+Integer.toHexString( Integer.parseInt( result ) ) );
        } catch (NumberFormatException e) {
        }

        return result;
    }


    /*
    public static void sigar() throws SigarException {
        Sigar sigar = new Sigar();
        System.out.println("Uptime:"+sigar.getUptime());
        Cpu cpu = sigar.getCpu();
        System.out.println("Idle:"+cpu.getIdle());
        for( FileSystem fs : sigar.getFileSystemList() ) {
            System.out.println("FS: "+fs.getDevName()+", "+fs.getDirName()+", "+fs.getSysTypeName()+", "+fs.getTypeName()+", "+fs.getOptions());
        }
        DiskUsage du = sigar.getDiskUsage(".");
        System.out.println("Reads:"+du.getReads());
    }
        */


}
