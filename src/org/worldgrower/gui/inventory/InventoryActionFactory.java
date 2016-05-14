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

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.start.Game;

public class InventoryActionFactory {

	private WorldObject playerCharacter;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	
	public InventoryActionFactory(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, DungeonMaster dungeonMaster, WorldPanel container) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.container = container;
	}
	
	public InventoryActionFactory setDialog(InventoryDialog dialog) {
		this.dialog = dialog;
		return this;
	}

	public List<Action> getInventoryActions(int inventoryItemId) {
		List<Action> inventoryActions = new ArrayList<>();
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
		
		for(org.worldgrower.actions.InventoryAction action : Actions.getInventoryActions()) {
			WorldObject inventoryItem = inventory.get(inventoryItemId);
			if (action.isValidInventoryItem(inventoryItem, inventory, playerCharacter)) {
				inventoryActions.add(new InventoryItemAction(action, inventoryItemId));
			}
		}
		return inventoryActions;
	}
	
	private class InventoryItemAction extends AbstractAction {
		
		private final ManagedOperation action;
		private final int inventoryItemId;
		
		public InventoryItemAction(ManagedOperation action, int inventoryItemId) {
			super(action.getSimpleDescription(), new ImageIcon(imageInfoReader.getImage(action.getImageIds(), null)));
			this.action = action;
			this.inventoryItemId = inventoryItemId;
			this.putValue(Action.LONG_DESCRIPTION, action.getRequirementsDescription());
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int index = inventoryItemId;
			Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, playerCharacter.getOperation(action), new int[] { index }, world, dungeonMaster, playerCharacter, container);
			
			dialog.refresh(new InventoryDialogModel(playerCharacter));
		}

		@Override
		public boolean isEnabled() {
			int index = inventoryItemId;
			return Game.canActionExecute(playerCharacter, action, new int[] { index }, world, playerCharacter);
		}
	}
}