package com.snovaks.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DataWrapper {

	private List<Object> crawlResults;
	private String domainName;
	
	public DataWrapper() {
		crawlResults = new ArrayList<>();
	}
	
}