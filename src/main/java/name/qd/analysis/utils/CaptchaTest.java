package name.qd.analysis.utils;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

public class CaptchaTest {
	
	private TWSECaptchaSolver solver;
	private String folderPath = "./bsr/image/";
	private String wrongPath = "./bsr/wrong/";
	private String fileToSolve = "2A64U.jpg";

	private CaptchaTest() {
		init();
		
//		solve(fileToSolve);
		
		try {
			solveAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		solver = new TWSECaptchaSolver();
	}
	
	private void solve(String file) {
		String filePath = folderPath + file;
		String ans = solver.solve(filePath);
		String name = file.split("\\.")[0];
		System.out.println(name + "  but -> " + ans);
		System.out.println(ans.equals(name));
	}
	
	private void solveAll() throws IOException {
		File folder = new File(folderPath);
		for(File file : folder.listFiles()) {
			String ans = solver.solve(file.getCanonicalPath());
			String name = file.getName().split("\\.")[0];
			if(!ans.equals(name)) {
				File target = new File(wrongPath + file.getName());
				Files.copy(file, target);
			}
		}
	}
	
	public static void main(String[] s) {
		new CaptchaTest();
	}
}
