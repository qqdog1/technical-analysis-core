package name.qd.analysis.dataSource.FAKE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.dataSource.vo.DailyClosingInfo;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;

public class FakeDataSource implements DataSource {
	@Override
	public List<ProductClosingInfo> getProductClosingInfo(String product, Date from, Date to) throws Exception {
		int productId = Integer.parseInt(product);
		if(productId % 2 == 0) {
			return dailyIncrease(product, from, to);
		} else {
			return dailyDecrease(product, from, to);
		}
	}
	
	private List<ProductClosingInfo> dailyIncrease(String product, Date from, Date to) {
		List<ProductClosingInfo> lst = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		while(!to.before(currentDate)) {
			ProductClosingInfo productClosingInfo = new ProductClosingInfo();
			int day = calendar.get(Calendar.DATE);
			if(day == 1) {
				productClosingInfo.setADStatus(ProductClosingInfo.DECLINE);
			} else {
				productClosingInfo.setADStatus(ProductClosingInfo.ADVANCE);
			}
			int priceBase = Integer.parseInt(product)/100;
			double price = priceBase + day;
			productClosingInfo.setAvgPrice(price);
			productClosingInfo.setDate(currentDate);
			productClosingInfo.setClosePrice(price);
			productClosingInfo.setFilledAmount(5000d*price);
			productClosingInfo.setFilledShare(5000);
			productClosingInfo.setLowerPrice(price);
			productClosingInfo.setOpenPrice(price);
			productClosingInfo.setProduct(product);
			productClosingInfo.setUpperPrice(price);
			lst.add(productClosingInfo);
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		return lst;
	}
	
	private List<ProductClosingInfo> dailyDecrease(String product, Date from, Date to) {
		List<ProductClosingInfo> lst = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		while(!to.before(currentDate)) {
			ProductClosingInfo productClosingInfo = new ProductClosingInfo();
			int day = calendar.get(Calendar.DATE);
			if(day == 1) {
				productClosingInfo.setADStatus(ProductClosingInfo.ADVANCE);
			} else {
				productClosingInfo.setADStatus(ProductClosingInfo.DECLINE);
			}
			int priceBase = Integer.parseInt(product)/100 +30;
			double price = priceBase - day;
			productClosingInfo.setAvgPrice(price);
			productClosingInfo.setDate(currentDate);
			productClosingInfo.setClosePrice(price);
			productClosingInfo.setFilledAmount(5000d*price);
			productClosingInfo.setFilledShare(5000);
			productClosingInfo.setLowerPrice(price);
			productClosingInfo.setOpenPrice(price);
			productClosingInfo.setProduct(product);
			productClosingInfo.setUpperPrice(price);
			lst.add(productClosingInfo);
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		return lst;
	}

	@Override
	public List<DailyClosingInfo> getDailyClosingInfo(Date from, Date to) throws Exception {
		List<DailyClosingInfo> lst = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		while(!to.before(currentDate)) {
			DailyClosingInfo dailyClosingInfo = new DailyClosingInfo();
			int day = calendar.get(Calendar.DATE);
			dailyClosingInfo.setAdvance(day + 400);
			dailyClosingInfo.setDate(currentDate);
			dailyClosingInfo.setDecline(400 - day);
			dailyClosingInfo.setUnchanged(100);
			lst.add(dailyClosingInfo);
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		return lst;
	}

	@Override
	public Map<Date, List<ProductClosingInfo>> getAllProductClosingInfo(Date from, Date to) throws Exception {
		Map<Date, List<ProductClosingInfo>> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		while(!to.before(currentDate)) {
			List<ProductClosingInfo> lst = new ArrayList<>();
			lst.addAll(getProductClosingInfo("1101", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("2202", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("3303", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("4404", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("5505", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("6606", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("7707", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("8808", currentDate, currentDate));
			lst.addAll(getProductClosingInfo("9909", currentDate, currentDate));
			map.put(currentDate, lst);
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		return map;
	}

	@Override
	public Map<Date, List<BuySellInfo>> getBuySellInfo(String product, Date from, Date to) throws Exception {
		Map<Date, List<BuySellInfo>> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		while(!to.before(currentDate)) {
			List<BuySellInfo> lst = new ArrayList<>();
			for(int i = 0; i < 10; i++) {
				BuySellInfo buySellInfo = new BuySellInfo();
				buySellInfo.setBrokerName(i +" QQ");
				buySellInfo.setBuyShare(6000);
				buySellInfo.setSellShare(5000);
				buySellInfo.setDate(currentDate);
				int day = calendar.get(Calendar.DATE);
				int priceBase = Integer.parseInt(product)/100;
				double price = priceBase + day;
				buySellInfo.setPrice(price);
				buySellInfo.setProduct(product);
				buySellInfo.setSeqNo(i);
				lst.add(buySellInfo);
			}
			map.put(currentDate, lst);
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		return map;
	}

	@Override
	public Map<Date, Map<String, List<BuySellInfo>>> getBuySellInfo(Date from, Date to) throws Exception {
		Map<Date, Map<String, List<BuySellInfo>>> map = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		Date currentDate = calendar.getTime();
		while(!to.before(currentDate)) {
			Map<String, List<BuySellInfo>> mapInfo = new HashMap<>();
			mapInfo.put("1101", getBuySellInfo("1101", currentDate, currentDate).get(currentDate));
			mapInfo.put("2202", getBuySellInfo("2202", currentDate, currentDate).get(currentDate));
			mapInfo.put("3303", getBuySellInfo("3303", currentDate, currentDate).get(currentDate));
			mapInfo.put("4404", getBuySellInfo("4404", currentDate, currentDate).get(currentDate));
			mapInfo.put("5505", getBuySellInfo("5505", currentDate, currentDate).get(currentDate));
			mapInfo.put("6606", getBuySellInfo("6606", currentDate, currentDate).get(currentDate));
			mapInfo.put("7707", getBuySellInfo("7707", currentDate, currentDate).get(currentDate));
			mapInfo.put("8808", getBuySellInfo("8808", currentDate, currentDate).get(currentDate));
			mapInfo.put("9909", getBuySellInfo("9909", currentDate, currentDate).get(currentDate));
			map.put(currentDate, mapInfo);
			calendar.add(Calendar.DATE, 1);
			currentDate = calendar.getTime();
		}
		return map;
	}
}
