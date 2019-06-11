package com.snovaks.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snovaks.entity.CrawlContentEntity;
import com.snovaks.entity.CrawlEntity;
import com.snovaks.service.DownloadService;

@RestController
@RequestMapping("/api/download")
public class DownloadController {

	private DownloadService downloadService;
	
	@Autowired
	public DownloadController(DownloadService downloadService) {
		this.downloadService = downloadService;
	}

	@GetMapping(value="/{url}", produces="application/zip")
	public void getZipFiles(@PathVariable String url, HttpServletResponse response) throws IOException {
		int numberOfSite = 0;
		int sizeOfSite = 2;
		Page<CrawlEntity> page = downloadService.findCrawlsAndInitializeChildren(url, numberOfSite, sizeOfSite);
		List<CrawlEntity> list = page.getContent();
		response.setStatus(HttpServletResponse.SC_OK);
		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
		
		for(CrawlEntity ce : list) {
			for(CrawlContentEntity cce : ce.getCrawlContentEntities()) {
				String fileName = ce.getDomainURL() + ce.getId() + "/" + cce.getId();
				File file = File.createTempFile(fileName, ".bin");
				byte[] bytes = cce.getContent();
				OutputStream os = new FileOutputStream(file);
				os.write(bytes);
				os.close();
				zipOutputStream.putNextEntry(new ZipEntry(fileName));
				FileInputStream fis = new FileInputStream(file);
				IOUtils.copy(fis, zipOutputStream);
				fis.close();
				zipOutputStream.closeEntry();
			}
		}
		zipOutputStream.close();
	}
	
}