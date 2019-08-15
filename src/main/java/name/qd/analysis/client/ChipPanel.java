package name.qd.analysis.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import name.qd.analysis.Constants.Exchange;
import name.qd.analysis.chip.ChipAnalyzers;
import name.qd.analysis.chip.InputField;
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.client.datePicker.DayModel;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

public class ChipPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(TechPanel.class);
	private ChipAnalyzerManager analyzerManager;
	private DataSource dataSource;
	
	private JPanel selectPanel = new JPanel();
	private JLabel labelCA = new JLabel("Chip Analyzer:");
	private JComboBox<String> comboChip = new JComboBox<>();
	private JLabel labelBroker = new JLabel("Broker:");
	private JComboBox<String> comboBroker = new JComboBox<>();
	private JLabel labelProduct = new JLabel("Product:");
	private JTextField tfProduct = new JTextField(6);
	private JLabel labelFrom = new JLabel("From:");
	private JDatePickerImpl dpFrom = new JDatePickerImpl(new JDatePanelImpl(new DayModel()), null);
	private JLabel labelTo = new JLabel("To:");
	private JDatePickerImpl dpTo = new JDatePickerImpl(new JDatePanelImpl(new DayModel()), null);
	private JButton btnRun = new JButton("Run");
	private JLabel labelCost = new JLabel("TradeCost > ");
	private JTextField tfCost = new JTextField(6);
	private JLabel labelPnl = new JLabel("Pnl > ");
	private JTextField tfPnl = new JTextField(6);
	private JLabel labelPnlRate = new JLabel("Pnl Rate > ");
	private JTextField tfPnlRate = new JTextField(6);
	private JCheckBox checkBoxOpenPnl = new JCheckBox();
	private JLabel labelOpenPnl = new JLabel("With Open PnL");
	private GridBagConstraints gridBagConstraints = new GridBagConstraints();
	
	private JTable table = new JTable();
	private JScrollPane scrollPane = new JScrollPane(table);
	
	public ChipPanel() {
		initPanel();
		initManager();
		initActionListener();
	}
	
	private void initPanel() {
		this.setLayout(new BorderLayout());
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		selectPanel.setLayout(new GridBagLayout());
		addToSelectPanel(labelCA, 0, 0);
		addToSelectPanel(comboChip, 1, 0);
		addToSelectPanel(labelBroker, 2, 0);
		addToSelectPanel(comboBroker, 3, 0);
		addToSelectPanel(labelProduct, 4, 0);
		addToSelectPanel(tfProduct, 5, 0);
		addToSelectPanel(labelFrom, 6, 0);
		addToSelectPanel(dpFrom, 7, 0);
		addToSelectPanel(labelTo, 8, 0);
		addToSelectPanel(dpTo, 9, 0);
		addToSelectPanel(btnRun, 10, 0);
		addToSelectPanel(labelCost, 0, 1);
		addToSelectPanel(tfCost, 1, 1);
		addToSelectPanel(labelPnl, 2, 1);
		addToSelectPanel(tfPnl, 3, 1);
		addToSelectPanel(labelPnlRate, 4, 1);
		addToSelectPanel(tfPnlRate, 5, 1);
		addToSelectPanel(checkBoxOpenPnl, 6, 1);
		addToSelectPanel(labelOpenPnl, 7, 1);
		
		add(selectPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void addToSelectPanel(Component comp, int x, int y) {
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		selectPanel.add(comp, gridBagConstraints);
	}
	
	private void initManager() {
		analyzerManager = ChipAnalyzerManager.getInstance();
		dataSource = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
	}
	
	private void initActionListener() {
		setComboListener();
		setComboData();
		setButtonListener();
	}
	
	private void setComboData() {
		for(ChipAnalyzers analyzer : ChipAnalyzers.values()) {
			comboChip.addItem(analyzer.name());
		}
		AutoCompleteDecorator.decorate(comboChip);
		
		try {
			List<String> lst = Files.readAllLines(Paths.get("./config/TWSEBrokers.txt"));
			for(String broker : lst) {
				comboBroker.addItem(broker);
			}
		} catch (IOException e) {
			log.error("Get broker file failed: ./config/TWSEBrokers.txt", e);
		}
		AutoCompleteDecorator.decorate(comboBroker);
	}
	
	private void setComboListener() {
		comboChip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzerString = comboChip.getSelectedItem().toString();
				ChipAnalyzers analyzer = ChipAnalyzers.valueOf(analyzerString);
				int inputField = analyzerManager.getInputField(analyzer);
				setInputField(inputField);
			}
		});
	}
	
	private void setAllInputDisable() {
		comboBroker.setEnabled(false);
		tfProduct.setEditable(false);
		dpFrom.setEnabled(false);
		dpTo.setEnabled(false);
		tfCost.setEditable(false);
		tfPnl.setEditable(false);
		tfPnlRate.setEditable(false);
		checkBoxOpenPnl.setEnabled(false);
	}
	
	private void setInputField(int inputField) {
		setAllInputDisable();
		if(InputField.isBroker(inputField)) {
			comboBroker.setEnabled(true);
		}
		if(InputField.isProduct(inputField)) {
			tfProduct.setEditable(true);
		}
		if(InputField.isFrom(inputField)) {
			dpFrom.setEnabled(true);
		}
		if(InputField.isTo(inputField)) {
			dpTo.setEnabled(true);
		}
		if(InputField.isTradeCost(inputField)) {
			tfCost.setEditable(true);
		}
		if(InputField.isPNL(inputField)) {
			tfPnl.setEditable(true);
		}
		if(InputField.isPNLRate(inputField)) {
			tfPnlRate.setEditable(true);
		}
		if(InputField.isWithOpenPnl(inputField)) {
			checkBoxOpenPnl.setEnabled(true);
		}
	}
	
	private void setButtonListener() {
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzer = comboChip.getSelectedItem().toString();
				ChipAnalyzers chipAnalyzers = ChipAnalyzers.valueOf(analyzer);
				String branch = comboBroker.getSelectedItem().toString();
				String product = tfProduct.getText();
				Date from = (Date) dpFrom.getModel().getValue();
				Date to = (Date) dpTo.getModel().getValue();
				double tradeCost = 0;
				if(!"".equals(tfCost.getText())) {
					tradeCost = Double.parseDouble(tfCost.getText());
				}
				boolean isOpenPnl = checkBoxOpenPnl.isSelected();
			
				List<List<String>> lst = analyzerManager.getAnalysisResult(dataSource, chipAnalyzers, branch, product, from, to, tradeCost, isOpenPnl);
				showData(lst);
			}
		});
	}
	
	private void showData(List<List<String>> lst) {
		int size = lst.get(0).size();
		String[] header = new String[size];
		lst.get(0).toArray(header);
		String[][] dataArray = new String[lst.size()-1][];
		for(int i = 1 ; i < lst.size() ; i++) {
			String[] data = new String[size];
			lst.get(i).toArray(data);
			dataArray[i-1] = data;
		}
		remove(scrollPane);
		table = new JTable(dataArray, header);
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
}
