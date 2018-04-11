package name.qd.analysis.vo;

public class BuySellInfo {
	private String product;
	private int seqNo;
	private String brokerName;
	private double price;
	private double buyShare;
	private double sellShare;
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
	public double getBuyShare() {
		return buyShare;
	}
	public void setBuyShare(double buyShare) {
		this.buyShare = buyShare;
	}
	public double getSellShare() {
		return sellShare;
	}
	public void setSellShare(double sellShare) {
		this.sellShare = sellShare;
	}
}
