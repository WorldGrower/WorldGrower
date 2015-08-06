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
package org.worldgrower.actions;

import java.io.ObjectStreamException;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class BecomeLeaderCandidateAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		target.getProperty(Constants.CANDIDATES).add(performer);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.ORGANIZATION_ID)) && (target.getProperty(Constants.ORGANIZATION_ID) != null) && (target.hasProperty(Constants.TURN_COUNTER));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean isInCandidateStage = VotingPropertyUtils.votingBoxAcceptsCandidates(target);
		boolean isPerformerAlreadyCandidate = target.getProperty(Constants.CANDIDATES).contains(performer);
		int targetVotingStageDistance = isInCandidateStage ? 0 : 1;
		int performerAlreadyCandidateDistance = isPerformerAlreadyCandidate ? 1 : 0;
		return Reach.evaluateTarget(performer, args, target, 1) + targetVotingStageDistance + performerAlreadyCandidateDistance;
	}
	
	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "becoming leader candidate";
	}

	@Override
	public String getSimpleDescription() {
		return "become leader candidate";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}
