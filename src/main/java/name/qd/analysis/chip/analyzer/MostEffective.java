package name.qd.analysis.chip.analyzer;

import java.util.Date;
import java.util.List;
import java.util.Map;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;

public class MostEffective {
	
	public MostEffective() {
	}
	
	public String getMostEffectiveBroker(DataSource dataSource, Date from, Date to) throws Exception {
		Map<Date, Map<String, List<BuySellInfo>>> map = dataSource.getBuySellInfo(from, to);
		
		return null;
	}
}
