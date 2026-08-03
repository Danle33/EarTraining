package components;

import utils.*;
import javax.swing.*;

public class HomePanel extends JPanel {
    private StyledLabel lblTitle;
    private StyledButton btnIntervalMode;
    private StyledButton btnNoteMode;

    public HomePanel(MainFrame mainFrame) {
        setLayout(null);
        setBackground(Colors.BG_DARK);

        lblTitle = new StyledLabel("Ear Trainer", 30, true);
        btnIntervalMode = new StyledButton("Interval Guessing");
        btnNoteMode = new StyledButton("Note Guessing");

        btnNoteMode.setPrimaryColor(Colors.ACTION_BLUE);
        btnIntervalMode.setFontSize(18);
        btnNoteMode.setFontSize(18);

        add(lblTitle);
        add(btnIntervalMode);
        add(btnNoteMode);

        btnIntervalMode.addActionListener(e -> {
            mainFrame.showScreen(new IntervalsPanel(mainFrame));
        });

        btnNoteMode.addActionListener(e -> {
            mainFrame.showScreen(new NotePanel(mainFrame));
        });
    }
    
    /**
     * Swing automatically triggers this whenever the panel is validated
     * or gains concrete dimensions on screen.
     */
    @Override
    public void doLayout() {
        super.doLayout();
        
        lblTitle.setRelativeProperties(50, 10, 80, 10);
        btnIntervalMode.setRelativeProperties(50, 43, 50, 8);
        btnNoteMode.setRelativeProperties(50, 57, 50, 8);
    }
}