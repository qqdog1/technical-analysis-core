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
import name.qd.analysis.chip.analyzer.ChipAnalyzerManager;
import name.qd.analysis.chip.vo.DailyOperate;
import name.qd.analysis.client.datePicker.DayModel;
import name.qd.analysis.dataSource.DataSource;
import name.qd.analysis.dataSource.DataSourceFactory;
import name.qd.analysis.chip.ChipAnalyzers;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

public class ChipPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(TechPanel.class);
	private ChipAnalyzerManager analyzerManager;
	private DataSource twseDataManager;
	
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
		twseDataManager = DataSourceFactory.getInstance().getDataSource(Exchange.TWSE);
	}
	
	private void initActionListener() {
		setComboData();
		setComboListener();
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
				
			}
		});
	}
	
	private void setButtonListener() {
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String analyzer = comboChip.getSelectedItem().toString();
				ChipAnalyzers chipAnalyzers = ChipAnalyzers.valueOf(analyzer);
				
				switch(chipAnalyzers) {
				case MOST_EFFECTIVE:
					mostEffective();
					break;
				case CASH_FLOW:
					break;
				case FUZZY_QUERY:
					fuzzyQuery();
					break;
				}
			}
		});
	}
	
	private void mostEffective() {
		Date from = (Date) dpFrom.getModel().getValue();
		Date to = (Date) dpTo.getModel().getValue();
		try {
			List<DailyOperate> lst = analyzerManager.getEffectiveList(twseDataManager, from, to);
			showOnTable(lst);
		} catch (Exception e) {
			log.error("Get effective list failed.", e);
		}
	}
	
	private void fuzzyQuery() {
		Date from = (Date) dpFrom.getModel().getValue();
		Date to = (Date) dpTo.getModel().getValue();
		
		String broker = comboBroker.getSelectedItem().toString();
		if("".equals(broker)) broker = null;
		String product = tfProduct.getText().trim();
		if("".equals(product)) product = null;
		String cost = tfCost.getText().trim();
		Double tradeCost = "".equals(cost) ? null : Double.parseDouble(cost);
		String pnlString = tfPnl.getText().trim();
		Double pnl = "".equals(pnlString) ? null : Double.parseDouble(pnlString);
		String pnlRateString = tfPnlRate.getText().trim();
		Double pnlRate = "".equals(pnlRateString) ? null : Double.parseDouble(pnlRateString);
		
		try {
			List<DailyOperate> lst = analyzerManager.fuzzyQuery(twseDataManager, from, to, broker, product, tradeCost, pnl, pnlRate);
			showOnTable(lst);
		} catch (Exception e) {
			log.error("Fuzzy Query Failed.", e);
		}
	}
	
	private void showOnTable(List<DailyOperate> lst) {
		String[] header = {"Broker", "Product", "Trade Cost", "Pnl", "%"};
		String[][] data = new String[lst.size()][5];
		
		for(int i = 0 ; i < lst.size() ; i++) {
			String[] s = new String[5];
			DailyOperate operate = lst.get(i);
			s[0] = operate.getBrokerName();
			s[1] = operate.getProduct();
			s[2] = String.valueOf(operate.getTradeCost());
			s[3] = String.valueOf(operate.getPnl());
			s[4] = String.valueOf(operate.getPnlRate());
			data[i] = s;
		}
		remove(scrollPane);
		table = new JTable(data, header);
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
}
