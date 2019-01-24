package name.qd.analysis.dataSource.TWSE.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.dataSource.vo.BuySellInfo;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.utils.TimeUtil;

public class BSRRecorderManager {
	private Logger log = LoggerFactory.getLogger(BSRRecorderManager.class);
	private static int WORKER_COUNT = 5;
	private BuySellRecorder[] recorders;
	private Date date;
	private DataSource dataSource;
	private String dir;
	private SimpleDateFormat sdf = TimeUtil.getDateFormat();
	private List<String> lst = new ArrayList<>();
	private int total;
	private List<List<String>> lstWorkerProducts;
	private List<BuySellRecorder> lstWorkers;
	
	public BSRRecorderManager() {
		log.info("{} workers.", WORKER_COUNT);
		initDate();
		dataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
		initFolder();
		initProducts();
		initWorkers();
	}
	
	private void initDate() {
		date = TimeUtil.getToday();
//		try {
//			date = TimeUtil.getDateFormat().parse("20190104");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
	
	private void initFolder() {
		dir = "D:/SimpleConnect_Maven/techanalyst/file/TWSE/bsr/" + sdf.format(date) + "/";
		if(!Files.exists(new File(dir).toPath())) {
			try {
				Files.createDirectory(new File(dir).toPath());
			} catch (IOException e) {
				log.error("Create dir failed.", e);
			}
		}
	}
	
	private void initProducts() {
		Map<Date, List<ProductClosingInfo>> map = null;
		try {
			map = dataSource.getAllProductClosingInfo(date, date);
		} catch (Exception e) {
			log.error("Get product list failed.", e);
		}
		
		for(List<ProductClosingInfo> lstProducts : map.values()) {
			for(ProductClosingInfo productInfo : lstProducts) {
				lst.add(productInfo.getProduct());
			}
		}
		
		total = lst.size();
		log.info("Total products : {}", total);
		
		lstWorkerProducts = new ArrayList<>();
		
		for(int i = 0 ; i < WORKER_COUNT ; i++) {
			lstWorkerProducts.add(new ArrayList<>());
		}
		
		for(int i = 0 ; i < total ; i++) {
			int index = i % WORKER_COUNT;
			lstWorkerProducts.get(index).add(lst.get(i));
		}
	}
	
	private void initWorkers() {
		for(int i = 0 ; i < WORKER_COUNT ; i++) {
			lstWorkers.add(new BuySellRecorder(i, lstWorkerProducts.get(i)));
		}
	}
	
	public void run() {
		
	}
	
	public static void main(String[] s) {
		BSRRecorderManager manager = new BSRRecorderManager();
		manager.run();
	}
}
