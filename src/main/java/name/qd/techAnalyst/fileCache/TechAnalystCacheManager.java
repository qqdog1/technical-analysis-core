package name.qd.techAnalyst.fileCache;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.CacheManager;
import name.qd.fileCache.cache.IFileCacheObject;
import name.qd.techAnalyst.analyzer.impl.MovingAvg10Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg120Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg20Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg240Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg5Day;
import name.qd.techAnalyst.analyzer.impl.MovingAvg60Day;
import name.qd.techAnalyst.vo.AnalysisResult;

public class TechAnalystCacheManager {
	// 因為不知道實際交易日 和避免一些麻煩的補檔邏輯
	// 幫檔案紀錄 最前和做後時間 有超過就更新
	// EX: 1. 先抓了20160505 ~ 20160606的資料
	//     2. 要求20160707 ~ 20160808
	//     3. 直接補完 1的最後時間20160606到新的最後時間20160808
	// 往前亦同
	private Logger logger = LogManager.getLogger(TechAnalystCacheManager.class);
	private FileCacheManager fileCacheManager;
	private Map<String, Date> mapFirst = new HashMap<String, Date>();
	private Map<String, Date> mapLast = new HashMap<String, Date>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public TechAnalystCacheManager() {
		fileCacheManager = new FileCacheManager("./cache/");
		
		initCache(MovingAvg5Day.class.getSimpleName(), AnalysisResult.class.getName());
		initCache(MovingAvg10Day.class.getSimpleName(), AnalysisResult.class.getName());
		initCache(MovingAvg20Day.class.getSimpleName(), AnalysisResult.class.getName());
		initCache(MovingAvg60Day.class.getSimpleName(), AnalysisResult.class.getName());
		initCache(MovingAvg120Day.class.getSimpleName(), AnalysisResult.class.getName());
		initCache(MovingAvg240Day.class.getSimpleName(), AnalysisResult.class.getName());
		
		recoverDayRange();
	}
	
	public void putAnalysisResult(String cacheName, List<AnalysisResult> lst) {
		CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
		for(AnalysisResult result : lst) {
			try {
				Date date = sdf.parse(result.getDate());
				updateFirstDate(cacheName, date);
				updateLastDate(cacheName, date);
				cacheManager.put(result.getDate(), result);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	public ArrayList<AnalysisResult> getAnalysisResult(String cacheName, String from, String to) {
		ArrayList<AnalysisResult> lst = new ArrayList<AnalysisResult>();
		try {
			Date fromDate = sdf.parse(from);
			Date toDate = sdf.parse(to);
			CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fromDate);
			for(; calendar.getTime().before(toDate); calendar.add(Calendar.DATE, 1)) {
				AnalysisResult result = (AnalysisResult) cacheManager.get(sdf.format(calendar.getTime()));
				if(result != null) {
					lst.add(result);
				}
			}
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return lst;
	}
	
	public boolean isDateInRange(String cacheName, String from, String to) {
		if(!mapFirst.containsKey(cacheName) || !mapLast.containsKey(cacheName)) {
			return false;
		}
		
		try {
			Date fromDate = sdf.parse(from);
			Date toDate = sdf.parse(to);
			Date firstDate = mapFirst.get(cacheName);
			Date lastDate = mapLast.get(cacheName);
			if(fromDate.before(firstDate)) {
				return false;
			}
			if(toDate.after(lastDate)) {
				return false;
			}
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
	
	public String getFirstDateString(String cacheName, String sDate) {
		if(!mapFirst.containsKey(cacheName) || !mapLast.containsKey(cacheName)) {
			return sDate;
		}
		
		try {
			Date date = sdf.parse(sDate);
			Date firstDate = mapFirst.get(cacheName);
			Date lastDate = mapLast.get(cacheName);
			
			if(date.before(firstDate)) {
				return sDate;
			}
			return sdf.format(lastDate);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String getLastDateString(String cacheName, String sDate) {
		if(!mapFirst.containsKey(cacheName) || !mapLast.containsKey(cacheName)) {
			return sDate;
		}
		
		try {
			Date date = sdf.parse(sDate);
			Date firstDate = mapFirst.get(cacheName);
			Date lastDate = mapLast.get(cacheName);
			
			if(date.after(lastDate)) {
				return sDate;
			}
			return sdf.format(firstDate);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public void syncFile(String cacheName) {
		CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
		if(cacheManager != null) {
			try {
				cacheManager.writeCacheToFile();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
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
	
	private void initCache(String cacheName, String className) {
		if(fileCacheManager.getCacheInstance(cacheName) == null) {
			fileCacheManager.createCacheInstance(cacheName, className);
		}
	}
	
	private void recoverDayRange() {
		Set<String> set = fileCacheManager.getCacheNameSet();
		for(String cacheName : set) {
			CacheManager cacheManager = fileCacheManager.getCacheInstance(cacheName);
			for(IFileCacheObject obj : cacheManager.values()) {
				AnalysisResult result = (AnalysisResult) obj;
				try {
					Date date = sdf.parse(result.getDate());
					updateFirstDate(cacheName, date);
					updateLastDate(cacheName, date);
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
}
