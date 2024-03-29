package com.snovaks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snovaks.domain.SearchRequest;
import com.snovaks.service.CrawlerService;

@RestController
@RequestMapping("/api/image")
public class ImageController {

	private CrawlerService crawlerService;
	
	@Autowired
	public ImageController(CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public void searchImages(@RequestBody SearchRequest searchRequest) throws Exception {
		crawlerService.searchImages(searchRequest);
	}
	
}