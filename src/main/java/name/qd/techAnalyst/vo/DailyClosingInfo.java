package name.qd.techAnalyst.vo;

import java.util.Date;

public class DailyClosingInfo {
	private Date date;
	private int advance;
	private int decline;
	private int unchanged;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getAdvance() {
		return advance;
	}
	public void setAdvance(int advance) {
		this.advance = advance;
	}
	public int getDecline() {
		return decline;
	}
	public void setDecline(int decline) {
		this.decline = decline;
	}
	public int getUnchanged() {
		return unchanged;
	}
	public void setUnchanged(int unchanged) {
		this.unchanged = unchanged;
	}
}
