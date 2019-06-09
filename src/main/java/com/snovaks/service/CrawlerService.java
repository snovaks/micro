package com.snovaks.service;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
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
	
	public void searchText(SearchRequest searchRequest) throws Exception {
		
		File crawlStorageBase = new File("src/test/resources/crawler4j/html");
		String domainName = getDomainName(searchRequest.getUrl());
		
		CrawlConfig htmlConfig = new CrawlConfig();
		htmlConfig.setCrawlStorageFolder(new File(crawlStorageBase, domainName).getAbsolutePath());
		htmlConfig.setMaxPagesToFetch(100);
		htmlConfig.setResumableCrawling(true);
		PageFetcher pageFetcherHtml = new PageFetcher(htmlConfig);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcherHtml);
		
		CrawlController htmlController = new CrawlController(
				htmlConfig, pageFetcherHtml, robotstxtServer);
		
		htmlController.addSeed(searchRequest.getUrl());
		htmlController.startNonBlocking(HtmlCrawler::new, 10);
		htmlController.waitUntilFinish();
	}
	
	public void searchImages(SearchRequest searchRequest) throws Exception {
		
		File crawlStorageBase = new File("src/test/resources/crawler4j/image");
		String domainName = getDomainName(searchRequest.getUrl());
		
		CrawlConfig htmlConfig = new CrawlConfig();
		htmlConfig.setCrawlStorageFolder(new File(crawlStorageBase, domainName).getAbsolutePath());
		htmlConfig.setMaxPagesToFetch(100);
		htmlConfig.setResumableCrawling(true);
		htmlConfig.setIncludeBinaryContentInCrawling(true);
		PageFetcher pageFetcherHtml = new PageFetcher(htmlConfig);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcherHtml);
		
		CrawlController imageController = new CrawlController(
				htmlConfig, pageFetcherHtml, robotstxtServer);
		
		imageController.addSeed(searchRequest.getUrl());
		imageController.startNonBlocking(ImageCrawler::new, 10);
		imageController.waitUntilFinish();
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