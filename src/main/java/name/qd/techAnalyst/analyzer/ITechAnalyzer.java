package name.qd.techAnalyst.analyzer;

import java.util.List;

import name.qd.techAnalyst.dataSource.TWSEDataManager;
import name.qd.techAnalyst.vo.AnalysisResult;

public interface ITechAnalyzer {

	public List<AnalysisResult> analyze(TWSEDataManager dataManager, String from, String to, String prodId, Object ... customizeObjs);
}
