package com.porkerspicks.horses.image;

import java.io.File;

import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.LoadLibs;

public class ImageReader {
	
	public String readImage( String fileLocation ) {
		
		String text = null;
		try {
			ITesseract instance = new Tesseract();
			text = instance.doOCR( new File( fileLocation ) );
		} catch (TesseractException e) {
			System.out.println(e);
			throw new RuntimeException(e);
		}
		return text;
	}
	
	public static void main( String[] args ) {
		File tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
		System.setProperty("java.library.path", tmpFolder.getPath());
		ImageReader imageReader = new ImageReader();
		String text = imageReader.readImage("C:\\Users\\wullp\\OneDrive\\Documents\\Betting\\Horses\\Dec 20\\capture.png");
		System.out.println();
		System.out.println(text);
	}
}
