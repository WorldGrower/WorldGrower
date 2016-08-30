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
import javax.swing.JFrame;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BrawlFinishedListener;
import org.worldgrower.actions.BrawlListener;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.ListInputDialog;

public class GuiShowBrawlResult implements BrawlFinishedListener {
	private static final String[] RESPONSE_PREFIXES = new String[] { 
		"I win this brawl.",
		"Yes, I crushed you.",
		"Next time, try to be at least a bit challenging."
	};
	
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private WorldPanel container;
	private JFrame parentFrame;
	
	public GuiShowBrawlResult(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, WorldPanel container, World world, JFrame parentFrame) {
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.container = container;
		this.parentFrame = parentFrame;
		
		world.getListenerByClass(BrawlListener.class).addBrawlFinishedListener(this);
	}

	private String[] getResponses(int goldWon) {
		String[] responses = new String[RESPONSE_PREFIXES.length];
		for(int i=0; i<RESPONSE_PREFIXES.length; i++) {
			responses[i] = RESPONSE_PREFIXES[i] + " I get " + goldWon + " gold from you.";
		}
		return responses;
	}
	
	@Override
	public void brawlFinished(WorldObject performer, WorldObject target, int goldWon) {
		Icon performerIcon = IconUtils.getWorldObjectIcon(performer, imageInfoReader);
		Icon targetIcon = IconUtils.getWorldObjectIcon(target, imageInfoReader);
		String[] responses = getResponses(goldWon);
		
		if (!performer.isControlledByAI()) {
			//TODO: handle response
			String response = new ListInputDialog("Choose brawl ending line:", targetIcon, responses, soundIdReader, parentFrame).showMe();
		}
		
		if (!target.isControlledByAI()) {
			container.setStatusMessage(IconUtils.getWorldObjectImage(performer, imageInfoReader), responses[0]);
		}
		
	}
}