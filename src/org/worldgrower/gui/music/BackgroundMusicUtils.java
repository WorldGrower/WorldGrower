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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.worldgrower.gui.start.Game;

public class BackgroundMusicUtils {

	public static MusicPlayer startBackgroundMusic() {
		MusicPlayer musicPlayer = new MusicPlayer();
		new Thread() {
        	@Override
        	public void run() {
        		try {
					musicPlayer.play(new BufferedInputStream(new GZIPInputStream(Game.class.getResourceAsStream("/sound/Forest_Ambience8bit.wav.gz"))));
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
        	}
        }.start();
        return musicPlayer;
	}
	
	public static Clip readMusicFile(InputStream audioFilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFilePath);
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		
		Clip audioClip = (Clip) AudioSystem.getLine(info);
		audioClip.open(audioStream);
		return audioClip;
	}
}
