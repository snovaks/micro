package com.snovaks.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class ImageSoupHelper {

	private static final String IMAGE_DESTINATION_FOLDER = "./images";
	
	public void connectGetAndDownloadImage(String stringURL) {
		
		String stringImageName = stringURL.substring(stringURL.lastIndexOf("/") + 1);
		
		try {
			Response resultImageResponse = Jsoup.connect(stringURL)
					.ignoreContentType(true).execute();
			
			File file = new File(IMAGE_DESTINATION_FOLDER);
			
			boolean fileExists = file.exists();
			if(!fileExists) {
				fileExists = file.mkdir();
			}
			
			OutputStream outputStream = new FileOutputStream(
					IMAGE_DESTINATION_FOLDER + "/" + stringImageName);
			outputStream.write(resultImageResponse.bodyAsBytes());
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}