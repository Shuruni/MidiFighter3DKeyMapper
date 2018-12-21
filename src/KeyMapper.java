import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class KeyMapper {
	
	private JFrame frame;
	private JPanel midiFighterPanel;
	private JPanel selectorPanel;
	private JPanel panel4;
	private JPanel panel3;
	private JPanel panel2;
	private JPanel panel1;
	
	private JPanel currentDisplay;
	
	public KeyMapper(Map<Integer, String[]> keyMappings, Map<String, Integer> keyCodes) {
		setup();
		populatePanels(keyMappings, keyCodes);
		displayPanel(panel4);
	}

	private void populatePanels(Map<Integer, String[]> keyMappings, Map<String, Integer> keyCodes) {
		
		//ReMap Panel Buttons
		JButton button;
		int buttonNum;
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 4; j++) {
				buttonNum = 36 + i*4 - j;
				panel4.add(new MappingButton(buttonNum, keyMappings, keyCodes, Color.orange));
				
				buttonNum = 52 + i*4 - j;
				panel3.add(new MappingButton(buttonNum, keyMappings, keyCodes, Color.cyan));
				
				buttonNum = 68 + i*4 - j;
				panel2.add(new MappingButton(buttonNum, keyMappings, keyCodes, Color.green));
				
				buttonNum = 84 + i*4 - j;
				panel1.add(new MappingButton(buttonNum, keyMappings, keyCodes, Color.magenta));
			}
		}
		
		// Selector Panel Buttons
		
		button = new JButton("1");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayPanel(panel1);
			}});
		selectorPanel.add(button);
		
		button = new JButton("2");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayPanel(panel2);
			}});
		selectorPanel.add(button);
		
		button = new JButton("3");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayPanel(panel3);
			}});
		selectorPanel.add(button);
		
		button = new JButton("4");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayPanel(panel4);
			}});
		selectorPanel.add(button);
		
	}
	
	private void setup() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Midi Fighter 3D Key Map");
		midiFighterPanel = new JPanel();
		midiFighterPanel.setPreferredSize(new Dimension(350, 350));
		
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(4,4));
		panel1.setPreferredSize(new Dimension(350, 340));
		panel1.setVisible(false);
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(4,4));
		panel2.setPreferredSize(new Dimension(350, 340));
		panel2.setVisible(false);
		
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(4,4));
		panel3.setPreferredSize(new Dimension(350, 340));
		panel3.setVisible(false);
		
		panel4 = new JPanel();
		panel4.setLayout(new GridLayout(4,4));
		panel4.setPreferredSize(new Dimension(350, 340));
		panel4.setVisible(false);
		
		selectorPanel = new JPanel();
		selectorPanel.setPreferredSize(new Dimension(350, 50));
		selectorPanel.setLayout(new GridLayout(1, 4));
		
		midiFighterPanel.add(panel1);
		midiFighterPanel.add(panel2);
		midiFighterPanel.add(panel3);
		midiFighterPanel.add(panel4);
		
		frame.add(midiFighterPanel, BorderLayout.NORTH);
		frame.add(selectorPanel,BorderLayout.SOUTH);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	protected void displayPanel(JPanel panel) {
		if(this.currentDisplay != null) {this.currentDisplay.setVisible(false);}
		this.currentDisplay = panel;
		panel.setVisible(true);
	}

	public void show() {
		frame.setVisible(true);
	}
}
