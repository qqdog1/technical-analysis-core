package name.qd.analysis.chip.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.qd.analysis.chip.utils.ChipUtils;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;

public class MostEffective {
	private static double PNL_RATE_THRESHOLD = 0.001;
	
	public MostEffective() {
	}
	
	public DailyOperate getMostEffectiveBroker(DataSource dataSource, Date from, Date to) throws Exception {
		Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
		return findMostEffective(map);
	}
	
	public List<DailyOperate> getMostEffetiveList(DataSource dataSource, Date from, Date to) throws Exception {
		Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
		return findMostEffectiveList(map);
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
		
		Comparator<DailyOperate> comp = (DailyOperate o1, DailyOperate o2) -> {
			Double d1 = o1.getPnlRate();
			Double d2 = o2.getPnlRate();
			return d2.compareTo(d1);
		};
		
		Collections.sort(lst, comp);
		
		return lst;
	}
}
