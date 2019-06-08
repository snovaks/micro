package com.snovaks.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Configuration
public class CrawlerConfig {
	
	@Bean
	@Scope("prototype")
	public CrawlController textCrawlController() throws Exception {
		File crawlStorageBase = new File("src/test/resources/crawler4j");
		CrawlConfig htmlConfig = new CrawlConfig();
		htmlConfig.setCrawlStorageFolder(new File(crawlStorageBase, "html")
				.getAbsolutePath());
		htmlConfig.setMaxPagesToFetch(100);
		PageFetcher pageFetcherHtml = new PageFetcher(htmlConfig);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcherHtml);
		
		CrawlController htmlController = new CrawlController(
				htmlConfig, pageFetcherHtml, robotstxtServer);
		
		return htmlController;
	}
	
	@Bean
	@Scope("prototype")
	public CrawlController imageCrawlController() throws Exception {
		File crawlStorageBase = new File("src/test/resources/crawler4j");
		CrawlConfig imageConfig = new CrawlConfig();
		imageConfig.setCrawlStorageFolder(new File(crawlStorageBase, "image")
				.getAbsolutePath());
		imageConfig.setIncludeBinaryContentInCrawling(true);
		imageConfig.setMaxPagesToFetch(100);
		PageFetcher pageFetcherImage = new PageFetcher(imageConfig);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(
				robotstxtConfig, pageFetcherImage);

		CrawlController imageController = new CrawlController(
				imageConfig, pageFetcherImage, robotstxtServer);
		
		return imageController;
	}
	
}