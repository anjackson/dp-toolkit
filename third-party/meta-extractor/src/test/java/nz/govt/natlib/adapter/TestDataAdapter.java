/**
 * 
 */
package nz.govt.natlib.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Properties;

import nz.govt.natlib.fx.ParserContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author beaumontb
 *
 */
public abstract class TestDataAdapter {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Make sure that the file type is accepted.
	 */
	@Test
	public void testAcceptsFile() throws Exception {
		// Get a copy of the test file.
		File testFile = getTestFile();
		
		// Create the adapter.
		DataAdapter adapter = getAdapter();
		
		// Make sure that we accept the file.
		Assert.assertTrue(adapter.acceptsFile(testFile));
	}
	
	
	/**
	 * Make sure we can delete the file after a call to acceptsFile.
	 */
	@Test
	public void testAcceptsFileCloses() throws Exception {
		// Get a copy of the test file.
		File testFile = createTestFileCopy();
		
		// Create the adapter.
		DataAdapter adapter = getAdapter();
		adapter.acceptsFile(testFile);
		
		// Make sure that we accept the file.
		Assert.assertTrue(testFile.delete());
	}	
	// Make sure that we can delete the copied file (i.e. make sure the file is closed).
	
	
	/**
	 * Make sure that we can adapt a file.
	 */
	@Test
	public void testAdapt() throws Exception {
		// Get a copy of the test file.
		File testFile = getTestFile();
		
		// Create the adapter.
		DataAdapter adapter = getAdapter();
		ParserContext ctx   = new ParserContext();
		
		// Adapt the file.
		adapter.adapt(testFile,ctx);
	}

	
	/**
	 * Make sure we can delete a file after adapting.
	 */
	@Test
	public void testAdaptDelete() throws Exception {
		// Get a copy of the test file.
		File testFile = createTestFileCopy();
		
		// Create the adapter.
		DataAdapter adapter = getAdapter();
		ParserContext ctx   = new ParserContext();
		
		// Adapt the file.
		try {
			adapter.adapt(testFile,ctx);
		}
		catch(Exception ex) { 
			// Hide the exception
		}
		
		// Make sure that we can delete the copied file (i.e. make sure the file is closed).
		Assert.assertTrue(testFile.delete());
	}
	
	
	
	public File getTestFile() {
		return new File(System.getProperty("test.resources.dir"), getFilename());
	}
	
	public abstract String getFilename();
	
	public abstract DataAdapter getAdapter();
	
	public File createTestFileCopy() throws IOException { 
		File testFile = getTestFile();
		File destFile = new File(testFile.getParentFile(), "~"+testFile.getName());
		copyFile(testFile, destFile);
		return destFile;
	}
	



	/**
	 * Copies a file from source to destination.
	 * @param from The source file.
	 * @param to The destination file.
	 * @throws IOException If there are errors copying the file.
	 */
	public void copyFile(File from, File to) throws IOException {
		FileChannel fromChannel = new FileInputStream(from).getChannel();
		FileChannel toChannel   = new FileOutputStream(to).getChannel();
		fromChannel.transferTo(0, fromChannel.size(), toChannel);
		fromChannel.close();
		toChannel.close();
	}
	
	
}
