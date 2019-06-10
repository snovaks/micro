package com.snovaks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snovaks.entity.CrawlContentEntity;

public interface CrawlContentEntityRepository extends JpaRepository<CrawlContentEntity, Long> {
}