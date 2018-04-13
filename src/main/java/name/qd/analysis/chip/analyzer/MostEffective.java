package name.qd.analysis.chip.analyzer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.qd.analysis.chip.utils.ChipUtils;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;

public class MostEffective {
	
	public MostEffective() {
	}
	
	public DailyOperate getMostEffectiveBroker(DataSource dataSource, Date from, Date to) throws Exception {
		Map<String, Map<String, DailyOperate>> map = getAllDailyOperate(dataSource, from, to);
		return findMostEffective(map);
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
				if(compareOperate.getPnl() > operate.getPnl()) {
					operate = compareOperate;
				}
			}
		}
		return operate;
	}
}
