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

import org.worldgrower.gui.start.Game;

public class MusicLoader {

	private static final String[] MUSIC_FILES = { "/sound/Forest_Ambience8bit.wav.gz" };
	
	private final InputStream[] musicInputStreams;
	private int currentMusic;
	
	public MusicLoader() throws IOException {
		this.musicInputStreams = new InputStream[MUSIC_FILES.length];
		for(int i=0; i<MUSIC_FILES.length; i++) {
			this.musicInputStreams[i] = new BufferedInputStream(new GZIPInputStream(Game.class.getResourceAsStream(MUSIC_FILES[i])));
		}
		
		this.currentMusic = 0;
	}
	
	public InputStream getNextFile() {
		InputStream nextFile = musicInputStreams[currentMusic];
		currentMusic = (currentMusic + 1) % MUSIC_FILES.length;		
		return nextFile;
	}
}
