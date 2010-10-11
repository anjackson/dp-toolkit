/**
 * 
 */
package net.lovelycode.dp.contentprofiler.metadata;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import net.lovelycode.dp.contentprofiler.ContentProfiler;

/**
 * @author AnJackson
 *
 */
public class Computer {
    static private Logger log = Logger.getLogger(Computer.class);

    private String hostname;
    private String canonicalHostname;
    private String address;
    
    public Computer() {
        // How to get hostname?
        try {
            this.canonicalHostname = InetAddress.getLocalHost().getCanonicalHostName();
            log.debug("Computer name: "+this.canonicalHostname);
        } catch (UnknownHostException e) {
            fail("Exception "+e);
            e.printStackTrace();
        }

        try {
            log.debug(InetAddress.getLocalHost().getHostName());
            log.debug(InetAddress.getLocalHost().getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //        getSystemLoadAverage();
        //      com.sun.management.OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        log.debug(os.getSystemLoadAverage()+":"+os.getAvailableProcessors());
        
    }
    
    public static void printUsage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("get") 
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = e;
                } // try
                System.out.println(method.getName() + " = " + value);
            } // if
        } // for
    }

    public static String getMotherboardSN() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs =
                "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                + "Set colItems = objWMIService.ExecQuery _ \n"
                + "   (\"Select * from Win32_BaseBoard\") \n"
                + "For Each objItem in colItems \n"
                + "    Wscript.Echo objItem.SerialNumber \n"
                + "    exit for  ' do the first cpu only! \n"
                + "Next \n";

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
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result.trim();
    }

}
