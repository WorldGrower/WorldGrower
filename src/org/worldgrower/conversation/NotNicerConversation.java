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
package org.worldgrower.conversation;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.text.Text;

public class NotNicerConversation extends AbstractChangeOpinionConversation {

	@Override
	public Question createQuestion(WorldObject performer, WorldObject target, WorldObject subject) {
		return new Question(subject, Text.QUESTION_NOT_NICE, subject.getProperty(Constants.NAME));
	}

	@Override
	public void handleYesResponse(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		RelationshipPropertyUtils.changeRelationshipValue(target, subject, -50, 0, Actions.TALK_ACTION, Conversations.createArgs(this), world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking in order to avoid someone";
	}
}
