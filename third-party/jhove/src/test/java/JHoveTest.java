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
		args.add("pom.xml");
/*
		args[0] = "-c";
//		args[1] = "jar:file:///Users/anj/Projects/digitalpreservation/dp-toolkit/tools/jhove/target/jhove-1.4.1.jar!/jhove.conf";
//		args[1] = "file:///Users/anj/Projects/digitalpreservation/dp-toolkit/tools/jhove/target/checkout/conf/jhove.conf";
		args[1] = "target/checkout/conf/jhove.conf";
		args[2] = "pom.xml";
*/		
		Jhove.main( args.toArray( new String[0] ) );
//		fail("Not yet implemented");
	}

}
