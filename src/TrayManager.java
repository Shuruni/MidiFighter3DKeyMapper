import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TrayManager {
	private PopupMenu popup;
	private TrayIcon trayIcon;
	private SystemTray tray;
	
	public TrayManager() throws IOException {
		if (!SystemTray.isSupported()) {
	        System.out.println("SystemTray is not supported");
	        return;
	    }
		
	    Image trayImage = ImageIO.read(getClass().getResource("tray.png"));
	    
	    popup = new PopupMenu();
	    trayIcon = new TrayIcon(trayImage, "MidiFighter3D KeyMap", popup);
	    tray = SystemTray.getSystemTray();
	    
	    trayIcon.setPopupMenu(popup);
	    try {
	        tray.add(trayIcon);
	    } catch (AWTException e) {
	        System.out.println("TrayIcon could not be added.");
	    }
	}
	
	public void populate(KeyMapper keyMapper, ActionListener exitListener){
	    MenuItem exitItem = new MenuItem("Exit");
	    exitItem.addActionListener(exitListener);
	    
	    MenuItem keyMapItem = new MenuItem("Change Keymappings");
	    keyMapItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyMapper.show();
			}
		});
	    
	    popup.add(keyMapItem);
	    popup.add(exitItem);
	    
	}
}
