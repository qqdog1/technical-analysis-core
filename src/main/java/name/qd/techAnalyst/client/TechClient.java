package name.qd.techAnalyst.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

import name.qd.techAnalyst.Analyzer;
import name.qd.techAnalyst.TechAnalyst;
import name.qd.techAnalyst.Constants.AnalyzerType;
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
	private TechJFreeChart jFreechart = new TechJFreeChart();
	
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
	private JButton btnRemoveAll = new JButton("RemoveAll");
	private JPanel chartPanel;
	private List<JLabel> lstLabel = new ArrayList<>();
	private List<JTextField> lstTextField = new ArrayList<>();
	private GridBagConstraints gridBagConstraints = new GridBagConstraints();
	private Color disableColor = new Color(238,238,238);
	
	public TechClient() {
		initFrame();
		initLogger();
		initManager();
		initActionListener();
	}
	
	private void initFrame() {
		frame.setSize(1200, 768);
		frame.setMinimumSize(new Dimension(1024, 768));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.getContentPane().setLayout(new BorderLayout());
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		selectPanel.setLayout(new GridBagLayout());
		addToSelectPanel(labelTA, 0, 0);
		addToSelectPanel(comboTech, 1, 0);
		addToSelectPanel(labelProduct, 2, 0);
		addToSelectPanel(tfProduct, 3, 0);
		addToSelectPanel(labelFrom, 4, 0);
		addToSelectPanel(dpFrom, 5, 0);
		addToSelectPanel(labelTo, 6, 0);
		addToSelectPanel(dpTo, 7, 0);
		addToSelectPanel(btnAdd, 8, 0);
		addToSelectPanel(btnRemove, 9, 0);
		addToSelectPanel(btnRemoveAll, 10, 0);
		
		frame.add(selectPanel, BorderLayout.NORTH);
	}
	
	private void addToSelectPanel(Component comp, int x, int y) {
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		selectPanel.add(comp, gridBagConstraints);
	}
	
	private void initLogger() {
		System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
		log = LogManager.getLogger(TechAnalyst.class);
	}
	
	private void initManager() {
		analyzerManager = TechAnalyzerManager.getInstance();
		twseDataManager = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
	}
	
	private void initActionListener() {
		setComboListener();
		setComboData();
		setButtonListener();
	}
	
	private void setComboData() {
		for(Analyzer analyzer : Analyzer.values()) {
			comboTech.addItem(analyzer.name());
		}
	}
	
	private void setComboListener() {
		comboTech.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzerString = comboTech.getSelectedItem().toString();
				Analyzer analyzer = Analyzer.valueOf(analyzerString);
				List<String> lst = getCustomDesception(analyzer);
				AnalyzerType analyzerType = getAnalyzerType(analyzer);
				switch(analyzerType) {
				case PRODUCT:
					tfProduct.setEnabled(true);
					tfProduct.setBackground(Color.WHITE);
					break;
				case MARKET:
					tfProduct.setEnabled(false);
					tfProduct.setBackground(disableColor);
					break;
				}
				
				clearCustom();
				if(lst != null) {
					for(int i = 0 ; i < lst.size() ; i++) {
						JLabel label = new JLabel(lst.get(i));
						lstLabel.add(label);
						JTextField textField = new JTextField(6);
						lstTextField.add(textField);
						addToSelectPanel(label, i*2, 1);
						addToSelectPanel(textField, i*2+1, 1);
					}
				}
				frame.revalidate();
			}
		});
	}
	
	private void clearCustom() {
		for(JLabel label : lstLabel) {
			selectPanel.remove(label);
		}
		for(JTextField textField : lstTextField) {
			selectPanel.remove(textField);
		}
		lstLabel = new ArrayList<>();
		lstTextField = new ArrayList<>();
	}
	
	private void setButtonListener() {
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzer = comboTech.getSelectedItem().toString();
				String product = tfProduct.getText();
				Date from = (Date) dpFrom.getModel().getValue();
				Date to = (Date) dpTo.getModel().getValue();
				try {
					if(lstTextField.size() > 0) {
						boolean isCustom = false;
						List<String> lstCustomInput = new ArrayList<>();
						for(JTextField textField : lstTextField) {
							String input = textField.getText();
							if(!input.equals("")) {
								isCustom = true;
							}
							lstCustomInput.add(input);
						}
						if(isCustom) {
							String[] s = new String[lstCustomInput.size()];
							lstCustomInput.toArray(s);
							runCustomAnalyzer(Analyzer.valueOf(analyzer), product, from, to, s);
							return;
						}
					}
					runAnalyzer(Analyzer.valueOf(analyzer), product, from, to);
				} catch (Exception ex) {
					// TODO
				}
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzer = comboTech.getSelectedItem().toString();
				jFreechart.removeData(analyzer);
				paintResult();
			}
		});
		
		btnRemoveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jFreechart.removeAll();
				paintResult();
			}
		});
	}
	
	private void runAnalyzer(Analyzer analyzer, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = getAnalysisResult(analyzer, product, from, to);
		jFreechart.setData(analyzer.name(), lst);
		paintResult();
	}
	
	private void runCustomAnalyzer(Analyzer analyzer, String product, Date from, Date to, String ... inputs) throws Exception {
		List<AnalysisResult> lst = getCustomAnalysisResult(analyzer, product, from, to, inputs);
		jFreechart.setData(analyzer.name(), lst);
		paintResult();
	}
	
	private void paintResult() {
		if(chartPanel != null) {
			frame.remove(chartPanel);
		}
		chartPanel = jFreechart.getChartPanel();
		frame.add(chartPanel, BorderLayout.CENTER);
		
		chartPanel.revalidate();
		chartPanel.repaint();
		frame.revalidate();
		frame.repaint();
	}
	
	private List<AnalysisResult> getAnalysisResult(Analyzer analyzer, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = analyzerManager.getAnalysisResult(twseDataManager, analyzer, product, from, to);
		for(AnalysisResult result : lst) {
			System.out.println(result.getKeyString() + ":" + result.getValue());
		}
		return lst;
	}
	
	private List<AnalysisResult> getCustomAnalysisResult(Analyzer analyzer, String product, Date from, Date to, String ...inputs) throws Exception {
		List<AnalysisResult> lst = analyzerManager.getCustomAnalysisResult(twseDataManager, analyzer, product, from, to, inputs);
		for(AnalysisResult result : lst) {
			System.out.println(result.getKeyString() + ":" + result.getValue());
		}
		return lst;
	}
	
	private List<String> getCustomDesception(Analyzer analyzer) {
		return analyzerManager.getCustomDescription(analyzer);
	}
	
	private AnalyzerType getAnalyzerType(Analyzer analyzer) {
		return analyzerManager.getAnalyzerType(analyzer);
	}
	
	public static void main(String[] s) {
		new TechClient();
	}
}
