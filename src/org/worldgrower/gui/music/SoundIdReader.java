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
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.worldgrower.gui.start.Game;

public class SoundIdReader {

	private final Map<SoundIds, Clip> sounds = new HashMap<>();
	
	public SoundIdReader() {
		readSound(SoundIds.CUT_WOOD, "/sound/workshop - wood clap8bit.wav.gz");
		readSound(SoundIds.MINE, "/sound/workshop - metal tapping8bit.wav.gz");
		readSound(SoundIds.FLAMES, "/sound/flames8bit.wav.gz");
		readSound(SoundIds.FROST, "/sound/frost8bit.wav.gz");
		readSound(SoundIds.SHOCK, "/sound/shock8bit.wav.gz");
		readSound(SoundIds.TELEPORT, "/sound/teleport8bit.wav.gz");
		readSound(SoundIds.WATER, "/sound/waterspell8bit.wav.gz");
		readSound(SoundIds.EAT, "/sound/eating8bit.wav.gz");
		readSound(SoundIds.SWING, "/sound/swing8bit.wav.gz");
		readSound(SoundIds.BOW, "/sound/bow8bit.wav.gz");
		readSound(SoundIds.BUILD_WOODEN_BUILDING, "/sound/workshop - wood hammering8bit.wav.gz");
		readSound(SoundIds.BUILD_STONE_BUILDING, "/sound/workshop - wood on concrete8bit.wav.gz");
		readSound(SoundIds.SMITH, "/sound/smith18bit.wav.gz");
		readSound(SoundIds.PAPER, "/sound/scroll8bit.wav.gz");
		readSound(SoundIds.DARKNESS, "/sound/darkness8bit.wav.gz");
		readSound(SoundIds.CURSE, "/sound/cursespell8bit.wav.gz");
		readSound(SoundIds.ALCHEMIST, "/sound/alchemist18bit.wav.gz");
		readSound(SoundIds.DRINK, "/sound/fountain8bit.wav.gz");
		readSound(SoundIds.MAGIC1, "/sound/magical_18bit.wav.gz");
		readSound(SoundIds.MAGIC3, "/sound/magical_38bit.wav.gz");
		readSound(SoundIds.MAGIC6, "/sound/magical_68bit.wav.gz");
		readSound(SoundIds.MAGIC7, "/sound/magical_78bit.wav.gz");
		readSound(SoundIds.KNIFE_SLICE, "/sound/knifeSlice8bit.wav.gz");
		readSound(SoundIds.HANDLE_COINS, "/sound/handleCoins8bit.wav.gz");
		readSound(SoundIds.BOOK_FLIP, "/sound/bookFlip18bit.wav.gz");
		readSound(SoundIds.CLICK, "/sound/click18bit.wav.gz");
		readSound(SoundIds.ROLLOVER, "/sound/rollover18bit.wav.gz");

	}

	private void readSound(SoundIds soundIds, String path) {
		Clip audioClip;
		try {
			InputStream audioFilePath = new BufferedInputStream(new GZIPInputStream(Game.class.getResourceAsStream(path)));
			audioClip = BackgroundMusicUtils.readMusicFile(audioFilePath);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			throw new IllegalStateException("Problem reading " + path, e);
		}
		sounds.put(soundIds, audioClip);
	}
	
	public void playSoundEffect(SoundIds soundIds) {
		sounds.get(soundIds).setFramePosition(0);
		sounds.get(soundIds).start();
	}
}
