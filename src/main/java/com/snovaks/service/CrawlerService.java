package com.snovaks.service;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.snovaks.domain.SearchRequest;
import com.snovaks.utilities.HtmlCrawler;
import com.snovaks.utilities.ImageCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Service
public class CrawlerService {
	
	private CrawlController textCrawlController;
	private CrawlController imageCrawlController;
	
	@Autowired
	public CrawlerService(@Qualifier(value="textCrawlController")CrawlController textCrawlController, 
			@Qualifier(value="imageCrawlController")CrawlController imageCrawlController) {
		this.textCrawlController = textCrawlController;
		this.imageCrawlController = imageCrawlController;
	}
	
	public void searchText(SearchRequest searchRequest) throws Exception {
		textCrawlController.addSeed(searchRequest.getUrl());
		textCrawlController.startNonBlocking(HtmlCrawler::new, 10);
	}
	
	public void searchImages(SearchRequest searchRequest) throws Exception {
		imageCrawlController.addSeed(searchRequest.getUrl());
		imageCrawlController.startNonBlocking(ImageCrawler::new, 10);
		imageCrawlController.waitUntilFinish();
	}
	
	private String getDomainName(String url) {
		int indexSecondSlash = StringUtils.ordinalIndexOf(url, "/", 2);
		int indexThirdSlash = StringUtils.ordinalIndexOf(url, "/", 3);
		String domainName = url.substring(indexSecondSlash, indexThirdSlash);
		domainName = domainName.replace(".", "-");
		domainName = domainName.replace("/", "-");
		return domainName;
	}
	
}