package name.qd.techAnalyst.analyzer.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.analyzer.ITechAnalyzer;
import name.qd.techAnalyst.dataSource.TWSEDataManager;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class MovingAvg120Day implements ITechAnalyzer {
	@Override
	public List<AnalysisResult> analyze(TWSEDataManager dataManager, String sFrom, String sTo, String sProdId) {
		ArrayList<ProdClosingInfo> lst = null;
		try {
			lst = dataManager.getProdClosingInfo(sFrom, sTo, sProdId);
		} catch (ParseException | IOException e) {
			// TODO
			e.printStackTrace();
		}
		return AnalystUtils.NDaysAvg(lst, 120);
	}
}
