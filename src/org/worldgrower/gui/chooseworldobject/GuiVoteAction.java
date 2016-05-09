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
package org.worldgrower.gui.chooseworldobject;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.start.Game;

public class GuiVoteAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ChooseWorldObjectDialog dialog;
	private ImageInfoReader imageInfoReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private WorldObject worldObject;
	
	public GuiVoteAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, WorldPanel parent, DungeonMaster dungeonMaster, WorldObject worldObject) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.worldObject = worldObject;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		IdList candidates = worldObject.getProperty(Constants.CANDIDATES);
		List<WorldObject> voteWorldObjects = world.findWorldObjects(w -> candidates.contains(w.getProperty(Constants.ID)));
		
		ActionContainingArgs guiAction = new GuiAction();
		dialog = new ChooseWorldObjectDialog(playerCharacter, imageInfoReader, voteWorldObjects, parent, world, dungeonMaster, guiAction);
		dialog.showMe();
	}
	
	private class GuiAction extends AbstractAction implements ActionContainingArgs {
		private int[] args;
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, Actions.VOTE_FOR_LEADER_ACTION, args, world, dungeonMaster, worldObject, parent);
		}

		@Override
		public void setArgs(int[] args) {
			this.args = args;
		}
	}
}