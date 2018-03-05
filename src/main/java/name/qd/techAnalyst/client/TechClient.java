package name.qd.techAnalyst.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchart.style.Styler.YAxisPosition;

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.TechAnalyst;
import name.qd.techAnalyst.Constants.Exchange;
import name.qd.techAnalyst.analyzer.TechAnalyzerManager;
import name.qd.techAnalyst.client.datePicker.DayModel;
import name.qd.techAnalyst.dataSource.DataSource;
import name.qd.techAnalyst.dataSource.DataSourceFactory;
import name.qd.techAnalyst.vo.AnalysisResult;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

public class TechClient {
	private Logger log;
	private TechAnalyzerManager analyzerManager;
	private DataSource twseDataManager;
	private TechChartUI techChartUI = new TechChartUI("");
	
	private JFrame frame = new JFrame("Tech Analyze");
	private JPanel selectPanel = new JPanel();
	
	private JLabel labelTA = new JLabel("Tech Analyzer:");
	private JComboBox<String> comboTech = new JComboBox<>();
	private JLabel labelProduct = new JLabel("Product:");
	private JTextField tfProduct = new JTextField(6);
	private JLabel labelFrom = new JLabel("From:");
	private JDatePickerImpl dpFrom = new JDatePickerImpl(new JDatePanelImpl(new DayModel()), null);
	private JLabel labelTo = new JLabel("To:");
	private JDatePickerImpl dpTo = new JDatePickerImpl(new JDatePanelImpl(new DayModel()), null);
	private JButton btnAdd = new JButton("Add");
	private JButton btnRemove = new JButton("Remove");
	private JPanel chartPanel = techChartUI.getChartPanel();
	
	

	public TechClient() {
		initFrame();
		initLogger();
		initManager();
	}
	
	private void initFrame() {
		frame.setSize(1024, 768);
		frame.setMinimumSize(new Dimension(1024, 768));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.getContentPane().setLayout(new BorderLayout());
		selectPanel.setLayout(new GridBagLayout());
		selectPanel.add(labelTA);
		selectPanel.add(comboTech);
		selectPanel.add(labelProduct);
		selectPanel.add(tfProduct);
		selectPanel.add(labelFrom);
		selectPanel.add(dpFrom);
		selectPanel.add(labelTo);
		selectPanel.add(dpTo);
		selectPanel.add(btnAdd);
		selectPanel.add(btnRemove);
		
		frame.add(selectPanel, BorderLayout.NORTH);
		frame.add(chartPanel, BorderLayout.CENTER);
		
		setComboData();
		setButtonListener();
	}
	
	private void initLogger() {
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		log = LogManager.getLogger(TechAnalyst.class);
	}
	
	private void initManager() {
		analyzerManager = new TechAnalyzerManager();
		twseDataManager = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
	}
	
	private void setComboData() {
		for(Analyzer analyzer : Analyzer.values()) {
			comboTech.addItem(analyzer.name());
		}
	}
	
	private void setButtonListener() {
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzer = comboTech.getSelectedItem().toString();
				String product = tfProduct.getText();
				Date from = (Date) dpFrom.getModel().getValue();
				Date to = (Date) dpTo.getModel().getValue();
				runAnalyzer(Analyzer.valueOf(analyzer), product, from, to);
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzer = comboTech.getSelectedItem().toString();
				
			}
		});
	}
	
	private void runAnalyzer(Analyzer analyzer, String product, Date from, Date to) {
		List<AnalysisResult> lst = getAnalysisResult(analyzer, product, from, to);
		techChartUI.setData(analyzer.name(), lst, YAxisPosition.Left);
		frame.revalidate();
		chartPanel.repaint();
	}
	
	private List<AnalysisResult> getAnalysisResult(Analyzer analyzer, String product, Date from, Date to) {
		List<AnalysisResult> lst = analyzerManager.getAnalysisResult(twseDataManager, analyzer, product, from, to);
		for(AnalysisResult result : lst) {
			System.out.println(result.getKeyString() + ":" + result.getValue());
		}
		return lst;
	}
	
	public static void main(String[] s) {
		new TechClient();
	}
}
