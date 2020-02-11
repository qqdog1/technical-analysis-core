package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.ChipAnalyzers;
import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.dataSource.DataSource;
import name.qd.fileCache.FileCacheManager;

/**
 * 近X日
 * 最會賺前Y名分公司
 * 當日買進超過Z
 * 列出股票觀察
 */
public class BestBranchBuySell extends ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BestBranchBuySell.class);
	
	public BestBranchBuySell(ChipAnalyzerManager chipAnalyzerManager) {
		super(chipAnalyzerManager);
	}
	
	@Override
	public int getInputField() {
		return InputField.FROM + InputField.TO + InputField.TRADE_COST;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Branch");
		lst.add("Product");
		lst.add("B/S");
		lst.add("Price");
		lst.add("Share");
		lst.add("Volume");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl, String ... customInputs) {
		if(customInputs.length != 1) {
			log.error("Please input how many branch you want.");
			return null;
		}
		
		int branchCount;
		try {
			branchCount = Integer.parseInt(customInputs[0]);
		} catch (NumberFormatException e) {
			log.error("Input how many branch you want. {}", customInputs[0]);
			return null;
		}
		
		List<List<String>> lstPnlResult = chipAnalyzerManager.getAnalysisResult(dataSource, ChipAnalyzers.TOTAL_PNL, "", "", from, to, tradeCost, false, customInputs);
		List<String> lstBranchs = new ArrayList<>();
		
		if(lstPnlResult.size() < branchCount) {
			branchCount = lstPnlResult.size();
		}
		
		for(int i = 0; i < branchCount ; i++) {
			lstBranchs.add(lstPnlResult.get(i).get(0));
		}
		
		List<List<String>> lst = new ArrayList<>();
		for(String topBranch : lstBranchs) {
			List<List<String>> lstDailyResult = chipAnalyzerManager.getAnalysisResult(dataSource, ChipAnalyzers.DAILY_OPEN, topBranch, "", to, to, tradeCost, false, customInputs);
			lst.addAll(lstDailyResult);
		}
		return lst;
	}

	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Top X branch:");
		return lst;
	}
}
