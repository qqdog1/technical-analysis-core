package name.qd.analysis.chip.analyzer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants;
import name.qd.analysis.chip.ChipAnalyzers;
import name.qd.analysis.chip.utils.ChipUtils;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.utils.TimeUtil;
import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.CoordinateCacheManager;

public class ChipAnalyzerManager {
	private static Logger log = LoggerFactory.getLogger(ChipAnalyzerManager.class);
	private FileCacheManager fileCacheManager;
	private ChipAnalyzerFactory chipAnalyzerFactory = new ChipAnalyzerFactory();

	public ChipAnalyzerManager(String cachePath) {
		try {
			fileCacheManager = new FileCacheManager(cachePath);
		} catch (Exception e) {
			log.error("Init file cache manager failed.", e);
		}
	}
	
	public List<List<String>> getAnalysisResult(DataSource dataSource, ChipAnalyzers analyzer, String branch, String product, Date from, Date to, double tradeCost, boolean isOpenPnl) {
		log.debug("Trying to run {}", analyzer);
		ChipAnalyzer chipAnalyzer = chipAnalyzerFactory.getAnalyzer(analyzer);
		if(chipAnalyzer == null) {
			log.error("Analyzer not exist. {}", analyzer);
			return null;
		}
		
		checkDailyCache(dataSource, from, to);
		
		List<List<String>> lst = new ArrayList<>();
		lst.add(chipAnalyzer.getHeaderString(branch, product));
		List<List<String>> lstData = chipAnalyzer.analyze(dataSource, fileCacheManager, from, to, branch, product, tradeCost, isOpenPnl);
		if(lstData != null) {
			lst.addAll(lstData);
		}
		return lst;
	}
	
	public int getInputField(ChipAnalyzers analyzer) {
		ChipAnalyzer chipAnalyzer = chipAnalyzerFactory.getAnalyzer(analyzer);
		if(chipAnalyzer == null) {
			log.error("Analyzer not exist. {}", analyzer);
			return 0;
		}
		return chipAnalyzer.getInputField();
	}
	
	private void checkDailyCache(DataSource dataSource, Date from, Date to) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date date = calendar.getTime();
		
		while(!to.before(date)) {
			if(isFolderExist(date)) {
				if(!isCacheExist(date)) {
					log.debug("Cache not exist, transforming ... {}", date);
					transDailyCache(dataSource, date);
					log.debug("Cache transformed. {}", date);
				}
			} 
			
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
		}
	}
	
	private boolean isFolderExist(Date date) {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		Path path = Paths.get(Constants.BSR_FOLDER, sdf.format(date));
		return Files.exists(path);
	}
	
	private boolean isCacheExist(Date date) {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		String cacheName = "bsr_" + sdf.format(date);
		return fileCacheManager.isCacheExist(cacheName);
//		CoordinateCacheManager cacheManager;
//		try {
//			cacheManager = fileCacheManager.getCoordinateCacheInstance("bsr_" + sdf.format(date), DailyOperate.class.getName());
//		} catch (Exception e) {
//			log.error("Fail to check cache.", e);
//			return false;
//		}
//		return cacheManager.values().size() > 0;
	}
	
	private void transDailyCache(DataSource dataSource, Date date) {
		CoordinateCacheManager cacheManager = null;
		try {
			String cacheName = "bsr_" + TimeUtil.getDateFormat().format(date);
			cacheManager = fileCacheManager.getCoordinateCacheInstance(cacheName, DailyOperate.class.getName());
			if(cacheManager.values().size() > 0) {
				return;
			}
		} catch (Exception e) {
			log.error("Get coordinate cache fail, {}", date.toString(), e);
			return;
		}
		
		try {
			Map<Date, Map<String, List<BuySellInfo>>> map = dataSource.getBuySellInfo(date, date);
			if(map.get(date).size() == 0) {
				log.info("{} was not a working day.", TimeUtil.getDateFormat().format(date));
				return;
			}
			Map<String, List<BuySellInfo>> mapProductBSLst = map.get(date);
			for(String product : mapProductBSLst.keySet()) {
				Map<String, DailyOperate> mapResults = new HashMap<>();
				ChipUtils.bsInfoToOperate(dataSource, product, date, mapProductBSLst.get(product), mapResults);
				for(String brokerName : mapResults.keySet()) {
					cacheManager.put(mapResults.get(brokerName));
				}
			}
			
			cacheManager.writeCacheToFile();
		} catch (Exception e) {
			log.error("Get bsr files fail. {}", date.toString(), e);
		}
	}
	
//	public List<DailyOperate> getEffectiveList(DataSource dataSource, Date from, Date to) throws Exception {
//		return getMostEffetiveList(dataSource, from, to);
//	}
	
//	public DailyOperate getMostEffectiveBroker(DataSource dataSource, Date from, Date to) throws Exception {
//		Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
//		return findMostEffective(map);
//	}
	
