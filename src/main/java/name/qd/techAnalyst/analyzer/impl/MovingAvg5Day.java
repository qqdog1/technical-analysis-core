package name.qd.techAnalyst.analyzer.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.ITechAnalyzer;
import name.qd.techAnalyst.dataSource.TWSEDataManager;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProdClosingInfo;

// 移動平均線-5日
// 5日均價
public class MovingAvg5Day implements ITechAnalyzer {
	private static Logger logger = LogManager.getLogger(MovingAvg5Day.class);
	@Override
	public List<AnalysisResult> analyze(TWSEDataManager dataManager, String from, String to, String prodId) {
		ArrayList<ProdClosingInfo> lst = null;
		try {
			lst = dataManager.getProdClosingInfo(from, to, prodId);
		} catch (ParseException | IOException e) {
			logger.error(e.getMessage(), e);
		}
		return AnalystUtils.NDaysAvg(lst, 5);
	}
}
