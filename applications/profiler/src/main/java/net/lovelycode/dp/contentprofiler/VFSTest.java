/**
 * 
 */
package net.lovelycode.dp.contentprofiler;

import java.io.File;
import java.net.URI;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;

/**
 * @author andy
 *
 */
public class VFSTest {

	static FileSystemManager fsManager = null;
	static 
	{
		try {
			 fsManager = VFS.getManager();
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 * @throws FileSystemException 
	 */
	public static void main(String[] args) throws FileSystemException {
		// TODO Auto-generated method stub
		// Locate the Jar file
		URI base = new File("src/test/resources").toURI();
		//FileObject fo = fsManager.resolveFile( new File("src/test/" ), "resources" );
		FileObject bf = fsManager.resolveFile(base.toASCIIString());
		//FileObject fo = fsManager.resolveFile( "iso:zip:"+base+"/test1.zip!/TEST1.iso" );
		FileObject fo = fsManager.resolveFile("jar:zip:file:///Users/andy/Documents/workspace/dpt/applications/profiler/src/test/resources/test3.zip!/test2.jar");
		//FileObject fo = fsManager.resolveFile( "zip:"+base+"/test1.zip" );
		recurse(fo);

	}
	
	private static void recurse( FileObject fo ) throws FileSystemException {
		// List the children of the Jar file
		FileObject[] children = fo.getChildren();
		System.out.println( "Children of " + fo.getName().getURI() );
		for ( FileObject c : children )
		{
		    System.out.println( c.getName().getFriendlyURI() );
		    if( c.getType() == FileType.FOLDER ) recurse(c);
		    System.out.println( fsManager.canCreateFileSystem(c) );
		}
	}

}
