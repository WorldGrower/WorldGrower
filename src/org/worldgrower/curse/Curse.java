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
package org.worldgrower.curse;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.gui.ImageIds;

public interface Curse extends Serializable {

	public List<Goal> getCurseGoals(List<Goal> normalGoals);
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world);
	public boolean canMove();
	public boolean canTalk();
	public String getExplanation();
	public String getDescription();
	public String getName();
	public ImageIds getImageId();
	public void curseStarts(WorldObject worldObject, WorldStateChangedListeners worldStateChangedListeners);
	public void curseEnds(WorldObject worldObject, WorldStateChangedListeners worldStateChangedListeners);
	public boolean performerWantsCurseRemoved(WorldObject performer, World world);
	
	public static final SirenCurse SIREN_CURSE = new SirenCurse();
	public static final ToadCurse TOAD_CURSE = new ToadCurse();
	public static final InfertilityCurse INFERTILITY_CURSE = new InfertilityCurse();
	public static final GluttonyCurse GLUTTONY_CURSE = new GluttonyCurse();
	public static final ChangeGenderCurse CHANGE_GENDER_CURSE = new ChangeGenderCurse();
	public static final VampireCurse VAMPIRE_CURSE = new VampireCurse();
	public static final WerewolfCurse WEREWOLF_CURSE = new WerewolfCurse();
	public static final LichCurse LICH_CURSE = new LichCurse();
	public static final GhoulCurse GHOUL_CURSE = new GhoulCurse();
	
	public static final List<Curse> BESTOWABLE_CURSES = Arrays.asList(
			INFERTILITY_CURSE, 
			GLUTTONY_CURSE, 
			CHANGE_GENDER_CURSE
	);

	public static List<ImageIds> getBestowableCurseImageIds() {
		return BESTOWABLE_CURSES.stream().map(c -> c.getImageId()).collect(Collectors.toList());
	}
}
