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
import javax.swing.JFrame;

import org.worldgrower.WorldObject;
import org.worldgrower.gui.music.SoundIdReader;

public class MagicOverviewAction extends AbstractAction {

	private WorldObject playerCharacter;
	private MagicOverviewDialog dialog;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private JFrame parentFrame;
	
	public MagicOverviewAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog = new MagicOverviewDialog(playerCharacter, imageInfoReader, soundIdReader, parentFrame);
		dialog.setVisible(true);
	}
}