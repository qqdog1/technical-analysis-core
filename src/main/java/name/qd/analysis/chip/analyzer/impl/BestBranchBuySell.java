package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 當日買進-賣出超過Z
 * 或賣出-買進超過Z
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
		
		log.info("Best Buy Sell from {} to {}, top {} branchs, trade over {}", from, to, branchCount, tradeCost);
		
		List<List<String>> lstPnlResult = chipAnalyzerManager.getAnalysisResult(dataSource, ChipAnalyzers.TOTAL_PNL, "", "", from, to, 0, false, customInputs);
		List<String> lstBranchs = new ArrayList<>();
		
		if(lstPnlResult.size() < branchCount) {
			branchCount = lstPnlResult.size();
		}
		
		for(int i = 0; i < branchCount ; i++) {
			lstBranchs.add(lstPnlResult.get(i).get(0));
		}
		
		List<List<String>> lst = new ArrayList<>();
		for(String topBranch : lstBranchs) {
			Map<String, List<String>> mapProductList = new HashMap<>();
			List<List<String>> lstDailyResult = chipAnalyzerManager.getAnalysisResult(dataSource, ChipAnalyzers.DAILY_OPEN, topBranch, "", to, to, tradeCost, false, customInputs);
			for(List<String> lstData : lstDailyResult) {
				String thisProduct = lstData.get(1);
				if(!mapProductList.containsKey(thisProduct)) {
					mapProductList.put(thisProduct, lstData);
				} else {
					compareTwoList(mapProductList.get(thisProduct), lstData, tradeCost, lst);
				}
			}
		}
		return lst;
	}

	private void compareTwoList(List<String> l1, List<String> l2, double tradeCost, List<List<String>> lst) {
		double cost1 = Double.valueOf(l1.get(5));
		double cost2 = Double.valueOf(l2.get(5));
		
		if(cost1-cost2 > tradeCost) {
			lst.add(l1);
		} else if(cost2-cost1 > tradeCost) {
			lst.add(l2);
		}
	}
	
	@Override
	public List<String> getCustomDescreption() {
		List<String> lst = new ArrayList<>();
		lst.add("Top X branch:");
		return lst;
	}
}
