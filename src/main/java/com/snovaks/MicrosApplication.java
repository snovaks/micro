package com.snovaks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.snovaks.domain.SearchRequest;
import com.snovaks.service.CrawlerService;

@SpringBootApplication
public class MicrosApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(MicrosApplication.class, args);

	
	}

}