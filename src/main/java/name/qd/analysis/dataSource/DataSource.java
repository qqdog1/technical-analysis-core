package name.qd.analysis.dataSource;

import java.util.Date;
import java.util.List;
import java.util.Map;

import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.dataSource.vo.DailyClosingInfo;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;

public interface DataSource {
	public String getMarket();
	public String getDataPath();
	public List<ProductClosingInfo> getProductClosingInfo(String product, Date from, Date to) throws Exception;
	public List<DailyClosingInfo> getDailyClosingInfo(Date from, Date to) throws Exception;
	public Map<Date, List<ProductClosingInfo>> getAllProductClosingInfo(Date from, Date to) throws Exception;
	public Map<Date, List<BuySellInfo>> getBuySellInfo(String product, Date from, Date to) throws Exception;
	public Map<Date, Map<String, List<BuySellInfo>>> getBuySellInfo(Date from, Date to) throws Exception;
}
