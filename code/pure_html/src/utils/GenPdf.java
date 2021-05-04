package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.IOException;





public interface GenPdf {
	
	

//TODO: Implement IText7 - HTML2PDF
	
	public static void go(String string) throws IOException, FileNotFoundException, java.io.IOException {	
	    File pdfDest = new File("output.pdf");
	     // pdfHTML specific code
	    ConverterProperties converterProperties = new ConverterProperties();
	    HtmlConverter.convertToPdf(new FileInputStream(string), new FileOutputStream(pdfDest), converterProperties);
	}

}
