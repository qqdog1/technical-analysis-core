package name.qd.techAnalyst.dataSource.TWSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import name.qd.techAnalyst.Constants.Exchange;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.dataSource.DataSourceFactory;
import name.qd.techAnalyst.utils.StringCombineUtil;
import name.qd.techAnalyst.utils.TimeUtil;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class TWSEDataSourceTest {

	@Test
	public void testDataParser() {
		DataSource twseDataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		try {
			List<ProductClosingInfo> lst = twseDataSource.getProductClosingInfo("2330", sdf.parse("20121001"), sdf.parse("20121031"));

			for (ProductClosingInfo p : lst) {
				String s = StringCombineUtil.combine(sdf.format(p.getDate()), ":", String.valueOf(p.getFilledShare()),
						",", String.valueOf(p.getFilledAmount()), ",", String.valueOf(p.getOpenPrice()), ",",
						String.valueOf(p.getUpperPrice()), ",", String.valueOf(p.getLowerPrice()), ",",
						String.valueOf(p.getClosePrice()));
				System.out.println(s);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
