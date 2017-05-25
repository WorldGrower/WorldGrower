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
			ImageIds.MALE_BLUE_PANTS_BOY_COMMONER,
			ImageIds.MALE_OLD_COMMONER,
			ImageIds.MALE_SPIKY_HAIRED_COMMONER,
			ImageIds.MALE_GREEN_HAIRED_COMMONER,
			ImageIds.MALE_BROWN_HAIR_BOY_COMMONER,
			ImageIds.MALE_3C_1,
			ImageIds.MALE_3C_2,
			ImageIds.MALE_3C_3,
			ImageIds.MALE_3C_4,
			ImageIds.MALE_3D_1,
			ImageIds.MALE_3D_2,
			ImageIds.MALE_3D_3,
			ImageIds.MALE_3E_1,
			ImageIds.MALE_3E_2,
			ImageIds.MALE_3E_3,
			ImageIds.MALE_3E_4,
			ImageIds.MALE_3F_1,
			ImageIds.MALE_3F_2,
			ImageIds.MALE_3F_3,
			ImageIds.MALE_3F_4,
			ImageIds.MALE_3G_1,
			ImageIds.MALE_3G_2,
			ImageIds.MALE_3G_3,
			ImageIds.MALE_3G_4,
			ImageIds.MALE_01_1,
			ImageIds.MALE_01_2,
			ImageIds.MALE_01_3,
			ImageIds.MALE_01_4,
			ImageIds.MALE_AKTOR1_1,
			ImageIds.MALE_AKTOR1_2,
			ImageIds.MALE_AKTOR1_3,
			ImageIds.MALE_AKTOR1_4,
			ImageIds.MALE_AKTOR3_1,
			ImageIds.MALE_AKTOR3_2,
			ImageIds.MALE_AKTOR3_3,
			ImageIds.MALE_AKTOR3_4,
			ImageIds.MALE_HERO,
			ImageIds.SAM_NPC,
			ImageIds.NICOLAI_NPC,
			ImageIds.MALE_PIRATE,
			ImageIds.MALE_PIRATE52,
			ImageIds.MALE_PIRATE02,
			ImageIds.MALE_PIRATE03
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
			ImageIds.FEMALE_ORANGE_HAIR_GIRL_COMMONER,
			ImageIds.FEMALE_OLDER_COMMONER,
			ImageIds.FEMALE_MILKER_COMMONER,
			ImageIds.FEMALE_BLOND_GIRL,
			ImageIds.FEMALE_BLUE_HAIR_GIRL_COMMONER,
			ImageIds.FEMALE_3C_1,
			ImageIds.FEMALE_3C_2,
			ImageIds.FEMALE_3C_3,
			ImageIds.FEMALE_3C_4,
			ImageIds.FEMALE_3D_1,
			ImageIds.FEMALE_3D_2,
			ImageIds.FEMALE_3D_3,
			ImageIds.FEMALE_3E_1,
			ImageIds.FEMALE_3E_2,
			ImageIds.FEMALE_3E_3,
			ImageIds.FEMALE_3E_4,
			ImageIds.FEMALE_3F_1,
			ImageIds.FEMALE_3F_2,
			ImageIds.FEMALE_3F_3,
			ImageIds.FEMALE_3F_4,
			ImageIds.FEMALE_3G_1,
			ImageIds.FEMALE_3G_2,
			ImageIds.FEMALE_3G_3,
			ImageIds.FEMALE_3G_4,
			ImageIds.FEMALE_01_1,
			ImageIds.FEMALE_01_2,
			ImageIds.FEMALE_01_3,
			ImageIds.FEMALE_01_4,
			ImageIds.FEMALE_AKTOR1_1,
			ImageIds.FEMALE_AKTOR1_2,
			ImageIds.FEMALE_AKTOR1_3,
			ImageIds.FEMALE_AKTOR1_4,
			ImageIds.FEMALE_AKTOR3_1,
			ImageIds.FEMALE_AKTOR3_2,
			ImageIds.FEMALE_AKTOR3_3,
			ImageIds.FEMALE_AKTOR3_4,
			ImageIds.FEMALE_HERO,
			ImageIds.SOPHIE_NPC,
			ImageIds.FEMALE_PIRATE,
			ImageIds.FEMALE_PIRATE52,
			ImageIds.FEMALE_PIRATE02,
			ImageIds.FEMALE_PIRATE03
			);
	
	private int currentMaleCommonerIndex = 0;
	private int currentFemaleCommonerIndex = 0;
	
	private final ImageIds imageIdToSkip;
	
	public CommonerImageIds() {
		this.imageIdToSkip = null;
	}
	
	public CommonerImageIds(ImageIds imageIdToSkip) {
		this.imageIdToSkip = imageIdToSkip;
	}
	
	public ImageIds getNextMaleCommonerImageId() {
		ImageIds id = maleCommonerIds.get(currentMaleCommonerIndex);
		currentMaleCommonerIndex = ((currentMaleCommonerIndex+1) % maleCommonerIds.size());
		if (id == imageIdToSkip) {
			id = getNextMaleCommonerImageId();
		}
		return id;
	}

	public ImageIds getNextFemaleCommonerImageId() {
		ImageIds id = femaleCommonerIds.get(currentFemaleCommonerIndex);
		currentFemaleCommonerIndex = ((currentFemaleCommonerIndex+1) % femaleCommonerIds.size());
		if (id == imageIdToSkip) {
			id = getNextFemaleCommonerImageId();
		}
		return id;
	}
}