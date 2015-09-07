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
import java.util.List;

import javax.swing.AbstractAction;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;

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
		List<InventoryDialogAction> inventoryActions = getInventoryActions();
		
		dialog = new InventoryDialog(new InventoryDialogModel(playerCharacter), playerCharacter.getProperty(Constants.INVENTORY), imageInfoReader, inventoryActions);
		dialog.showMe();
	}

	private List<InventoryDialogAction> getInventoryActions() {
		List<InventoryDialogAction> inventoryActions = new ArrayList<>();
		
		for(ManagedOperation action : Actions.getInventoryActions()) {
			if (Main.canActionExecute(playerCharacter, action, new int[0], world, playerCharacter)) {
				inventoryActions.add(new InventoryDialogAction() {
	
					@Override
					public String getDescription() {
						return action.getSimpleDescription();
					}
	
					@Override
					public ActionListener getGuiAction() {
						return new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								Main.executeAction(playerCharacter, playerCharacter.getOperation(action), new int[0], world, dungeonMaster, playerCharacter, container);
							
								dialog.refresh(playerCharacter.getProperty(Constants.INVENTORY), playerCharacter.getProperty(Constants.GOLD), getInventoryActions());
							}
						};
					}
	
					@Override
					public boolean isPossible(InventoryItem inventoryItem) {
						return false;
					}
					
				});
			}
		}
		return inventoryActions;
	}
}