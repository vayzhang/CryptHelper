package com.vay.crypthelper.event;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class WaterMarkPageEvent extends PdfPageEventHelper {

    //public static Font FONT = new Font(Font.FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.85f));
	//public static BaseFont FONT =  BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
    
    private String waterMark ;
    private int  fontSize;
    private float opacity;
    private int xSize;
    private int ySize;
    private Font FONT ;
    //private BaseFont font;
    
    public WaterMarkPageEvent(WaterMarkerStyle wmStyle) {
    	super();    	
    	this.waterMark = wmStyle.getText();
    	this.fontSize = wmStyle.getFontSize();
        this.opacity = wmStyle.getOpacity();
        this.xSize = wmStyle.getXSize();
        this.ySize = wmStyle.getYSize();

    	try {
    		BaseFont font  = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
    		this.FONT = new Font(font, this.fontSize, Font.NORMAL, new GrayColor(0.88f));

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
    	
    	PdfContentByte canvas ;
    	PdfGState gstate = new PdfGState();
    	gstate.setFillOpacity(this.opacity);
    	for(int i=0; i<= this.ySize; i++) {
    		for(int j=0; j<= this.xSize; j++) {
    			canvas = writer.getDirectContent();
    			//writer.getDirectContentUnder()
    			canvas.setGState(gstate);
    			 ColumnText.showTextAligned(canvas,
    		                Element.ALIGN_CENTER, new Phrase(waterMark, FONT),
    		                (50.5f + i*300), (42f + j*300), writer.getPageNumber() % 2 == 1 ? 45 : -45);
        	}
    	}
//        ColumnText.showTextAligned(writer.getDirectContentUnder(),
//                Element.ALIGN_CENTER, new Phrase(waterMark, FONT),
//                297.5f, 421, writer.getPageNumber() % 2 == 1 ? 45 : -45);
        
//        ColumnText.showTextAligned(writer.getDirectContentUnder(),
//                Element.ALIGN_CENTER, new Phrase(waterMark, FONT),
//                297.5f, 421, writer.getPageNumber() % 2 == 1 ? 45 : -45);
        
    }
}