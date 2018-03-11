package name.qd.techAnalyst.backtest;

import java.util.Date;
import java.util.List;

import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.VerifyResult;

public interface BackTesting {
	public VerifyResult verify(DataSource dataSource, List<AnalysisResult> lst, String product, Date from, Date to, Object ... customObjs);
}
