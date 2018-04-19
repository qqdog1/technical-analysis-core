package name.qd.analysis.chip.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.utils.ChipUtils;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;

public class ChipAnalyzerManager {
	private static Logger log = LoggerFactory.getLogger(ChipAnalyzerManager.class);
	private static ChipAnalyzerManager instance = new ChipAnalyzerManager();
	private static double PNL_RATE_THRESHOLD = 0.001;
	
	public static ChipAnalyzerManager getInstance() {
		return instance;
	}

	private ChipAnalyzerManager() {
	}
	
	public List<DailyOperate> getEffectiveList(DataSource dataSource, Date from, Date to) throws Exception {
		return getMostEffetiveList(dataSource, from, to);
	}
	
	public DailyOperate getMostEffectiveBroker(DataSource dataSource, Date from, Date to) throws Exception {
		Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
		return findMostEffective(map);
	}
	
	public List<DailyOperate> getMostEffetiveList(DataSource dataSource, Date from, Date to) throws Exception {
		Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
		return findMostEffectiveList(map);
	}
	
	public List<DailyOperate> fuzzyQuery(DataSource dataSource, Date from, Date to, String broker, String product, Double tradeCost, Double pnl, Double pnlRate) throws Exception {
		List<DailyOperate> lst = new ArrayList<>();
		
		double costThres = 0;
		double pnlThres = Double.NEGATIVE_INFINITY;
		double pnlRateThres = Double.NEGATIVE_INFINITY;
		if(tradeCost != null) costThres = tradeCost;
		if(pnl != null) pnlThres = pnl;
		if(pnlRate != null) pnlRateThres = pnlRate;

		if(product == null) {
			// Map<product, Map<broker, DailyOperate>>
			Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
			if(broker == null) {
				for(String prod : map.keySet()) {
					Map<String, DailyOperate> mapDailyOperate = map.get(prod);
					for(String brokerName : mapDailyOperate.keySet()) {
						DailyOperate operate = mapDailyOperate.get(brokerName);
						if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
							lst.add(operate);
						}
					}
				}
			} else {
				for(String prod : map.keySet()) {
					Map<String, DailyOperate> mapDailyOperate = map.get(prod);
					DailyOperate operate = mapDailyOperate.get(broker);
					if(operate != null) {
						if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
							lst.add(operate);
						}
					}
				}
			}
		} else {
			// broker, operate
			Map<String, DailyOperate> map = getAllDailyOperate(dataSource, from, to, product);
			if(broker == null) {
				for(String brokerName : map.keySet()) {
					DailyOperate operate = map.get(brokerName);
					if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
						lst.add(operate);
					}
				}
			} else {
				DailyOperate operate = map.get(broker);
				if(operate.getTradeCost() > costThres && operate.getPnl() > pnlThres && operate.getPnlRate() > pnlRateThres) {
					lst.add(operate);
				}
			}
		}
		
		sortByPnlRate(lst);
		
		return lst;
	}
	
	private Map<String, Map<String, DailyOperate>> getAllDailyOperate(DataSource dataSource, Date from, Date to) throws Exception {
		Map<Date, Map<String, List<BuySellInfo>>> mapAllInfo = dataSource.getBuySellInfo(from, to);
		
		// Map<product, Map<broker, DailyOperate>>
		Map<String, Map<String, DailyOperate>> map = new HashMap<>();
		
		for(Date date : mapAllInfo.keySet()) {
			Map<String, List<BuySellInfo>> mapDayInfo = mapAllInfo.get(date);
			for(String product : mapDayInfo.keySet()) {
				if(!map.containsKey(product)) {
					map.put(product, new HashMap<>());
				}
				List<BuySellInfo> lst = mapDayInfo.get(product);
				ChipUtils.bsInfoToOperate(lst, map.get(product));
			}
		}
		return map;
	}
	
	private Map<String, DailyOperate> getAllDailyOperate(DataSource dataSource, Date from, Date to, String product) throws Exception {
		Map<Date, List<BuySellInfo>> mapInfo = dataSource.getBuySellInfo(product, from, to);
		// broker dailyOperate
		Map<String, DailyOperate> map = new HashMap<>();
		for(Date date : mapInfo.keySet()) {
			List<BuySellInfo> lst = mapInfo.get(date);
			ChipUtils.bsInfoToOperate(lst, map);
		}
		return map;
	}
	
	private DailyOperate findMostEffective(Map<String, Map<String, DailyOperate>> map) {
		DailyOperate operate = new DailyOperate();
		for(String product : map.keySet()) {
			Map<String, DailyOperate> mapDailyOperate = map.get(product);
			for(String broker : mapDailyOperate.keySet()) {
				DailyOperate compareOperate = mapDailyOperate.get(broker);
				double comparePnlRate = compareOperate.getPnl()/compareOperate.getTradeCost();
				double pnlRate = operate.getPnl()/operate.getTradeCost();
				if(Double.isNaN(pnlRate)) pnlRate = 0;
				if(comparePnlRate > pnlRate) {
					operate = compareOperate;
				}
			}
		}
		return operate;
	}
	
	private List<DailyOperate> findMostEffectiveList(Map<String, Map<String, DailyOperate>> map) {
		List<DailyOperate> lst = new ArrayList<>();
		for(String product : map.keySet()) {
			Map<String, DailyOperate> mapDailyOperate = map.get(product);
			for(String broker : mapDailyOperate.keySet()) {
				DailyOperate operate = mapDailyOperate.get(broker);
				if(operate.getPnl() > 0 && operate.getPnlRate() > PNL_RATE_THRESHOLD) {
					lst.add(operate);
				}
			}
		}
		
		sortByPnlRate(lst);
		
		return lst;
	}
	
	private void sortByPnlRate(List<DailyOperate> lst) {
		Comparator<DailyOperate> comp = (DailyOperate o1, DailyOperate o2) -> {
			Double d1 = o1.getPnlRate();
			Double d2 = o2.getPnlRate();
			return d2.compareTo(d1);
		};
		
		Collections.sort(lst, comp);
	}
}
