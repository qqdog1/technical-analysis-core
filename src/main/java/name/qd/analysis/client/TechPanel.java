package name.qd.analysis.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.AnalyzerType;
import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.client.datePicker.DayModel;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.tech.TechAnalyzers;
import name.qd.analysis.tech.analyzer.TechAnalyzerManager;
import name.qd.analysis.tech.vo.AnalysisResult;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

public class TechPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(TechPanel.class);
	private TechAnalyzerManager analyzerManager;
	private DataSource twseDataManager;
	private TechJFreeChart jFreechart = new TechJFreeChart();
	
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
	private JButton btnBackTest = new JButton("BackTest");
	private JPanel chartPanel;
	private List<JLabel> lstLabel = new ArrayList<>();
	private List<JTextField> lstTextField = new ArrayList<>();
	private GridBagConstraints gridBagConstraints = new GridBagConstraints();
	private Color disableColor = new Color(238,238,238);
	
	public TechPanel() {
		initPanel();
		initManager();
		initActionListener();
	}
	
	private void initPanel() {
		this.setLayout(new BorderLayout());
		
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
		addToSelectPanel(btnBackTest, 11, 0);
		
		add(selectPanel, BorderLayout.NORTH);
	}
	
	private void addToSelectPanel(Component comp, int x, int y) {
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		selectPanel.add(comp, gridBagConstraints);
	}
	
	private void initManager() {
		analyzerManager = TechAnalyzerManager.getInstance();
		twseDataManager = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
	}
	
	private void initActionListener() {
		setComboData();
		setComboListener();
		setButtonListener();
	}
	
	private void setComboData() {
		for(TechAnalyzers analyzer : TechAnalyzers.values()) {
			comboTech.addItem(analyzer.name());
		}
		AutoCompleteDecorator.decorate(comboTech);
	}
	
	private void setComboListener() {
		comboTech.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzerString = comboTech.getSelectedItem().toString();
				TechAnalyzers analyzer = TechAnalyzers.valueOf(analyzerString);
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
				revalidate();
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
							runCustomAnalyzer(TechAnalyzers.valueOf(analyzer), product, from, to, s);
							return;
						}
					}
					runAnalyzer(TechAnalyzers.valueOf(analyzer), product, from, to);
				} catch (Exception ex) {
					// TODO
					log.error("Run Analyzer failed.", ex);
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
		
		btnBackTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	
	private void runAnalyzer(TechAnalyzers analyzer, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = getAnalysisResult(analyzer, product, from, to);
		jFreechart.setData(analyzer.name(), lst);
		paintResult();
	}
	
	private void runCustomAnalyzer(TechAnalyzers analyzer, String product, Date from, Date to, String ... inputs) throws Exception {
		List<AnalysisResult> lst = getCustomAnalysisResult(analyzer, product, from, to, inputs);
		jFreechart.setData(analyzer.name(), lst);
		paintResult();
	}
	
	private void paintResult() {
		if(chartPanel != null) {
			remove(chartPanel);
		}
		chartPanel = jFreechart.getChartPanel();
		add(chartPanel, BorderLayout.CENTER);
		
		chartPanel.revalidate();
		chartPanel.repaint();
		revalidate();
		repaint();
	}
	
	private List<AnalysisResult> getAnalysisResult(TechAnalyzers analyzer, String product, Date from, Date to) throws Exception {
		List<AnalysisResult> lst = analyzerManager.getAnalysisResult(twseDataManager, analyzer, product, from, to);
		for(AnalysisResult result : lst) {
			System.out.println(result.getKeyString() + ":" + result.getValue());
		}
		return lst;
	}
	
	private List<AnalysisResult> getCustomAnalysisResult(TechAnalyzers analyzer, String product, Date from, Date to, String ...inputs) throws Exception {
		List<AnalysisResult> lst = analyzerManager.getCustomAnalysisResult(twseDataManager, analyzer, product, from, to, inputs);
		for(AnalysisResult result : lst) {
			System.out.println(result.getKeyString() + ":" + result.getValue());
		}
		return lst;
	}
	
	private List<String> getCustomDesception(TechAnalyzers analyzer) {
		return analyzerManager.getCustomDescription(analyzer);
	}
	
	private AnalyzerType getAnalyzerType(TechAnalyzers analyzer) {
		return analyzerManager.getAnalyzerType(analyzer);
	}
	
	public static void main(String[] s) {
		new TechPanel();
	}
}
