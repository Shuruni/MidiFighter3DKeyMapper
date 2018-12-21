import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MappingButton extends JButton {
	
	/**
	 * 
	 */
	private static final Font FONT = new Font("Arial", Font.BOLD, 10);
	private static final long serialVersionUID = 1L;
	protected Map<Integer, String[]> keyMappings;
	private int buttonNum;
	private Map<String, Integer> keyCodes;
	
	public MappingButton(int buttonNum, Map<Integer, String[]> keyMappings, Map<String, Integer> keyCodes, Color bg) {
		this.buttonNum = buttonNum;
		this.keyMappings = keyMappings;
		this.keyCodes = keyCodes;
		
		updateText();
		setEvent();
		this.setBackground(bg);
		this.setFont(FONT);
	}
	
	private void updateText() {
		this.setText("<html><center>" + String.join(" + ",keyMappings.get(buttonNum)) + "</center></html>");
	}
	
	private void setEvent() {
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] choices = keyCodes.keySet().toArray(new String[keyCodes.keySet().size()]);
				Arrays.sort(choices);
				
				int choiceNum = 0;
				ArrayList<String> remap = new ArrayList<>();
				boolean completed = false;
				while(!completed) {
					JComboBox<String> cb = createComboBox(choices, choiceNum);
							
					int diagResp = JOptionPane.showOptionDialog(
							null, 
							createReMapDialog(cb, remap), 
							"Remap Button", 
							JOptionPane.DEFAULT_OPTION, 
							JOptionPane.PLAIN_MESSAGE, 
							null, 
							new String[]{"Keep Adding Keys", "Go with Current Combination"}, 
							"Keep Adding Keys");
					String selected = (String) cb.getSelectedItem();
					
					switch (diagResp) {
						case -1:
							completed = true;
							remap.clear();
							break;
						case 1:
							completed = true;
							//fall through
						case 0:
							remap.add(selected);
							if (cb.getSelectedItem().equals("Disabled")) {
								completed = true;
							}
							break;
						default:
							System.out.println("diagResp = " + diagResp);
					}
					choiceNum++;
				}
				if(!remap.isEmpty()) {
					keyMappings.put(buttonNum, remap.toArray(new String[remap.size()]));
					updateText();
				}
			}
		});
	}
	
	private JComboBox<String> createComboBox(String[] choices, int choiceNum) {
		JComboBox<String> cb = new JComboBox<>();
		if (choiceNum == 0) {
			cb.addItem("Disabled");
		}
		for (String keyName : choices) {
			cb.addItem(keyName);
		}
		
		String selected = choiceNum < keyMappings.get(buttonNum).length ? keyMappings.get(buttonNum)[choiceNum] : null;
		
		if (selected != null) {
			cb.setSelectedItem(selected);
		}
		
		return cb;
	}
	
	private JPanel createReMapDialog(JComboBox<String> cb, ArrayList<String> remappedKeys) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JLabel message = new JLabel("<html><center>Choose a key to add to the key combination on this button.</center></html>");
		JLabel current = new JLabel("Current combination: " + 
						String.join(" + ", remappedKeys) + 
						(remappedKeys.isEmpty() ? "" : " + "));
		message.setFont(new Font("Arial", Font.PLAIN, 16));
		current.setFont(new Font("Arial", Font.PLAIN, 16));
		
		JPanel cbPanel = new JPanel();
		cbPanel.add(cb);
		
		panel.add(message, BorderLayout.NORTH);
		panel.add(current, BorderLayout.CENTER);
		panel.add(cbPanel, BorderLayout.EAST);
		
		return panel;
	}
	
}
