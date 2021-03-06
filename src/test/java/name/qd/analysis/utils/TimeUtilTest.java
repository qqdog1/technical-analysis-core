package name.qd.analysis.utils;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import name.qd.analysis.utils.TimeUtils;

public class TimeUtilTest {
	@Test
	public void getDateBetweenTest() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 1, 1);
		Date dateFrom = calendar.getTime();
		calendar.set(2016, 2, 2);
		Date dateTo = calendar.getTime();
		
		List<String> lst = TimeUtils.getDateBetween(dateFrom, dateTo);
		calendar.set(2015, 1, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for(String s : lst) {
			Assert.assertEquals(s, sdf.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}
	}
	
	@Test
	public void AD2ROCTest() {
		String s = "20110101";
		String date = null;
		try {
			date = TimeUtils.AD2ROC(s);
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		Assert.assertEquals(date, "100/01/01");
	}
}
