package com.snovaks.utilities;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class HtmlCrawler extends WebCrawler {

	private final static Pattern EXCLUSIONS
		= Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");
	
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
				Document document = Jsoup.connect(stringURL).get();
				Elements elements = document.getAllElements();
				
				
				for(Element element : elements) {
					String text = element.text();
					System.out.println(text);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}
	
}