package name.qd.analysis.chip.vo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import name.qd.fileCache.cache.NormalObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;

public class BranchFinalInfo extends NormalObject {
	private String branch;
	private Date from;
	private Date to;
	private Map<String, ProductInfo> mapProductInfo;

	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
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
	public double getPosition(String product) {
		if(mapProductInfo.containsKey(product)) {
			return mapProductInfo.get(product).getPosition();
		}
		return 0;
	}
	public void setPosition(String product, double position) {
		if(!mapProductInfo.containsKey(product)) {
			mapProductInfo.put(product, new ProductInfo());
		}
		mapProductInfo.get(product).setPosition(position);
	}
	public double getPositionDiff(String product) {
		if(mapProductInfo.containsKey(product)) {
			return mapProductInfo.get(product).getPositionDiff();
		}
		return 0;
	}
	public void setPositionDiff(String product, double positionDiff) {
		if(!mapProductInfo.containsKey(product)) {
			mapProductInfo.put(product, new ProductInfo());
		}
		mapProductInfo.get(product).setPositionDiff(positionDiff);
	}
	public double getPnl(String product) {
		if(mapProductInfo.containsKey(product)) {
			return mapProductInfo.get(product).getPnl();
		}
		return 0;
	}
	public void setPnl(String product, double pnl) {
		if(!mapProductInfo.containsKey(product)) {
			mapProductInfo.put(product, new ProductInfo());
		}
		mapProductInfo.get(product).setPnl(pnl);
	}
	public double getAvgPrice(String product) {
		if(mapProductInfo.containsKey(product)) {
			return mapProductInfo.get(product).getAvgPrice();
		}
		return 0;
	}
	public void setAvgPrice(String product, double avgPrice) {
		if(!mapProductInfo.containsKey(product)) {
			mapProductInfo.put(product, new ProductInfo());
		}
		mapProductInfo.get(product).setAvgPrice(avgPrice);
	}
	
	@Override
	public String getKeyString() {
		return branch;
	}
	
	@Override
	public byte[] parseToFileFormat() throws IOException {
		TransOutputStream tOut = new TransOutputStream();
		tOut.writeString(branch);
		tOut.writeLong(from.getTime());
		tOut.writeLong(to.getTime());
		tOut.writeInt(mapProductInfo.size());
		for(String product : mapProductInfo.keySet()) {
			tOut.writeString(product);
			ProductInfo info = mapProductInfo.get(product);
			tOut.writeDouble(info.getAvgPrice());
			tOut.writeDouble(info.getPosition());
			tOut.writeDouble(info.getPnl());
			tOut.writeDouble(info.getPositionDiff());
		}
		return tOut.toByteArray();
	}

	@Override
	public void toValueObject(byte[] data) throws IOException {
		TransInputStream tIn = new TransInputStream(data);
		branch = tIn.getString();
		long timestamp = tIn.getLong();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		from = calendar.getTime();
		timestamp = tIn.getLong();
		calendar.setTimeInMillis(timestamp);
		to = calendar.getTime();
		int size = tIn.getInt();
		for(int i = 0 ; i < size; i++) {
			String product = tIn.getString();
			ProductInfo info = new ProductInfo();
			double avgPrice = tIn.getDouble();
			double position = tIn.getDouble();
			double pnl = tIn.getDouble();
			double positionDiff = tIn.getDouble();
			info.setAvgPrice(avgPrice);
			info.setPosition(position);
			info.setPnl(pnl);
			info.setPositionDiff(positionDiff);
			mapProductInfo.put(product, info);
		}
	}
	
	private class ProductInfo {
		private double avgPrice;
		private double position;
		private double pnl;
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
		public double getPnl() {
			return pnl;
		}
		public void setPnl(double pnl) {
			this.pnl = pnl;
		}
	}
}
