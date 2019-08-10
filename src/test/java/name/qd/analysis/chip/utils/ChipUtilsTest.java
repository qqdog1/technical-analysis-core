package name.qd.analysis.chip.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.utils.TimeUtil;

public class ChipUtilsTest {
	@Ignore
	@Test
	public void dailyOperateTest() {
		try {
			DataSource dataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
			Date from = TimeUtil.getDateFormat().parse("20180403");
			Date to = TimeUtil.getDateFormat().parse("20180412");
			Map<Date, List<BuySellInfo>> map = dataSource.getBuySellInfo("9931", from, to);
			
			Map<String, DailyOperate> mapOperate = new HashMap<>();
			
			List<String> lstDate = TimeUtil.getDateBetween(from, to);
			for(String dateString : lstDate) {
				Date date = TimeUtil.getDateFormat().parse(dateString);
				if(map.containsKey(date)) {
					List<BuySellInfo> lst = map.get(date);
					ChipUtils.bsInfoToOperate(dataSource, "", date, lst, mapOperate);
				}
			}
			
			for(String broker : mapOperate.keySet()) {
				DailyOperate operate = mapOperate.get(broker);
				System.out.println(broker + ":" + operate.getDate() + ":" + operate.getBrokerName() + ":" + operate.getProduct() + ":" + operate.getOpenShare() + ":" + operate.getAvgPrice() + ":" + operate.getClosePnl());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
