package driver;

import javax.swing.SwingUtilities;

import components.MainFrame;
import utils.SoundHandler;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
			SoundHandler.init();
			
			SoundHandler.preLoadSounds("Assets");
			SoundHandler.preLoadSounds("Assets/Piano");
			
			SoundHandler.AudioSample silence = SoundHandler.getSilence();
			SoundHandler.playSound(silence);
            MainFrame frame = new MainFrame();
            
            frame.setVisible(true);
        });
	}

}
