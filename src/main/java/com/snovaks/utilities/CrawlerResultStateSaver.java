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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.snovaks.entity.CrawlContentEntity;
import com.snovaks.entity.CrawlEntity;
import com.snovaks.repository.CrawlContentEntityRepository;
import com.snovaks.repository.CrawlEntityRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class CrawlerResultStateSaver implements OnCrawlStateListener {

	private static final long MAX_NUMBER_OF_BYTES = 4096L;
	private CrawlEntityRepository crawlEntityRepository;
	private CrawlContentEntityRepository crawlContentEntityRepository;
	
	public CrawlerResultStateSaver(CrawlEntityRepository crawlEntityRepository,
			CrawlContentEntityRepository crawlContentEntityRepository) {
		this.crawlEntityRepository = crawlEntityRepository;
		this.crawlContentEntityRepository = crawlContentEntityRepository;
	}
	
	@Override
	public void onCrawlStarted() {
		
	}
	
	@Override
	public void onCrawlFinished(List<Object> crawlResults) {
		
		log.info("CrawlerResultStateSaver - onCrawlFinished ... saving");
		System.out.println("CrawlerResultStateSaver - onCrawlFinished ... saving");
		
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
		
		for(int i = 0; i < files.size(); i++) {
			
			System.out.println("Pętla iteracja");
			
			if(tempDataSize > MAX_NUMBER_OF_BYTES) {
				
				tempDataSize = 0;
				File file = files.get(i);
				CrawlContentEntity cce = new CrawlContentEntity();
				
				try {
					byte[] content = Files.readAllBytes(file.toPath());
					cce.setContent(content);
					
					//tutaj jeszcze muszę dodać informacje o CrawlContentEntity - contentType

					crawlEntity.getCrawlContentEntities().add(cce);
				} catch (IOException e) {
					log.warn("Error durning file saving");
				}
			} 
			
			tempDataSize += files.get(i).length();
		}
		
		crawlEntityRepository.save(crawlEntity);
		
	}
	
}