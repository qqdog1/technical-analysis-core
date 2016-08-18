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
	
	public void putAnalysisResult(String sCacheName, List<AnalysisResult> lst) {
		CacheManager cacheManager = fileCacheManager.getCacheInstance(sCacheName);
		for(AnalysisResult result : lst) {
			try {
				Date date = sdf.parse(result.getDate());
				updateFirstDate(sCacheName, date);
				updateLastDate(sCacheName, date);
				cacheManager.put(result.getDate(), result);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<AnalysisResult> getAnalysisResult(String sCacheName, String sFrom, String sTo) {
		ArrayList<AnalysisResult> lst = new ArrayList<AnalysisResult>();
		try {
			Date fromDate = sdf.parse(sFrom);
			Date toDate = sdf.parse(sTo);
			CacheManager cacheManager = fileCacheManager.getCacheInstance(sCacheName);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fromDate);
			for(; calendar.getTime().before(toDate); calendar.add(Calendar.DATE, 1)) {
				AnalysisResult result = (AnalysisResult) cacheManager.get(sdf.format(calendar.getTime()));
				if(result != null) {
					lst.add(result);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lst;
	}
	
	public boolean isDateInRange(String sCacheName, String sFrom, String sTo) {
		if(!mapFirst.containsKey(sCacheName) || !mapLast.containsKey(sCacheName)) {
			return false;
		}
		
		try {
			Date fromDate = sdf.parse(sFrom);
			Date toDate = sdf.parse(sTo);
			Date firstDate = mapFirst.get(sCacheName);
			Date lastDate = mapLast.get(sCacheName);
			if(fromDate.before(firstDate)) {
				return false;
			}
			if(toDate.after(lastDate)) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public String getFirstDateString(String sCacheName, String sDate) {
		if(!mapFirst.containsKey(sCacheName) || !mapLast.containsKey(sCacheName)) {
			return sDate;
		}
		
		try {
			Date date = sdf.parse(sDate);
			Date firstDate = mapFirst.get(sCacheName);
			Date lastDate = mapLast.get(sCacheName);
			
			if(date.before(firstDate)) {
				return sDate;
			}
			return sdf.format(lastDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getLastDateString(String sCacheName, String sDate) {
		if(!mapFirst.containsKey(sCacheName) || !mapLast.containsKey(sCacheName)) {
			return sDate;
		}
		
		try {
			Date date = sdf.parse(sDate);
			Date firstDate = mapFirst.get(sCacheName);
			Date lastDate = mapLast.get(sCacheName);
			
			if(date.after(lastDate)) {
				return sDate;
			}
			return sdf.format(firstDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void syncFile(String sCacheName) {
		CacheManager cacheManager = fileCacheManager.getCacheInstance(sCacheName);
		if(cacheManager != null) {
			try {
				cacheManager.writeCacheToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateFirstDate(String sCacheName, Date date) {
		if(!mapFirst.containsKey(sCacheName)) {
			mapFirst.put(sCacheName, date);
		}
		Date firstDate = mapFirst.get(sCacheName);
		if(date.before(firstDate)) {
			mapFirst.put(sCacheName, date);
		}
	}
	
	private void updateLastDate(String sCacheName, Date date) {
		if(!mapLast.containsKey(sCacheName)) {
			mapLast.put(sCacheName, date);
		}
		Date lastDate = mapLast.get(sCacheName);
		if(date.after(lastDate)) {
			mapLast.put(sCacheName, date);
		}
	}
	
	private void initCache(String sCacheName, String sClassName) {
		if(fileCacheManager.getCacheInstance(sCacheName) == null) {
			fileCacheManager.createCacheInstance(sCacheName, sClassName);
		}
	}
	
	private void recoverDayRange() {
		Set<String> set = fileCacheManager.getCacheNameSet();
		for(String sCacheName : set) {
			CacheManager cacheManager = fileCacheManager.getCacheInstance(sCacheName);
			for(IFileCacheObject obj : cacheManager.values()) {
				AnalysisResult result = (AnalysisResult) obj;
				try {
					Date date = sdf.parse(result.getDate());
					updateFirstDate(sCacheName, date);
					updateLastDate(sCacheName, date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
