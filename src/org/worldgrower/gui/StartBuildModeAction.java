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

import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BuildAction;

public class StartBuildModeAction extends AbstractAction implements ActionContainingArgs {

	private WorldObject playerCharacter;
	private CharacterDialog dialog;
	private ImageInfoReader imageInfoReader;
	private WorldPanel worldPanel;
	private BuildAction buildAction;
	private int[] args = new int[0];
	
	public StartBuildModeAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, WorldPanel worldPanel, BuildAction buildAction) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.worldPanel = worldPanel;
		this.buildAction = buildAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		worldPanel.startBuildMode(buildAction, args);
	}

	@Override
	public void setArgs(int[] args) {
		this.args = args;
	}
}