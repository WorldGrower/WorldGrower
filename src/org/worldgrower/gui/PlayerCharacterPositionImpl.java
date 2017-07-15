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

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

public class PlayerCharacterPositionImpl implements PlayerCharacterPosition {

	private final WorldPanel worldPanel;
	private final AnimationPainter animationPainter;
	
	public PlayerCharacterPositionImpl(WorldPanel worldPanel, AnimationPainter animationPainter) {
		super();
		this.worldPanel = worldPanel;
		this.animationPainter = animationPainter;
	}

	@Override
	public int getScreenX(WorldObject playerCharacter) {
		int currentX = worldPanel.getScreenX(playerCharacter.getProperty(Constants.X)) * 48;
		int deltaX = animationPainter.getDeltaX(playerCharacter);
		return currentX + deltaX;
	}

	@Override
	public int getScreenY(WorldObject playerCharacter) {
		int currentY = worldPanel.getScreenY(playerCharacter.getProperty(Constants.Y)) * 48;
		int deltaY = animationPainter.getDeltaY(playerCharacter);
		return currentY + deltaY;
	}
}
