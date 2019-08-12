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
public class DailyPnl implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(DailyPnl.class);

	@Override
	public int getInputField() {
		return InputField.FROM + InputField.TO + InputField.BROKER + InputField.PRODUCT + InputField.WITH_OPEN_PNL;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Date");
		lst.add("Branch");
		if (!"".equals(product)) {
			lst.add("Product");
		}
		lst.add("PnL");
		return lst;
	}

	@Override
	public List<List<String>> analyze(FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, boolean isOpenPnl) {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();

		log.debug("Analyze Daily Pnl. From {} to {}. Branch:{}, Product:{}, With Open Pnl:{}", sdf.format(from), sdf.format(to), branch, product, isOpenPnl);

		List<List<String>> lst = new ArrayList<>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();

		boolean isSpecificBranch = !"".equals(branch);
		boolean isSpecificProduct = !"".equals(product);

		while (!to.before(currentDate)) {
			CoordinateCacheManager lastCacheManager = null;
			try {
				lastCacheManager = fileCacheManager.getCoordinateCacheInstance("bsr_" + sdf.format(currentDate), DailyOperate.class.getName());
			} catch (Exception e) {
				log.error("Fail to get daily cache.", e);
			}
			// branch, product, pnl
			Map<String, Map<String, Double>> mapBranchProfit = new HashMap<>();
			Collection<CoordinateObject> objs = lastCacheManager.values();
			if (objs.size() > 0) {
				for (CoordinateObject obj : objs) {
					DailyOperate dailyOperate = (DailyOperate) obj;
					if (!mapBranchProfit.containsKey(dailyOperate.getBrokerName())) {
						mapBranchProfit.put(dailyOperate.getBrokerName(), new HashMap<>());
					}
					Map<String, Double> mapProductProfit = mapBranchProfit.get(dailyOperate.getBrokerName());
					double pnl = dailyOperate.getClosePnl();
					if (isOpenPnl) {
						pnl += dailyOperate.getOpenPnl();
					}
					mapProductProfit.put(dailyOperate.getProduct(), pnl);
				}

				if (isSpecificBranch) {
					List<String> lstData = new ArrayList<>();
					lstData.add(sdf.format(currentDate));
					lstData.add(branch);
					if (isSpecificProduct) {
						lstData.add(product);
						Double pnl = null;
						Map<String, Double> mapData = mapBranchProfit.get(branch);
						if (mapData != null) {
							if (mapData.containsKey(product)) {
								pnl = mapData.get(product);
							}
						}

						if (pnl != null) {
							lstData.add(String.valueOf(pnl));
							lst.add(lstData);
						}
					} else {
						Map<String, Double> mapPnl = mapBranchProfit.get(branch);
						double pnl = 0;
						for (Double p : mapPnl.values()) {
							pnl += p;
						}
						lstData.add(String.valueOf(pnl));
						lst.add(lstData);
					}
				} else {
					for (String keyBranch : mapBranchProfit.keySet()) {
						List<String> lstData = new ArrayList<>();
						lstData.add(sdf.format(currentDate));
						lstData.add(keyBranch);
						if (isSpecificProduct) {
							lstData.add(product);
							Double pnl = mapBranchProfit.get(keyBranch).get(product);
							if (pnl != null) {
								lstData.add(String.valueOf(pnl));
								lst.add(lstData);
							}
						} else {
							Map<String, Double> mapPnl = mapBranchProfit.get(keyBranch);
							double pnl = 0;
							for (Double p : mapPnl.values()) {
								pnl += p;
							}
							lstData.add(String.valueOf(pnl));
							lst.add(lstData);
						}
					}
				}
			}

			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		return lst;
	}
}
