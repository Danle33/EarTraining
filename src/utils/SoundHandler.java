package utils;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

public class SoundHandler {

    // Simple container for cached note data in RAM
    public record AudioSample(byte[] pcmData, AudioFormat format) {}
    
    private static AudioSample silence = null;
    private static final List<AudioSample> samplePool = new ArrayList<>();
    private static final Random random = new Random();

    /**
     * Optional explicit initialization / pre-warm of the OS Audio System.
     */
    public static void init() {
        new Thread(() -> {
            try {
                // Pre-warms Java's AudioSystem driver engine to eliminate first-play lag
                AudioSystem.getMixerInfo();
            } catch (Exception ignored) {}
        }).start();
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
                if (file.getName() == "silence.wav") {
                	silence = new AudioSample(data, format);
                }
                else {
                	samplePool.add(new AudioSample(data, format));
                }
                
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
    
    public static AudioSample getSilence() {
    	return silence;
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

                // Auto-close clip line resources when playback finishes
                clip.addLineListener(event -> {
                    if (LineEvent.Type.STOP.equals(event.getType())) {
                        clip.close();
                    }
                });

                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}