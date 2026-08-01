package components;

import utils.*;
import javax.swing.*;

public class IntervalsPanel extends JPanel {
    private StyledButton btnPlay;

    public IntervalsPanel(MainFrame mainFrame) {
        setLayout(null);
        setBackground(Colors.BG_DARK);

        btnPlay = new StyledButton("Play");
        btnPlay.setPrimaryColor(Colors.ACTION_BLUE);
        btnPlay.setFontSize(20);

        

        add(btnPlay);

        btnPlay.addActionListener(e -> {
            // mainFrame.showScreen(new IntervalPanel(mainFrame));
        });

    }
    
    /**
     * Swing automatically triggers this whenever the panel is validated
     * or gains concrete dimensions on screen.
     */
    @Override
    public void doLayout() {
        super.doLayout();
        
        btnPlay.setRelativeProperties(50, 10, 50, 8);
    }
}