package name.qd.techAnalyst.analyzer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.CacheManager;
import name.qd.fileCache.cache.FileCacheObject;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.AnalysisResult;

public class TechAnalyzerManager {
	private Logger log = LogManager.getLogger(TechAnalyzerManager.class);
	private FileCacheManager fileCacheManager;
	private Map<String, Date> mapFirst = new HashMap<String, Date>();
	private Map<String, Date> mapLast = new HashMap<String, Date>();
	private SimpleDateFormat sdf = TimeUtil.getDateTimeFormat();
	private TechAnalyzerFactory techAnalyzerFactory = TechAnalyzerFactory.getInstance();
	
	public TechAnalyzerManager() {
		try {
			fileCacheManager = new FileCacheManager("./cache/");
		} catch (Exception e) {
			log.error("Init file cache manager failed.", e);
		}
	}
	
	public List<AnalysisResult> getAnalysisResult(DataSource dataManager, String analyzerName, String product, Date from, Date to) {
		if(techAnalyzerFactory.getAnalyzer(analyzerName) == null) {
			log.error("Analyzer not exist. {}", analyzerName);
			return null;
		}
		
		log.info("Get analyzer : {}", analyzerName);
		TechAnalyzer techAnalyzer = techAnalyzerFactory.getAnalyzer(analyzerName);
		String cacheName = techAnalyzer.getCacheName(product);
		
		if(!isDateInRange(techAnalyzer, product, from, to)) {
			log.info("Result data not in range.");
			// get data source
			try {
				dataManager.checkDataAndDownload(product, from, to);
			} catch (Exception e) {
				log.error("download data failed. product:{}, {}-{}", product, from.toString(), to.toString());
			}
			// analyze new data and cache
			updateCache(dataManager, techAnalyzer, product, from, to);
		}
		
		ArrayList<AnalysisResult> lst = new ArrayList<AnalysisResult>();
		CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(to);
		calendar.add(Calendar.DATE, 1);
		Date endDate = calendar.getTime();
		calendar.setTime(from);
		for(; calendar.getTime().before(endDate); calendar.add(Calendar.DATE, 1)) {
			AnalysisResult result = (AnalysisResult) cacheManager.get(sdf.format(calendar.getTime()));
			if(result != null) {
				lst.add(result);
			}
		}
		return lst;
	}
	
	private void putAnalysisResult(String cacheName, List<AnalysisResult> lst) {
		CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
		for(AnalysisResult result : lst) {
			Date date = result.getDate();
			updateFirstDate(cacheName, date);
			updateLastDate(cacheName, date);
			cacheManager.put(result.getKeyString(), result);
		}
	}
	
	private boolean isDateInRange(TechAnalyzer techAnalyzer, String product, Date from, Date to) {
		String cacheName = techAnalyzer.getCacheName(product);
		loadCache(techAnalyzer, product);
		
		if(!mapFirst.containsKey(cacheName) || !mapLast.containsKey(cacheName)) {
			return false;
		}
		
		Date firstDate = mapFirst.get(cacheName);
		Date lastDate = mapLast.get(cacheName);
		if(from.before(firstDate)) {
			return false;
		}
		if(to.after(lastDate)) {
			return false;
		}
		return true;
	}
	
	private void loadCache(TechAnalyzer techAnalyzer, String product) {
		String cacheName = techAnalyzer.getCacheName(product);
		CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
		if(cacheManager != null) {
			for(FileCacheObject obj : cacheManager.values()) {
				AnalysisResult result = (AnalysisResult) obj;
				Date date = result.getDate();
				updateFirstDate(cacheName, date);
				updateLastDate(cacheName, date);
			}
		} else {
			fileCacheManager.createCacheInstance(cacheName, AnalysisResult.class.getName());
		}
	}
	
	private Date getFirstDate(String cacheName, Date date) {
		if(!mapFirst.containsKey(cacheName) || !mapLast.containsKey(cacheName)) {
			return date;
		}
		
		Date firstDate = mapFirst.get(cacheName);
		Date lastDate = mapLast.get(cacheName);
		if(date.before(firstDate)) {
			return date;
		}
		return lastDate;
	}
	
	private Date getLastDate(String cacheName, Date date) {
		if(!mapFirst.containsKey(cacheName) || !mapLast.containsKey(cacheName)) {
			return date;
		}
		
		Date firstDate = mapFirst.get(cacheName);
		Date lastDate = mapLast.get(cacheName);
		if(date.after(lastDate)) {
			return date;
		}
		return firstDate;
	}
	
	private void syncFile(String cacheName) {
		CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
		if(cacheManager != null) {
			try {
				cacheManager.writeCacheToFile();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	private void updateFirstDate(String cacheName, Date date) {
		if(!mapFirst.containsKey(cacheName)) {
			mapFirst.put(cacheName, date);
		}
		Date firstDate = mapFirst.get(cacheName);
		if(date.before(firstDate)) {
			mapFirst.put(cacheName, date);
		}
	}
	
	private void updateLastDate(String cacheName, Date date) {
		if(!mapLast.containsKey(cacheName)) {
			mapLast.put(cacheName, date);
		}
		Date lastDate = mapLast.get(cacheName);
		if(date.after(lastDate)) {
			mapLast.put(cacheName, date);
		}
	}
	
	private void updateCache(DataSource dataManager, TechAnalyzer analyzer, String product, Date from, Date to) {
		String cacheName = analyzer.getCacheName(product);
		Date first = getFirstDate(cacheName, from);
		Date last = getLastDate(cacheName, to);
		log.info("Analyze {}, {}-{}", cacheName, first, last);
		List<AnalysisResult> lst = analyzer.analyze(dataManager, product, first, last);
		putAnalysisResult(cacheName, lst);
		syncFile(cacheName);
	}
}