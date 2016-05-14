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
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.start.Game;

public class GuiGetItemAction extends AbstractAction {

	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	private WorldObject target;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	
	public GuiGetItemAction(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, WorldPanel container, WorldObject target, ImageInfoReader imageInfoReader) {
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
		InventoryActionFactory inventoryActionFactory = new InventoryActionFactory(playerCharacter, imageInfoReader, world, dungeonMaster, container);
		dialog = new InventoryDialog(new InventoryDialogModel(playerCharacter, target), new InventoryDialogBuyAction(), imageInfoReader, inventoryActionFactory);
		inventoryActionFactory.setDialog(dialog);
		dialog.showMe();
	}

	private class InventoryDialogBuyAction implements InventoryDialogAction {

		@Override
		public String getDescription() {
			return "Take selected item";
		}
		
		@Override
		public String getDescription2() {
			return null;
		}

		@Override
		public ActionListener getGuiAction() {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					InventoryItem inventoryItem = getSelectedItem(dialog);
					int[] args = new int[] { inventoryItem.getId() };
					buy(args);
					
					dialog.refresh(new InventoryDialogModel(playerCharacter, target));
				}
			};
		}

		@Override
		public boolean isPossible(InventoryItem inventoryItem) {
			return true;
		}

		@Override
		public InventoryItem getSelectedItem(InventoryDialog dialog) {
			return dialog.getTargetSelectedValue();
		}
		
		@Override
		public ActionListener getGuiAction2() {
			return null;
		}
	}
	
	public void buy(int[] args) {
		Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, playerCharacter.getOperation(Actions.GET_ITEM_FROM_INVENTORY_ACTION), args, world, dungeonMaster, target, container);
	}
}