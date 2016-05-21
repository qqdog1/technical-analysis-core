package name.qd.techAnalyst.util;

public class ADROCUtil {
	public static String AD2ROC(String sAD) {
		return String.valueOf(AD2ROC(Integer.parseInt(sAD)));
	}
	
	public static String ROC2AD(String sROC) {
		return String.valueOf(ROC2AD(Integer.parseInt(sROC)));
	}
	
	public static int AD2ROC(int iAD) {
		return iAD - 1911;
	}
	
	public static int ROC2AD(int iROC) {
		return iROC + 1911;
	}
}
