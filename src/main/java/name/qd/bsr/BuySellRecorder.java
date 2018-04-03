package name.qd.bsr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import name.qd.techAnalyst.Constants.Exchange;
import name.qd.techAnalyst.TechAnalyst;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.dataSource.DataSourceFactory;
import name.qd.techAnalyst.util.TimeUtil;
import name.qd.techAnalyst.vo.ProductClosingInfo;

public class BuySellRecorder {
	private Logger log;
	private WebDriver webDriver;
	private BufferedImage bufferedImage;
	private DataSource dataSource;
	private CaptchaSolver captchaSolver;
	private String filePath = "bsr/abc.jpg";
	private List<String> lst = new ArrayList<>();
	private Date date = TimeUtil.getToday();
	private SimpleDateFormat sdf = TimeUtil.getDateFormat();
	private String dir;
	
	private BuySellRecorder() {
		initLogger();
		init();
		downloadData();
		end();
		log.info("Done.");
	}
	
	private void downloadData() {
		prepareAllProducts();
		startDownload();
	}
	
	private void downloadData(String product) throws Exception {
		downloadCaptcha();
		String ans = captchaSolver.solve(filePath);
		downloadFile(product, ans);
		moveFile(product);
	}
	
	private void moveFile(String product) {
		String productFile = product + ".csv";
		try {
			Files.move(new File("C:/Users/Shawn.Chou/Downloads/" + productFile).toPath(), new File(dir + productFile).toPath());
		} catch (IOException e) {
			log.error("Move file failed.", e);
		}
	}
	
	private void downloadFile(String product, String ans) throws Exception {
		WebElement inputStock = webDriver.findElement(By.name("TextBox_Stkno"));
		inputStock.clear();
		inputStock.sendKeys(product);
		WebElement inputCaptcha = webDriver.findElement(By.name("CaptchaControl1"));
		inputCaptcha.sendKeys(ans);
		WebElement btn = webDriver.findElement(By.name("btnOK"));
		btn.click();
		WebElement downloadLink = webDriver.findElement(By.id("HyperLink_DownloadCSV"));
		String downloadPath = downloadLink.getAttribute("href");
		webDriver.get(downloadPath);
	}
	
	private void downloadCaptcha() throws Exception {
		List<WebElement> images = webDriver.findElements(By.tagName("img"));
		URL url;
		url = new URL(images.get(1).getAttribute("src"));
		bufferedImage = ImageIO.read(url);
		ImageIO.write(bufferedImage, "jpg", new File(filePath));
	}
	
	private void startDownload() {
		List<String> lstTemp = new ArrayList<>();
		webDriver.get("http://bsr.twse.com.tw/bshtm/bsMenu.aspx");
		for(String product : lst) {
			try {
				if(isFileExist(product)) {
					log.info("File already exist. {}", product);
					continue;
				}
				downloadData(product);
			} catch (Exception e) {
				log.error("Download {} failed.", product);
				lstTemp.add(product);
				continue;
			}
			log.info("Download {} success.", product);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lst = lstTemp;
		
		if(lst.size() > 0) {
			log.info("List not empty. Download remain data.");
			startDownload();
		}
	}
	
	private void prepareAllProducts() {
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
	}
	
	private boolean isFileExist(String product) {
		return Files.exists(new File(dir + product + ".csv").toPath());
	}
	
	private void init() {
		webDriver = new ChromeDriver();
		dataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
		captchaSolver = new CaptchaSolver();
		dir = "D:/SimpleConnect_Maven/techanalyst/bsr/data/" + sdf.format(date) + "/";
		
		if(!Files.exists(new File(dir).toPath())) {
			try {
				Files.createDirectory(new File(dir).toPath());
			} catch (IOException e) {
				log.error("Create dir failed.", e);
			}
		}
	}
	
	private void initLogger() {
		System.setProperty("log4j.configurationFile", "./config/log4j2_bsr.xml");
		log = LogManager.getLogger(TechAnalyst.class);
	}
	
	private void end() {
		captchaSolver.end();
	}
	
	public static void main(String[] s) {
		new BuySellRecorder();
	}
}
