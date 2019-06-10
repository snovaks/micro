package com.snovaks.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Configuration
public class CrawlerConfig {
	
	@Bean(name = "textCrawlController")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Autowired
	public CrawlController textCrawlController(@Qualifier(value = "htmlCrawlConfig")CrawlConfig crawlConfig) throws Exception {
		PageFetcher pageFetcherHtml = new PageFetcher(crawlConfig);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcherHtml);
		
		CrawlController htmlController = new CrawlController(
				crawlConfig, pageFetcherHtml, robotstxtServer);
		
		return htmlController;
	}
	
	@Bean(name = "imageCrawlController")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Autowired
	public CrawlController imageCrawlController(@Qualifier(value = "imageCrawlConfig")CrawlConfig crawlConfig) throws Exception {
		
		PageFetcher pageFetcherImage = new PageFetcher(crawlConfig);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcherImage);

		CrawlController imageController = new CrawlController(
				crawlConfig, pageFetcherImage, robotstxtServer);
		
		return imageController;
	}
	
	@Bean
	public CrawlConfig htmlCrawlConfig(@Value("${crawler.storage.metadata.location}")String storageLocation) {
		File crawlStorageBase = new File(storageLocation);
		CrawlConfig imageConfig = new CrawlConfig();
		imageConfig.setCrawlStorageFolder(new File(crawlStorageBase, "html")
				.getAbsolutePath());
		imageConfig.setMaxPagesToFetch(100);
		return imageConfig;
	}
	
	@Bean
	public CrawlConfig imageCrawlConfig(@Value("${crawler.storage.metadata.location}")String storageLocation) {
		File crawlStorageBase = new File(storageLocation);
		CrawlConfig imageConfig = new CrawlConfig();
		imageConfig.setCrawlStorageFolder(new File(crawlStorageBase, "image")
				.getAbsolutePath());
		imageConfig.setIncludeBinaryContentInCrawling(true);
		imageConfig.setMaxPagesToFetch(100);
		return imageConfig;
	}
	
}