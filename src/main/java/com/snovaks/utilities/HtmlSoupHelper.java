package com.snovaks.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlSoupHelper {

	private static final String TEXT_DESTINATION_FOLDER = "./texts";
	
	public void connectGetAndDownloadTheDocument(String stringURL) {
		try {
			Document document = Jsoup.connect(stringURL).timeout(10 * 1000).get();
			String documentText = document.body().text();
			String documentName = document.baseUri();
			saveTheText(documentText, documentName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveTheText(String textContent, String stringElementURL) {
		File file = new File(TEXT_DESTINATION_FOLDER);
		
		boolean fileExists = file.exists();
		if(!fileExists) {
			fileExists = file.mkdir();
		}
		
		String domainName = stringElementURL.substring(stringElementURL.indexOf(".") + 1, stringElementURL.lastIndexOf("."));
		String textFileName = domainName + "-" + stringElementURL.substring(stringElementURL.lastIndexOf("/") + 1) + ".txt";
				
		try (
			var fileWriter = new FileWriter(TEXT_DESTINATION_FOLDER + "/" + textFileName);
			var writer = new BufferedWriter(fileWriter);
		) {
			writer.write(textContent);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}