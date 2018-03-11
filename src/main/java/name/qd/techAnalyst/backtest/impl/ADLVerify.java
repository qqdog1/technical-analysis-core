package name.qd.techAnalyst.backtest.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.backtest.BackTesting;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;
import name.qd.techAnalyst.vo.VerifyResult;
import name.qd.techAnalyst.vo.VerifyResult.VerifyDetail;

public class ADLVerify implements BackTesting {
	private static Logger log = LoggerFactory.getLogger(ADLVerify.class);

	@Override
	public VerifyResult verify(DataSource dataSource, List<AnalysisResult> lst, String product, Date from, Date to, Object... customObjs) {
		List<ProductClosingInfo> lstProductInfo = null;
		VerifyResult verifyResult = new VerifyResult();
		verifyResult.setFrom(from);
		verifyResult.setTo(to);
		try {
			lstProductInfo = dataSource.getProductClosingInfo(product, from, to);
		} catch (Exception e) {
			log.error("get product closing info failed. {}, {}-{}", product, from.toString(), to.toString(), e);
		}
		
		Map<Date, ProductClosingInfo> map = new HashMap<>();
		for(ProductClosingInfo productInfo : lstProductInfo) {
			map.put(productInfo.getDate(), productInfo);
		}
		
		return null;
	}

}
