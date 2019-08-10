package name.qd.analysis.chip.analyzer.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzer;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.utils.TimeUtil;
import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.CoordinateCacheManager;
import name.qd.fileCache.cache.CoordinateObject;

/**
 * 每日是否有賺錢 只看當日PnL
 */
public class WinLossByDate implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(WinLossByDate.class);
	private SimpleDateFormat sdf = TimeUtil.getDateFormat();

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
		lst.add("Win|Loss");
		return lst;
	}

	@Override
	public List<List<String>> analyze(FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, boolean isOpenPnl) {
		List<List<String>> lst = new ArrayList<>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		
		CoordinateCacheManager lastCacheManager = null;
		try {
			lastCacheManager = fileCacheManager.getCoordinateCacheInstance("bsr_" + sdf.format(currentDate), DailyOperate.class.getName());
		} catch (Exception e) {
			log.error("Fail to get daily cache.", e);
		}
		
		boolean isSpecificBranch = !"".equals(branch);
		boolean isSpecificProduct = !"".equals(product);
		
		while(!to.before(currentDate)) {
			// branch, product, pnl
			Map<String, Map<String, Double>> mapBranchProfit = new HashMap<>();
			try {
				Collection<CoordinateObject> objs = lastCacheManager.values();
				if(objs.size() > 0) {
					for(CoordinateObject obj : objs) {
						DailyOperate dailyOperate = (DailyOperate) obj;
						if(!mapBranchProfit.containsKey(dailyOperate.getBrokerName())) {
							mapBranchProfit.put(dailyOperate.getBrokerName(), new HashMap<>());
						}
						Map<String, Double> mapProductProfit = mapBranchProfit.get(dailyOperate.getBrokerName());
						double pnl = dailyOperate.getClosePnl();
						if(isOpenPnl) {
							pnl += dailyOperate.getOpenPnl();
						}
						mapProductProfit.put(dailyOperate.getProduct(), pnl);
					}
					
					// add to list
					List<String> lstData = new ArrayList<>();
					
					if(isSpecificBranch) {
						lstData.add(sdf.format(currentDate));
						lstData.add(branch);
						if(isSpecificProduct) {
							lstData.add(product);
							lstData.add(String.valueOf(mapBranchProfit.get(branch).get(product)));
						} else {
							Map<String, Double> mapPnl = mapBranchProfit.get(branch);
							double pnl = 0;
							for(Double p : mapPnl.values()) {
								pnl += p;
							}
							lstData.add(String.valueOf(pnl));
						}
					} else {
						for(String keyBranch : mapBranchProfit.keySet()) {
							lstData.add(sdf.format(currentDate));
							lstData.add(keyBranch);
							if(isSpecificProduct) {
								lstData.add(product);
								lstData.add(String.valueOf(mapBranchProfit.get(keyBranch).get(product)));
							} else {
								Map<String, Double> mapPnl = mapBranchProfit.get(keyBranch);
								double pnl = 0;
								for(Double p : mapPnl.values()) {
									pnl += p;
								}
								lstData.add(String.valueOf(pnl));
							}
						}
					}
				}
				
				calendar.add(Calendar.DATE, 1);
				currentDate = calendar.getTime();
			} catch (Exception e) {
				log.error("Fail to get daily cache.");
			}
		}
		return lst;
	}
}
