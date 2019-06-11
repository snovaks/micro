package com.snovaks.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.snovaks.domain.DataWrapper;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageCrawler extends WebCrawler {

	private static final Pattern EXCLUSIONS 
		= Pattern.compile(".*(\\.(css|js|xml|gif|png|mp3|mp4|zip|gz|pdf))$");
	
	private static final Pattern IMG_PATTERNS = Pattern.compile(".*(\\.(jpg|jpeg))$");
	private List<Object> result = new ArrayList<>();
	
	private ImageSoupHelper imageSoupHelper = new ImageSoupHelper();
	
	private DataWrapper dataWrapper = new DataWrapper();
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String urlString = url.getURL().toLowerCase();
		
		if(EXCLUSIONS.matcher(urlString).matches()) {
			return false;
		}
		
		if(IMG_PATTERNS.matcher(urlString).matches()
				|| urlString.startsWith(referringPage.getWebURL().getURL())) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if(IMG_PATTERNS.matcher(url).matches()
				&& page.getParseData() instanceof BinaryParseData) {
			try {
				File file = imageSoupHelper.connectGetAndDownloadImage(url);
				result.add(file);
				dataWrapper.setCrawlResults(result);
				dataWrapper.setDomainName(url);
			} catch (IOException e) {
				log.warn("Exception", e);
			}
		}
	}
	
	@Override
	public Object getMyLocalData() {
		return dataWrapper;
	}
	
}