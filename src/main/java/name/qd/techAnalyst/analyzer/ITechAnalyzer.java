package name.qd.techAnalyst.analyzer;

import java.util.List;

import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public interface ITechAnalyzer {

	public List<AnalysisResult> analyze(String sFrom, String sTo, List<ProdClosingInfo> lst);
}
