package com.snovaks.utilities;

import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class HtmlCrawler extends WebCrawler {

	private final static Pattern EXCLUSIONS
		= Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");
	
	private HtmlSoupHelper htmlSoupHelper = new HtmlSoupHelper();
	
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
			htmlSoupHelper.connectGetAndDownloadTheDocument(stringURL);
		}
	}
	
}