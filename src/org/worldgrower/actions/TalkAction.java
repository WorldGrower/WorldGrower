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
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Response;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class TalkAction implements ManagedOperation, AnimatedAction {

	private Conversations conversations = new Conversations();
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int question = args[0];
		int subjectId = args[1];
		int historyItemId = args[2];
		int additionalValue = args[3];
		int additionalValue2 = args[4];
		Response answer = null;
		
		// if target is controlled by AI, the AI will respond.
		// otherwise, a gui will be shown to a player by world.logAction
		if (target.isControlledByAI()) {
			answer = conversations.getReplyPhrase(question, subjectId, historyItemId, performer, target, world, additionalValue, additionalValue2);
			
			WorldObject performerFacade = createFacade(performer, performer, target, world);
			WorldObject targetFacade = createFacade(target, performer, target, world);
			
			RelationshipPropertyUtils.changeRelationshipValueUsingFacades(performer, target, 1, this, args, world);
			
			conversations.handleResponse(answer.getId(), question, subjectId, historyItemId, performerFacade, targetFacade, world, additionalValue, additionalValue2);
			
			performer.increment(Constants.SOCIAL, 70);
			target.increment(Constants.SOCIAL, 70);
		} else {
			RelationshipPropertyUtils.changeRelationshipValueUsingFacades(performer, target, 1, this, args, world);
			performer.increment(Constants.SOCIAL, 70);
			target.increment(Constants.SOCIAL, 70);
		}
		world.logAction(this, performer, target, args, answer);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		int question = args[0];
		int subjectId = args[1];
		int additionalValue = args[3];
		int additionalValue2 = args[4];
		final WorldObject subject;
		if (subjectId != -1) {
			if (world.exists(subjectId)) {
				subject = world.findWorldObjectById(subjectId);
			} else {
				return false;// if subject no longer exists, talkaction cannot possibly succeed
			}
		} else {
			subject = null;
		}
		
		return conversations.isConversationAvailable(question, performer, target, subject, world)
		&& conversations.additionalValuesValid(question, performer, target, additionalValue, additionalValue2, world);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}
	
	@Override
	public String getDescription() {
		return "talk with a person";
	}

	@Override
	public boolean requiresArguments() {
		return true;
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
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.GOLD_RING;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.GOLD_RING_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}