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
import java.util.HashMap;

import javax.swing.AbstractAction;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.deity.Deity;
import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.start.Game;
import org.worldgrower.profession.Professions;

public class GuiCreateNewsPaperAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	
	public GuiCreateNewsPaperAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, WorldPanel parent, DungeonMaster dungeonMaster) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ActionContainingArgs guiAction = new GuiAction();
		ChooseKnowledgeAction action = new ChooseKnowledgeAction(playerCharacter, imageInfoReader, world, parent, guiAction);
		action.actionPerformed(null);
	}
	
	private class GuiAction extends AbstractAction implements ActionContainingArgs {
		private int[] args;
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Game.executeAction(playerCharacter, Actions.CREATE_NEWS_PAPER_ACTION, args, world, dungeonMaster, playerCharacter, parent);
		}

		@Override
		public void setArgs(int[] args) {
			this.args = args;
		}
	}
	
	public static void main(String[] args) throws Exception {
		World world = new WorldImpl(0, 0, null, null);
		DungeonMaster dungeonMaster = new DungeonMaster();
		HashMap<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		properties.put(Constants.ID, 3);
		properties.put(Constants.IMAGE_ID, ImageIds.KNIGHT);
		properties.put(Constants.NAME, "performer");
		WorldObject playerCharacter = new WorldObjectImpl(properties);
		world.addWorldObject(playerCharacter);
		playerCharacter.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(3, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		playerCharacter.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(3, Constants.DEITY, Deity.ARES);
		
		new GuiCreateNewsPaperAction(playerCharacter, new ImageInfoReader(), world, null, dungeonMaster).actionPerformed(null);
	}
}