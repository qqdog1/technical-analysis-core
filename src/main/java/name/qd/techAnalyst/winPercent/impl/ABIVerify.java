package name.qd.techAnalyst.winPercent.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.techAnalyst.Constants.WinLose;
import name.qd.techAnalyst.analyzer.AnalystUtils;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.vo.AnalysisResult;
import name.qd.techAnalyst.vo.ProductClosingInfo;
import name.qd.techAnalyst.vo.VerifyResult;
import name.qd.techAnalyst.winPercent.WPVerifier;

public class ABIVerify implements WPVerifier {
	private static Logger log = LoggerFactory.getLogger(ABIVerify.class);
	// 可看大盤 台指 台50 etc
	public VerifyResult verify(DataSource dataSource, List<AnalysisResult> lst, String product, Date from, Date to, Object ... customObjs) {
		// 1.要用幾日均線驗證?
		// 2.均線值超過多少 算發生?
		// 3.均線超過後 要用第幾日後交易日價格 與發生日比較?
		int ma = (int) customObjs[0];
		int threshold = (int) customObjs[1];
		int verifyDay = (int) customObjs[2];
		
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
		
		List<AnalysisResult> lstMA = AnalystUtils.NDaysAvgByAnalysisResult(lst, ma);
		for(AnalysisResult analysisResult : lstMA) {
			if(analysisResult.getValue() >= threshold) {
				Date date = analysisResult.getDate();
				log.info("value > threshold, {} {}", analysisResult.getValue(), date);
				double price = map.get(date).getClosePrice();
				ProductClosingInfo productInfo = getNextNDay(map, date, verifyDay, to);
				if(productInfo == null) {
					continue;
				} else {
					double afterPrice = productInfo.getClosePrice();
					if(afterPrice > price) {
						log.info("WIN, {} {} > {}", productInfo.getDate(), afterPrice, price);
						verifyResult.addVerifyDetail(date, WinLose.WIN);
					} else if(afterPrice < price) {
						log.info("LOSE, {} {} < {}", productInfo.getDate(), afterPrice, price);
						verifyResult.addVerifyDetail(date, WinLose.LOSE);
					} else {
						log.info("NONE, {} {} = {}", productInfo.getDate(), afterPrice, price);
						verifyResult.addVerifyDetail(date, WinLose.NONE);
					}
				}
			}
		}
		return verifyResult;
	}
	
	private ProductClosingInfo getNextNDay(Map<Date, ProductClosingInfo> map, Date date, int n, Date to) {
		
		ProductClosingInfo productInfo = null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		while(n >= 0) {
			if(date.after(to)) {
				return null;
			}
			if(n == 0) {
				if(map.containsKey(date)) {
					productInfo = map.get(date);
					n--;
				}
			}
			if(map.containsKey(date)) {
				n--;
			}
			c.add(Calendar.DATE, 1);
			date = c.getTime();
		}
		return productInfo;
	}
}
