package name.qd.analysis.chip.analyzer.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.fileCache.FileCacheManager;

/**
 * Back Test Pattern 1
 * 1. User keyin a Branch name and an Open Volume, and an analyze time range
 * 2. During time range, if open volume - close volume > input value, consider this to be an entry signal
 * 3. Check everyday close volume, if close volume - open volume >= input value/2, consider this to be a close signal
 * 4. check price
 */

public class BackTestPattern1 extends ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BackTestPattern1.class);
	
	public BackTestPattern1(ChipAnalyzerManager chipAnalyzerManager) {
		super(chipAnalyzerManager);
	}
	
	@Override
	public int getInputField() {
		return InputField.BROKER + InputField.FROM + InputField.TO + InputField.TRADE_COST;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Product");
		lst.add("Buy Date");
		lst.add("Buy Price");
		lst.add("Sell Date");
		lst.add("Sell Price");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl, String ... customInputs) {
		if("".equals(branch)) {
			log.warn("Please choose a branch.");
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		
		Map<String, List<Double>> mapPrice = new HashMap<>();
		List<List<String>> lst = new ArrayList<>();
		
		Map<Date, Map<String, List<BuySellInfo>>> mapBSInfo = null;
		try {
			mapBSInfo = dataSource.getBuySellInfo(from, to);
		} catch (Exception e) {
			log.error("Try to get buy sell info failed.", e);
			return null;
		}
		
		while(!to.before(currentDate)) {
			Map<String, List<BuySellInfo>> mapProductBS = mapBSInfo.get(currentDate);
			for(String p : mapProductBS.keySet()) {
				List<BuySellInfo> lstInfo = mapProductBS.get(p);
				double buyShare = 0;
				double buyCost = 0;
				double sellShare = 0;
				double sellCost = 0;
				// 2.
				for(BuySellInfo info : lstInfo) {
					if(info.getBrokerName().equals(branch)) {
						buyShare += info.getBuyShare();
						buyCost += info.getBuyShare() * info.getPrice();
						sellShare += info.getSellShare();
						sellCost += info.getSellShare() * info.getPrice(); 
					}
				}
				if(buyCost - sellCost >= tradeCost) {
					if(!mapPrice.containsKey(p)) {
						mapPrice.put(p, new ArrayList<>());
					}
					mapPrice.get(p).add(buyCost/buyShare);
				}
				
				// 3.
				else if(sellCost - buyCost >= tradeCost / 2) {
					if(mapPrice.containsKey(p)) {
						
					}
				}
			}
			
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		
		return lst;
	}

	@Override
	public List<String> getCustomDescreption() {
		return null;
	}
}
