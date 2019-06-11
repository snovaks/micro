package com.snovaks.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlSoupHelper {

	public File connectGetAndDownloadText(String stringURL) throws IOException {
		
		Document document = Jsoup.connect(stringURL).timeout(10 * 1000).get();
		String documentText = document.body().text();
		byte[] bytes = documentText.getBytes(StandardCharsets.UTF_8);
		File file = File.createTempFile("texts", ".txt");
		
		OutputStream outputStream = new FileOutputStream(file);
		outputStream.write(bytes);
		outputStream.close();
		
		return file;
	}
	
}