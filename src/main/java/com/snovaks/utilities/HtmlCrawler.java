package com.snovaks.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.snovaks.domain.DataWrapper;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HtmlCrawler extends WebCrawler {

	private final static Pattern EXCLUSIONS
		= Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");
	
	private List<Object> result = new ArrayList<>();
	private HtmlSoupHelper htmlSoupHelper = new HtmlSoupHelper();
	private DataWrapper dataWrapper = new DataWrapper();
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String urlString = url.getURL().toLowerCase();
		return !EXCLUSIONS.matcher(urlString).matches()
				&& urlString.startsWith(referringPage.getWebURL().getURL());
	}
	
	@Override
	public void visit(Page page) {
		String stringURL = page.getWebURL().getURL();
		if(page.getParseData() instanceof HtmlParseData) {
			try {
				File file = htmlSoupHelper.connectGetAndDownloadText(stringURL);
				result.add(file);
				dataWrapper.setCrawlResults(result);
				dataWrapper.setDomainName(stringURL);
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