package com.snovaks.utilities;

import java.io.File;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class ExtendedCrawlController extends CrawlController {

	private OnCrawlStateListener crawlStateListener;
	
	public ExtendedCrawlController(CrawlConfig config, PageFetcher pageFetcher,
            RobotstxtServer robotstxtServer) throws Exception {
		super(config, pageFetcher, robotstxtServer);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		List<Object> objects = getCrawlersLocalData();
		if(crawlStateListener != null) {
			crawlStateListener.onCrawlFinished(objects);
		}
	}

	public void setCrawlStateListener(OnCrawlStateListener crawlStateListener) {
		this.crawlStateListener = crawlStateListener;
	}
	
}