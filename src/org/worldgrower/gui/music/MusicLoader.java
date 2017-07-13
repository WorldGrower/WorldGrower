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

	private static final String[] MUSIC_FILES = {
		"/sound/Ove - Earth Is All We Have.wav.gz",
		"/sound/old city theme.wav.gz",
		"/sound/Ove Melaa - I have just been eaten.wav.gz",
		"/sound/regular battle.wav.gz",
		"/sound/Ove Melaa - They Came By Boat.wav.gz",
		"/sound/Lonely Witch.wav.gz",
		"/sound/Ove Melaa - Dark Blue.wav.gz",
		"/sound/overworld theme.wav.gz"
	};
	
	private int currentMusic;
	
	public MusicLoader() throws IOException {
		this.currentMusic = 0;
	}

	private BufferedInputStream getInputStream(String path) throws IOException {
		return new BufferedInputStream(new GZIPInputStream(Game.class.getResourceAsStream(path)));
	}
	
	public InputStream getNextFile() {
		try {
			InputStream nextFile = getInputStream(MUSIC_FILES[currentMusic]);
			currentMusic = (currentMusic + 1) % MUSIC_FILES.length;		
			return nextFile;
		} catch(IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
