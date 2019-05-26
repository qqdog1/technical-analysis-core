package name.qd.analysis.chip.vo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import name.qd.fileCache.cache.CoordinateObject;
import name.qd.fileCache.common.TransInputStream;
import name.qd.fileCache.common.TransOutputStream;

public class DailyOperate extends CoordinateObject {
	private Date date;
	private String brokerName;
	private String product;
	private double pnl;
	private double tradeCost;
	private long openShare;
	private double avgPrice;
	private double pnlRate;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public double getPnl() {
		return pnl;
	}
	public void setPnl(double pnl) {
		this.pnl = pnl;
	}
	public double getTradeCost() {
		return tradeCost;
	}
	public void setTradeCost(double tradeCost) {
		this.tradeCost = tradeCost;
	}
	public long getOpenShare() {
		return openShare;
	}
	public void setOpenShare(long openShare) {
		this.openShare = openShare;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	public double getPnlRate() {
		return pnlRate;
	}
	public void setPnlRate() {
		pnlRate = pnl/tradeCost;
	}
	@Override
	public String getXKey() {
		return product;
	}
	@Override
	public String getYKey() {
		return brokerName;
	}
	@Override
	public byte[] parseToFileFormat() throws IOException {
		TransOutputStream tOut = new TransOutputStream();
		tOut.writeLong(date.getTime());
		tOut.writeString(product);
		tOut.writeString(brokerName);
		tOut.writeDouble(pnl);
		tOut.writeDouble(tradeCost);
		tOut.writeLong(openShare);
		tOut.writeDouble(avgPrice);
		tOut.writeDouble(pnlRate);
		return tOut.toByteArray();
	}
	@Override
	public void toValueObject(byte[] data) throws IOException {
		TransInputStream tIn = new TransInputStream(data);
		long timestamp = tIn.getLong();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		date = calendar.getTime();
		product = tIn.getString();
		brokerName = tIn.getString();
		pnl = tIn.getDouble();
		tradeCost = tIn.getDouble();
		openShare = tIn.getLong();
		avgPrice = tIn.getDouble();
		pnlRate = tIn.getDouble();
	}
}
