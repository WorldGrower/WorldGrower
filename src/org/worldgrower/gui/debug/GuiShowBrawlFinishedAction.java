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
package org.worldgrower.gui.debug;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.conversation.GuiShowBrawlResult;
import org.worldgrower.gui.music.SoundIdReader;

public class GuiShowBrawlFinishedAction extends AbstractAction {
	private final WorldObject performer;
	private final WorldObject target;
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private final WorldPanel container;
	private final World world;
	private final JFrame parentFrame;
	
	public GuiShowBrawlFinishedAction(WorldObject performer, WorldObject target, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, WorldPanel container, World world, JFrame parentFrame) {
		super();
		this.performer = performer;
		this.target = target;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.container = container;
		this.world = world;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GuiShowBrawlResult guiShowBrawlResult = new GuiShowBrawlResult(imageInfoReader, soundIdReader, container, world, parentFrame); 
		guiShowBrawlResult.brawlFinished(performer, target, 100);
	}
}