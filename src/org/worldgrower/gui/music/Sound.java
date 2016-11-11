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

	private final byte[] audioFilePath;
	
	public Sound(String path) {
		try {
			audioFilePath = readFully(new BufferedInputStream(new GZIPInputStream(Game.class.getResourceAsStream(path))));
		} catch (IOException e) {
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
	
	public void play() {
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioFilePath));
			 
	        AudioFormat format = audioStream.getFormat();
	
	        DataLine.Info info = new DataLine.Info(Clip.class, format);
	
	        Clip audioClip = (Clip) AudioSystem.getLine(info);
	
	        audioClip.addLineListener(new LineListener() {
	            public void update(LineEvent myLineEvent) {
	              if (myLineEvent.getType() == LineEvent.Type.STOP)
	            	  audioClip.close();
	            }
	          });
        
	        audioClip.open(audioStream);
	         
	        audioClip.start();
	        audioClip.setFramePosition(0);
	        audioClip.start();

		} catch(IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			throw new IllegalStateException(e);
		}
	}
}
