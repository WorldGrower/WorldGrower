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

import static org.worldgrower.goal.FacadeUtils.createFacade;

import java.io.ObjectStreamException;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Response;

public class TalkAction implements ManagedOperation {

	private Conversations conversations = new Conversations();
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int question = args[0];
		int subjectId = args[1];
		int historyItemId = args[2];
		
		Response answer = conversations.getReplyPhrase(question, subjectId, historyItemId, performer, target, world);
		
		WorldObject performerFacade = createFacade(performer, performer, target);
		WorldObject targetFacade = createFacade(target, performer, target);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(targetFacade.getProperty(Constants.ID), 1);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performerFacade.getProperty(Constants.ID), 1);
		
		conversations.handleResponse(answer.getId(), question, subjectId, historyItemId, performerFacade, targetFacade, world);
		
		performer.increment(Constants.SOCIAL, 70);
		target.increment(Constants.SOCIAL, 70);
		
		world.logAction(this, performer, target, args, answer);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 10);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		ArgumentRange[] argumentRanges = new ArgumentRange[1];
		argumentRanges[0] = new ArgumentRange(0, conversations.size());
		return argumentRanges;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasIntelligence() && target.getProperty(Constants.CREATURE_TYPE).canTalk());
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "talking to " + target.getProperty(Constants.NAME);
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}