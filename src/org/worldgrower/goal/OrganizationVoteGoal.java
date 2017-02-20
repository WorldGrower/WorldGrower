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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class OrganizationVoteGoal implements Goal {

	public OrganizationVoteGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject votingBox = VotingPropertyUtils.getVotingBox(performer, world);
		
		final int chosenCandidateId;
		IdList candidates = votingBox.getProperty(Constants.CANDIDATES);
		if (candidates.contains(performer)) {
			chosenCandidateId = performer.getProperty(Constants.ID);
		} else {
			IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
			chosenCandidateId = relationships.findBestId(w -> candidates.contains(w), world);
		}
		
		if (chosenCandidateId != -1) {
			return new OperationInfo(performer, votingBox, new int[] { chosenCandidateId } , Actions.VOTE_FOR_LEADER_ACTION);
		} else {
			return null;
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObject votingBox = VotingPropertyUtils.getVotingBox(performer, world);
		if (votingBox != null && VotingPropertyUtils.votingBoxAcceptsVotes(votingBox, world) && VotingPropertyUtils.canVoteAtVotingBox(performer, votingBox, world)) {
			boolean performerAlreadyVoted = world.getHistory().findHistoryItems(performer, votingBox, Actions.VOTE_FOR_LEADER_ACTION).size() > 0;
			return performerAlreadyVoted;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_ORGANIZATION_VOTE);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
