package com.snovaks.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="t_crawl", schema="dbo")
@Data
public class CrawlEntity {

	@Id
	@Column(name="id_crawl")
	private Long id;
	
	@Column(name="domain_url")
	private String domainURL;
	
	@Column(name="crawl_date_time")
	private LocalDateTime crawlDateTime;
	
	@OneToMany(mappedBy="crawlEntity", fetch=FetchType.LAZY)
	private List<CrawlContentEntity> crawlContentEntities;
	
}