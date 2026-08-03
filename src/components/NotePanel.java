package components;

import utils.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.swing.*;

public class NotePanel extends JPanel {
	MainFrame mainFrame = null;
	
    private StyledButton btnPlay;
    private StyledButton btnHome;
    private StyledButton btnNext;
    private StyledButton btnPiano, btnGuitar;
    
    private JCheckBox chkboxSequencially;
    private JCheckBox chkboxSameOctave;
    
    private StyledLabel lblScore;
    
    private List<StyledButton> choiceButtons = new ArrayList<StyledButton>();
    
    private SoundHandler.AudioSample note = null;
    
    private int noteIndex = -1;
    
    private int numOfTries = 0;
    private int numOfCorrect = 0;
    
    // User can spam guesses until correct but they will not count
    private boolean validChoice = true;
    
    private Timer sequentialAudioTimer = null; // Track scheduled second note

    public NotePanel(MainFrame mainFrame) {
    	this.mainFrame = mainFrame;
    	
    	SoundHandler.preLoadSounds("Assets/Piano");
    	
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
        chkboxSequencially.setVisible(false);
        
        chkboxSameOctave = new JCheckBox("Same octave");
        chkboxSameOctave.setFont(new Font("SansSerif", Font.PLAIN, 16));
        chkboxSameOctave.setFocusPainted(false);
        chkboxSameOctave.setForeground(Color.WHITE);
        chkboxSameOctave.setBackground(getBackground());
        chkboxSameOctave.setVisible(false);
        
        choiceButtons.add(new StyledButton("C"));
        choiceButtons.add(new StyledButton("C#"));
        choiceButtons.add(new StyledButton("D"));
        choiceButtons.add(new StyledButton("D#"));
        choiceButtons.add(new StyledButton("E"));
        choiceButtons.add(new StyledButton("F"));
        choiceButtons.add(new StyledButton("F#"));
        choiceButtons.add(new StyledButton("G"));
        choiceButtons.add(new StyledButton("G#"));
        choiceButtons.add(new StyledButton("A"));
        choiceButtons.add(new StyledButton("A#"));
        choiceButtons.add(new StyledButton("B"));
        
        for (StyledButton intervalButton : choiceButtons) {
        	intervalButton.setFontSize(13);
        	intervalButton.setPrimaryColor(getBackground());
        	add(intervalButton);
        }
        
        add(btnPlay);
        add(btnHome);
        add(btnNext);
        add(btnPiano);
        add(btnGuitar);
        //add(chkboxSequencially);
        //add(chkboxSameOctave);
        add(lblScore);
        
        setupListeners();

    }
    
    private void setupListeners() {
    	for (int i = 0; i < choiceButtons.size(); i++) {
            StyledButton btn = choiceButtons.get(i);
            btn.setIntervalIndex(i);

            btn.addActionListener(e -> {
                if (note == null) return;
                
                
                int selectedIndex = btn.getIntervalIndex();
                if (noteIndex == selectedIndex) {
                	
                	// mark green and proceed
                	if (validChoice)
                		numOfCorrect++;
                	
                    btn.markCorrect();
                    
                    Timer timer = new Timer(300, event -> {
                        btn.resetStyle();
                        handleNext();
                    });
                    
                    timer.setRepeats(false);
                    timer.start();
                }
                else {
                	// blink red
                	validChoice = false;
                	
                    btn.markWrong();

                    Timer timer = new Timer(300, event -> {
                        btn.resetStyle();
                    });
                    
                    timer.setRepeats(false);
                    timer.start();
                }
            });
        }

        btnPlay.addActionListener(e -> {
        	if (note == null) {
        		note = SoundHandler.getRandomSound();
            	
            	noteIndex = SoundHandler.getNoteIndex(note);
            	
        	}
        	
        	playNote(note);
        });
        
        btnHome.addActionListener(e -> {
        	resetPlaybackState();
        	mainFrame.showScreen(new HomePanel(mainFrame));
        });
        
        btnNext.addActionListener(e -> {
        	handleNext();
        });
        
        btnPiano.addActionListener(e -> {
            if (!btnPiano.markedCorrect()) {
                resetPlaybackState();
                SoundHandler.preLoadSounds("Assets/Piano");
            	btnPiano.markCorrect();
            	btnGuitar.resetStyle();
            }
        });
        
        btnGuitar.addActionListener(e -> {
            if (!btnGuitar.markedCorrect()) {
            	resetPlaybackState();
                SoundHandler.preLoadSounds("Assets/Guitar");
            	btnGuitar.markCorrect();
            	btnPiano.resetStyle();
            }
        });
		
	}

    private void playNote(SoundHandler.AudioSample note) {
        SoundHandler.playSound(note);
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
        
        chkboxSequencially.setBounds(chkX, chkY - 20, chkWidth, chkHeight);
        chkboxSameOctave.setBounds(chkX, chkY + 20, chkWidth, chkHeight);
        
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
        
        int[] rowCounts = {4, 4, 4}; 

        int buttonHeight = getHeight() / 18;
        int startY = getHeight() / 2 + 15;

        int currentIndex = 0;

        for (int rowIndex = 0; rowIndex < rowCounts.length; rowIndex++) {
            int countInRow = rowCounts[rowIndex];
            if (countInRow <= 0) continue;

            double buttonWidth = (double) getWidth() / countInRow;
            int currentY = startY + (buttonHeight * rowIndex);

            for (int col = 0; col < countInRow; col++) {
                // Guard against IndexOutOfBounds if rowCounts sum exceeds list size
                if (currentIndex >= choiceButtons.size()) break;

                double centerX = (buttonWidth * col) + (buttonWidth / 2.0);

                choiceButtons.get(currentIndex).setAbsoluteProperties(
                    centerX,
                    currentY,
                    buttonWidth,
                    buttonHeight
                );

                currentIndex++;
            }
        }
        
    }
    
    private void resetPlaybackState() {
        
        SoundHandler.stopPlayback();
        note = null;
        for (StyledButton btn : choiceButtons) {
            btn.resetStyle();
        }
    }
    
    private void handleNext() {
    	if (note == null)
    		return;
    	
    	numOfTries++;
    	
    	resetPlaybackState();
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("Your score: ").append(numOfCorrect).append("/").append(numOfTries);
    	lblScore.setText(sb.toString());
    	
    	validChoice = true;
    }
}