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

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.ConversationCategory;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Question;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class GuiAskQuestionAction extends AbstractAction implements Answerer {

	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private JComponent container;
	private WorldObject target;
	private Conversations conversations = new Conversations();
	private AskQuestionDialog dialog;
	private ImageInfoReader imageInfoReader;
	
	public GuiAskQuestionAction(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, JComponent container, WorldObject target, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.container = container;
		this.target = target;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ImageIds imageIdPerformer = playerCharacter.getProperty(Constants.IMAGE_ID);
		ImageIds imageIdTarget = target.getProperty(Constants.IMAGE_ID);
		
		Map<Integer, ImageIds> subjectImageIds = createSubjectImageIdsMap();
		
		dialog = new AskQuestionDialog(this, conversations, imageIdPerformer, imageIdTarget, subjectImageIds, imageInfoReader);
		world.addListener(dialog);
		dialog.showMe();
	}

	private Map<Integer, ImageIds> createSubjectImageIdsMap() {
		Map<Integer, ImageIds> subjectImageIds = new HashMap<>();
		for(List<Question> questionList : getQuestionPhrases().values()) {
			for(Question question : questionList) {
				if (question.getSubjectId() != -1) {
					WorldObject subject = world.findWorldObject(Constants.ID, question.getSubjectId());
					ImageIds subjectImageId = subject.getProperty(Constants.IMAGE_ID);
					subjectImageIds.put(question.getSubjectId(), subjectImageId);
				}
			}
		}
		return subjectImageIds;
	}

	@Override
	public void askQuestion(int[] args) {
		Main.executeAction(playerCharacter, playerCharacter.getOperation(Actions.TALK_ACTION), args, world, dungeonMaster, target, container);
	}

	@Override
	public Map<ConversationCategory, List<Question>> getQuestionPhrases() {
		return conversations.getQuestionPhrases(playerCharacter, target, world);
	}

	@Override
	public void doneTalking() {
		world.removeListener(dialog);
	}

	@Override
	public boolean filterMessage(WorldObject performer) {
		return (performer == playerCharacter);
	}
}