package name.qd.analysis.vo;

import java.util.Date;

public class OrderProgress {
	private Date date;
	private int position;
	private double avgPrice;
	private double closePnl;
	private double openPnl;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}
	public double getClosePnl() {
		return closePnl;
	}
	public void setClosePnl(double closePnl) {
		this.closePnl = closePnl;
	}
	public double getOpenPnl() {
		return openPnl;
	}
	public void setOpenPnl(double openPnl) {
		this.openPnl = openPnl;
	}
	public double getPnl() {
		return openPnl + closePnl;
	}
}
