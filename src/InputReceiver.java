import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class InputReceiver implements Receiver{
	
	private Robot robot;
	private Map<Integer, String[]> keyMappings;
	private Map<String, Integer> keyCodes;
	private Map<Byte, Boolean> buttonStatusMap;
	
	public InputReceiver(Map<Integer, String[]> keyMappings, Map<String, Integer> keyCodes) throws AWTException, IOException{
		this.robot = new Robot();
		this.keyMappings = keyMappings;
		this.keyCodes = keyCodes;
		
		//Initialize Button Status Map
		this.buttonStatusMap = new HashMap<>();
		for (byte key = 36; key <= 99; key++) {
			this.buttonStatusMap.put(key, false);
		}
	}
	
	@Override
	public void send(MidiMessage message, long timeStamp) {
		
		// Note OFF: 128 -> 143
		// Note  ON: 144 -> 159
		int status = message.getStatus();
		boolean on = status >= 144 && status <= 159;
		boolean off = status >= 128 && status <= 143;
		
		// actual key press
		Byte data = message.getMessage()[1];
		
		if (this.buttonStatusMap.containsKey(data.byteValue())) {
			if (!this.buttonStatusMap.get(data.byteValue()) && on) {
				System.out.println(data + " on");
				for (String keyName : keyMappings.get(data.intValue())) {
					if(!"Disabled".equals(keyName)) {
						try{
							int keyCode = keyCodes.get(keyName);
							robot.keyPress(keyCode);
						} catch(Exception e) {
							System.out.println("Invalid Key Pressed: " + keyName);
						}
					}
				}
			} 
			else if (this.buttonStatusMap.get(data.byteValue()) && off) {
				System.out.println(data + " off");
				for (String keyName : keyMappings.get(data.intValue())) {
					if(!"Disabled".equals(keyName)) {
						try{
							int keyCode = keyCodes.get(keyName);
							robot.keyRelease(keyCode);
						} catch(Exception e) {
							System.out.println("Invalid Key Pressed: " + keyName);
						}
					}
				}
			}
			
			if (on || off) {
				this.buttonStatusMap.put(data, on);
			}
			
		}
		
	}
	public void close() {}
}
