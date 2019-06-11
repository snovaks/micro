package com.snovaks.utilities;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.snovaks.domain.DataWrapper;
import com.snovaks.entity.CrawlContentEntity;
import com.snovaks.entity.CrawlEntity;
import com.snovaks.repository.CrawlContentEntityRepository;
import com.snovaks.repository.CrawlEntityRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

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
		
		List<DataWrapper> dataWrappers = crawlResults
				.stream()
				.filter(x -> x instanceof DataWrapper)
				.map(x -> (DataWrapper) x)
				.collect(Collectors.toCollection(ArrayList<DataWrapper>::new));
		
		List<Object> results = dataWrappers
				.stream()
				.map(x -> x.getCrawlResults())
				.collect(Collectors.toCollection(ArrayList<Object>::new));
		
		List<File> files = (ArrayList<File>) results
				.stream()
				.filter(x -> x instanceof List)
				.map(x -> (List) x)
				.flatMap(x -> x.stream())
				.filter(x -> x instanceof File)
				.map(x -> (File) x)
				.collect(Collectors.toCollection(ArrayList<File>::new));
		
		List<String> domainNames = dataWrappers
				.stream()
				.map(x -> x.getDomainName())
				.collect(Collectors.toCollection(ArrayList<String>::new));
		
		String domainURL = getDomain(domainNames);
		String domainName = proceedToDomainName(domainURL);
		
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
				crawlEntity.addCrawlContentEntity(cce);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		crawlEntity.setDomainURL(domainName);
		crawlEntity.setCrawlDateTime(LocalDateTime.now());
		
		crawlEntityRepository.save(crawlEntity);
	}
	
	private String proceedToDomainName(String url) {
		
		int indexOfSecondSlash = StringUtils.ordinalIndexOf(url, "/", 2);
		int indexOfThirdSlash = StringUtils.ordinalIndexOf(url, "/", 3);
		
		return url.substring(indexOfSecondSlash + 1, indexOfThirdSlash);
	}
	
	private String getDomain(List<String> domainNames) {
		for(String domain : domainNames) {
			if(domain != null) {
				return domain;
			}
		}
		return "//unknown domain//";
	}
	
}