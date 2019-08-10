package name.qd.analysis.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisClient {
	private Logger log = LoggerFactory.getLogger(AnalysisClient.class);
	private JFrame frame = new JFrame("Analyzer");
	private JPanel panelAnalysis = new JPanel();
	private JButton btnTech = new JButton("Tech Analysis");
	private JButton btnChip = new JButton("Chip Analysis");
	private JPanel techPanel = new TechPanel();
	private JPanel chipPanel = new ChipPanel();
	
	private AnalysisClient() {
		initFrame();
		
		setButtonListener();
	}
	
	private void initFrame() {
		frame.setSize(1200, 768);
//		frame.setMinimumSize(new Dimension(1024, 768));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.getContentPane().setLayout(new BorderLayout());
		
		panelAnalysis.setLayout(new FlowLayout());
		panelAnalysis.add(btnTech);
		panelAnalysis.add(btnChip);
		
		frame.add(panelAnalysis, BorderLayout.NORTH);
	}
	
	private void setButtonListener() {
		btnTech.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.debug("Click tech panel.");
				frame.remove(chipPanel);
				frame.add(techPanel, BorderLayout.CENTER);
				frame.revalidate();
				frame.repaint();
			}
		});
		
		btnChip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.debug("Click chip panel.");
				frame.remove(techPanel);
				frame.add(chipPanel, BorderLayout.CENTER);
				frame.revalidate();
				frame.repaint();
			}
		});
	}
	
	public static void main(String[] args) {
		new AnalysisClient();
	}
}
