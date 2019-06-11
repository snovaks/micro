package com.snovaks.service;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.snovaks.entity.CrawlEntity;
import com.snovaks.repository.CrawlContentEntityRepository;
import com.snovaks.repository.CrawlEntityRepository;
import com.snovaks.specification.CrawlEntitySpecification;

@Service
@Transactional
public class DownloadService {

	private CrawlEntityRepository crawlEntityRepository;
	private CrawlContentEntityRepository crawlContentEntityRepository;
	
	@Autowired
	public DownloadService(CrawlEntityRepository crawlEntityRepository, CrawlContentEntityRepository crawlContentEntityRepository) {
		this.crawlEntityRepository = crawlEntityRepository;
		this.crawlContentEntityRepository = crawlContentEntityRepository;
	}	
		
		
		public Page<CrawlEntity> findCrawls(String url, int numberOfSite, int sizeOfSite) {
			PageRequest pageable = new PageRequest(numberOfSite, sizeOfSite);
			Specification<CrawlEntity> spec = CrawlEntitySpecification.withURL(url);
			return crawlEntityRepository.findAll(spec, pageable);
		}
	
		
		public Page<CrawlEntity> findCrawlsAndInitializeChildren(String url, int numberOfSite, int sizeOfSite) {
			Page<CrawlEntity> page = findCrawls(url, numberOfSite, sizeOfSite);
			List<CrawlEntity> list = page.getContent();
			for(CrawlEntity ce : list) {
				Hibernate.initialize(ce.getCrawlContentEntities());
			}
			return page;
		}
	
}