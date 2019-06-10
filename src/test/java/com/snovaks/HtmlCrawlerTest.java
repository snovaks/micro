package com.snovaks;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.snovaks.utilities.HtmlCrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

@RunWith(JUnit4.class)
public class HtmlCrawlerTest {

	@Test
	public void shouldVisitWhenItIsJSPage() {
		HtmlCrawler htmlCrawler = new HtmlCrawler();
		WebURL rootUrl = new WebURL();
		rootUrl.setURL("https://example.com/");
		Page page = new Page(rootUrl);
		WebURL url = new WebURL();
		url.setURL("https://example.com/folder/folderTwo/content.js");
		Assert.assertFalse(htmlCrawler.shouldVisit(page, url));
	}
	
	@Test
	public void shouldVisitWhenItIsTextPage() {
		HtmlCrawler htmlCrawler = new HtmlCrawler();
		WebURL rootUrl = new WebURL();
		rootUrl.setURL("https://example.com/");
		Page page = Mockito.mock(Page.class);
		Mockito.when(page.getWebURL()).thenReturn(rootUrl);
		WebURL url = Mockito.mock(WebURL.class);
		Mockito.when(url.getURL()).thenReturn("https://example.com/folder/folderTwo/content.html");
		Assert.assertTrue(htmlCrawler.shouldVisit(page, url));
	}
	
}