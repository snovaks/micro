package com.snovaks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.snovaks.config.CrawlerConfig;
import com.snovaks.service.CrawlerService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {CrawlerConfig.class})
public class CrawlerServiceTest {

	@Autowired
	private CrawlerService crawlerService;
	
	@Test
	public void x() {
		
		//crawlerService.searchText();
	}
	
}