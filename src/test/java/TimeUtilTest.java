import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import name.qd.techAnalyst.util.TimeUtil;

public class TimeUtilTest {

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			List<String[]> lst = TimeUtil.getYearMonthBetween(sdf.parse("20160111"), sdf.parse("20160701"));
			for(String[] s : lst) {
				for(String ss : s) {
					System.out.println(ss);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getDateBetweenTest() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 1, 1);
		Date dateFrom = calendar.getTime();
		calendar.set(2016, 2, 2);
		Date dateTo = calendar.getTime();
		
		List<String> lst = TimeUtil.getDateBetween(dateFrom, dateTo);
		Assert.assertEquals(0, 1);
	}
}
