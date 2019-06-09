package com.snovaks.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
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
	
	private void saveTheText(String textContent, String stringElementURL) throws IOException {
		File file = new File(TEXT_DESTINATION_FOLDER);
		boolean fileExists = file.exists();
		if(!fileExists) {
			fileExists = file.mkdir();
		}
		
		int indexSecondSlash = StringUtils.ordinalIndexOf(stringElementURL, "/", 2);
		int indexThirdSlash = StringUtils.ordinalIndexOf(stringElementURL, "/", 3);
		String domainName = stringElementURL.substring(indexSecondSlash, indexThirdSlash);
		domainName = domainName.replace(".", "-");
		
		String domainFilePath = TEXT_DESTINATION_FOLDER + "/" + domainName;
		File domainFile = new File(domainFilePath);
		
		boolean domainFileExists = domainFile.exists();
		if(!domainFileExists) {
			domainFileExists = domainFile.mkdir();
		}
		
		String textFileName = calculateTextFileName(stringElementURL);
		String finalTextFileName = domainFilePath + "/" + textFileName + ".txt";
		
		try (
			var fileWriter = new FileWriter(finalTextFileName);
			var writer = new BufferedWriter(fileWriter);
		) {
			writer.write(textContent);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private String calculateTextFileName(String stringURL) {
		String textFileName = stringURL;
		int stringLength = stringURL.length();
		char lastChar = stringURL.charAt(stringLength - 1);
		
		if(lastChar == '/') {
			StringBuilder sb = new StringBuilder(stringURL);
			sb.deleteCharAt(stringLength - 1);
			System.out.println("Zmieniona nazwa: " + sb.toString());
			textFileName = sb.toString();
		}
		
		return textFileName.substring(textFileName.lastIndexOf("/") + 1);
	}
	
}