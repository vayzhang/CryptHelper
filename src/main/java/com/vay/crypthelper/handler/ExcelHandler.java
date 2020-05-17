package com.vay.crypthelper.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vay.crypthelper.constant.CryptFileType;

public class ExcelHandler {

	private String srcPath;
	
	private String destPath;
	
	private String pwd;
	
	private String fileType;


	public ExcelHandler(String srcPath, String destPath, String pwd, String fileType) {
		super();
		this.srcPath = srcPath;
		this.destPath = destPath;
		this.fileType = fileType;
		this.pwd = pwd;
	}



//	public void action() throws Exception {
//		try (POIFSFileSystem fs = new POIFSFileSystem()) {
//			  //EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
//			  EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile, CipherAlgorithm.aes192, HashAlgorithm.sha384, -1, -1, null);
//			  Encryptor enc = info.getEncryptor();
//			  enc.confirmPassword(pwd);
//			  // Read in an existing OOXML file and write to encrypted output stream
//			  // don't forget to close the output stream otherwise the padding bytes aren't added
//			  try (OPCPackage opc = OPCPackage.open(new File(srcPath), PackageAccess.READ_WRITE);
//			    OutputStream os = enc.getDataStream(fs)) {
//			    opc.save(os);
//			  }
//			  // Write out the encrypted version
//			  try (FileOutputStream fos = new FileOutputStream(destPath)) {
//			    fs.writeFilesystem(fos);
//			  }
//			  
//			  
//			  
//			}
//	}

	public void action() throws Exception {
		
		FileInputStream fins = new FileInputStream(srcPath);
		
		Workbook workbook = WorkbookFactory.create(fins);

//		int sheetCnt = workbook.getNumberOfSheets();
//		
//		for(int i=0; i< sheetCnt; i++) {
//			Sheet sheet = workbook.getSheetAt(i);
//		 
//	        Row row;
//	        for (int rowIndex = 0; i < sheet.getLastRowNum(); rowIndex++) {
//	            row = sheet.createRow(rowIndex);
//	            row.createCell(0).setCellValue((String) ITEM_PROJECT_ID_LIST.get(rowIndex));
//	        }
//		}
       
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);// 临时存储流到内存
        baos.flush();
 
        ByteArrayInputStream workbookInput = new ByteArrayInputStream(baos.toByteArray());
        // 创建POIFS文件系统 加密文件
        POIFSFileSystem fs = new POIFSFileSystem();
        EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
        //EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile, CipherAlgorithm.aes192, HashAlgorithm.sha384, -1, -1, null);
        Encryptor enc = info.getEncryptor();
        enc.confirmPassword(pwd);
        
        // 然后把字节输入到输入流，然后输入到OPC包里面
        OPCPackage opc = OPCPackage.open(workbookInput);
        OutputStream os = enc.getDataStream(fs);
 
        opc.save(os);
        opc.close();
 
        baos = new ByteArrayOutputStream();
        fs.writeFilesystem(baos);
        baos.flush();
 
        fins.close();
        workbook.close();
        baos.close();
 
        //return new ByteArrayInputStream(baos.toByteArray());
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



	public String getVersion() {
		return fileType;
	}



	public void setFileType(String fileType) {
		this.fileType = fileType;
	}



	public String getPwd() {
		return pwd;
	}



	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	

//	public static void main(String[] args) throws Exception {
//	if (args.length > 0) {
//		System.out.println(args[0]);
//	}
//
//	if ((args.length > 0) && (args[0].equals("normal"))) {
//		System.out.println("***   normal mode **** ");
//		transForm();
//	} else {
//		transFormAndCrypt();
//	}
//}
	
}
