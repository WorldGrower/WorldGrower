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

import java.util.HashMap;
import java.util.Map;

public class SoundIdReader {

	private SoundOutput soundOutput;
	private boolean enabled;
	private final Map<SoundIds, Sound> sounds = new HashMap<>();

	private void initialize() throws SoundException {
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
		readSound(SoundIds.COW, "/sound/Mudchute_cow_18bit.wav.gz");
		readSound(SoundIds.HEALING, "/sound/Healing Full8bit.wav.gz");
		readSound(SoundIds.WIND, "/sound/Wind effects 58bit.wav.gz");
		readSound(SoundIds.CLOTH, "/sound/cloth8bit.wav.gz");
		readSound(SoundIds.CLATTER, "/sound/workshop - clatter8bit.wav.gz");
		readSound(SoundIds.BLESSING2, "/sound/blessing28bit.wav.gz");
		readSound(SoundIds.CURSE_SPELL, "/sound/curse8bit.wav.gz");
		readSound(SoundIds.FORCE_PUSH, "/sound/forcepush8bit.wav.gz");
		readSound(SoundIds.FORCE_PULSE, "/sound/forcepulse8bit.wav.gz");
		readSound(SoundIds.CURSE5, "/sound/curse58bit.wav.gz");
		readSound(SoundIds.CURSE3, "/sound/curse38bit.wav.gz");
		readSound(SoundIds.ZAP2, "/sound/zap28bit.wav.gz");
		readSound(SoundIds.ENCHANT, "/sound/enchant8bit.wav.gz");
		readSound(SoundIds.ENCHANT2, "/sound/enchant28bit.wav.gz");
		readSound(SoundIds.DISENCHANT, "/sound/disenchant8bit.wav.gz");
		readSound(SoundIds.RUSTLE01, "/sound/rustle018bit.wav.gz");
		readSound(SoundIds.CONFUSION, "/sound/confusion8bit.wav.gz");
		readSound(SoundIds.ZAP2G, "/sound/zap2g8bit.wav.gz");
		readSound(SoundIds.RANDOM1, "/sound/random18bit.wav.gz");
		readSound(SoundIds.HANDLE_SMALL_LEATHER, "/sound/handleSmallLeather8bit.wav.gz");
		readSound(SoundIds.MOVE, "/sound/full steps stereo8bit.wav.gz");
		readSound(SoundIds.RUSTLE3, "/sound/rustle038bit.wav.gz");
		readSound(SoundIds.KISS, "/sound/179303__gflower__perfect-kiss8bit.wav.gz");
		readSound(SoundIds.DYING, "/sound/Human_DyingBreath_018bit.wav.gz");
		readSound(SoundIds.SHOVEL, "/sound/qubodupshovelSpell18bit.wav.gz");
		readSound(SoundIds.POISON, "/sound/219566__qubodup__poison-spell-magic8bit.wav.gz");
		readSound(SoundIds.SEX, "/sound/182016__safadancer__sex-groaning-18bit.wav.gz");
		readSound(SoundIds.RELIGIOUS, "/sound/135489__felix-blume__bells-and-religious-hymn8bit.wav.gz");
		readSound(SoundIds.SWISH, "/sound/swish-98bit.wav.gz");
		readSound(SoundIds.METAL_SMALL1, "/sound/metal-small18bit.wav.gz");
		readSound(SoundIds.DOOR_OPEN, "/sound/doorOpen_18bit.wav.gz");
		readSound(SoundIds.BLESSING, "/sound/blessing8bit.wav.gz");
		readSound(SoundIds.PICKLOCK, "/sound/picklock.wav.gz");
		readSound(SoundIds.DOOR_CLOSE, "/sound/doorClose_4.wav.gz");
		readSound(SoundIds.MAGIC_SHIELD, "/sound/magicshield8bit.wav.gz");
		readSound(SoundIds.DROP_LEATHER, "/sound/dropLeather.wav.gz");
	}

	public SoundIdReader(SoundOutput soundOutput, boolean enabled) throws SoundException {
		this.soundOutput = soundOutput;
		this.enabled = enabled;
		
		if (enabled) {
			initialize();
		}
	}

	private void readSound(SoundIds soundIds, String path) throws SoundException {
		Sound audioClip = new Sound(path);
		sounds.put(soundIds, audioClip);
	}
	
	public void playSoundEffect(SoundIds soundIds) {
		if (enabled) {
			Sound sound = sounds.get(soundIds);
			sound.play();
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		try {
			initialize();
		} catch (SoundException e) {
			throw new IllegalStateException(e);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
}
