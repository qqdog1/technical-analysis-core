package name.qd.analysis.dataSource.vo;

import java.util.Date;

public class BuySellInfo {
	private Date date;
	private String product;
	private int seqNo;
	private String brokerName;
	private double price;
	private long buyShare;
	private long sellShare;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getBuyShare() {
		return buyShare;
	}
	public void setBuyShare(long buyShare) {
		this.buyShare = buyShare;
	}
	public long getSellShare() {
		return sellShare;
	}
	public void setSellShare(long sellShare) {
		this.sellShare = sellShare;
	}
}
