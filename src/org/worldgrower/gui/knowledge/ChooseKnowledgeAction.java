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
package org.worldgrower.gui.knowledge;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.conversation.KnowledgeToDescriptionMapper;
import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.music.SoundIdReader;

public class ChooseKnowledgeAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private JComponent parent;
	private ActionContainingArgs guiAction;
	private JFrame parentFrame;
	
	public ChooseKnowledgeAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, JComponent parent, ActionContainingArgs guiAction, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.guiAction = guiAction;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<Knowledge> knowledgeList = playerCharacter.getProperty(Constants.KNOWLEDGE_MAP).getSortedKnowledge(playerCharacter, world);
		List<String> knowledgeDescriptions = new ArrayList<>();
		List<ImageIds> imageIds = new ArrayList<>();
		
		KnowledgeToDescriptionMapper mapper = new KnowledgeToDescriptionMapper();
		for(Knowledge knowledge : knowledgeList) {
			knowledgeDescriptions.add(mapper.getStatementDescription(knowledge, world));
			int subjectId = knowledge.getSubjectId();
			WorldObject subject = world.findWorldObject(Constants.ID, subjectId);
			imageIds.add(subject.getProperty(Constants.IMAGE_ID));
		}
		
		ChooseKnowledgeDialog dialog = new ChooseKnowledgeDialog(knowledgeDescriptions, imageInfoReader, soundIdReader, imageIds, parent, guiAction, parentFrame);
		dialog.showMe();
	}
}