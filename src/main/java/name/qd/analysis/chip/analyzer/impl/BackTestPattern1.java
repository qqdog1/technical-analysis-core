package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

/**
 * Back Test Pattern 1
 * 1. User keyin Branch name and Open Volume, and analyze time range
 * 2. During time range, if open volume > input value, consider this to be an entry signal
 * 3. Check everyday close volume, if close volume >= open volume/2, consider this to be aa close signal
 * 4. check price
 */

public class BackTestPattern1 implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BackTestPattern1.class);

	@Override
	public int getInputField() {
		return InputField.BROKER + InputField.FROM + InputField.TO + InputField.TRADE_COST;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Date");
		lst.add("B/S");
		lst.add("Price");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl) {
		if("".equals(branch)) {
			log.warn("Please choose a branch.");
			return null;
		}
		
		
		
		
		return null;
	}
}
