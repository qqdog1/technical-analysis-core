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
import name.qd.analysis.chip.utils.ChipUtils;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.utils.TimeUtil;
import name.qd.fileCache.FileCacheManager;
import name.qd.fileCache.cache.CoordinateCacheManager;
import name.qd.fileCache.cache.CoordinateObject;

public class TotalPnl implements ChipAnalyzer {
	private static Logger log = LoggerFactory.getLogger(TotalPnl.class);

	@Override
	public int getInputField() {
		return InputField.FROM + InputField.TO + InputField.BROKER + InputField.PRODUCT + InputField.WITH_OPEN_PNL;
	}

	@Override
	public List<String> getHeaderString(String branch, String product) {
		List<String> lst = new ArrayList<>();
		lst.add("Branch");
		if(!"".equals(product)) {
			lst.add("Product");
		}
		lst.add("PnL");
		return lst;
	}

	@Override
	public List<List<String>> analyze(FileCacheManager fileCacheManager, Date from, Date to, String branch, String product, boolean isOpenPnl) {
		SimpleDateFormat sdf = TimeUtil.getDateFormat();
		
		log.debug("Analyze Total Pnl. From {} to {}. Branch:{}, Product:{}, With Open Pnl:{}", sdf.format(from), sdf.format(to), branch, product, isOpenPnl);
		
		List<List<String>> lst = new ArrayList<>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		
		boolean isSpecificBranch = !"".equals(branch);
		boolean isSpecificProduct = !"".equals(product);
		
		Map<String, Map<String, DailyOperate>> map = new HashMap<>();
		
		while(!to.before(currentDate)) {
			CoordinateCacheManager lastCacheManager = null;
			try {
				lastCacheManager = fileCacheManager.getCoordinateCacheInstance("bsr_" + sdf.format(currentDate), DailyOperate.class.getName());
			} catch (Exception e) {
				log.error("Fail to get daily cache.", e);
			}
			
			Collection<CoordinateObject> objs = lastCacheManager.values();
			if (objs.size() > 0) {
				for (CoordinateObject obj : objs) {
					DailyOperate dailyOperate = (DailyOperate) obj;
					if(!map.containsKey(dailyOperate.getBrokerName())) {
						map.put(dailyOperate.getBrokerName(), new HashMap<>());
					}
					Map<String, DailyOperate> mapProduct = map.get(dailyOperate.getBrokerName());
					if(!mapProduct.containsKey(dailyOperate.getProduct())) {
						mapProduct.put(dailyOperate.getProduct(), dailyOperate);
					} else {
						DailyOperate oldOp = mapProduct.get(dailyOperate.getProduct());
						DailyOperate newOp = ChipUtils.updateDailyOperate(oldOp, dailyOperate);
						mapProduct.put(newOp.getProduct(), newOp);
					}
				}
			}
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		
		if(isSpecificBranch) {
			if(isSpecificProduct) {
				List<String> lstData = new ArrayList<>();
				Map<String, DailyOperate> mapBranch = map.get(branch);
				if(mapBranch != null) {
					DailyOperate op = mapBranch.get(product);
					if(op != null) {
						lstData.add(op.getBrokerName());
						lstData.add(op.getProduct());
						if(isOpenPnl) {
							lstData.add(String.valueOf(op.getClosePnl() + op.getOpenPnl()));
						} else {
							lstData.add(String.valueOf(op.getClosePnl()));
						}
						lst.add(lstData);
					}
				}
			} else {
				List<String> lstData = new ArrayList<>();
				Map<String, DailyOperate> mapBranch = map.get(branch);
				if(mapBranch != null) {
					double pnl = 0;
					for(DailyOperate op : mapBranch.values()) {
						pnl += op.getClosePnl();
						if(isOpenPnl) {
							pnl += op.getOpenPnl();
						}
					}
					lstData.add(branch);
					lstData.add(String.valueOf(pnl));
					lst.add(lstData);
				}
			}
		} else {
			if(isSpecificProduct) {
				for(String keyBranch : map.keySet()) {
					List<String> lstData = new ArrayList<>();
					DailyOperate op = map.get(keyBranch).get(product);
					if(op != null) {
						lstData.add(keyBranch);
						lstData.add(product);
						double pnl = op.getClosePnl();
						if(isOpenPnl) {
							pnl += op.getOpenPnl();
						}
						lstData.add(String.valueOf(pnl));
						lst.add(lstData);
					}
				}
			} else {
				for(String keyBranch : map.keySet()) {
					List<String> lstData = new ArrayList<>();
					Map<String, DailyOperate> mapBranch = map.get(keyBranch);
					lstData.add(keyBranch);
					double pnl = 0;
					for(DailyOperate op : mapBranch.values()) {
						pnl += op.getClosePnl();
						if(isOpenPnl) {
							pnl += op.getOpenPnl();
						}
					}
					lstData.add(String.valueOf(pnl));
					lst.add(lstData);
				}
			}
		}
		
		return lst;
	}

}
