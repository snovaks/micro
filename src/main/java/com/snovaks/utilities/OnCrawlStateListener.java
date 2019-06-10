package com.snovaks.utilities;

import java.util.List;

public interface OnCrawlStateListener {

	public void onCrawlStarted();
	public void onCrawlFinished(List<Object> crawlResults);
	
}