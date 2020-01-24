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
import name.qd.analysis.dataSource.TWSE.utils.TWSEPathUtil;
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
	
	public List<List<String>> getAnalysisResult(DataSource dataSource, ChipAnalyzers analyzer, String branch, String product, Date from, Date to, double tradeCost, boolean isOpenPnl, String ... inputs) {
		log.debug("Trying to run {}", analyzer);
		ChipAnalyzer chipAnalyzer = chipAnalyzerFactory.getAnalyzer(analyzer, this);
		if(chipAnalyzer == null) {
			log.error("Analyzer not exist. {}", analyzer);
			return null;
		}
		
		checkDailyCache(dataSource, from, to);
		
		List<List<String>> lst = new ArrayList<>();
		lst.add(chipAnalyzer.getHeaderString(branch, product));
		List<List<String>> lstData = chipAnalyzer.analyze(dataSource, fileCacheManager, from, to, branch, product, tradeCost, isOpenPnl, inputs);
		if(lstData != null) {
			lst.addAll(lstData);
		}
		return lst;
	}
	
	public int getInputField(ChipAnalyzers analyzer) {
		ChipAnalyzer chipAnalyzer = chipAnalyzerFactory.getAnalyzer(analyzer, this);
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
					log.info("Cache not exist, transforming ... {}", date);
					transDailyCache(dataSource, date);
					log.info("Cache transformed. {}", date);
				}
			} 
			
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
		}
	}
	
	private boolean isFolderExist(Date date) {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		Path path = Paths.get(TWSEPathUtil.BUY_SELL_INFO_DIR, sdf.format(date));
		return Files.exists(path);
	}
	
	private boolean isCacheExist(Date date) {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		String cacheName = Constants.getBSRCacheName(sdf.format(date));
		return fileCacheManager.isCacheExist(cacheName);
	}
	
	private void transDailyCache(DataSource dataSource, Date date) {
		CoordinateCacheManager cacheManager = null;
		try {
			SimpleDateFormat sdf = TimeUtil.getDateFormat();
			String cacheName = Constants.getBSRCacheName(sdf.format(date));
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
}
