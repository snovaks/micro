package com.snovaks.utilities;

import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.warn("OnCrawlStateListener is null");
	}

	public void setOnCrawlStateListener(OnCrawlStateListener onCrawlStateListener) {
		this.onCrawlStateListener = onCrawlStateListener;
	}

}