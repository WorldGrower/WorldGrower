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

import javax.swing.AbstractAction;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageInfoReader;

public class InventoryAction extends AbstractAction {

	private WorldObject playerCharacter;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	
	public InventoryAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		dialog = new InventoryDialog(playerCharacter.getProperty(Constants.GOLD), playerCharacter.getProperty(Constants.INVENTORY), imageInfoReader);
		dialog.showMe();
	}
}