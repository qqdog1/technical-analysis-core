package name.qd.techAnalyst.analyzer.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Constants.AnalyzerType;
import name.qd.techAnalyst.analyzer.TechAnalyzer;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;

public class ASI implements TechAnalyzer {
	private static Logger log = LoggerFactory.getLogger(ASI.class);

	@Override
	public String getCacheName(String product) {
		return null;
	}

	@Override
	public List<AnalysisResult> analyze(DataSource dataManager, String product, Date from, Date to) {
		return null;
	}
	
	@Override
	public List<AnalysisResult> customResult(List<AnalysisResult> lst, Object... objs) {
		return null;
	}
	
	@Override
	public List<String> customDescreption() {
		return null;
	}

	@Override
	public AnalyzerType getAnalyzerType() {
		return null;
	}
}
