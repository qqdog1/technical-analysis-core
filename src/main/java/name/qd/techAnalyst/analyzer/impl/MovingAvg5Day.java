package name.qd.techAnalyst.analyzer.impl;

import java.util.List;

import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.ITechAnalyzer;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProdClosingInfo;

// 移動平均線-5日
// 5日均價
public class MovingAvg5Day implements ITechAnalyzer {
	@Override
	public List<AnalysisResult> analyze(String sFrom, String sTo, List<ProdClosingInfo> lst) {
		return AnalystUtils.NDaysAvg(sFrom, sTo, lst, 5);
	}
}
