package com.snovaks.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class ImageSoupHelper {

	public File connectGetAndDownloadImage(String stringURL) throws IOException {
		Response resultImageResponse = Jsoup.connect(stringURL)
				.ignoreContentType(true).execute();
			
		File file = File.createTempFile("images", ".txt");
			
		OutputStream outputStream = new FileOutputStream(file);
		outputStream.write(resultImageResponse.bodyAsBytes());
		outputStream.close();
			
		return file;
	}
	
}