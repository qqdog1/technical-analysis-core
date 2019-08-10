package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.fileCache.FileCacheManager;

public class TotalPnl implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(TotalPnl.class);

	@Override
	public int getInputField() {
		return InputField.FROM + InputField.TO + InputField.BROKER + InputField.PRODUCT;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Date");
		lst.add("Branch");
		if(!"".equals(product)) {
			lst.add("Product");
		}
		lst.add("PnL");
		return lst;
	}

	@Override
	public List<List<String>> analyze(FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, boolean isOpenPnl) {
		return null;
	}

}
