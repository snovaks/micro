package com.snovaks.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.spi.FileTypeDetector;
import java.time.LocalDateTime;
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
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

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
		crawlEntity.setCrawlContentEntities(new ArrayList<>());
		
		List<File> files = (ArrayList<File>) crawlResults
				.stream()
				.filter(x -> x instanceof List)
				.map(x -> (List) x)
				.flatMap(x -> x.stream())
				.filter(x -> x instanceof File)
				.map(x -> (File) x)
				.collect(Collectors.toCollection(ArrayList<File>::new));
		
		long tempDataSize = 0;
		
		System.out.println("files size" + files.size());
		
		for(int i = 0; i < files.size(); i++) {
			
			System.out.println("iteracja: " + i);
			File file = files.get(i);
			CrawlContentEntity cce = new CrawlContentEntity();
			Magic magic = new Magic();
			try {
				MagicMatch match = magic.getMagicMatch(file, false);
				String contentType = match.getMimeType();
				System.out.println(contentType);
				byte[] content = Files.readAllBytes(file.toPath());
				cce.setContent(content);
				cce.setContentType(contentType);
				crawlEntity.getCrawlContentEntities().add(cce);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		crawlEntity.setDomainURL("domena.pl");
		crawlEntity.setCrawlDateTime(LocalDateTime.now());
		
		System.out.println("saving to repository");
		if(crawlEntityRepository.save(crawlEntity) != null) {
			System.out.println("***saved***");
		} else {
			System.out.println("***not saved***");
		}
		
	}
	
}