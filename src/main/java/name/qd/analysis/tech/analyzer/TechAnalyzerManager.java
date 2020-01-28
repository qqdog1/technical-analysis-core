package name.qd.analysis.tech.analyzer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.utils.TimeUtils;
import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.FileCacheObject;
import name.qd.fileCache.cache.NormalCacheManager;

public class TechAnalyzerManager {
	private static Logger log = LoggerFactory.getLogger(TechAnalyzerManager.class);
	private FileCacheManager fileCacheManager;
	private Map<String, Date> mapFirst = new HashMap<String, Date>();
	private Map<String, Date> mapLast = new HashMap<String, Date>();
	private SimpleDateFormat sdf = TimeUtils.getDateTimeFormat();
	protected TechAnalyzerFactory techAnalyzerFactory = new TechAnalyzerFactory();
	private String className = AnalysisResult.class.getName();
	
	public TechAnalyzerManager(String cachePath) {
		try {
			fileCacheManager = new FileCacheManager(cachePath);
		} catch (Exception e) {
			log.error("Init file cache manager failed.", e);
		}
	}
	
	public List<AnalysisResult> getAnalysisResult(DataSource dataSource, TechAnalyzers analyzer, String product, Date from, Date to) throws Exception {
		TechAnalyzer techAnalyzer = techAnalyzerFactory.getAnalyzer(analyzer, this);
		if(techAnalyzer == null) {
			log.error("Analyzer not exist. {}", analyzer);
			return null;
		}
		
		log.info("Get analyzer : {}", analyzer);
		
		String cacheName = techAnalyzer.getCacheName(product);
		List<AnalysisResult> lst = new ArrayList<AnalysisResult>();
		
		if(!isDateInRange(techAnalyzer, product, from, to)) {
			log.info("Result data not in range.");
			// analyze new data and cache
			updateCache(dataSource, techAnalyzer, product, from, to);
		}
		
		NormalCacheManager cacheManager = fileCacheManager.getNormalCacheInstance(cacheName, className);
		
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
	
	public List<AnalysisResult> getCustomAnalysisResult(DataSource dataSource, TechAnalyzers analyzer, String product, Date from, Date to, String ... inputs) throws Exception {
		TechAnalyzer techAnalyzer = techAnalyzerFactory.getAnalyzer(analyzer, this);
		if(techAnalyzer == null) {
			log.error("Analyzer not exist. {}", analyzer);
			return null;
		}
		return techAnalyzer.customResult(dataSource, product, from, to, inputs);
	}
	
	public List<String> getCustomDescription(TechAnalyzers analyzer) {
		TechAnalyzer techAnalyzer = techAnalyzerFactory.getAnalyzer(analyzer, this);
		if(techAnalyzer == null) {
			log.error("Analyzer not exist. {}", analyzer);
			return null;
		}
		return techAnalyzer.getCustomDescreption();
	}
	
	public AnalyzerType getAnalyzerType(TechAnalyzers analyzer) {
		TechAnalyzer techAnalyzer = techAnalyzerFactory.getAnalyzer(analyzer, this);
		if(techAnalyzer == null) {
			log.error("Analyzer not exist. {}", analyzer);
			return null;
		}
		return techAnalyzer.getAnalyzerType();
	}
	
	private void putAnalysisResult(String cacheName, List<AnalysisResult> lst) {
		NormalCacheManager cacheManager;
		try {
			cacheManager = fileCacheManager.getNormalCacheInstance(cacheName, className);
			for(AnalysisResult result : lst) {
				Date date = result.getDate();
				updateFirstDate(cacheName, date);
				updateLastDate(cacheName, date);
				cacheManager.put(result);
			}
		} catch (Exception e) {
			log.error("Get {} cache failed.", cacheName, e);
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
		try {
			NormalCacheManager cacheManager = fileCacheManager.getNormalCacheInstance(cacheName, className);
			for(FileCacheObject obj : cacheManager.values()) {
				AnalysisResult result = (AnalysisResult) obj;
				Date date = result.getDate();
				updateFirstDate(cacheName, date);
				updateLastDate(cacheName, date);
			}
		} catch (Exception e) {
			log.error("Get {} cache failed.", cacheName, e);
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
		try {
			NormalCacheManager cacheManager = fileCacheManager.getNormalCacheInstance(cacheName, className);
			cacheManager.writeCacheToFile();
		} catch (Exception e) {
			log.error("Get {} cache failed.", cacheName, e);
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
	
	private void updateCache(DataSource dataSource, TechAnalyzer analyzer, String product, Date from, Date to) throws Exception {
		String cacheName = analyzer.getCacheName(product);
		Date first = getFirstDate(cacheName, from);
		Date last = getLastDate(cacheName, to);
		log.info("Analyze {}, {}-{}", cacheName, first, last);
		List<AnalysisResult> lst = analyzer.analyze(dataSource, product, first, last);
		putAnalysisResult(cacheName, lst);
		syncFile(cacheName);
	}
}