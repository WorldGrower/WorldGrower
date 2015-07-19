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
package org.worldgrower.gui;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CommonerImageIds implements Serializable {

	private final List<ImageIds> maleCommonerIds = Arrays.asList(
			ImageIds.BLUE_HAIRED_COMMONER,
			ImageIds.TULBAN_MALE_COMMONER,
			ImageIds.GREEN_HAIRED_COMMONER,
			ImageIds.MALE_SCIENTIST_COMMONER,
			ImageIds.GREEN_HAIRED_MALE_BOY,
			ImageIds.GREEN_HAIRED_MALE_COMMONER,
			ImageIds.PURPLE_HAIRED_MALE_COMMONER,
			ImageIds.GREY_HAIRED_MALE_COMMONER,
			ImageIds.BLUE_HAT_MALE_COMMONER,
			ImageIds.BOLD_MALE_COMMONER,
			ImageIds.MALE_CHEF_COMMONER,
			ImageIds.SPIKY_HAIR_MALE_COMMONER,
			ImageIds.GREY_HAIR_MALE_COMMONER,
			ImageIds.MALE_BAKER_COMMONER,
			ImageIds.GREEN_HAIR_MALE_COMMONER,
			ImageIds.MALE_SAGE_COMMONER,
			ImageIds.MALE_MERCHANT_COMMONER,
			ImageIds.MALE_CAPTAIN_COMMONER,
			ImageIds.ORANGE_HAIR_BOY_COMMONER,
			ImageIds.TULBAN_BOY_COMMONER,
			ImageIds.SKINHEAD_BOY_COMMONER,
			ImageIds.MALE_LEADER,
			ImageIds.MALE_SENATOR,
			ImageIds.MALE_BELLBOY,
			ImageIds.MALE_LAWYER,
			ImageIds.MALE_SAILOR,
			ImageIds.MALE_WHITE_SHIRT_SAILOR,
			ImageIds.MALE_BLOND_SAILOR,
			ImageIds.MALE_CAPTAIN,
			ImageIds.MALE_BALD_SAILOR,
			ImageIds.MALE_GREEN_BANDANA_SAILOR,
			ImageIds.MALE_BALD_COMMONER,
			ImageIds.MALE_BEARDED_COMMONER,
			ImageIds.YOUNG_BOY_COMMONER,
			ImageIds.MALE_BLUE_PANTS_BOY_COMMONER
			);
	
	private final List<ImageIds> femaleCommonerIds = Arrays.asList(
			ImageIds.BLACK_HAIRED_FEMALE_COMMONER,
			ImageIds.PURPLE_HAIRED_MALE_GIRL,
			ImageIds.PURPLE_HAT_COMMONER,
			ImageIds.PINK_HAT_FEMALE_COMMONER,
			ImageIds.RED_HAIR_FEMALE_COMMONER,
			ImageIds.FEMALE_NUN_COMMONER,
			ImageIds.BLUE_DRESS_FEMALE_COMMONER,
			ImageIds.FEMALE_BRIDE_COMMONER,
			ImageIds.FEMALE_SENATOR,
			ImageIds.FEMALE_RED_EYE,
			ImageIds.FEMALE_BUNNY,
			ImageIds.FEMALE_MAID,
			ImageIds.FEMALE_RED_HAIRED_SAILOR,
			ImageIds.FEMALE_SAILOR,
			ImageIds.FEMALE_OLD_COMMONER,
			ImageIds.FEMALE_BLOND_COMMONER,
			ImageIds.FEMALE_ORANGE_GIRL,
			ImageIds.FEMALE_ORANGE_HAIR_GIRL_COMMONER
			);
	
	private int currentMaleCommonerIndex = 0;
	private int currentFemaleCommonerIndex = 0;
	
	public ImageIds getNextMaleCommonerImageId() {
		ImageIds id = maleCommonerIds.get(currentMaleCommonerIndex);
		currentMaleCommonerIndex = ((currentMaleCommonerIndex+1) % maleCommonerIds.size());
		return id;
	}

	public ImageIds getNextFemaleCommonerImageId() {
		ImageIds id = femaleCommonerIds.get(currentFemaleCommonerIndex);
		currentFemaleCommonerIndex = ((currentFemaleCommonerIndex+1) % femaleCommonerIds.size());
		return id;
	}
}