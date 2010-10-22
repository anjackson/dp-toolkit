import static org.junit.Assert.*;

import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */

/**
 * @author anj
 *
 */
public class JHoveTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link Jhove#main(java.lang.String[])}.
	 */
	@Test
	public void testMain() {
		List<String> args = new Vector<String>();
		args.add("src/test/java/JHoveTest.java");
		//args.add("pom.xml");
/*
		args.add("-c");
		args.add("target/checkout/conf/jhove.conf");
*/		
		Jhove.main( args.toArray( new String[0] ) );
//		fail("Not yet implemented");
	}

}
