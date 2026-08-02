package components;

import utils.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.swing.*;

public class IntervalsPanel extends JPanel {
    private StyledButton btnPlay;
    private StyledButton btnHome;
    private StyledButton btnNext;
    private StyledButton btnPiano, btnGuitar;
    private JCheckBox chkboxSequencially;
    private StyledLabel lblScore;
    
    private List<StyledButton> intervalButtons = new ArrayList<StyledButton>();

    public IntervalsPanel(MainFrame mainFrame) {
        setLayout(null);
        setBackground(Colors.BG_DARK);
        
        lblScore = new StyledLabel("Your score: 0/0", 25, true);

        btnPlay = new StyledButton("Play");
        btnPlay.setPrimaryColor(Colors.ACTION_BLUE);
        btnPlay.setFontSize(22);
        
        btnHome = new StyledButton("Home");
        btnHome.setFontSize(16);
        
        btnNext = new StyledButton("Next");
        btnNext.setFontSize(16);
        
        btnPiano = new StyledButton("Piano");
        btnPiano.setFontSize(16);
        btnPiano.markCorrect();
        btnPiano.disableHoverPainting();
        
        btnGuitar = new StyledButton("Guitar");
        btnGuitar.setFontSize(16);
        btnGuitar.disableHoverPainting();
        
        chkboxSequencially = new JCheckBox("Play notes sequencially");
        chkboxSequencially.setFont(new Font("SansSerif", Font.PLAIN, 16));
        chkboxSequencially.setFocusPainted(false);
        chkboxSequencially.setForeground(Color.WHITE);
        chkboxSequencially.setBackground(getBackground());
        
        intervalButtons.add(new StyledButton("Minor 2nd"));
        intervalButtons.add(new StyledButton("Major 2nd"));
        intervalButtons.add(new StyledButton("Minor 3rd"));
        intervalButtons.add(new StyledButton("Major 3rd"));
        intervalButtons.add(new StyledButton("4th"));
        intervalButtons.add(new StyledButton("5th"));
        intervalButtons.add(new StyledButton("Minor 6th"));
        intervalButtons.add(new StyledButton("Major 6th"));
        intervalButtons.add(new StyledButton("Minor 7th"));
        intervalButtons.add(new StyledButton("Major 7th"));
        intervalButtons.add(new StyledButton("Octave"));
        
        for (StyledButton intervalButton : intervalButtons) {
        	intervalButton.setFontSize(13);
        	intervalButton.setPrimaryColor(getBackground());
        	add(intervalButton);
        }
        
        add(btnPlay);
        add(btnHome);
        add(btnNext);
        add(btnPiano);
        add(btnGuitar);
        add(chkboxSequencially);
        add(lblScore);

        btnPlay.addActionListener(e -> {
        	SoundHandler.AudioSample targetNote = SoundHandler.getRandomSound();
            
            // Play it instantly without reading from disk
            SoundHandler.playSound(targetNote);
        });
        
        btnHome.addActionListener(e -> {
        	mainFrame.showScreen(new HomePanel(mainFrame));
        });
        
        btnHome.addActionListener(e -> {
        	handleNext();
        });
        
        btnPiano.addActionListener(e -> {
            if (!btnPiano.markedCorrect()) {
            	btnPiano.markCorrect();
            	btnGuitar.resetStyle();
            }
        });
        
        btnGuitar.addActionListener(e -> {
            if (!btnGuitar.markedCorrect()) {
            	btnGuitar.markCorrect();
            	btnPiano.resetStyle();
            }
        });
        
        chkboxSequencially.addActionListener(e -> {
            if (chkboxSequencially.isSelected()) {
                
            } else {
                
            }
        });

    }
    
    /**
     * Swing automatically triggers this whenever the panel is validated
     * or gains concrete dimensions on screen.
     */
    @Override
    public void doLayout() {
        super.doLayout();
        
        lblScore.setRelativeProperties(50, 79, 80, 10);
        
        int chkWidth = 200;
        int chkHeight = 30;
        int chkX = (getWidth() - chkWidth) / 2 + 8;
        int chkY = (int) (getHeight() * 0.21);
        
        chkboxSequencially.setBounds(chkX, chkY, chkWidth, chkHeight);
        
        btnPlay.setRelativeProperties(50, 12, 32, 6);
        
        btnHome.setRelativeProperties(20, 92, 25, 5);
        btnNext.setRelativeProperties(80, 92, 25, 5);
        
        int btnInstrumentWidth = getWidth() / 4;
        int btnInstrumentHeight = getHeight() / 18;
        int btnInstrumentY = getHeight() / 3;
        btnPiano.setAbsoluteProperties(
        		getWidth() / 2 - btnInstrumentWidth / 2,
        		btnInstrumentY,
        		btnInstrumentWidth,
        		btnInstrumentHeight);
        btnGuitar.setAbsoluteProperties(
        		getWidth() / 2 + btnInstrumentWidth / 2,
        		btnInstrumentY,
        		btnInstrumentWidth,
        		btnInstrumentHeight);
        
        int firstRowN = 4;
        int secondRowN = 4;
        int thirdRowN = 3;

        int buttonHeight = getHeight() / 18;
        int startY = getHeight() / 2 + getHeight() / 20;

        double row1ButtonWidth = (double) getWidth() / firstRowN;
        for (int i = 0; i < firstRowN; i++) {
            intervalButtons.get(i).setAbsoluteProperties(
                row1ButtonWidth * i + row1ButtonWidth / 2, 
                startY, 
                row1ButtonWidth, 
                buttonHeight
            );
        }

        int row2Start = firstRowN;
        int row2End = firstRowN + secondRowN;
        double row2ButtonWidth = (double) getWidth() / secondRowN;

        for (int i = row2Start; i < row2End; i++) {
            intervalButtons.get(i).setAbsoluteProperties(
                row2ButtonWidth * (i - row2Start) + row2ButtonWidth / 2,
                startY + buttonHeight, 
                row2ButtonWidth, 
                buttonHeight
            );
        }

        int row3Start = firstRowN + secondRowN;
        int row3End = intervalButtons.size();
        double row3ButtonWidth = (double) getWidth() / thirdRowN;

        for (int i = row3Start; i < row3End; i++) {
            intervalButtons.get(i).setAbsoluteProperties(
                row3ButtonWidth * (i - row3Start) + row3ButtonWidth / 2,
                startY + (buttonHeight * 2), 
                row3ButtonWidth, 
                buttonHeight
            );
        }
        
    }
    
    private void handleNext() {
    	
    }
}