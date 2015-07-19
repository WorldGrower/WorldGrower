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
package org.worldgrower.gui.conversation;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Response;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class GuiRespondToQuestion implements Questioner, ManagedOperationListener {
	private WorldObject performer;
	private WorldObject playerCharacter;
	private int[] args;
	private World world;
	private Conversations conversations = new Conversations();
	private ImageInfoReader imageInfoReader;
	
	public GuiRespondToQuestion(WorldObject playerCharacter, World world, ImageInfoReader imageInfoReader) {
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.imageInfoReader = imageInfoReader;
	
		world.addListener(this);       
	}

	@Override
	public List<Response> getResponsePhrases(int id, int subjectId, int historyItemId) {
		return conversations.getReplyPhrases(id, subjectId, historyItemId, performer, playerCharacter, world);
	}

	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if ((target == playerCharacter) && (managedOperation == Actions.TALK_ACTION)) {
			this.performer = performer;
			this.args = args;
			Response response = (Response) value;
			ImageIds imageIdPerformer = performer.getProperty(Constants.IMAGE_ID);
			ImageIds imageIdTarget = target.getProperty(Constants.IMAGE_ID);
			RespondToQuestionDialog dialog = new RespondToQuestionDialog(args[0], args[1], args[2], GuiRespondToQuestion.this, conversations, imageIdPerformer, imageIdTarget, imageInfoReader);
			int selectedResponse = dialog.showMe();
			conversations.handleResponse(selectedResponse, args[0], args[1], args[2], performer, target, world);
		}
	}

	@Override
	public String getQuestionPhrase() {
		return conversations.getQuestionPhrase(args[0], args[1], args[2], performer, playerCharacter, world);
	}
}