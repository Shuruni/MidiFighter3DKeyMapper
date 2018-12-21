import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 * @author Shuruni
 *
 *	A simple Key mapping application for the Midi Fighter 3D
 *
 *	The pattern in which the Button numbers fall is like so:
 * 			4	3	2	1
 *  		8	7	6	5
 *   		12	11	10	9
 *    		16	15	14	13
 *    
 *	MidiFighter3D note values:
 *	Min = 36   Max = 99
 *	
 *	4th: 36-51
 *	3rd: 52-67
 *	2nd: 68-83
 *	1st: 84-99
 */
public class Main {
	
	private static String LOCAL_DATA_LOCATION = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "Midi Fighter 3D Key Mappings";
	public static String CONFIG_FILE = "default.cfg";
	public static final Map<Integer, String[]> KEY_MAPPINGS = new HashMap<>();
	private static final Map<String, Integer> KEY_CODES = new HashMap<>();

	public static void main(String[] args) {
		try {
			// Load Key Mappings from Config
			while (KEY_MAPPINGS.isEmpty()) {
				try(BufferedReader file = new BufferedReader(new FileReader(LOCAL_DATA_LOCATION + File.separator + CONFIG_FILE))) {
					loadConfig(file);
				} catch(FileNotFoundException e) {
					generateConfig();
					JOptionPane.showMessageDialog(
							null, 
							"A Default Config File has been created at " + LOCAL_DATA_LOCATION + File.separator + CONFIG_FILE, 
							"Directory Added", 
							1);
				}
			}
			
			// Populate KeyCodes Conversions Map
			for (int i = 0; i < 65500; i++) {
				String keyName = KeyEvent.getKeyText(i);
				if(!keyName.contains("Unknown keyCode:")) {
					KEY_CODES.put(keyName, i);
				}
			}
			
			//Connect Custom Midi Receiver to System Midi Transmitter
			Transmitter trans = MidiSystem.getTransmitter();
			trans.setReceiver(new InputReceiver(KEY_MAPPINGS, KEY_CODES));
			
			//Set-up GUI Key Re-mapping Window
			KeyMapper keyMapper = new KeyMapper(KEY_MAPPINGS, KEY_CODES);
			
			//Set-up System Tray Application
			TrayManager trayManager = new TrayManager();
			trayManager.populate(keyMapper, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveConfig();
					System.exit(1);
				}
			});
			
			while(true) {
				Thread.sleep(3000);
			}
		}
		catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			JOptionPane.showMessageDialog(null, exceptionAsString, "Exception Occurred", JOptionPane.NO_OPTION);
		}
	}
	
	private static void generateConfig() throws IOException {
		System.out.println("generating config...");
		new File(LOCAL_DATA_LOCATION).mkdir();
		try(BufferedWriter file = new BufferedWriter(new FileWriter(LOCAL_DATA_LOCATION + File.separator + CONFIG_FILE))) {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classloader.getResourceAsStream(CONFIG_FILE);
			InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(streamReader);
			StringBuilder toWrite = new StringBuilder();
			for (String line; (line = reader.readLine()) != null;) {
				toWrite.append(line + "\n");
			}
			toWrite.delete(toWrite.length() - 1, toWrite.length());
			file.write(toWrite.toString());
		}
	}

	private static void loadConfig(BufferedReader file) throws IOException {
		System.out.println("loading config...");
		int noteNum = 36;
		for (String line; (line = file.readLine()) != null;) {
			populateKeyMap(line,noteNum);
			noteNum++;
		}
		System.out.println("Config Successfully Loaded!");
	}
	
	private static void saveConfig() {
		System.out.println("saving config...");
		try(BufferedWriter file = new BufferedWriter(new FileWriter(LOCAL_DATA_LOCATION + File.separator + CONFIG_FILE))) {
			StringBuilder toWrite = new StringBuilder();
			for (int i = 36; i <= 99; i++) {
				toWrite.append(String.join(", " , KEY_MAPPINGS.get(i)) + "\n");
			}
			toWrite.delete(toWrite.length() - 1, toWrite.length());
			file.write(toWrite.toString());
			System.out.println("config saved!");
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			JOptionPane.showMessageDialog(null, "Failed to save config! see traceback: \n" + exceptionAsString, "Save Failed!", 0);
		}
	}
	
	private static void populateKeyMap(String line, int note) {
		String[] confKeys = line.split(", ");
		KEY_MAPPINGS.put(note, confKeys);
	}
}
