package com.vay.crypthelper.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import com.vay.crypthelper.constant.CryptFileType;
import com.vay.crypthelper.event.WaterMarkerStyle;
import com.vay.crypthelper.utils.FileUtils;
import com.vay.text.pdf.PdfWriter;

public class EncryptFileHandler implements IFileHandler {

	private static String srcPath;
	private static String dstPath;

	private static Properties userConfigs = new Properties();
	
	private static String pwdStr;
//	private static String waterMarker;
//	private static String strictPrinting;
	private static WaterMarkerStyle  wmStyle = new WaterMarkerStyle();
	private final static String subFolder = "\\Crypt";
	
	

	public void loadConfig() throws Exception {
		InputStream ins = null;
		try {
			ins = new FileInputStream(System.getProperty("user.dir") + "\\config.properties");
			InputStreamReader insReader = new InputStreamReader(ins, "UTF-8");
			
			userConfigs.load(insReader);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("�����ļ�[config.properties]ȱʧ ");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("���������ļ�[config.properties]ʧ�� ");
		}
		
		srcPath = userConfigs.getProperty("src.path");
		dstPath = userConfigs.getProperty("dst.path");		
		pwdStr = userConfigs.getProperty("cypt.pwd");
		String text = userConfigs.getProperty("watermarker.text");
		String fontSize = userConfigs.getProperty("watermarker.font.size");
		String opacity = userConfigs.getProperty("watermarker.opacity");
		String xSize = userConfigs.getProperty("watermarker.xsize");
		String ySize = userConfigs.getProperty("watermarker.ysize");
		
		if(null!=text && !("").equals(text)) {
			wmStyle.setText(text);
		}
		
		if(null!=fontSize && !("").equals(fontSize)) {
			wmStyle.setFontSize(Integer.valueOf(fontSize));
		}
		
		if(null!=opacity && !("").equals(opacity)) {
			wmStyle.setOpacity(Float.valueOf(opacity));
		}
		
		if(null!=xSize && !("").equals(xSize)) {
			wmStyle.setXSize(Integer.valueOf(xSize)-1);
		}
		
		if(null!=ySize && !("").equals(ySize)) {
			wmStyle.setYSize(Integer.valueOf(ySize)-1);
		}
		
		if ((srcPath == null) && (srcPath != "")) {
			throw new Exception("��������ԭ�ļ�(������)·��");
		}
		
	}

	public  void initFile() {

		// Ŀ��·������ָ��, ����ԭ�ļ�·�������½��ļ���
		if ((dstPath == null) && (dstPath != "")) {
			dstPath = srcPath + subFolder;
		}
		
		File destFile = new File(dstPath);

		if (destFile.exists()) {
			List<String> files = FileUtils.getFileList(dstPath);
			for (int i = files.size() - 1; i >= 0; i--) {
				File f = new File((String) files.get(i));
				if (f.exists()) {
					f.delete();
				}

			}

			List<String> dirs = FileUtils.getFileDirs(dstPath);
			for (int i = dirs.size() - 1; i >= 0; i--) {
				File f = new File((String) dirs.get(i));
				if (f.exists()) {
					f.delete();
				}
			}
		} else {
			destFile.mkdirs();
		}
	}

	public static void main(String[] args) throws Exception {
		
//		if (args.length > 0) {
//			System.out.println(args[0]);
//		}
//
//		if ((args.length > 0) && (args[0].equals("normal"))) {
//			System.out.println("***   normal mode **** ");
//			transForm();
//		} else {
//			transFormAndCrypt();
//		}
		System.out.println(PdfWriter.ALLOW_SCREENREADERS >> 8);
		System.out.println(PdfWriter.ALLOW_SCREENREADERS >> 16);
		System.out.println(PdfWriter.ALLOW_SCREENREADERS >> 24);
		
		System.out.println(PdfWriter.ALLOW_PRINTING >> 8);
		System.out.println(PdfWriter.ALLOW_PRINTING >> 16);
		System.out.println(PdfWriter.ALLOW_PRINTING >> 24);
		
		EncryptFileHandler encryptFileHandler = new EncryptFileHandler();
		encryptFileHandler.encryptFile();
		
		
	}


	public void encryptFile() throws Exception  {
		loadConfig();
		initFile();
		
		CryptFileType[] fileTypes = CryptFileType.values();
		// ��Ҫת����PDF�汾
		char pdfVersion = (null  != userConfigs.get("pdf.version")) ? userConfigs.get("pdf.version").toString().charAt(0) : '0';
		
		//������ʱ������תPDF�汾
		if ( pdfVersion == '0' || pwdStr == null ||  pwdStr == "" ) {
			throw new Exception("�������������ָ����Ҫת��PDF�İ汾��");
		}

		for(int i=0; i < fileTypes.length; i++) {
			String fileType = fileTypes[i].toString();
			List<String> fileNames = FileUtils.getFileNameWithfilter(srcPath, fileType);
	
			for (String n : fileNames) {
				String dPath = n.replace(srcPath, dstPath);

				int index = dPath.lastIndexOf(File.separator);
				String s = "";
				if (index > 0) {
					s = dPath.substring(0, index);
					System.out.println(s);
					File f = new File(s);
					if (!f.exists()) {
						f.mkdirs();
					}
				}
				System.out.println(" ***** " + n);
				System.out.println(" ---- " + dPath);

				// just for test
//				if(n.indexOf("����")>0)
//					continue;
				encryptFile(n, dPath, pdfVersion, pwdStr, fileType, wmStyle);
			}
		}
	}



	

	public void encryptFile(String srcPath, String destPath, char version, String pwd, String fileType, WaterMarkerStyle wmStyle)
			throws Exception {
		if(fileType.equals(CryptFileType.PDF.toString())) {
			PDFHandler pdfHandler = new PDFHandler(srcPath, destPath, version, pwd, wmStyle);
			pdfHandler.action();
		}else {
			ExcelHandler excelHandler = new ExcelHandler(srcPath, destPath, pwd, fileType);
			//excelHandler.action();
		}
		
	}

//	public static void test1() {
//		String realpath = "D:\\myTemp\\TMP\\20170707";
//
//		String srcPath = realpath + "/test.pdf";
//		String targetPath = realpath + "/test-cry.pdf";
//		try {
//			copyAndCryptPdf(targetPath, srcPath, '4', "000000", true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//	}
	
	///////////////////////// getter setter
	public static Properties getUserConfigs() {
		return userConfigs;
	}

	public static void setUserConfigs(Properties userConfigs) {
		EncryptFileHandler.userConfigs = userConfigs;
	}
	
	
}
