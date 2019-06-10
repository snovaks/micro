package com.snovaks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.snovaks.entity.CrawlEntity;

public interface CrawlEntityRepository extends JpaRepository<CrawlEntity, Long>, JpaSpecificationExecutor<CrawlEntity> {
}