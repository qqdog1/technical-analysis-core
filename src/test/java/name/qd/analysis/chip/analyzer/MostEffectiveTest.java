package name.qd.analysis.chip.analyzer;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.utils.TimeUtil;

public class MostEffectiveTest {

	@Test
	public void MostEffTest() {
		MostEffective mostEffective = new MostEffective();
		DataSource dataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
		try {
			Date from = TimeUtil.getDateFormat().parse("20180403");
			Date to = TimeUtil.getDateFormat().parse("20180413");
			
			DailyOperate operate = mostEffective.getMostEffectiveBroker(dataSource, from, to);
			
			System.out.println(operate.getBrokerName() + ":" + operate.getProduct() + ":" + operate.getPnl());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
