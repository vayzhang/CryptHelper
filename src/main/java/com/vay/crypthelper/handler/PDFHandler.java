package com.vay.crypthelper.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vay.crypthelper.event.WaterMarkPageEvent;
import com.vay.crypthelper.event.WaterMarkerStyle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFHandler {

	private String srcPath;
	
	private String destPath;
	
	private char version;
	
	private String pwd;

	//private String waterMark;
	
	private WaterMarkerStyle wmStyle;
	
	//private String strictPrinting = "N";
	private String waterMrkerFontSize;

	public PDFHandler(String srcPath, String destPath, char version, String pwd, WaterMarkerStyle wmStyle) {
		super();
		this.srcPath = srcPath;
		this.destPath = destPath;
		this.version = version;
		this.pwd = pwd;
//		this.waterMark = waterMark;		 
//		this.waterMrkerFontSize = waterMrkerFontSize;
		this.wmStyle = wmStyle;
		
	}



	public void action() throws Exception {
		PdfReader reader = new PdfReader(srcPath);

		FileOutputStream out = new FileOutputStream(destPath);

		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, out);
		
		//设置隐藏菜单栏和工具栏
        writer.setViewerPreferences(PdfWriter.HideMenubar | PdfWriter.HideToolbar);

        byte[] pwdBytes = null;
        
		if ((pwd != null) && !("").equals(pwd)) {
			pwdBytes = pwd.getBytes();
			
		}
		
//		if(strictPrinting.equals("Y")) {
//			writer.setEncryption(pwdBytes, pwdBytes, PdfWriter.ALLOW_SCREENREADERS, 0);
//			//writer.setEncryption(null, null, PdfWriter.ALLOW_PRINTING, 0);
//		}else {
//			writer.setEncryption(pwdBytes, pwdBytes, PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_ASSEMBLY, 0);
//		}
		
		writer.setEncryption(pwdBytes, pwdBytes, PdfWriter.ALLOW_SCREENREADERS, 0);

		if (version != 0) {
			char baseVersion = reader.getPdfVersion();
			writer.setPdfVersion(baseVersion > version ? version : baseVersion);
		}

		writer.setPageEvent(new WaterMarkPageEvent(wmStyle));
		
		document.open();
		PdfContentByte cb = writer.getDirectContent();

		int totalPages = reader.getNumberOfPages();

		List<PdfReader> readers = new ArrayList<PdfReader>();
		readers.add(reader);

		int pageOfCurrentReaderPDF = 0;
		Iterator iteratorPDFReader = readers.iterator();

		while (iteratorPDFReader.hasNext()) {
			PdfReader pdfReader = (PdfReader) iteratorPDFReader.next();
			
//			if(null!=waterMark && !("").equals(waterMark)) {
//				setWatermark(pdfReader, cb, out, waterMark);
//			}
			
			while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
				Rectangle rectangle = reader.getPageSize(pageOfCurrentReaderPDF + 1);
				document.setPageSize(rectangle);
				document.newPage();

				pageOfCurrentReaderPDF++;
				PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);

				cb.addTemplate(page, 0.0F, 0.0F);
				
			}
			
			pageOfCurrentReaderPDF = 0;
			
			
		}
		
		
		
		out.flush();
		document.close();
		out.close();
	}
	
	public void setWatermark(PdfReader reader, PdfContentByte content, OutputStream out, String waterMarkName)
	  throws DocumentException, IOException {
	
		PdfStamper stamper = new PdfStamper(reader, out);
		
		BaseFont baseFont = null;
		try {
		  //base = BaseFont.createFont("/calibri.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
		// set opacity
		PdfGState state = new PdfGState();
		state.setFillOpacity(0.3f);
		state.setStrokeOpacity(0.4f);
		
		//PdfContentByte content = null;
		
		int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
        	
		  // add watermark over the content
		  content = stamper.getOverContent(i);
		  // add watermark under the content 
		  //content = stamper.getUnderContent(i);
		  content.saveState();
		  content.setGState(state);
		
		  // set Font
		  content.beginText();
		  content.setFontAndSize(baseFont, 10);
		  
		
		  // define font style
		  float ta = 1F, tb = 0F, tc = 0F, td = 1F, tx = 0F, ty = 0F;
		  // bold the font
		  ta += 0.25F;
		  td += 0.05F;
		  ty -= 0.2F;
		  // 设置倾斜(倾斜程序自己改)
		  tc += 0.8F;
		  content.setTextMatrix(ta, tb, tc, td, tx, ty);
		
		  // bottom left corner is (0, 0)
		  content.moveText(300F, 5F);
		  // show text
		  content.showText(waterMarkName);
		
		  content.endText();
		  content.stroke();
		  content.restoreState();
		}
       
		//stamper.close();
		//reader.close();
	}
	
	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	public char getVersion() {
		return version;
	}

	public void setVersion(char version) {
		this.version = version;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public WaterMarkerStyle getWmStyle() {
		return wmStyle;
	}



	public void setWmStyle(WaterMarkerStyle wmStyle) {
		this.wmStyle = wmStyle;
	}



	public static void main(String[] args) throws Exception {
		String fileName = "202001 Bonus Summary(CA) LOCAL.PDF";
		String srcPath = "H\\:\\myTemp\\TMP\\20200312\\source\\" + fileName;
		
		String destPath = "H\\:\\myTemp\\TMP\\20200312\\Cypto\\" + fileName;
		
		char version = '9';
		
		String pwd ="123456";
	
		String waterMark = "vay test";
		int fontSize = 20;
		
		WaterMarkerStyle wmStyle = new WaterMarkerStyle(waterMark, fontSize, 0.5f, 3, 4);
		PDFHandler pdfHandler = new PDFHandler(srcPath, destPath, version, pwd , wmStyle);
		pdfHandler.action();
	}

	
}
