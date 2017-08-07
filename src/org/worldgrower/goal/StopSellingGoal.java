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
package org.worldgrower.goal;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Profession;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class StopSellingGoal implements Goal {

	public StopSellingGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		for(WorldObject nonMember : getNonMembersSellingItems(performer, world)) {
			boolean actionAlreadyPerformed = world.getHistory().findHistoryItems(performer, nonMember, Conversations.createArgs(Conversations.STOP_SELLING_CONVERSATION), Actions.TALK_ACTION).size() > 0;
			if (!actionAlreadyPerformed) {
				return new OperationInfo(performer, nonMember, Conversations.createArgs(Conversations.STOP_SELLING_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		return null;
	}

	private List<WorldObject> getNonMembersSellingItems(WorldObject performer, World world) {
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(performer, world);
		Profession profession = performer.getProperty(Constants.PROFESSION);
		if (profession != null) {
			List<Item> itemsSoldByProfession = profession.getSellItems();
			for(WorldObject organization : organizations) {
				return world.findWorldObjectsByProperty(Constants.STRENGTH, w -> nonMemberSoldItem(performer, organization, w, itemsSoldByProfession));
			}
		}
		return new ArrayList<>();
	}
	
	private boolean nonMemberSoldItem(WorldObject performer, WorldObject organization, WorldObject w, List<Item> items) {
		return w.hasProperty(Constants.ITEMS_SOLD) 
				&& !w.getProperty(Constants.ITEMS_SOLD).isEmpty()
				&& w.getProperty(Constants.ITEMS_SOLD).containsAny(items)
				&& !w.getProperty(Constants.GROUP).contains(organization)
				&& !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w);
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> nonMembers = getNonMembersSellingItems(performer, world);
		return nonMembers.size() == 0;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_STOP_SELLING);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
