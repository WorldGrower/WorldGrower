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

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class SoundOutput {

	private final Mixer mixer;

	private SoundOutput(Mixer mixer) {
		this.mixer = mixer;
	}
	
	public static List<SoundOutput> getAllSoundOutputs() {
		List<SoundOutput> soundOutputs = new ArrayList<>();
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		for(Mixer.Info info : mixerInfos) {
			soundOutputs.add(new SoundOutput(AudioSystem.getMixer(info)));
		}
		
		return soundOutputs;
	}
	
	public static SoundOutput getDefaultSoundOutput() {
		return new SoundOutput(AudioSystem.getMixer(null));
	}
	
	public static SoundOutput create(String description) {
		List<SoundOutput> soundOutputs = getAllSoundOutputs();
		
		for(SoundOutput soundOutput : soundOutputs) {
			if (description.equals(soundOutput.getDescription())) {
				return soundOutput;
			}
		}
		return null;
	}
	
	public String getDescription() {
		return mixer.getMixerInfo().getDescription();
	}

	@Override
	public String toString() {
		return getDescription();
	}

	Mixer getMixer() {
		return mixer;
	}

	@Override
	public int hashCode() {
		return mixer.getMixerInfo().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SoundOutput) {
			SoundOutput other = (SoundOutput) obj;
			return mixer.getMixerInfo().equals(other.mixer.getMixerInfo());
		} else {
			return false;
		}
	}
	
	
}
