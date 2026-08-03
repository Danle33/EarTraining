package driver;

import javax.swing.SwingUtilities;

import components.MainFrame;
import utils.SoundHandler;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
		SwingUtilities.invokeLater(() -> {
			SoundHandler.init();
			
            MainFrame frame = new MainFrame();
            
            frame.setVisible(true);
        });
	}

}
