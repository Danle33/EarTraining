package utils;

import javax.sound.sampled.*;
import java.io.File;
import java.util.*;

public class SoundHandler {

    // Simple container for cached note data in RAM
    public record AudioSample(byte[] pcmData, AudioFormat format, String fileName) {}

    private static final List<AudioSample> samplePool = new ArrayList<>();
    
    private static final Map<String, Integer> notesMap = new HashMap<String, Integer>();
    
    static {
    	notesMap.put("C", 1);
    	notesMap.put("C#", 2);
    	notesMap.put("D", 3);
    	notesMap.put("D#", 4);
    	notesMap.put("E", 5);
    	notesMap.put("F", 6);
    	notesMap.put("F#", 7);
    	notesMap.put("G", 8);
    	notesMap.put("G#", 9);
    	notesMap.put("A", 10);
    	notesMap.put("A#", 11);
    	notesMap.put("B", 12);
    }
    
    // Thread-safe set to track all actively playing Clips
    private static final Set<Clip> activeClips = Collections.synchronizedSet(new HashSet<>());
    
    private static final Random random = new Random();

    public static void init() {
        startAudioKeepAlive();
    }

    public static void preLoadSounds(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));

        if (files == null || files.length == 0) {
            System.err.println("No .wav files found in directory: " + folderPath);
            return;
        }

        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(file)) {
                AudioFormat format = ais.getFormat();
                byte[] data = ais.readAllBytes();
                samplePool.add(new AudioSample(data, format, file.getName()));
            } catch (Exception e) {
                System.err.println("Failed to load sample: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public static AudioSample getRandomSound() {
        if (samplePool.isEmpty()) {
            throw new IllegalStateException("No sounds preloaded! Call preLoadSounds() first.");
        }
        return samplePool.get(random.nextInt(samplePool.size()));
    }
    
    public static AudioSample getRandomSound(AudioSample referenceNote, boolean sameOctave) {
        if (samplePool.isEmpty()) {
            throw new IllegalStateException("No sounds preloaded! Call preLoadSounds() first.");
        }
        
        List<AudioSample> notesFromSameOctave = new ArrayList<>();
        int referenceOctave = extractNoteOctave(referenceNote.fileName);
        
        for (AudioSample sample : samplePool) {
        	if (extractNoteOctave(sample.fileName) == referenceOctave) {
        		notesFromSameOctave.add(sample);
        	}
        }
        
        if (notesFromSameOctave.isEmpty()) {
            return referenceNote; // Fallback to unison if octave lookup fails
        }
        
        return notesFromSameOctave.get(random.nextInt(notesFromSameOctave.size()));
        
    }

    // Keeps audio drivers warmed up, avoiding playback delay
    private static void startAudioKeepAlive() {
        Thread keepAliveThread = new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
                byte[] silence = new byte[(int) (format.getSampleRate() * format.getFrameSize() * 0.1)];

                Clip keepAlive = AudioSystem.getClip();
                keepAlive.open(format, silence, 0, silence.length);
                keepAlive.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception ignored) {}
        });

        keepAliveThread.setDaemon(true);
        keepAliveThread.start();
    }

    public static void playSound(AudioSample sample) {
        if (sample == null) return;

        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream ais = new AudioInputStream(
                        new java.io.ByteArrayInputStream(sample.pcmData()),
                        sample.format(),
                        sample.pcmData().length / sample.format().getFrameSize()
                );

                clip.open(ais);
                
                activeClips.add(clip);

                // Let the line close itself asynchronously when playback finishes
                clip.addLineListener(event -> {
                    if (LineEvent.Type.STOP.equals(event.getType())) {
                        activeClips.remove(clip);
                        clip.close(); 
                    }
                });

                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Halts active audio output instantly (0ms delay) without blocking hardware lines.
     */
    public static void stopPlayback() {
        synchronized (activeClips) {
            for (Clip clip : activeClips) {
                try {
                    if (clip.isRunning()) {
                        clip.stop(); // Halts sound output instantly without closing line synchronously
                    }
                } catch (Exception ignored) {}
            }
            // LineListener will handle activeClips.remove() and clip.close() automatically
        }
    }

	public static int getNotesDistance(AudioSample note1, AudioSample note2) {
		
		String note1name = extractNoteName(note1.fileName);
		String note2name = extractNoteName(note2.fileName);
		
		return Math.abs(notesMap.get(note2name) - notesMap.get(note1name));
		
	}
	
	private static String extractNoteName(String note) {
		// filename pattern: id_octave_noteName.wav
	    int underscoreIdx = note.lastIndexOf('_');
	    int dotIdx = note.lastIndexOf('.');

	    if (underscoreIdx != -1 && dotIdx != -1 && underscoreIdx < dotIdx) {
	        return note.substring(underscoreIdx + 1, dotIdx);
	    }
	    
	    return null;
	}
	
	private static int extractNoteOctave(String note) {
		// filename pattern: id_octave_noteName.wav
	    int underscoreIdx2 = note.lastIndexOf('_');
	    int underscoreIdx1 = note.indexOf('_');

	    if (underscoreIdx2 != -1 && underscoreIdx1 != -1 && underscoreIdx1 < underscoreIdx2) {
	        return Integer.parseInt(note.substring(underscoreIdx1 + 1, underscoreIdx2));
	    }
	    
	    return -1;
	}
}