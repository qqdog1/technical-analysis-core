package name.qd.techAnalyst.dataSource;

import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public interface DataSource {
	public List<ProductClosingInfo> getProductClosingInfo(String product, Date from, Date to) throws Exception;
	public List<DailyClosingInfo> getDailyClosingInfo(Date from, Date to) throws Exception;
}
