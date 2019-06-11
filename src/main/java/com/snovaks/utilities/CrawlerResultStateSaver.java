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

	private CrawlEntityRepository crawlEntityRepository;
	private CrawlContentEntityRepository crawlContentEntityRepository;
	
	public CrawlerResultStateSaver(CrawlEntityRepository crawlEntityRepository,
			CrawlContentEntityRepository crawlContentEntityRepository) {
		this.crawlEntityRepository = crawlEntityRepository;
		this.crawlContentEntityRepository = crawlContentEntityRepository;
	}
	
	@Override
	public void onCrawlStarted() {
		log.info("onCrawl started");
	}
	
	@Override
	public void onCrawlFinished(List<Object> crawlResults) {
		
		log.info("CrawlerResultStateSaver - onCrawlFinished method ... saving");
		
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
		
		for(int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			CrawlContentEntity cce = new CrawlContentEntity();
			Magic magic = new Magic();
			try {
				MagicMatch match = magic.getMagicMatch(file, false);
				String contentType = match.getMimeType();
				byte[] content = Files.readAllBytes(file.toPath());
				cce.setContent(content);
				cce.setContentType(contentType);
				crawlEntity.addCrawlContentEntity(cce);
			} catch (Exception e) {
				log.warn("Exception during file processing", e);
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