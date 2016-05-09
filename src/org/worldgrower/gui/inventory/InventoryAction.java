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
package org.worldgrower.gui.inventory;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.start.Game;

public class InventoryAction extends AbstractAction {

	private WorldObject playerCharacter;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	
	public InventoryAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, DungeonMaster dungeonMaster, WorldPanel container) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.container = container;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Action> inventoryActions = getInventoryActions();
		
		dialog = new InventoryDialog(new InventoryDialogModel(playerCharacter), imageInfoReader, inventoryActions);
		dialog.showMe();
	}

	private List<Action> getInventoryActions() {
		List<Action> inventoryActions = new ArrayList<>();
		
		for(ManagedOperation action : Actions.getInventoryActions()) {
			inventoryActions.add(new InventoryItemAction(action));
		}
		return inventoryActions;
	}
	
	private class InventoryItemAction extends AbstractAction {
		
		private final ManagedOperation action;
		
		public InventoryItemAction(ManagedOperation action) {
			super(action.getSimpleDescription(), new ImageIcon(imageInfoReader.getImage(action.getImageIds(), null)));
			this.action = action;
			this.putValue(Action.LONG_DESCRIPTION, action.getRequirementsDescription());
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int index = getIndex();
			Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, playerCharacter.getOperation(action), new int[] { index }, world, dungeonMaster, playerCharacter, container);
			
			dialog.refresh(new InventoryDialogModel(playerCharacter), getInventoryActions());
		}

		private int getIndex() {
			InventoryItem inventoryItem = dialog.getPlayerCharacterSelectedValue();
			int index = inventoryItem.getId();
			return index;
		}

		@Override
		public boolean isEnabled() {
			int index = getIndex();
			return Game.canActionExecute(playerCharacter, action, new int[] { index }, world, playerCharacter);
		}
	}
}