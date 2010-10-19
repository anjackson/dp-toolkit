package net.lovelycode.dp.tools.nanite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import uk.gov.nationalarchives.pronom.*;

public class SigFileUtils {

	private static JAXBContext jc;
	
	static {
		try {
			jc  = JAXBContext.newInstance(SignatureFileType.class.getPackage().getName());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return
	 */
	public static SigFile getLatestSigFile() {
		PronomService_Service pss = new PronomService_Service();
		PronomService pronomService = pss.getPronomServiceSoap();
		return pronomService.getSignatureFileV1();
	}
	
	/**
	 * @param sigFile.getFFSignatureFile()
	 * @param os
	 * @throws JAXBException
	 */
	public static void writeSigFileToOutputStream( SigFile sigFile, OutputStream os ) throws JAXBException {
		//Create marshaller
		Marshaller m = jc.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		//Marshal object into file.
		/* This override uses a different root element 
	    JAXBElement<SignatureFileType> rootElement = 
			new JAXBElement<SignatureFileType>(
					new QName("http://www.nationalarchives.gov.uk/pronom/SignatureFile","FFSignatureFile"), 
					SignatureFileType.class, sigFile.getFFSignatureFile() );
		m.marshal( rootElement , os);
		*/
		GetSignatureFileV1Response container = new GetSignatureFileV1Response();
		container.setSignatureFile(sigFile);
		FFSignatureFile ffsf = new FFSignatureFile(sigFile.getFFSignatureFile());
		m.marshal(ffsf, os);
	}
	
	public static GetSignatureFileV1Response readSigFile( File input ) throws FileNotFoundException, JAXBException {
		Unmarshaller u = jc.createUnmarshaller();		
		return (GetSignatureFileV1Response)u.unmarshal( new FileInputStream(input) );		
	}
	
	/**
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 * @throws Exception
	 */
	public static void main(String[] args) throws JAXBException, UnsupportedEncodingException, FileNotFoundException {
		SigFile sigFile = getLatestSigFile();
		System.out.println("SigFile v"+sigFile.getFFSignatureFile().getVersion());
		for( FileFormatType fft : sigFile.getFFSignatureFile().getFileFormatCollection().getFileFormat()) {
			System.out.println("PUID "+fft.getPUID());
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		writeSigFileToOutputStream(sigFile,bos);
		// Turn it into a string:
		String xml = bos.toString("UTF-8");
		System.out.println(xml.substring(0, 200));
	
		// Write it to a file:
		String filename = "droid-signature_"+sigFile.getFFSignatureFile().getVersion()+".xml";
		writeSigFileToOutputStream( sigFile, new FileOutputStream(filename) );
		
		// Read it back:
		GetSignatureFileV1Response s2 = readSigFile( new File(filename) );
		// Cache detailed report for each format:
		//http://www.nationalarchives.gov.uk/PRONOM/fmt/1.xml
	}

}
