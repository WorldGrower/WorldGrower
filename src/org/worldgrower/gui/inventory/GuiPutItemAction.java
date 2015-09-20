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
import java.util.ArrayList;

import javax.swing.AbstractAction;

import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;

public class GuiPutItemAction extends AbstractAction {

	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	private WorldObject target;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	
	public GuiPutItemAction(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, WorldPanel container, WorldObject target, ImageInfoReader imageInfoReader) {
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
		dialog = new InventoryDialog(new InventoryDialogModel(playerCharacter, target), new InventoryDialogSellAction(), imageInfoReader, new ArrayList<>());
		dialog.showMe();
	}

	private class InventoryDialogSellAction implements InventoryDialogAction {

		@Override
		public String getDescription() {
			return "Put selected item";
		}

		@Override
		public ActionListener getGuiAction() {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					InventoryItem inventoryItem = getSelectedItem(dialog);
					int[] args = new int[] { inventoryItem.getId() };
					sell(args);
					
					dialog.refresh(new InventoryDialogModel(playerCharacter, target), new ArrayList<>());
				}
			};
		}

		@Override
		public boolean isPossible(InventoryItem inventoryItem) {
			return true;
		}

		@Override
		public InventoryItem getSelectedItem(InventoryDialog dialog) {
			return dialog.getPlayerCharacterSelectedValue();
		}
		
	}
	
	public void sell(int[] args) {
		Main.executeAction(playerCharacter, playerCharacter.getOperation(Actions.PUT_ITEM_INTO_INVENTORY_ACTION), args, world, dungeonMaster, target, container);
	}
}