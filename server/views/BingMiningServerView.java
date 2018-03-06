package server.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.util.ArrayList;
import java.util.List;

public class BingMiningServerView {

	private JFrame frame;
	private JPanel panel;
	private JLabel title;
	private JLabel moneyAmount;
	private JLabel connectedUsersAmount;
	private JLabel animationLabel;
	private JLabel dbTableLabel;
	
	private List<OnCloseCallback> closeCallbacks;

	public BingMiningServerView() {
		closeCallbacks = new ArrayList<>();
		this.setup();
	}
	
	public void setUserAmount(int amount) {
		connectedUsersAmount.setText("Active Miners : " + amount);
	}
	
	public void setMoneyMade(double amount) {
		moneyAmount.setText("MONEY MADE : $" + String.format("%.2f", 0.0));
	}
	
	public void setDBText(String db) {
		dbTableLabel.setText("<html><pre>"+db+"</pre></html>");
		frame.pack();
	}
	
	public void addOnCloseCallback(OnCloseCallback cc) {
		closeCallbacks.add(cc);
	}
	
	private void setup() {
		frame = new JFrame("Bing Mining");
		frame.setSize(900, 150);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		{
			Font bigFont = new Font("arial", Font.BOLD, 50);
			Font smallFont = new Font("arial", Font.ITALIC, 20);
			
			title = new JLabel("BING MINING");
			title.setFont(bigFont);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			moneyAmount = new JLabel();
			setMoneyMade(0.0);
			moneyAmount.setFont(smallFont);
			moneyAmount.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			connectedUsersAmount = new JLabel();
			setUserAmount(0);
			connectedUsersAmount.setFont(smallFont);
			connectedUsersAmount.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			animationLabel = new JLabel(new ImageIcon("res/bingmining.gif"));
			animationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			dbTableLabel = new JLabel("\na\nb\n\n\n\n\n\n\n\n\n\n\nz\n");
			dbTableLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			dbTableLabel.setPreferredSize(new Dimension(500, 200));
			JScrollPane scrollFrame = new JScrollPane(dbTableLabel);
			dbTableLabel.setAutoscrolls(true);
			scrollFrame.setPreferredSize(new Dimension(500, 200));
			
			
			panel.add(title);
			panel.add(moneyAmount);
			panel.add(connectedUsersAmount);
			panel.add(animationLabel);
			panel.add(scrollFrame);
		}
		frame.add(panel);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for (OnCloseCallback cc : closeCallbacks)
					cc.onClose();
			}
		});
		
		frame.setVisible(true);
	}

}