package name.qd.techAnalyst.dataSource.TWSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.Constants;
import name.qd.techAnalyst.dataSource.DataManager;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class TWSEDataManager implements DataManager {
	private Logger log = LogManager.getLogger(TWSEDataManager.class);
	private Path folderPath;
	private TWSEDataPoller poller;
	private TWSEDataParser parser;
	
	public TWSEDataManager() {
		folderPath = new File(Constants.FILE_DIR).toPath();
		poller = new TWSEDataPoller2018(new TWSEDataPoller2016(null));
		parser = new TWSEDataParser();
		
		initFolder();
		
		log.info("TWSE data source path:[{}]", Constants.FILE_DIR);
	}
	
	private void initFolder() {
		if(!Files.exists(folderPath)) {
			try {
				Files.createDirectory(folderPath);
			} catch (IOException e) {
				log.error("Create {} folder failed.", Constants.FILE_DIR, e);
			}
		}
	}
	
	@Override
	public void checkDataAndDownload(String product, Date from, Date to) throws ParseException, IOException {
		List<String[]> lstYearMonth = TimeUtil.getYearMonthBetween(from, to);
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		checkAndDownloadProdClosing(lstYearMonth, product);
		checkAndDownloadDailyClosing(lstDate);
	}
	
	@Override
	public List<ProductClosingInfo> getProductClosingInfo(String product, Date from, Date to) throws Exception {
		List<String[]> lstYearMonth = TimeUtil.getYearMonthBetween(from, to);
		ArrayList<ProductClosingInfo> lstProd = new ArrayList<ProductClosingInfo>();
		for(String[] yearMonth : lstYearMonth) {
			File file = new File(Constants.getProdClosingFilePath(yearMonth[0], yearMonth[1], product));
			if(file.exists()) {
				lstProd.addAll(parser.readProdClosingInfo(yearMonth[0], yearMonth[1], product));
			}
		}
		return lstProd;
	}
	
	@Override
	public List<DailyClosingInfo> getDailyClosingInfo(Date from, Date to) throws ParseException, FileNotFoundException, IOException {
		List<String> lstDate = TimeUtil.getDateBetween(from, to);
		ArrayList<DailyClosingInfo> lstInfo = new ArrayList<DailyClosingInfo>();
		for(String date : lstDate) {
			File file = new File(Constants.getDailyClosingFilePath(date));
			if(file.exists()) {
				DailyClosingInfo dailyClosingInfo = parser.readDailyClosingInfo(date);
				if(dailyClosingInfo != null) {
					lstInfo.add(dailyClosingInfo);
				}
			}
		}
		return lstInfo;
	}
	
	private void checkAndDownloadProdClosing(List<String[]> lst, String product) throws IOException {
		for(String[] yearMonth : lst) {
			File file = new File(Constants.getProdClosingFilePath(yearMonth[0], yearMonth[1], product));
			if(!file.exists()) {
				log.info("Download product closing info. {}{}", yearMonth[0], yearMonth[1]);
				poller.downloadProdClosingInfo(yearMonth[0], yearMonth[1], product);
			}
		}
		String[] lastYearMonth = lst.get(lst.size() - 1);
		poller.downloadProdClosingInfo(lastYearMonth[0], lastYearMonth[1], product);
	}
	
	private void checkAndDownloadDailyClosing(List<String> lstDate) throws IOException {
		for(int i = 0 ; i < lstDate.size() ; i++) {
			File file = new File(Constants.getDailyClosingFilePath(lstDate.get(i)));
			if(!file.exists()) {
				log.info("Download daily closing info. {}", lstDate.get(i));
				poller.downloadDailyClosingInfo(lstDate.get(i));
			}
		}
	}
}
