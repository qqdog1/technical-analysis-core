package name.qd.analysis.tech.backTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.OrderPriceType;
import name.qd.analysis.Constants.OrderTriggerType;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.ActionResult;
import name.qd.analysis.tech.vo.AnalysisResult;
import name.qd.analysis.tech.vo.OrderProgress;

public class BackTestingManager {
	private static Logger log = LoggerFactory.getLogger(BackTestingManager.class);
	private static BackTestingManager instance = new BackTestingManager();
	private BackTestingFactory factory = new BackTestingFactory();
	
	public BackTestingManager getInstance() {
		return instance;
	}
	
	private BackTestingManager() {
	}
	
	public List<OrderProgress> getOrderProgress(DataSource dataSource, TechAnalyzers analyzer, String product, Date from, Date to, OrderPriceType orderPriceType, OrderTriggerType orderTriggerType, String ... custom) throws Exception {
		List<ActionResult> lstAction = getActionResult(dataSource, analyzer, product, from, to, custom);
		List<AnalysisResult> lstPrice = getPriceResult(dataSource, product, from, to, orderPriceType);
		Map<Date, AnalysisResult> mapPrice = new HashMap<>();
		for(AnalysisResult info : lstPrice) {
			mapPrice.put(info.getDate(), info);
		}
		
		List<OrderProgress> lstProgress = new ArrayList<>();
		OrderProgress lastProgress = new OrderProgress();
		for(ActionResult action : lstAction) {
			AnalysisResult price = mapPrice.get(action.getDate());
			OrderProgress progress = new OrderProgress();
			progress.setDate(action.getDate());
			switch(action.getAction()) {
			case BUY:
				buyProgress(progress, lastProgress, price.getValue().get(0), orderTriggerType);
				break;
			case SELL:
				sellProgress(progress, lastProgress, price.getValue().get(0), orderTriggerType);
				break;
			case NONE:
				nonProgress(progress, lastProgress, price.getValue().get(0));
				break;
			}
			lastProgress = progress;
			lstProgress.add(progress);
		}
		return lstProgress;
	}
	
	private void buyProgress(OrderProgress progress, OrderProgress lastProgress, double price, OrderTriggerType orderTriggerType) {
		int position = lastProgress.getPosition();
		switch(orderTriggerType) {
		case EVERY:
			buyProgress(progress, lastProgress, price);
			break;
		case FIRST_BUY:
			if(position == 0) {
				buyProgress(progress, lastProgress, price);
			}
			break;
		case FIRST_EVERY_SIDE:
			if(position != 1) {
				buyProgress(progress, lastProgress, price);
			}
			break;
		}
	}
	
	private void sellProgress(OrderProgress progress, OrderProgress lastProgress, double price, OrderTriggerType orderTriggerType) {
		int position = lastProgress.getPosition();
		switch(orderTriggerType) {
		case EVERY:
			sellProgress(progress, lastProgress, price);
			break;
		case FIRST_BUY:
			if(position == 1) {
				sellProgress(progress, lastProgress, price);
			}
			break;
		case FIRST_EVERY_SIDE:
			if(position != -1) {
				sellProgress(progress, lastProgress, price);
			}
			break;
		}
	}
	
	private void buyProgress(OrderProgress progress, OrderProgress lastProgress, double price) {
		int oldPosition = lastProgress.getPosition();
		double oldCost = lastProgress.getAvgPrice() * oldPosition;
		progress.setPosition(oldPosition+1);
		if(oldPosition < 0) {
			// close
			double closePnl = lastProgress.getAvgPrice() - price;
			progress.setClosePnl(lastProgress.getClosePnl() + closePnl);
			progress.setAvgPrice(lastProgress.getAvgPrice());
			progress.setOpenPnl(lastProgress.getOpenPnl());
		} else if(oldPosition >= 0) {
			// open
			progress.setClosePnl(lastProgress.getClosePnl());
			progress.setAvgPrice((oldCost + price) / (double)progress.getPosition());
			progress.setOpenPnl((price - progress.getAvgPrice()) * (double)progress.getPosition());
		}
	}
	
	private void sellProgress(OrderProgress progress, OrderProgress lastProgress, double price) {
		int oldPosition = lastProgress.getPosition();
		double oldCost = lastProgress.getAvgPrice() * oldPosition;
		progress.setPosition(oldPosition-1);
		if(oldPosition > 0) {
			// close
			double closePnl = price - lastProgress.getAvgPrice();
			progress.setClosePnl(lastProgress.getClosePnl() + closePnl);
			progress.setAvgPrice(lastProgress.getAvgPrice());
			progress.setOpenPnl(lastProgress.getOpenPnl());
		} else if(oldPosition <= 0) {
			// open
			progress.setClosePnl(lastProgress.getClosePnl());
			progress.setAvgPrice((oldCost + price) / (double)progress.getPosition());
			progress.setOpenPnl((progress.getAvgPrice() - price) * (double)progress.getPosition());
		}
	}
	
	private void nonProgress(OrderProgress progress, OrderProgress lastProgress, double price) {
		progress.setAvgPrice(lastProgress.getAvgPrice());
		progress.setClosePnl(lastProgress.getClosePnl());
		progress.setPosition(lastProgress.getPosition());
		
		int position = progress.getPosition();
		double pnl = 0;
		if(position > 0) {
			pnl = (price - progress.getAvgPrice()) * progress.getPosition();
		} else if(position < 0) {
			pnl = (progress.getAvgPrice() - price) * progress.getPosition();
		}
		progress.setOpenPnl(pnl);
	}
	
	private List<AnalysisResult> getPriceResult(DataSource dataSource, String product, Date from, Date to, OrderPriceType orderPriceType) throws Exception {
		TechAnalyzers analyzer = null;
		switch (orderPriceType) {
		case AVERAGE:
			analyzer = TechAnalyzers.AVERAGE_PRICE;
			break;
		case CLOSE:
			analyzer = TechAnalyzers.CLOSE_PRICE;
			break;
		case OPEN:
			analyzer = TechAnalyzers.OPEN_PRICE;
			break;
		case UPPER:
			analyzer = TechAnalyzers.UPPER_PRICE;
			break;
		case LOWER:
			analyzer = TechAnalyzers.LOWER_PRICE;
		break;
		}
		
		return TechAnalyzerManager.getInstance().getAnalysisResult(dataSource, analyzer, product, from, to);
	}
	
	private List<ActionResult> getActionResult(DataSource dataSource, TechAnalyzers analyzer, String product, Date from, Date to, String ... custom) throws Exception {
		BackTesting testing = factory.getVerifier(analyzer);
		if(testing == null) {
			log.error("BackTesting not exist. {}", analyzer);
			return null;
		}
		return testing.getAction(dataSource, product, from, to, custom);
	}
}
