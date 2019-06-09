package com.snovaks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snovaks.domain.SearchRequest;
import com.snovaks.service.CrawlerService;

@RestController
@RequestMapping("/api/text")
public class TextController {

	private CrawlerService crawlerService;
	
	@Autowired
	public TextController(CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}
	
	@GetMapping
	public String getAllTexts() {
		return "allTexts";
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public void searchText(@RequestBody SearchRequest searchRequest) {
		System.out.println("Otrzymano searchRequest: " + searchRequest);
		crawlerService.searchPlainText(searchRequest);
	}
	
}