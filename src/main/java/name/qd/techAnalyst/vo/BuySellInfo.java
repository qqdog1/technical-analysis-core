package name.qd.techAnalyst.vo;

public class BuySellInfo {
	private String product;
	private int seqNo;
	private String brokerNo;
	private String brokerName;
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
	public String getBrokerNo() {
		return brokerNo;
	}
	public void setBrokerNo(String brokerNo) {
		this.brokerNo = brokerNo;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
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
