package name.qd.analysis.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.dataSource.TWSE.utils.TWSEPathUtil;

public class DateUtils {
	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static List<Date> getFromToByDays(String filePath, String market, int days) {
		List<Date> lst = new ArrayList<>();
		Exchange exchange = Exchange.valueOf(market);
		Path path = null;
		switch (exchange) {
		case TWSE:
			path = Paths.get(filePath, market, TWSEPathUtil.BUY_SELL_INFO_DIR);
			break;
		default:
			break;
		}

		if (path != null) {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			
			List<String> lstFolder = new ArrayList<>();
			
			while(lstFolder.size() < days) {
				lstFolder.addAll(getFolderListOfYear(year, path));
				year = year - 1;
			}
			sortList(lstFolder);
			
			SimpleDateFormat sdf = TimeUtils.getDateFormat();
			try {
				lst.add(sdf.parse(lstFolder.get(days-1)));
				lst.add(sdf.parse(lstFolder.get(0)));
			} catch (ParseException e) {
				logger.error("Parse time failed.", e);
			}
		}
		return lst;
	}

	private static List<String> getFolderListOfYear(int year, Path basePath) {
		Path pathYear = Paths.get(basePath.toString(), String.valueOf(year));

		// list all folders in this year
		List<String> lstYearFolder = new ArrayList<>();
		try {
			Files.list(pathYear).forEach(p -> lstYearFolder.add(p.getFileName().toString()));
		} catch (IOException e) {
			logger.error("List all files failed. {}", pathYear.toString(), e);
		}
		return lstYearFolder;
	}

	private static void sortList(List<String> lst) {
		Collections.sort(lst, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				Integer i1 = Integer.parseInt(s1);
				Integer i2 = Integer.parseInt(s2);
				return i2.compareTo(i1);
			}
		});
	}
}
