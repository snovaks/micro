package com.snovaks.utilities;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.snovaks.repository.CrawlEntityRepository;

public class CrawlerResultStateSaver implements OnCrawlStateListener {

	private CrawlEntityRepository crawlEntityRepository;
	
	public CrawlerResultStateSaver(CrawlEntityRepository crawlEntityRepository) {
		this.crawlEntityRepository = crawlEntityRepository;
	}
	
	@Override
	public void onCrawlStarted() {
		
	}
	
	@Override
	public void onCrawlFinished(List<Object> crawlResults) {
		
		crawlResults
			.stream()
			.filter(x -> x instanceof List)
			.map(x -> (List) x)
			.flatMap(x -> x.stream())
			.filter(x -> x instanceof File)
			.map(x -> (File) x).forEach(); //file zmapować na encję
		
	}
	
}