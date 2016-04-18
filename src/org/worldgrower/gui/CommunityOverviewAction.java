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

import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class CommunityOverviewAction extends AbstractAction {

	private WorldObject playerCharacter;
	private CommunityDialog dialog;
	private ImageInfoReader imageInfoReader;
	private World world;
	
	public CommunityOverviewAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog = new CommunityDialog(playerCharacter, imageInfoReader, world);
		dialog.setVisible(true);
	}
}