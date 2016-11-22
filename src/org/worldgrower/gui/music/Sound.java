package org.worldgrower.gui.music;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.worldgrower.gui.start.Game;

public class Sound {

	private static final boolean CLOSE_AFTER_PLAYING = true;
	private static final boolean DONT_CLOSE_AFTER_PLAYING = false;
	
	private final byte[] audioFilePath;
	private final Clip preLoadedClip;
	
	public Sound(String path) {
		try {
			audioFilePath = readFully(new BufferedInputStream(new GZIPInputStream(Game.class.getResourceAsStream(path))));
			if (isNumberOfLinesUnlimited(audioFilePath)) {
				preLoadedClip = openClip(DONT_CLOSE_AFTER_PLAYING);
			} else {
				preLoadedClip = null;
			}
			
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static byte[] readFully(InputStream input) throws IOException
	{
	    byte[] buffer = new byte[8192];
	    int bytesRead;
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    while ((bytesRead = input.read(buffer)) != -1)
	    {
	        output.write(buffer, 0, bytesRead);
	    }
	    return output.toByteArray();
	}
	
	private static boolean isNumberOfLinesUnlimited(byte[] audioFilePath) {
		AudioInputStream audioStream;
		try {
			audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioFilePath));
			DataLine.Info info = getLineInfo(audioStream);
			return AudioSystem.getMixer(null).getMaxLines(info) == AudioSystem.NOT_SPECIFIED;
		} catch (UnsupportedAudioFileException | IOException e) {
			throw new IllegalStateException(e);
		}
		
	}
	
	public void play() {
		try {
			final Clip clipToPlay;
			if (preLoadedClip != null) {
				clipToPlay = preLoadedClip;
				clipToPlay.setFramePosition(0);
			} else {
				clipToPlay = openClip(CLOSE_AFTER_PLAYING);
			}
			clipToPlay.start();

		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			throw new IllegalStateException(e);
		}
	}

	private Clip openClip(boolean closeAfterPlaying) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioFilePath));
		DataLine.Info info = getLineInfo(audioStream);
		Clip audioClip = (Clip) AudioSystem.getLine(info);

		if (closeAfterPlaying) {
			audioClip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent myLineEvent) {
					if (myLineEvent.getType() == LineEvent.Type.STOP)
						audioClip.close();
				}
			});
		}

		audioClip.open(audioStream);
		return audioClip;
	}

	private static DataLine.Info getLineInfo(AudioInputStream audioStream) {
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format, 100000);
		return info;
	}
}
