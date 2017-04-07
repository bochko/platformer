package game.subsidiaries.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FilterInputStream;
import java.io.InputStream;

public class LoopingSound extends Thread {

	String filename;	// The name of the file to play
	boolean finished;	// A flag showing that the thread has finished

	public LoopingSound(String fname) {
		filename = fname;
		finished = false;
	}

	/**
	 * run will play the actual sound but you should not call it directly.
	 * You need to call the 'start' method of your sound object (inherited
	 * from Thread, you do not need to declare your own). 'run' will
	 * eventually be called by 'start' when it has been scheduled by
	 * the process scheduler.
	 */
	public void run() {
		try {
			File file = new File(filename);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat	format = stream.getFormat();
			ReverseReverbSoundFilter filter = new ReverseReverbSoundFilter(stream);
			AudioInputStream fstream = new AudioInputStream(filter,format,stream.getFrameLength());
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(fstream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			Thread.sleep(100);
			while (clip.isRunning()) {
				Thread.sleep(100);
			}
			clip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finished = true;
	}
}
