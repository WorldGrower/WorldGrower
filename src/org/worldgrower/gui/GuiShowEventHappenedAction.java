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
import org.worldgrower.actions.GovernanceOption;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.attribute.BooleanProperty;
import org.worldgrower.attribute.Gender;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.GroupPropertyUtils;
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
		if (worldObject.equals(playerCharacter)) {
			MessageDialogUtils.showMessage(description, "Changing creature type", worldObject, container, imageInfoReader);
		}
	}

	@Override
	public void electionStarted(WorldObject organization) {
		if (playerCharacter.getProperty(Constants.GROUP).contains(organization)) {
			String description = "An election started for organization "  + organization.getProperty(Constants.NAME);
			MessageDialogUtils.showMessage(description, "Election started", playerCharacter, container, imageInfoReader);
		}
	}
	
	@Override
	public void electionFinished(WorldObject winner, WorldObject organization, IdList candidates, int electionWonPercentage) {
		int winnerId = winner.getProperty(Constants.ID);
		int playerId = playerCharacter.getProperty(Constants.ID);
		
		if (candidates.contains(playerId)) {
			final String description;
			if (playerId == winnerId) {
				description = "Congratulations, you won with " + electionWonPercentage + "% of the votes and you are the new leader of the " + organization.getProperty(Constants.NAME);
			} else {
				description = createElectionWinnerDescription(winner, organization, electionWonPercentage);
			}
			MessageDialogUtils.showMessage(description, "Election finished", winner, container, imageInfoReader);
		} else if (organization.equals(GroupPropertyUtils.getVillagersOrganization(world))) {
			String description = createElectionWinnerDescription(winner, organization, electionWonPercentage);
			MessageDialogUtils.showMessage(description, "Election finished", winner, container, imageInfoReader);
		}
	}

	String createElectionWinnerDescription(WorldObject winner, WorldObject organization, int electionWonPercentage) {
		return winner.getProperty(Constants.NAME) + " won with " + electionWonPercentage + "% of the votes and is the new leader of the " + organization.getProperty(Constants.NAME);
	}

	@Override
	public void governanceChanged(List<LegalAction> changedLegalActions, List<GovernanceOption> changedGovernanceOptions, WorldObject villagerLeader) {
		if (!villagerLeader.equals(playerCharacter)) {
			String description = createGovernanceDescription(changedLegalActions, changedGovernanceOptions, villagerLeader);
			MessageDialogUtils.showMessage(description, "Legal Actions changed", villagerLeader, container, imageInfoReader);
		}
	}

	static String createGovernanceDescription(List<LegalAction> changedLegalActions, List<GovernanceOption> changedGovernanceOptions, WorldObject villagerLeader) {
		StringBuilder descriptionBuilder = new StringBuilder(villagerLeader.getProperty(Constants.NAME) + " changed governance: ");
		boolean appendComma = false;
		for(LegalAction legalAction : changedLegalActions) {
			if (appendComma) {
				descriptionBuilder.append(", ");
			} else {
				appendComma = true;
			}
			descriptionBuilder.append("changed legality of ").append(legalAction.getDescription());
		}
		for(GovernanceOption governanceOption : changedGovernanceOptions) {
			if (appendComma) {
				descriptionBuilder.append(", ");
			} else {
				appendComma = true;
			}
			if (governanceOption.getProperty() instanceof IntProperty) {
				descriptionBuilder.append("changed ").append(governanceOption.getProperty().getName()).append(" to ").append(governanceOption.getNewValue());
			} else if (governanceOption.getProperty() instanceof BooleanProperty) {
				int value = governanceOption.getNewValue();
				boolean booleanValue = (value == 1);
				descriptionBuilder.append("changed '").append(governanceOption.getProperty().getName()).append("' to ").append(booleanValue);
			}
		}
		return descriptionBuilder.toString();
	}

	@Override
	public void thrownOutOfGroup(WorldObject worldObject, WorldObject target, int[] args, ManagedOperation action, IdList oldGroup, IdList newGroup) {
		if (worldObject.equals(playerCharacter)) {
			String description = "You have been thrown out of the following groups:\n";
			List<Integer> thrownOutGroups = oldGroup.getIdsNotPresentInOther(newGroup);
			if (thrownOutGroups.size() > 0) {
				for(int groupId : thrownOutGroups) {
					WorldObject organization = world.findWorldObjectById(groupId);
					description += organization.getProperty(Constants.NAME) + "\n";
				}
				
				MessageDialogUtils.showMessage(description, "Thrown out of group(s)", playerCharacter, container, imageInfoReader);
			}
		}
	}

	@Override
	public void skillIncreased(WorldObject worldObject, SkillProperty skillProperty, int oldValue, int newValue) {
		if (worldObject.equals(playerCharacter)) {
			String description = "Skill " + skillProperty.getName() + " increased from " + oldValue + " to " + newValue;
			MessageDialogUtils.showMessage(description, "Skill Increase", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void conditionGained(WorldObject worldObject, Condition condition) {
		if (worldObject.equals(playerCharacter)) {
			String description = playerCharacter.getProperty(Constants.NAME) + " gained condition '" + condition.getDescription() + "'";
			MessageDialogUtils.showMessage(description, "Condition Gained", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void conditionLost(WorldObject worldObject, Condition condition) {
		if (worldObject.equals(playerCharacter)) {
			String description = "Condition '" + condition.getDescription() + "' ended on " + playerCharacter.getProperty(Constants.NAME);
			MessageDialogUtils.showMessage(description, "Condition Lost", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void levelIncreased(WorldObject worldObject, int newValue) {
		if (worldObject.equals(playerCharacter)) {
			String description = playerCharacter.getProperty(Constants.NAME) + " is now level " + newValue + ".\n Hit points, energy and carrying capacity have increased.";
			MessageDialogUtils.showMessage(description, "Level increase", playerCharacter, container, imageInfoReader);
		}
		
	}

	@Override
	public void lostLeadership(WorldObject worldObject, WorldObject organization) {
		if (worldObject.equals(playerCharacter)) {
			String description = playerCharacter.getProperty(Constants.NAME) + " has lost leadership of organization " + organization.getProperty(Constants.NAME);
			MessageDialogUtils.showMessage(description, "Leadership Lost", playerCharacter, container, imageInfoReader);
		}
	}
	
	@Override
	public void skillsDeteriorated(WorldObject worldObject) {
		if (worldObject.equals(playerCharacter)) {
			String description = " The skills of " + playerCharacter.getProperty(Constants.NAME) + " have decreased, removing progress made since the last skill increase";
			MessageDialogUtils.showMessage(description, "Skills Deteriorated", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void fireAssetsSeized(WorldObject worldObject, List<Integer> buildingIds) {
		if (worldObject.equals(playerCharacter)) {
			String description = " All shacks and houses of " + playerCharacter.getProperty(Constants.NAME) + " have been seized in order to pay for taxes";
			MessageDialogUtils.showMessage(description, "Assets Seized", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void fireGenderChanged(WorldObject worldObject, Gender oldGender, Gender newGender) {
		if (worldObject.equals(playerCharacter)) {
			String description = " The gender of " + playerCharacter.getProperty(Constants.NAME) + " was changed from " + oldGender + " to " + newGender;
			MessageDialogUtils.showMessage(description, "Gender Changed", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void fireCreatureCursed(WorldObject performer, WorldObject target, Curse curse) {
		if (performer.equals(playerCharacter) || target.equals(playerCharacter)) {
			String description = " " + performer.getProperty(Constants.NAME) + " cursed " + target.getProperty(Constants.NAME) + " with " + curse.getName();
			MessageDialogUtils.showMessage(description, "Gender Changed", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void fireCreatureUncursed(WorldObject performer, WorldObject target, Curse curse) {
		if (performer.equals(playerCharacter) || target.equals(playerCharacter)) {
			String description = " " + performer.getProperty(Constants.NAME) + " removed curse " + curse.getName() + " from " + target.getProperty(Constants.NAME);
			MessageDialogUtils.showMessage(description, "Gender Changed", playerCharacter, container, imageInfoReader);
		}
	}

	@Override
	public void fireRebellionStarted(WorldObject organization) {
		String description = " A rebellion has spread to the majority of the villagers and the current leader has been overthrown";
		MessageDialogUtils.showMessage(description, "Rebellion started", playerCharacter, container, imageInfoReader);
	}

	@Override
	public void deityRetributed(Deity deity, String description) {
		MessageDialogUtils.showMessage(description, "Deity Displeased", playerCharacter, container, imageInfoReader);
	}
}