package mlk.theodore.avanthica.projetisn.proto.middle;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

// n'est plus utilisée car remplacée par la biblithèque TinySound
@Deprecated
public class PlaySound {
	private static final int BUFFER_SIZE = 4000;
	private static Thread playingThread;
	private static boolean running;
	/**
	 * @param filename
	 *            the name of the file that is going to be played
	 */
	private static void playSound(String filename) {
		try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
				PlaySound.class.getResourceAsStream(filename)))) {
			AudioFormat audioFormat = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			try (SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info)) {
				sourceLine.open(audioFormat);
				sourceLine.start();

				int nBytesRead = 0;
				byte[] abData = new byte[BUFFER_SIZE];
				running = true;
				while (nBytesRead != -1 && running) {
					try {
						nBytesRead = audioStream.read(abData, 0, abData.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (nBytesRead >= 0) {
						sourceLine.write(abData, 0, nBytesRead);
					}
				}

				sourceLine.drain();
			}
		} catch (Exception e) {
			e.printStackTrace();
			running = false;
		}
	}
	
	public static void startPlaying(String fileName) {
		if (playingThread == null) {
			playingThread = new Thread(new Runnable() {
				@Override
				public void run() {
					playSound(fileName);
				}
			});
		}
		playingThread.start();
	}
	
	public static void stopPlaying() {
		if (playingThread != null) {
			running = false;
			playingThread.interrupt();
			playingThread = null;
		}
	}
}