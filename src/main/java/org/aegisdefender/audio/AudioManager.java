package org.aegisdefender.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioManager {

    private Clip backroungSound;
    private Clip enemiHit;
    private FloatControl gainControl;

    public AudioManager() {
        this.backroungSound = loadClip("/Sounds/backgroundSound.wav");
        //this.enemiHit = loadClip("/Sounds/");
    }

    public Clip loadClip(String filePath){
        URL resource = getClass().getResource(filePath);
       // System.out.println(resource);

        try(AudioInputStream audio = AudioSystem.getAudioInputStream(resource)){
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        }
        catch(IOException | UnsupportedAudioFileException | LineUnavailableException e){
            System.err.println("Something went wrong when loading audio" + e.getMessage());
            return null;
        }
    }

    public void playSound() {
        try {
                // get control --> volume
                if (this.backroungSound.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    gainControl = (FloatControl) this.backroungSound.getControl(FloatControl.Type.MASTER_GAIN);
                }
            this.backroungSound.loop(Clip.LOOP_CONTINUOUSLY);
            //setVolume(50); // set the current volume

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setVolume(int volumePercent) {
        if (gainControl == null) return;

        volumePercent = Math.clamp(volumePercent, 0, 100);

        float min = gainControl.getMinimum();

        if (volumePercent == 0) {
            gainControl.setValue(min);
        } else {
            float dB = (float) (20 * Math.log10(volumePercent / 100.0));
            gainControl.setValue(Math.max(min, dB));
        }
    }


    private void stopSound() {
        if (this.backroungSound != null) {
            this.backroungSound.stop();
            this.backroungSound.setMicrosecondPosition(0); // Revenir au début
        }
    }
}
