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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.attribute.IdList;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.gui.util.MessageDialogUtils;

public class GuiShowEventHappenedAction implements WorldStateChangedListener {

	private WorldObject playerCharacter;
	private World world;
	private WorldPanel container;
	private ImageInfoReader imageInfoReader;
	
	public GuiShowEventHappenedAction(WorldObject playerCharacter, World world, WorldPanel container, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.container = container;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void creatureTypeChange(WorldObject worldObject, CreatureType newCreatureType, String description) {
		if (worldObject == playerCharacter) {
			MessageDialogUtils.showMessage(description, "Changing creature type", worldObject, container, imageInfoReader);
		}
	}

	@Override
	public void electionFinished(WorldObject winner, WorldObject organization, IdList candidates) {
		int winnerId = winner.getProperty(Constants.ID);
		int playerId = playerCharacter.getProperty(Constants.ID);
		
		if (candidates.contains(playerId)) {
			final String description;
			if (playerId == winnerId) {
				description = "Congratulations, you are the new leader of the " + organization.getProperty(Constants.NAME);
			} else {
				description = winner.getProperty(Constants.NAME) + " is the new leader of the " + organization.getProperty(Constants.NAME);
			}
			MessageDialogUtils.showMessage(description, "Election finished", winner, container, imageInfoReader);
		}
	}

	@Override
	public void legalActionsChanged(List<LegalAction> changedLegalActions, WorldObject villagerLeader) {
		if (!villagerLeader.equals(playerCharacter)) {
			String description = villagerLeader.getProperty(Constants.NAME) + " changed the following laws:\n";
			for(LegalAction legalAction : changedLegalActions) {
				description += legalAction.getDescription() + "\n";
			}
			
			MessageDialogUtils.showMessage(description, "Legal Actions changed", villagerLeader, container, imageInfoReader);
		}
	}

	@Override
	public void thrownOutOfGroup(WorldObject worldObject, WorldObject target, int[] args, ManagedOperation action, IdList oldGroup, IdList newGroup) {
		if (worldObject.equals(playerCharacter)) {
			String description = "You have been thrown out of the following groups:\n";
			List<Integer> thrownOutGroups = oldGroup.getIdsNotPresentInOther(newGroup);
			for(int groupId : thrownOutGroups) {
				WorldObject organization = world.findWorldObject(Constants.ID, groupId);
				description += organization.getProperty(Constants.NAME) + "\n";
			}
			
			MessageDialogUtils.showMessage(description, "Thrown out of group(s)", playerCharacter, container, imageInfoReader);
		}
	}
}