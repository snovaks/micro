package com.snovaks.utilities;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class ExtendedCrawlController extends CrawlController {

	private OnCrawlStateListener onCrawlStateListener;
	
	public ExtendedCrawlController(CrawlConfig config, PageFetcher pageFetcher,
            RobotstxtServer robotstxtServer) throws Exception {
		super(config, pageFetcher, robotstxtServer);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		List<Object> objects = getCrawlersLocalData();
		if(onCrawlStateListener != null) {
			onCrawlStateListener.onCrawlFinished(objects);
		}
		
		System.out.println("Crawlowanie zakończone");
	}

	public void setOnCrawlStateListener(OnCrawlStateListener onCrawlStateListener) {
		this.onCrawlStateListener = onCrawlStateListener;
	}

}