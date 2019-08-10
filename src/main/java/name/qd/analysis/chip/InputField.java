package name.qd.analysis.chip;

public class InputField {
	public static int BROKER = 1;
	public static int PRODUCT = 2;
	public static int FROM = 4;
	public static int TO = 8;
	public static int TRADE_COST = 16;
	public static int PNL = 32;
	public static int PNL_RATE = 64;
	
	public static boolean isBroker(int inputField) {
		return (inputField & BROKER) == BROKER;
	}
	public static boolean isProduct(int inputField) {
		return (inputField & PRODUCT) == PRODUCT;
	}
	public static boolean isFrom(int inputField) {
		return (inputField & FROM) == FROM;
	}
	public static boolean isTo(int inputField) {
		return (inputField & TO) == TO;
	}
	public static boolean isTradeCost(int inputField) {
		return (inputField & TRADE_COST) == TRADE_COST;
	}
	public static boolean isPNL(int inputField) {
		return (inputField & PNL) == PNL;
	}
	public static boolean isPNLRate(int inputField) {
		return (inputField & PNL_RATE) == PNL_RATE;
	}
}
