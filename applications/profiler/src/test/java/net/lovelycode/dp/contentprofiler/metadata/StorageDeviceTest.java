/**
 * 
 */
package net.lovelycode.dp.contentprofiler.metadata;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * @author AnJackson
 *
 */
public class StorageDeviceTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link uk.bl.dpt.contentprofiler.metadata.StorageDevice#StorageDevice(java.io.File)}.
     */
    @Test
    public void testStorageDevice() {
        StorageDevice sd = new StorageDevice( new File("src/test/data/files/world.jp2").getAbsoluteFile());
        assertNotNull("StorageDevice() returned null.",sd);
        // FIXME These need sigar?
//        assertNotNull("StorageDevice().volumeLabel returned null.",sd.volumeLabel);
//        assertNotNull("StorageDevice().volumeType returned null.",sd.volumeType);
        String summary = sd.toString();
        assertNotNull("Description should not be null",summary);
        assertFalse("Summary should not be empty","".equals(summary.trim()));
        System.out.println(sd);
    }

}
