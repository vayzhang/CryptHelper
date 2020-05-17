package com.vay.crypthelper.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import com.vay.crypthelper.handler.ExcelHandler;
import com.vay.crypthelper.constant.CryptFileType;
import com.vay.crypthelper.event.WaterMarkerStyle;
import com.vay.crypthelper.utils.FileUtils;
import com.itextpdf.text.pdf.PdfWriter;

public class EncryptFileHandler implements IFileHandler {

	private static String srcPath;
	private static String dstPath;

	private static Properties userConfigs = new Properties();

	private static String pwdStr;
	private static String ownerPwd;

	private static WaterMarkerStyle  wmStyle = new WaterMarkerStyle();
	private final static String subFolder = "\\Crypt";



	public void loadConfig() throws Exception {
		InputStream ins ;
		try {
			ins = new FileInputStream(System.getProperty("user.dir") + "\\config.properties");
			InputStreamReader insReader = new InputStreamReader(ins, "UTF-8");
			userConfigs.load(insReader);
			//userConfigs.load(ins);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("配置文件[config.properties]缺失 ");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("加载配置文件[config.properties]失败 ");
		}

		srcPath = userConfigs.getProperty("src.path");
		dstPath = userConfigs.getProperty("dst.path");
		pwdStr = userConfigs.getProperty("cypt.pwd");
		ownerPwd = userConfigs.getProperty("owner.pwd");
		String text = userConfigs.getProperty("watermarker.text");
		String fontSize = userConfigs.getProperty("watermarker.font.size");
		String opacity = userConfigs.getProperty("watermarker.opacity");
		String xSize = userConfigs.getProperty("watermarker.xsize");
		String ySize = userConfigs.getProperty("watermarker.ysize");

		if(null!=text && !("").equals(text)) {
			wmStyle.setText(text);
		}

		if(null!=fontSize && !("").equals(fontSize)) {
			wmStyle.setFontSize(Integer.parseInt(fontSize));
		}

		if(null!=opacity && !("").equals(opacity)) {
			wmStyle.setOpacity(Float.parseFloat(opacity));
		}

		if(null!=xSize && !("").equals(xSize)) {
			wmStyle.setXSize(Integer.parseInt(xSize)-1);
		}

		if(null!=ySize && !("").equals(ySize)) {
			wmStyle.setYSize(Integer.parseInt(ySize)-1);
		}

		if ((srcPath == null) || ("").equals(srcPath)) {
			throw new Exception("请先配置原文件(待加密)路径");
		}

	}

	public  void initFile() {

		// 目标路径如无指定, 则在原文件路径下面新建文件夹
		if ((dstPath == null) || ("").equals(dstPath)) {
			dstPath = srcPath + subFolder;
		}

		File destFile = new File(dstPath);

		if (destFile.exists()) {
			List<String> files = FileUtils.getFileList(dstPath);
			for (int i = files.size() - 1; i >= 0; i--) {
				File f = new File(files.get(i));
				if (f.exists()) {
					f.delete();
				}

			}

			List<String> dirs = FileUtils.getFileDirs(dstPath);
			for (int i = dirs.size() - 1; i >= 0; i--) {
				File f = new File(dirs.get(i));
				if (f.exists()) {
					f.delete();
				}
			}
		} else {
			destFile.mkdirs();
		}
	}

	public static void main(String[] args) throws Exception {
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
		// 需要转换的PDF版本
		String pdfVersion = (null  != userConfigs.get("pdf.version")) ? userConfigs.get("pdf.version").toString(): "0";

		//无密码时，单纯转PDF版本
		if ( !(Integer.parseInt(pdfVersion) > 0) && ( pwdStr == null ||  ("").equals(pwdStr) )) {
			throw new Exception("请先配置密码或指定需要转换PDF的版本号");
		}

		for (CryptFileType type : fileTypes) {
			String fileType = type.toString();
			List<String> fileNames = FileUtils.getFileNameWithfilter(srcPath, fileType);

			for (String n : fileNames) {
				String dPath = n.replace(srcPath, dstPath);

				int index = dPath.lastIndexOf(File.separator);
				String s;
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
//				if(n.indexOf("集中")>0)
//					continue;
				encryptFile(n, dPath, pdfVersion, pwdStr, ownerPwd, fileType, wmStyle);
			}
		}
	}





	public void encryptFile(String srcPath, String destPath, String version, String pwd, String ownerPwd, String fileType, WaterMarkerStyle wmStyle)
			throws Exception {
		if(fileType.equals(CryptFileType.PDF.toString())) {
			PDFHandler pdfHandler = new PDFHandler(srcPath, destPath, version, pwd, ownerPwd, wmStyle);
			pdfHandler.action();
		}else {
			ExcelHandler excelHandler = new ExcelHandler(srcPath, destPath, pwd, fileType);
			//excelHandler.action();
		}

	}

}
