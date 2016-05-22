import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import name.qd.techAnalyst.util.TimeUtil;

public class TimeUtilTest {

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			List<String> lst = TimeUtil.getYearMonthBetween(sdf.parse("20160511"), sdf.parse("20160701"));
			System.out.println(lst);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
