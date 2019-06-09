package com.snovaks.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.snovaks.domain.SearchRequest;
import com.snovaks.utilities.HtmlCrawler;
import com.snovaks.utilities.ImageCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlController;

@Service
public class CrawlerService {

	private CrawlController textCrawlController;
	private CrawlController imageCrawlController;
	
	public void searchPlainText(SearchRequest searchRequest) {
		String url = searchRequest.getUrl();
		textCrawlController.addSeed(url);
		textCrawlController.startNonBlocking(HtmlCrawler::new, 10);
		textCrawlController.waitUntilFinish();
	}
	
	public void searchImages(SearchRequest searchRequest) {
		String url = searchRequest.getUrl();
		imageCrawlController.addSeed(url);
		imageCrawlController.startNonBlocking(ImageCrawler::new, 10);
		imageCrawlController.waitUntilFinish();
	}

	@Resource(name = "textCrawlController")
	public void setTextCrawlController(CrawlController textCrawlController) {
		this.textCrawlController = textCrawlController;
	}

	@Resource(name = "imageCrawlController")
	public void setImageCrawlController(CrawlController imageCrawlController) {
		this.imageCrawlController = imageCrawlController;
	}
	
}