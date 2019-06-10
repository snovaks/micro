package com.snovaks.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="t_crawl_content", schema="dbo")
@Data
public class CrawlContentEntity {

	@Id
	@Column(name="id_crawl_content")
	private Long id;
	
	@Column(name="content_type")
	private String contentType;
	
	@ManyToOne
	@JoinColumn(name="crawl_id")
	private CrawlEntity crawlEntity;
	
	@Lob
	@Column(name="content")
	private byte[] content;
	
}