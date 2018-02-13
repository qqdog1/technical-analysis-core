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
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg10Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg120Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg20Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg240Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg5Day;
import name.qd.techAnalyst.analyzer.impl.ma.MovingAvg60Day;
import name.qd.techAnalyst.dataSource.DataManager;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.AnalysisResult;

public class TechAnalyzerManager {
	private Logger log = LogManager.getLogger(TechAnalyzerManager.class);
	private Map<String, TechAnalyzer> mapAnalyzer = new HashMap<>();
	private FileCacheManager fileCacheManager;
	private Map<String, Date> mapFirst = new HashMap<String, Date>();
	private Map<String, Date> mapLast = new HashMap<String, Date>();
	private SimpleDateFormat sdf = TimeUtil.getDateFormat();
	
	public TechAnalyzerManager() {
		try {
			fileCacheManager = new FileCacheManager("./cache/");
		} catch (Exception e) {
			log.error("Init file cache manager failed.", e);
		}
		
		mapAnalyzer.put(MovingAvg5Day.class.getSimpleName(), new MovingAvg5Day());
		mapAnalyzer.put(MovingAvg10Day.class.getSimpleName(), new MovingAvg10Day());
		mapAnalyzer.put(MovingAvg20Day.class.getSimpleName(), new MovingAvg20Day());
		mapAnalyzer.put(MovingAvg60Day.class.getSimpleName(), new MovingAvg60Day());
		mapAnalyzer.put(MovingAvg120Day.class.getSimpleName(), new MovingAvg120Day());
		mapAnalyzer.put(MovingAvg240Day.class.getSimpleName(), new MovingAvg240Day());
		
		for(String className : mapAnalyzer.keySet()) {
			log.info("Created analyzer instance: {}", className);
		}
	}
	
	public ArrayList<AnalysisResult> getAnalysisResult(DataManager dataManager, String analyzerName, String product, Date from, Date to) {
		if(!mapAnalyzer.containsKey(analyzerName)) {
			log.error("Analyzer not exist. {}", analyzerName);
			return null;
		}
		TechAnalyzer techAnalyzer = mapAnalyzer.get(analyzerName);
		String cacheName = techAnalyzer.getCacheName(product);
		
		if(!isDateInRange(cacheName, from, to)) {
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
		calendar.setTime(from);
		for(; calendar.getTime().before(to); calendar.add(Calendar.DATE, 1)) {
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
	
	private boolean isDateInRange(String cacheName, Date from, Date to) {
		loadCache(cacheName);
		
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
	
	private void loadCache(String cacheName) {
		CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
		if(cacheManager != null) {
			for(FileCacheObject obj : cacheManager.values()) {
				AnalysisResult result = (AnalysisResult) obj;
				Date date = result.getDate();
				updateFirstDate(cacheName, date);
				updateLastDate(cacheName, date);
			}
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
	
	private void updateCache(DataManager dataManager, TechAnalyzer analyzer, String product, Date from, Date to) {
		String cacheName = analyzer.getCacheName(product);
		Date first = getFirstDate(cacheName, from);
		Date last = getLastDate(cacheName, to);
		List<AnalysisResult> lst = analyzer.analyze(dataManager, product, first, last);
		putAnalysisResult(cacheName, lst);
		syncFile(cacheName);
	}
}