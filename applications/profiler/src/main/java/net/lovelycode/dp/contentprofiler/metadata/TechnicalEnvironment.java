/**
 * 
 */
package net.lovelycode.dp.contentprofiler.metadata;

import java.util.Map;
import java.util.Properties;

/**
 * @author AnJackson
 *
 */
public class TechnicalEnvironment {

    Computer computer;
    User user;
    Properties virtualMachineProperties;
    Map<String,String> environmentVariables;
    
    TechnicalEnvironment() {
        computer = new Computer();
        user = new User();
        virtualMachineProperties = System.getProperties();
        environmentVariables = System.getenv();
/*        
        // Look at system properties.
        for( Object k : virtualMachineProperties.keySet() ) {
            System.out.println((String)k+":"+virtualMachineProperties.getProperty((String)k));
        }
        for( Object k : environmentVariables.keySet() ) {
            System.out.println((String)k+":"+environmentVariables.get((String)k));
        }
        */
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TechnicalEnvironment [computer=" + computer
                + ", environmentVariables=" + environmentVariables + ", user="
                + user + ", virtualMachineProperties="
                + virtualMachineProperties + "]";
    }

    
    
}
