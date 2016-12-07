import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import name.qd.techAnalyst.util.TimeUtil;

public class TimeUtilTest {
	@Test
	public void getDateBetweenTest() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 1, 1);
		Date dateFrom = calendar.getTime();
		calendar.set(2016, 2, 2);
		Date dateTo = calendar.getTime();
		
		List<String> lst = TimeUtil.getDateBetween(dateFrom, dateTo);
		calendar.set(2015, 1, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for(String s : lst) {
			Assert.assertEquals(s, sdf.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}
	}
}
