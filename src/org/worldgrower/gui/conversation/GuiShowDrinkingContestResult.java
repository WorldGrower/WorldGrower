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

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.DrinkingContestFinishedListener;
import org.worldgrower.actions.DrinkingContestListener;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.ImageSubstituter;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.ListData;
import org.worldgrower.gui.util.ListInputDialog;

public class GuiShowDrinkingContestResult implements DrinkingContestFinishedListener {
	private static final String[] RESPONSE_PREFIXES = new String[] { 
		"I win this drinking contest.",
		"Yes, I crushed you.",
		"Next time, try to be at least a bit challenging."
	};
	
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private WorldPanel container;
	private JFrame parentFrame;
	private ImageSubstituter imageSubstituter;
	
	public GuiShowDrinkingContestResult(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, WorldPanel container, World world, JFrame parentFrame) {
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.container = container;
		this.parentFrame = parentFrame;
		this.imageSubstituter = new ImageSubstituter(imageInfoReader);
		
		world.getListenerByClass(DrinkingContestListener.class).addDrinkingContestFinishedListener(this);
	}

	private String[] getResponses(int goldWon) {
		String[] responses = new String[RESPONSE_PREFIXES.length];
		for(int i=0; i<RESPONSE_PREFIXES.length; i++) {
			responses[i] = "<html>" + RESPONSE_PREFIXES[i] + " I get " + goldWon + " gold from you.</html>";
			responses[i] = imageSubstituter.substituteImagesInHtml(responses[i]);
		}
		return responses;
	}
	
	@Override
	public void drinkingContestFinished(WorldObject performer, WorldObject target, int goldWon) {
		Icon performerIcon = IconUtils.getWorldObjectIcon(performer, imageInfoReader);
		Icon targetIcon = IconUtils.getWorldObjectIcon(target, imageInfoReader);
		String[] responses = getResponses(goldWon);
		
		if (!performer.isControlledByAI()) {
			//TODO: handle response
			String response = new ListInputDialog("Choose drinking contest ending line:", targetIcon, new ListData(responses), imageInfoReader, soundIdReader, parentFrame).showMe();
		}
		
		if (!target.isControlledByAI()) {
			container.setStatusMessage(IconUtils.getWorldObjectImage(performer, imageInfoReader), responses[0]);
		}
		
	}
}