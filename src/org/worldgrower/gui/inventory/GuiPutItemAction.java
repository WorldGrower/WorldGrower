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
import javax.swing.JComponent;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.gui.ImageInfoReader;

public class GuiPutItemAction extends AbstractAction {

	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private JComponent container;
	private WorldObject target;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	
	public GuiPutItemAction(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, JComponent container, WorldObject target, ImageInfoReader imageInfoReader) {
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
		dialog = new InventoryDialog(playerCharacter.getProperty(Constants.GOLD), playerCharacter.getProperty(Constants.INVENTORY), new InventoryDialogSellAction(), imageInfoReader);
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
					InventoryItem inventoryItem = dialog.getSelectedValue();
					int price = BuySellUtils.getPrice(playerCharacter, inventoryItem.getId());
					int[] args = new int[] { inventoryItem.getId(), price };
					sell(args);
					
					dialog.refresh(playerCharacter.getProperty(Constants.INVENTORY), playerCharacter.getProperty(Constants.GOLD));
				}
			};
		}

		@Override
		public boolean isPossible(InventoryItem inventoryItem) {
			return true;
		}
		
	}
	
	public void sell(int[] args) {
		Main.executeAction(playerCharacter, playerCharacter.getOperation(Actions.PUT_ITEM_INTO_INVENTORY_ACTION), args, world, dungeonMaster, target, container);
	}
}