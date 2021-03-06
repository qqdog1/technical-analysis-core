package name.qd.analysis.dataSource.TWSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.utils.StringCombineUtil;
import name.qd.analysis.utils.TimeUtils;

public class TWSEDataSourceTest {
	private String TEST_FOLDER = "./file/TWSE";
	
	@Ignore
	@Test
	public void testDataParser() {
		DataSource twseDataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE, TEST_FOLDER);
		SimpleDateFormat sdf = TimeUtils.getDateFormat();
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
