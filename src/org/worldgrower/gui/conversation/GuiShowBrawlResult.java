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
package org.worldgrower.gui.conversation;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BrawlFinishedListener;
import org.worldgrower.actions.BrawlListener;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class GuiShowBrawlResult implements BrawlFinishedListener {
	private ImageInfoReader imageInfoReader;
	
	public GuiShowBrawlResult(ImageInfoReader imageInfoReader, World world) {
		this.imageInfoReader = imageInfoReader;
		
		world.getListenerByClass(BrawlListener.class).addBrawlFinishedListener(this);
	}

	@Override
	public void brawlFinished(WorldObject performer, WorldObject target) {
		Icon performerIcon = getWorldObjectIcon(performer);
		Icon targetIcon = getWorldObjectIcon(target);
		
		if (!performer.isControlledByAI()) {
			String[] responses = new String[]{ "I win this brawl" };
			String response = (String)JOptionPane.showInputDialog(
                    null,
                    "Choose brawl ending line:",
                    "brawl result dialog",
                    JOptionPane.INFORMATION_MESSAGE,
                    targetIcon,
                    responses,
                    responses[0]);
		}
		
		if (!target.isControlledByAI()) {
			JOptionPane.showMessageDialog(null,
				    "I won this brawl",
				    "brawl result dialog",
				    JOptionPane.INFORMATION_MESSAGE,
				    performerIcon);
		}
		
	}

	private Icon getWorldObjectIcon(WorldObject performer) {
		ImageIds performerImageIds = performer.getProperty(Constants.IMAGE_ID);
		Icon performerIcon = new ImageIcon(imageInfoReader.getImage(performerImageIds, null));
		return performerIcon;
	}
}