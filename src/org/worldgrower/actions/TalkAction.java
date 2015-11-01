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
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class TalkAction implements ManagedOperation {

	private Conversations conversations = new Conversations();
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int question = args[0];
		int subjectId = args[1];
		int historyItemId = args[2];
		int additionalValue = args[3];
		Response answer = null;
		
		// if target is controlled by AI, the AI will respond.
		// otherwise, a gui will be shown to a player by world.logAction
		if (target.isControlledByAI()) {
			answer = conversations.getReplyPhrase(question, subjectId, historyItemId, performer, target, world, additionalValue);
			
			WorldObject performerFacade = createFacade(performer, performer, target, world);
			WorldObject targetFacade = createFacade(target, performer, target, world);
			
			RelationshipPropertyUtils.changeRelationshipValueUsingFacades(performer, target, 1, this, args, world);
			
			conversations.handleResponse(answer.getId(), question, subjectId, historyItemId, performerFacade, targetFacade, world, additionalValue);
			
			performer.increment(Constants.SOCIAL, 70);
			target.increment(Constants.SOCIAL, 70);
		}
		world.logAction(this, performer, target, args, answer);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int question = args[0];
		int subjectId = args[1];
		final WorldObject subject;
		if (subjectId != -1) {
			subject = world.findWorldObject(Constants.ID, subjectId);
		} else {
			subject = null;
		}
		return Reach.evaluateTarget(performer, args, target, 10)
				+ conversations.distance(question, performer, target, subject, world);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		ArgumentRange[] argumentRanges = new ArgumentRange[1];
		argumentRanges[0] = new ArgumentRange(0, conversations.size());
		return argumentRanges;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasIntelligence() && target.getProperty(Constants.CREATURE_TYPE).canTalk() && !performer.equals(target));
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		int question = args[0];
		return conversations.getConversation(question).getDescription(performer, target, world);
	}

	@Override
	public String getSimpleDescription() {
		return "talk";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.GOLD_RING;
	}
}