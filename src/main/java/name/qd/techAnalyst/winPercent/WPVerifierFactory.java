package name.qd.techAnalyst.winPercent;

public class WPVerifierFactory {
	private static WPVerifierFactory instance = new WPVerifierFactory();
	
	private WPVerifierFactory() {
	}
	
	public WPVerifierFactory getInstance() {
		return instance;
	}
	
	
}
