package name.qd.analysis.dataSource.TWSE.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.utils.TWSECaptchaSolver;
import name.qd.analysis.utils.TimeUtil;

public class BuySellRecorder {
	private Logger log = LoggerFactory.getLogger(BuySellRecorder.class);
	private WebDriver webDriver;
	private BufferedImage bufferedImage;
	private TWSECaptchaSolver captchaSolver;
	private String captchaPath;
	private List<String> lst;
	private List<String> lstRemain = new ArrayList<>();
	private SimpleDateFormat sdf = TimeUtil.getDateFormat();
	private String dir;
	private int total;
	private final int workerId;
	
	public BuySellRecorder(int workerId, List<String> lst) {
		this.lst = lst;
		this.workerId = workerId;
		init();
		downloadData();
		end();
		log.info("{} worker done. {}", workerId, total);
	}
	
	private void downloadData() {
		startDownload();
	}
	
	private void downloadData(String product) throws Exception {
		downloadCaptcha();
		String ans = captchaSolver.solve(captchaPath);
		downloadFile(product, ans);
	}
	
	private void moveFile(String product) {
		String productFile = product + ".csv";
		Path filePath = new File("C:/Users/Shawn.Chou/Downloads/" + productFile).toPath();
		try {
			if(Files.exists(filePath)) {
				Files.move(filePath, new File(dir + productFile).toPath());
			}
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
		ImageIO.write(bufferedImage, "jpg", new File(captchaPath));
	}
	
	private void startDownload() {
		webDriver.get("http://bsr.twse.com.tw/bshtm/bsMenu.aspx");
		for(String product : lst) {
			try {
				if(isFileExist(product)) {
					log.info("File downloaded. {}", product);
					lstRemain.remove(product);
					continue;
				}
				downloadData(product);
			} catch (Exception e) {
				log.error("Download {} failed.", product);
				break;
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(String product : lst) {
			moveFile(product);
		}
		
		lst.addAll(lstRemain);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(lst.size() > 0) {
			log.info("List not empty. Download remain data. {}", lst.size());
			startDownload();
		}
	}
	
	private boolean isFileExist(String product) {
		return Files.exists(new File(dir + product + ".csv").toPath());
	}
	
	private void init() {
		webDriver = new ChromeDriver();
		captchaSolver = new TWSECaptchaSolver();
		captchaPath = "bsr/" + workerId + "/twse.jpg";
		
		
	}
	
	private void end() {
		captchaSolver.end();
	}
}
