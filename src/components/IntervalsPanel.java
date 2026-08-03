package components;

import utils.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.swing.*;

public class IntervalsPanel extends JPanel {
	MainFrame mainFrame = null;
	
    private StyledButton btnPlay;
    private StyledButton btnHome;
    private StyledButton btnNext;
    private StyledButton btnPiano, btnGuitar;
    
    private JCheckBox chkboxSequencially;
    private JCheckBox chkboxSameOctave;
    
    private StyledLabel lblScore;
    
    private List<StyledButton> choiceButtons = new ArrayList<StyledButton>();
    
    private SoundHandler.AudioSample note1 = null;
    private SoundHandler.AudioSample note2 = null;
    
    private int currentNoteDistance = -1;
    
    private int numOfTries = 0;
    private int numOfCorrect = 0;
    
    // User can spam guesses until correct but they will not count
    private boolean validChoice = true;
    
    private boolean isTransitioning = false;
    
    private Timer sequentialAudioTimer = null; // Track scheduled second note

    public IntervalsPanel(MainFrame mainFrame) {
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
        
        chkboxSameOctave = new JCheckBox("Same octave");
        chkboxSameOctave.setFont(new Font("SansSerif", Font.PLAIN, 16));
        chkboxSameOctave.setFocusPainted(false);
        chkboxSameOctave.setForeground(Color.WHITE);
        chkboxSameOctave.setBackground(getBackground());
        
        choiceButtons.add(new StyledButton("Unison"));
        choiceButtons.add(new StyledButton("Minor 2nd"));
        choiceButtons.add(new StyledButton("Major 2nd"));
        choiceButtons.add(new StyledButton("Minor 3rd"));
        choiceButtons.add(new StyledButton("Major 3rd"));
        choiceButtons.add(new StyledButton("4th"));
        choiceButtons.add(new StyledButton("Tritone"));
        choiceButtons.add(new StyledButton("5th"));
        choiceButtons.add(new StyledButton("Minor 6th"));
        choiceButtons.add(new StyledButton("Major 6th"));
        choiceButtons.add(new StyledButton("Minor 7th"));
        choiceButtons.add(new StyledButton("Major 7th"));
        choiceButtons.add(new StyledButton("Octave"));
        
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
        add(chkboxSequencially);
        add(chkboxSameOctave);
        add(lblScore);
        
        getNotes();
        
        setupListeners();

    }
    
    private void setupListeners() {
    	for (int i = 0; i < choiceButtons.size(); i++) {
            StyledButton btn = choiceButtons.get(i);
            btn.setIntervalIndex(i);

            btn.addActionListener(e -> {
                if (isTransitioning || note1 == null || note2 == null) return;
                
                
                int selectedIndex = btn.getIntervalIndex();
                if (currentNoteDistance == selectedIndex
                		|| (12 - currentNoteDistance == selectedIndex
                		&& !chkboxSameOctave.isSelected())) {
                	
                	isTransitioning = true; // Lock UI during green highlight delay
                	
                	// mark green and proceed
                	if (validChoice) {
                		numOfCorrect++;
                		SoundHandler.playSound(SoundHandler.getSoundCorrect());  
                	}
                	
                    btn.markCorrect();
                    
                    Timer timer = new Timer(300, event -> {
                        btn.resetStyle();
                        handleNext();
                        isTransitioning = false; // Unlock UI for next note
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
        	
        	playInterval(note1, note2);
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
            	if (sequentialAudioTimer != null && sequentialAudioTimer.isRunning()) {
                    sequentialAudioTimer.stop();
                }
                resetPlaybackState();
                SoundHandler.preLoadSounds("Assets/Piano");
                getNotes();
            	btnPiano.markCorrect();
            	btnGuitar.resetStyle();
            }
        });
        
        btnGuitar.addActionListener(e -> {
            if (!btnGuitar.markedCorrect()) {
            	if (sequentialAudioTimer != null && sequentialAudioTimer.isRunning()) {
                    sequentialAudioTimer.stop();
                }
            	resetPlaybackState();
                SoundHandler.preLoadSounds("Assets/Guitar");
                getNotes();
            	btnGuitar.markCorrect();
            	btnPiano.resetStyle();
            }
        });
        
        chkboxSequencially.addActionListener(e -> {
        	if (sequentialAudioTimer != null && sequentialAudioTimer.isRunning()) {
                sequentialAudioTimer.stop();
            }
            SoundHandler.stopPlayback();
        });
        
        chkboxSameOctave.addActionListener(e -> {
        	resetPlaybackState();
        	getNotes();
        });
		
	}

    private void playInterval(SoundHandler.AudioSample note1, SoundHandler.AudioSample note2) {
        if (sequentialAudioTimer != null && sequentialAudioTimer.isRunning()) {
            sequentialAudioTimer.stop();
        }

        if (!chkboxSequencially.isSelected()) {
            SoundHandler.playSound(note1);
            SoundHandler.playSound(note2);
        } else {
            SoundHandler.playSound(note1);
            
            sequentialAudioTimer = new Timer(500, e -> {
                SoundHandler.playSound(note2);
            });
            
            sequentialAudioTimer.setRepeats(false);
            sequentialAudioTimer.start();
        }
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
        
        int[] rowCounts = {3, 3, 4, 3}; 

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
        // Stop any pending delayed note from triggering
        if (sequentialAudioTimer != null && sequentialAudioTimer.isRunning()) {
            sequentialAudioTimer.stop();
        }
        
        SoundHandler.stopPlayback();
        note1 = null;
        note2 = null;
        for (StyledButton btn : choiceButtons) {
            btn.resetStyle();
        }
    }
    
    private void getNotes() {
    	if (note1 == null && note2 == null) {
    		note1 = SoundHandler.getRandomSound();
    		if (!chkboxSameOctave.isSelected()) {
    			note2 = SoundHandler.getRandomSound();
    		}
    		else {
    			note2 = SoundHandler.getRandomSound(note1, true);
    		}
        	
        	currentNoteDistance = SoundHandler.getNotesDistance(note1, note2);
        	
    	}
    }
    
    private void handleNext() {
    	
    	numOfTries++;
    	
    	resetPlaybackState();
    	
    	getNotes();
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("Your score: ").append(numOfCorrect).append("/").append(numOfTries);
    	lblScore.setText(sb.toString());
    	
    	validChoice = true;
    }
}