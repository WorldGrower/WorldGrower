/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.gui.music;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer implements LineListener {
	private boolean enabled;
	private Clip audioClip;
	private SoundOutput soundOutput;
	private final MusicLoader musicLoader;
	private MusicThread musicThread = null;
	
	public MusicPlayer(SoundOutput soundOutput, boolean enabled) throws IOException {
		this.soundOutput = soundOutput;
		this.enabled = enabled;
		this.musicLoader = new MusicLoader();
	}
	
	private void play(InputStream audioFilePath) {
		try {
			audioClip = BackgroundMusicUtils.readMusicFile(audioFilePath, soundOutput);
			audioClip.addLineListener(this);
			audioClip.start();
			
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	public void play() {
		if (enabled) {
			if (musicThread == null) {
				musicThread = new MusicThread();
				musicThread.start();
			}
		}
	}
	
	private class MusicThread extends Thread {
		@Override
    	public void run() {
    		play(musicLoader.getNextFile());
    	}
	}
	
	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) {
			if (audioClip != null) {
				audioClip.close();
				audioClip.removeLineListener(this);
				audioClip = null;
			}
			if (enabled) {
				sleep(500);
				play(musicLoader.getNextFile());
			}
		}
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public void stop() {
		audioClip.stop();
		audioClip.close();
		audioClip.removeLineListener(this);
		audioClip = null;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if (enabled) {
			play();
		} else if (audioClip != null){
			stop();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
}