package nz.govt.natlib.adapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import nz.govt.natlib.fx.ParserContext;

import org.junit.Assert;
import org.junit.Test;


public class AdapterTest {
	
	
	static String adapters[] = new String[] {
		"nz.govt.natlib.adapter.any.DefaultAdapter",
		"nz.govt.natlib.adapter.bmp.BitmapAdapter", 
		"nz.govt.natlib.adapter.excel.ExcelAdapter",
		"nz.govt.natlib.adapter.gif.GIFAdapter",
		"nz.govt.natlib.adapter.html.HTMLAdapter",
		"nz.govt.natlib.adapter.jpg.JpgAdapter",
		"nz.govt.natlib.adapter.mp3.MP3Adapter",
		"nz.govt.natlib.adapter.openoffice.OpenOfficeAdapter",
		"nz.govt.natlib.adapter.pdfbox.PDFBoxAdapter",
		"nz.govt.natlib.adapter.powerpoint.PowerPointAdapter",
		"nz.govt.natlib.adapter.tiff.TIFFAdapter",
		"nz.govt.natlib.adapter.wav.WaveAdapter",
		"nz.govt.natlib.adapter.word.WordAdapter",
		"nz.govt.natlib.adapter.wordperfect.WPAdapter",
		"nz.govt.natlib.adapter.works.DocAdapter",
		"nz.govt.natlib.adapter.xml.XMLAdapter",
	};
	
	
	@Test public void testAllAdapters() throws Exception { 
		DataAdapter adapter = null;
		
		File f = File.createTempFile("test", ".meta");
		f.deleteOnExit();
		if(f.exists()) { 
			if(!f.delete()) { 
				throw new IOException("Couldn't delete file on startup");
			}
		}

		ParserContext ctx = new ParserContext();
		
		for(int i=0;i<adapters.length;i++) { 
			adapter = (DataAdapter) Class.forName(adapters[i]).newInstance();
			
			createFile(f);
			adapter.acceptsFile(f); 
			Assert.assertTrue(f.delete());
			
			createFile(f);
			try {
				adapter.adapt(f, ctx);
			}
			catch(Exception ex) { 
			}
			Assert.assertTrue(f.delete());
		}
	}
	
	public static void createFile(File f) throws IOException { 
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new FileWriter(f));
			pw.println("<html>");
			pw.println("  <body>");
			pw.println("    <p>Hello</p>");
			pw.println("  </body>");
			pw.println("</html>");
		}
		finally {
			try { pw.close(); } catch(Exception ex) { ex.printStackTrace(); } 
		}
	}
	
	
}
