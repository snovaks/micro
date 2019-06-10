package com.snovaks.specification;

import org.springframework.data.jpa.domain.Specification;

import com.snovaks.entity.CrawlEntity;

public interface CrawlEntitySpecification {

	public static Specification<CrawlEntity> withURL(String url) {
		return (root, cq, cb ) -> {
			return cb.equal(root.get("domainURL"), url);
		};
	}
	
}