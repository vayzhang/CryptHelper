package com.vay.crypthelper;

import com.vay.crypthelper.handler.EncryptFileHandler;

public class Main {

	public static void main(String[] args) throws Exception {

		EncryptFileHandler fileHandler = new EncryptFileHandler();
		fileHandler.encryptFile();
	}
}
