package name.qd.techAnalyst.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.Constants.WinLose;

public class VerifyResult {
	private Date from;
	private Date to;
	private int totalChance;
	private int win;
	private int lose;
	private int none;
	private List<VerifyDetail> lst = new ArrayList<>();
	public double getWinPercent() {
		return (double) win / (double) totalChance;
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
	public int getTotalChance() {
		return totalChance;
	}
	public void setTotalChance(int totalChance) {
		this.totalChance = totalChance;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getLose() {
		return lose;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
	public int getNone() {
		return none;
	}
	public void setNone(int none) {
		this.none = none;
	}
	public List<VerifyDetail> getVerifyDetails() {
		return lst;
	}
	public void addVerifyDetail(Date date, WinLose winLose) {
		VerifyDetail detail = new VerifyDetail();
		detail.setDate(date);
		detail.setWinLose(winLose);
		lst.add(detail);
	}
	private void totalChanceIncrease() {
		totalChance++;
	}
	private void winIncrease() {
		win++;
	}
	private void loseIncrease() {
		lose++;
	}
	private void noneIncrease() {
		none++;
	}
	
	public class VerifyDetail {
		private Date date;
		private WinLose winLose;
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public WinLose getWinLose() {
			return winLose;
		}
		public void setWinLose(WinLose winLose) {
			this.winLose = winLose;
			totalChanceIncrease();
			switch(winLose) {
			case WIN:
				winIncrease();
				break;
			case LOSE:
				loseIncrease();
				break;
			case NONE:
				noneIncrease();
				break;
			}
		}
	}
}
