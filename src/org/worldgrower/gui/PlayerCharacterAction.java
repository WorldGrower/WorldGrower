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
package org.worldgrower.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class PlayerCharacterAction extends AbstractAction {
	private WorldObject playerCharacter;
	private World world;
	private JComponent container;
	private DungeonMaster dungeonMaster;
	private ManagedOperation action;
	private WorldObject target;
	
	public PlayerCharacterAction(WorldObject playerCharacter, World world, JComponent container, DungeonMaster dungeonMaster, ManagedOperation action, WorldObject target) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.container = container;
		this.dungeonMaster = dungeonMaster;
		this.action = action;
		this.target = target;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Main.executeAction(playerCharacter, action, new int[0], world, dungeonMaster, target, container);
	}
}