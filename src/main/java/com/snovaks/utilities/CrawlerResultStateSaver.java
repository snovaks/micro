package com.snovaks.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.snovaks.entity.CrawlContentEntity;
import com.snovaks.entity.CrawlEntity;
import com.snovaks.repository.CrawlEntityRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrawlerResultStateSaver implements OnCrawlStateListener {

	private static final long MAX_NUMBER_OF_BYTES = 4096L;
	private CrawlEntityRepository crawlEntityRepository;
	
	public CrawlerResultStateSaver(CrawlEntityRepository crawlEntityRepository) {
		this.crawlEntityRepository = crawlEntityRepository;
	}
	
	@Override
	public void onCrawlStarted() {
		
	}
	
	@Override
	public void onCrawlFinished(List<Object> crawlResults) {
		
		CrawlEntity crawlEntity = new CrawlEntity();
		
		List<File> files = (ArrayList<File>) crawlResults
				.stream()
				.filter(x -> x instanceof List)
				.map(x -> (List) x)
				.flatMap(x -> x.stream())
				.filter(x -> x instanceof File)
				.map(x -> (File) x)
				.collect(Collectors.toCollection(ArrayList<File>::new));
		
		
		long tempDataSize = 0;
		int tempIndex = 0;
		
		for(int i = 0; i < files.size(); i++) {
			
			if(tempDataSize < MAX_NUMBER_OF_BYTES) {
				tempDataSize += files.get(i).length();
			} else {
				
				for(int j = tempIndex; j < i; j++) {
					
					File file = files.get(j);
					CrawlContentEntity cce = new CrawlContentEntity();
				
					try {
						byte[] content = Files.readAllBytes(file.toPath());
						cce.setContent(content);
						
						//tutaj jeszcze muszę dodać informacje o CrawlContentEntity - contentType
						
						crawlEntity.getCrawlContentEntities().add(cce);
					} catch (IOException e) {
						e.printStackTrace();
					}					
					
				}
				
				tempDataSize = 0;
				tempIndex = i;
			}
			
		}
		
		crawlEntityRepository.save(crawlEntity);
		
	}
	
}