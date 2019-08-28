package name.qd.analysis.chip.vo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import name.qd.fileCache.cache.NormalObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;

public class BranchFinalInfo extends NormalObject {
	public static final String KEY = "";
	private Date from;
	private Date to;
	private Map<String, Map<String, ProductInfo>> map = new HashMap<>();

	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public double getPosition(String branch, String product) {
		ProductInfo info = getProductInfo(branch, product);
		if(info != null) {
			return info.getPosition();
		}
		return 0;
	}
	public void setPosition(String branch, String product, double position) {
		ProductInfo info = getAndCreateProductInfo(branch, product);
		info.setPosition(position);
	}
	public double getPositionDiff(String branch, String product) {
		ProductInfo info = getProductInfo(branch, product);
		if(info != null) {
			return info.getPositionDiff();
		}
		return 0;
	}
	public void setPositionDiff(String branch, String product, double positionDiff) {
		ProductInfo info = getAndCreateProductInfo(branch, product);
		info.setPositionDiff(positionDiff);
	}
	public double getPnl(String branch, String product) {
		ProductInfo info = getProductInfo(branch, product);
		if(info != null) {
			return info.getClosePnl();
		}
		return 0;
	}
	public void setPnl(String branch, String product, double pnl) {
		ProductInfo info = getAndCreateProductInfo(branch, product);
		info.setClosePnl(pnl);
	}
	public double getAvgPrice(String branch, String product) {
		ProductInfo info = getProductInfo(branch, product);
		if(info != null) {
			return info.getAvgPrice();
		}
		return 0;
	}
	public void setAvgPrice(String branch, String product, double avgPrice) {
		ProductInfo info = getAndCreateProductInfo(branch, product);
		info.setAvgPrice(avgPrice);
	}
	public Set<String> getAllProduct(String branch) {
		if(map.containsKey(branch)) {
			return map.get(branch).keySet();
		}
		return null;
	}
	private ProductInfo getProductInfo(String branch, String product) {
		if(map.containsKey(branch)) {
			Map<String, ProductInfo> mapInfo = map.get(branch);
			if(mapInfo.containsKey(product)) {
				return mapInfo.get(product);
			}
		}
		return null;
	}
	private ProductInfo getAndCreateProductInfo(String branch, String product) {
		if(!map.containsKey(branch)) {
			map.put(branch, new HashMap<>());
		}
		Map<String, ProductInfo> mapInfo = map.get(branch);
		if(!mapInfo.containsKey(product)) {
			mapInfo.put(product, new ProductInfo());
		}
		return mapInfo.get(product);
	}
	
	@Override
	public String getKeyString() {
		return KEY;
	}
	
	@Override
	public byte[] parseToFileFormat() throws IOException {
		TransOutputStream tOut = new TransOutputStream();
		tOut.writeLong(from.getTime());
		tOut.writeLong(to.getTime());
		tOut.writeInt(map.size());
		for(String branch : map.keySet()) {
			tOut.writeString(branch);
			Map<String, ProductInfo> mapInfo = map.get(branch);
			tOut.writeInt(mapInfo.size());
			for(String product : mapInfo.keySet()) {
				ProductInfo info = mapInfo.get(product);
				tOut.writeDouble(info.getAvgPrice());
				tOut.writeDouble(info.getPosition());
				tOut.writeDouble(info.getClosePnl());
				tOut.writeDouble(info.getPositionDiff());
			}
		}
		return tOut.toByteArray();
	}

	@Override
	public void toValueObject(byte[] data) throws IOException {
		TransInputStream tIn = new TransInputStream(data);
		long timestamp = tIn.getLong();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		from = calendar.getTime();
		timestamp = tIn.getLong();
		calendar.setTimeInMillis(timestamp);
		to = calendar.getTime();
		int size = tIn.getInt();
		for(int i = 0 ; i < size; i++) {
			String branch = tIn.getString();
			map.put(branch, new HashMap<>());
			Map<String, ProductInfo> mapInfo = map.get(branch);
			int infoSize = tIn.getInt();
			for(int j = 0 ; j < infoSize ; j++) {
				String product = tIn.getString();
				ProductInfo info = new ProductInfo();
				double avgPrice = tIn.getDouble();
				double position = tIn.getDouble();
				double pnl = tIn.getDouble();
				double positionDiff = tIn.getDouble();
				info.setAvgPrice(avgPrice);
				info.setPosition(position);
				info.setClosePnl(pnl);
				info.setPositionDiff(positionDiff);
				mapInfo.put(product, info);
			}
		}
	}
	
	private class ProductInfo {
		private double avgPrice;
		private double position;
		private double closePnl;
		private double positionDiff;
		
		public double getAvgPrice() {
			return avgPrice;
		}
		public void setAvgPrice(double avgPrice) {
			this.avgPrice = avgPrice;
		}
		public double getPosition() {
			return position;
		}
		public void setPosition(double position) {
			this.position = position;
		}
		public double getPositionDiff() {
			return positionDiff;
		}
		public void setPositionDiff(double positionDiff) {
			this.positionDiff = positionDiff;
		}
		public double getClosePnl() {
			return closePnl;
		}
		public void setClosePnl(double closePnl) {
			this.closePnl = closePnl;
		}
	}
}
