package name.qd.techAnalyst.dataSource.TWSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import name.qd.techAnalyst.dataSource.DataPoller;
import name.qd.techAnalyst.util.FileConstUtil;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.DailyClosingInfo;
import name.qd.techAnalyst.vo.ProdClosingInfo;

public class TWSEDataManager {
	private Logger log = LogManager.getLogger(TWSEDataManager.class);
	private Path folderPath;
	private DataPoller poller;
	private TWSEDataParser parser;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public TWSEDataManager() {
		folderPath = new File(FileConstUtil.FILE_DIR).toPath();
		poller = new TWSEDataPoller2018(new TWSEDataPoller2016(null));
		parser = new TWSEDataParser();
		
		initFolder();
		
		log.info("TWSE data source path:[{}]", FileConstUtil.FILE_DIR);
	}
	
	private void initFolder() {
		if(!Files.exists(folderPath)) {
			try {
				Files.createDirectory(folderPath);
			} catch (IOException e) {
				log.error("Create {} folder failed.", FileConstUtil.FILE_DIR, e);
			}
		}
	}
	
	public void checkDateAndDownload(String from, String to, String prodId) throws ParseException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dateFrom = sdf.parse(from);
		Date dateTo = sdf.parse(to);
		List<String[]> lstYearMonth = TimeUtil.getYearMonthBetween(dateFrom, dateTo);
		List<String> lstDate = TimeUtil.getDateBetween(dateFrom, dateTo);
		checkAndDownloadProdClosing(lstYearMonth, prodId);
		checkAndDownloadDailyClosing(lstDate);
	}
	
	private void checkAndDownloadProdClosing(List<String[]> lst, String prodId) throws IOException {
		for(String[] yearMonth : lst) {
			File file = new File(FileConstUtil.getProdClosingFilePath(yearMonth[0], yearMonth[1], prodId));
			if(!file.exists()) {
				poller.downloadProdClosingInfo(yearMonth[0], yearMonth[1], prodId);
			}
		}
		String[] lastYearMonth = lst.get(lst.size() - 1);
		poller.downloadProdClosingInfo(lastYearMonth[0], lastYearMonth[1], prodId);
	}
	
	private void checkAndDownloadDailyClosing(List<String> lstDate) throws IOException {
		for(int i = 0 ; i < lstDate.size() ; i++) {
			File file = new File(FileConstUtil.getDailyClosingFilePath(lstDate.get(i)));
			if(!file.exists()) {
				poller.downloadDailyClosingInfo(lstDate.get(i));
			}
		}
	}
	
	public ArrayList<ProdClosingInfo> getProdClosingInfo(String from, String to, String prodId) throws ParseException, FileNotFoundException, IOException {
		List<String[]> lstYearMonth = TimeUtil.getYearMonthBetween(sdf.parse(from), sdf.parse(to));
		ArrayList<ProdClosingInfo> lstProd = new ArrayList<ProdClosingInfo>();
		for(String[] yearMonth : lstYearMonth) {
			File file = new File(FileConstUtil.getProdClosingFilePath(yearMonth[0], yearMonth[1], prodId));
			if(file.exists()) {
				lstProd.addAll(parser.readProdClosingInfo(yearMonth[0], yearMonth[1], prodId));
			}
		}
		return lstProd;
	}
	
	public ArrayList<DailyClosingInfo> getDailyClosingInfo(String from, String to) throws ParseException, FileNotFoundException, IOException {
		List<String> lstDate = TimeUtil.getDateBetween(sdf.parse(from), sdf.parse(to));
		ArrayList<DailyClosingInfo> lstInfo = new ArrayList<DailyClosingInfo>();
		for(String date : lstDate) {
			File file = new File(FileConstUtil.getDailyClosingFilePath(date));
			if(file.exists()) {
				DailyClosingInfo dailyClosingInfo = parser.readDailyClosingInfo(date);
				if(dailyClosingInfo != null) {
					lstInfo.add(dailyClosingInfo);
				}
			}
		}
		return lstInfo;
	}
}
