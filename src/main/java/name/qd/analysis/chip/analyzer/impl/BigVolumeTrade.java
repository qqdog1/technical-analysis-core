package name.qd.analysis.chip.analyzer.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.utils.TimeUtil;
import name.qd.fileCache.FileCacheManager;
/**
 * 任何 買-賣 或是 賣-買 超過一定量 都列出來
 */
public class BigVolumeTrade implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(BigVolumeTrade.class);
	
	@Override
	public int getInputField() {
		return InputField.BROKER + InputField.PRODUCT + InputField.FROM + InputField.TO + InputField.TRADE_COST;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Date");
		lst.add("Product");
		lst.add("B/S");
		lst.add("Price");
		lst.add("Shares");
		lst.add("Volume");
		return lst;
	}

	@Override
	public List<List<String>> analyze(DataSource dataSource, FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, double tradeCost, boolean isOpenPnl, String ... customInputs) {
		if("".equals(branch)) {
			log.error("Please choose a branch.");
			return null;
		}
		
		boolean isSpecificProduct = !"".equals(product);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		
		List<List<String>> lst = new ArrayList<>();
		
		Map<Date, Map<String, List<BuySellInfo>>> mapBSInfo = null;
		try {
			mapBSInfo = dataSource.getBuySellInfo(from, to);
		} catch (Exception e) {
			log.error("Try to get buy sell info failed.", e);
			return null;
		}
		
		while(!to.before(currentDate)) {
			log.debug("Analyze date {}", currentDate);
			Map<String, List<BuySellInfo>> mapProductBS = mapBSInfo.get(currentDate);
			for(String p : mapProductBS.keySet()) {
				if(isSpecificProduct) {
					if(!p.equals(product)) {
						continue;
					}
				}
				List<BuySellInfo> lstInfo = mapProductBS.get(p);
				double buyShare = 0;
				double buyCost = 0;
				double sellShare = 0;
				double sellCost = 0;
				for(BuySellInfo info : lstInfo) {
					if(info.getBrokerName().equals(branch)) {
						buyShare += info.getBuyShare();
						buyCost += info.getBuyShare() * info.getPrice();
						sellShare += info.getSellShare();
						sellCost += info.getSellShare() * info.getPrice(); 
					}
				}
				List<String> lstData = new ArrayList<>();
				lstData.add(sdf.format(currentDate));
				lstData.add(p);
				if(buyCost - sellCost >= tradeCost) {
					lstData.add("B");
					lstData.add(String.valueOf(buyCost/buyShare));
					lstData.add(String.valueOf(buyShare));
					lstData.add(String.valueOf(buyCost));
					lst.add(lstData);
				} else if(sellCost - buyCost >= tradeCost) {
					lstData.add("S");
					lstData.add(String.valueOf(sellCost/sellShare));
					lstData.add(String.valueOf(sellShare));
					lstData.add(String.valueOf(sellCost));
					lst.add(lstData);
				}
			}
			
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		
		log.debug("Sorting data ...");
		sortWithProduct(lst);
		
		return lst;
	}
	
	private void sortWithProduct(List<List<String>> lst) {
		Collections.sort(lst, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> l1, List<String> l2) {
				String s1 = l1.get(1);
				String s2 = l2.get(1);
				Integer i1 = Integer.parseInt(s1);
				Integer i2 = Integer.parseInt(s2);
				return i2.compareTo(i1);
			}
		});
	}
}
