package name.qd.analysis.dataSource.TWSE.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.dataSource.vo.ProductClosingInfo;
import name.qd.analysis.utils.TimeUtil;

public class BSRRecorderManager {
	private Logger log;
	private static int WORKER_COUNT = 2;
	private static String CONF_PATH = "./config/bsr.conf";
	private static String BSR_FOLDER = "./file/TWSE/bsr/";
	private static String CHROME_DOWNLOAD_FOLDER = "chrome_download_folder";
	private final ExecutorService executor = Executors.newFixedThreadPool(WORKER_COUNT);
	private SimpleDateFormat sdf = TimeUtil.getDateFormat();
	private Date date;
	private DataSource dataSource;
	private String dir;
	private List<String> lst = new ArrayList<>();
	private int total;
	private List<List<String>> lstWorkerProducts;
	private Properties properties;
	
	public BSRRecorderManager() {
		initSysProp();
		initDate();
		initConfig();
		dataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
		initFolder();
		initProducts();
		initWorkers();
	}
	
	private void initDate() {
		date = TimeUtil.getToday();
		try {
			date = TimeUtil.getDateFormat().parse("20190125");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void initConfig() {
		try {
			properties = new Properties();
			FileInputStream fIn = new FileInputStream(CONF_PATH);
			properties.load(fIn);
			fIn.close();
		} catch (IOException e) {
			log.error("Init config failed.", e);
		}
	}
	
	private void initFolder() {
		dir = BSR_FOLDER + sdf.format(date) + "/";
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
	
	private void initSysProp() {
		Properties prop = System.getProperties();
		prop.setProperty("log4j.configurationFile", "./config/log4j2_bsr.xml");
		prop.setProperty("webdriver.chrome.driver", "./bsr/driver/chromedriver.exe");
		log = LoggerFactory.getLogger(BSRRecorderManager.class);
	}
	
	private void initWorkers() {
		for(int i = 0 ; i < WORKER_COUNT ; i++) {
			executor.execute(new BuySellRecorder(i, lstWorkerProducts.get(i), properties.getProperty(CHROME_DOWNLOAD_FOLDER), dir));
		}
	}
	
	
	public static void main(String[] s) {
		new BSRRecorderManager();
	}
}
