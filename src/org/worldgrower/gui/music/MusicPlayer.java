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
	private boolean playCompleted;
	private Clip audioClip;
	private SoundOutput soundOutput;
	private final MusicLoader musicLoader;
	
	public MusicPlayer(SoundOutput soundOutput, boolean enabled) throws IOException {
		this.soundOutput = soundOutput;
		this.enabled = enabled;
		this.musicLoader = new MusicLoader();
	}
	
	private void play(InputStream audioFilePath) {

		try {
			audioClip = BackgroundMusicUtils.readMusicFile(audioFilePath, soundOutput);
			audioClip.addLineListener(this);
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
			
			while (!playCompleted) {
				// wait for the playback completes
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					throw new IllegalStateException(ex);
				}
			}
			
			if (audioClip != null) {
				audioClip.close();
			}
			
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	public void play() {
		if (enabled) {
			new Thread() {
		    	@Override
		    	public void run() {
		    		play(musicLoader.getNextFile());
		    	}
		    }.start();
		}
	}
	
	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		
		if (type == LineEvent.Type.START) {
			
		} else if (type == LineEvent.Type.STOP) {
			playCompleted = true;
		}
	}

	public void stop() {
		audioClip.stop();
		audioClip.close();
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