//	public List<DailyOperate> getMostEffetiveList(DataSource dataSource, Date from, Date to) throws Exception {
//		Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
//		return findMostEffectiveList(map);
//	}
//	
//	public List<DailyOperate> fuzzyQuery(DataSource dataSource, Date from, Date to, String broker, String product, Double tradeCost, Double pnl, Double pnlRate) throws Exception {
//		List<DailyOperate> lst = new ArrayList<>();
//		
//		double costThres = 0;
//		double pnlThres = Double.NEGATIVE_INFINITY;
//		double pnlRateThres = Double.NEGATIVE_INFINITY;
//		if(tradeCost != null) costThres = tradeCost;
//		if(pnl != null) pnlThres = pnl;
//		if(pnlRate != null) pnlRateThres = pnlRate;
//
//		if(product == null) {
//			// Map<product, Map<broker, DailyOperate>>
//			Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
//			if(broker == null) {
//				for(String prod : map.keySet()) {
//					Map<String, DailyOperate> mapDailyOperate = map.get(prod);
//					for(String brokerName : mapDailyOperate.keySet()) {
//						DailyOperate operate = mapDailyOperate.get(brokerName);
//						if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
//							lst.add(operate);
//						}
//					}
//				}
//			} else {
//				for(String prod : map.keySet()) {
//					Map<String, DailyOperate> mapDailyOperate = map.get(prod);
//					DailyOperate operate = mapDailyOperate.get(broker);
//					if(operate != null) {
//						if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
//							lst.add(operate);
//						}
//					}
//				}
//			}
//		} else {
//			// broker, operate
//			Map<String, DailyOperate> map = getAllDailyOperate(dataSource, from, to, product);
//			if(broker == null) {
//				for(String brokerName : map.keySet()) {
//					DailyOperate operate = map.get(brokerName);
//					if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
//						lst.add(operate);
//					}
//				}
//			} else {
//				DailyOperate operate = map.get(broker);
//				if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
//					lst.add(operate);
//				}
//			}
//		}
//		
//		sortByPnlRate(lst);
//		
//		return lst;
//	}
	
//	private Map<String, Map<String, DailyOperate>> getAllDailyOperate(DataSource dataSource, Date from, Date to) throws Exception {
//		Map<Date, Map<String, List<BuySellInfo>>> mapAllInfo = dataSource.getBuySellInfo(from, to);
//		
//		// Map<product, Map<broker, DailyOperate>>
//		Map<String, Map<String, DailyOperate>> map = new HashMap<>();
//		
//		for(Date date : mapAllInfo.keySet()) {
//			Map<String, List<BuySellInfo>> mapDayInfo = mapAllInfo.get(date);
//			for(String product : mapDayInfo.keySet()) {
//				if(!map.containsKey(product)) {
//					map.put(product, new HashMap<>());
//				}
//				List<BuySellInfo> lst = mapDayInfo.get(product);
//				ChipUtils.bsInfoToOperate(lst, map.get(product));
//			}
//		}
//		return map;
//	}
	
//	private Map<String, DailyOperate> getAllDailyOperate(DataSource dataSource, Date from, Date to, String product) throws Exception {
//		Map<Date, List<BuySellInfo>> mapInfo = dataSource.getBuySellInfo(product, from, to);
//		// broker dailyOperate
//		Map<String, DailyOperate> map = new HashMap<>();
//		for(Date date : mapInfo.keySet()) {
//			List<BuySellInfo> lst = mapInfo.get(date);
//			ChipUtils.bsInfoToOperate(lst, map);
//		}
//		return map;
//	}
	
//	private DailyOperate findMostEffective(Map<String, Map<String, DailyOperate>> map) {
//		DailyOperate operate = new DailyOperate();
//		for(String product : map.keySet()) {
//			Map<String, DailyOperate> mapDailyOperate = map.get(product);
//			for(String broker : mapDailyOperate.keySet()) {
//				DailyOperate compareOperate = mapDailyOperate.get(broker);
//				double comparePnlRate = compareOperate.getClosePnl()/compareOperate.getTradeCost();
//				double pnlRate = operate.getClosePnl()/operate.getTradeCost();
//				if(Double.isNaN(pnlRate)) pnlRate = 0;
//				if(comparePnlRate > pnlRate) {
//					operate = compareOperate;
//				}
//			}
//		}
//		return operate;
//	}
	
//	private List<DailyOperate> findMostEffectiveList(Map<String, Map<String, DailyOperate>> map) {
//		List<DailyOperate> lst = new ArrayList<>();
//		for(String product : map.keySet()) {
//			Map<String, DailyOperate> mapDailyOperate = map.get(product);
//			for(String broker : mapDailyOperate.keySet()) {
//				DailyOperate operate = mapDailyOperate.get(broker);
//				if(operate.getPnl() > 0 && operate.getPnlRate() > PNL_RATE_THRESHOLD) {
//					lst.add(operate);
//				}
//			}
//		}
//		
//		sortByPnlRate(lst);
//		
//		return lst;
//	}
	
//	private void sortByPnlRate(List<DailyOperate> lst) {
//		Comparator<DailyOperate> comp = (DailyOperate o1, DailyOperate o2) -> {
//			Double d1 = o1.getPnlRate();
//			Double d2 = o2.getPnlRate();
//			return d2.compareTo(d1);
//		};
//		
//		Collections.sort(lst, comp);
//	}
}
