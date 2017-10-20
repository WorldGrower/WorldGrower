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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.ConversationCategory;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Question;
import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.chooseworldobject.ChooseWorldObjectDialog;
import org.worldgrower.gui.chooseworldobject.WorldObjectMapper;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;

public class GuiAskQuestionAction extends AbstractAction implements Answerer {

	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	private WorldObject target;
	private Conversations conversations = new Conversations();
	private AskQuestionDialog dialog;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private JFrame parentFrame;
	
	public GuiAskQuestionAction(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, WorldPanel container, WorldObject target, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.container = container;
		this.target = target;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final ImageIds imageIdPerformer = playerCharacter.getProperty(Constants.IMAGE_ID);
		final ImageIds imageIdTarget = target.getProperty(Constants.IMAGE_ID);
		final String performerName = playerCharacter.getProperty(Constants.NAME);
		final String targetName = target.getProperty(Constants.NAME);
		
		final Map<Integer, ImageIds> subjectImageIds = createSubjectImageIdsMap();
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				dialog = new AskQuestionDialog(GuiAskQuestionAction.this, conversations, imageIdPerformer, imageIdTarget, performerName, targetName, subjectImageIds, imageInfoReader, soundIdReader, parentFrame);
				world.addListener(dialog);
				dialog.showMe();
				
			}
		});
	}

	private Map<Integer, ImageIds> createSubjectImageIdsMap() {
		Map<Integer, ImageIds> subjectImageIds = new HashMap<>();
		for(List<Question> questionList : getQuestionPhrases().values()) {
			for(Question question : questionList) {
				if (question.getSubjectId() != -1) {
					WorldObject subject = world.findWorldObjectById(question.getSubjectId());
					ImageIds subjectImageId = subject.getProperty(Constants.IMAGE_ID);
					subjectImageIds.put(question.getSubjectId(), subjectImageId);
				}
			}
		}
		return subjectImageIds;
	}

	@Override
	public void askQuestion(int[] args) {
		Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, playerCharacter.getOperation(Actions.TALK_ACTION), args, world, dungeonMaster, target, container, imageInfoReader, soundIdReader);
	}
	
	@Override
	public boolean canAskQuestion() {
		return world.exists(target) 
				&& Game.canActionExecute(
						playerCharacter, 
						Actions.TALK_ACTION, 
						Conversations.createArgs(Conversations.NAME_CONVERSATION), 
						world, 
						target);
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

	@Override
	public List<WorldObject> getPossibleSubjects(Question question) {
		return conversations.getPossibleSubjects(question.getId(), -1, playerCharacter, target, world);
	}

	@Override
	public ChooseWorldObjectDialog createChooseWorldObjectsDialog(ActionContainingArgs guiAction, Question question, JDialog parentDialog) {
		return new ChooseWorldObjectDialog(playerCharacter, imageInfoReader, soundIdReader, getPossibleSubjects(question), parentDialog, world, dungeonMaster, guiAction, parentFrame, WorldObjectMapper.WORLD_OBJECT_ID);
	}

	@Override
	public String getDescription(int worldObjectId) {
		return world.findWorldObjectById(worldObjectId).getProperty(Constants.NAME);
	}

	@Override
	public int getRelationshipValue() {
		return target.getProperty(Constants.RELATIONSHIPS).getValue(playerCharacter);
	}
}