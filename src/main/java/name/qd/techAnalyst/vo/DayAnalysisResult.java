package name.qd.techAnalyst.vo;

import java.text.SimpleDateFormat;

import name.qd.techAnalyst.util.TimeUtil;

public class DayAnalysisResult extends AnalysisResult {
	private SimpleDateFormat sdf = TimeUtil.getDateFormat();
	
	@Override
	public String getKeyString() {
		return sdf.format(getDate());
	}
}
