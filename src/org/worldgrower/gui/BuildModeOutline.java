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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.attribute.ManagedProperty;

public class BuildModeOutline {

	private boolean buildMode = false;
	private BuildAction buildAction;
	private int[] args;
	
	public void startBuildMode(BuildAction buildAction, int[] args) {
		this.buildMode = true;
		this.buildAction = buildAction;
		this.args = args;
	}
	
	public void endBuildMode(boolean executeBuildAction, Point mouseLocation, int offsetX, int offsetY, WorldObject playerCharacter, World world, GuiMouseListener guiMouseListener) {
		this.buildMode = false;
		if (executeBuildAction) {
			WorldObject buildLocation = getBuildLocation(mouseLocation, offsetX, offsetY);
			if (buildAction.isValidTarget(playerCharacter, buildLocation, world)) {
				guiMouseListener.executeBuildAction(buildAction, buildLocation, args);
			}
		}
		
		this.buildAction = null;
	}
	
	public void repaintBuildMode(Graphics g, Point mouseLocation, int offsetX, int offsetY, WorldObject playerCharacter, World world) {
		if (buildMode) {
			Graphics2D g2 = (Graphics2D) g;
			
			final Color color;
			WorldObject buildLocation = getBuildLocation(mouseLocation, offsetX, offsetY);
			if (buildAction.isValidTarget(playerCharacter, buildLocation, world)) {
				color = Color.GREEN;
			} else {
				color = Color.RED;
			}
			
			g2.setColor(color);
			g2.draw(getRectangleToDraw(mouseLocation));
		}
	}
	
	private Rectangle2D.Double getRectangleToDraw(Point mouseLocation) {
		int x = ((mouseLocation.x) / 48) * 48;
		int y = ((mouseLocation.y) / 48) * 48;
		int width = buildAction.getWidth() * 48;
		int height = buildAction.getHeight() * 48;
		
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	private WorldObject getBuildLocation(Point mouseLocation, int offsetX, int offsetY) {
		int x = ((mouseLocation.x - (offsetX * 48)) / 48) * 48;
		int y = ((mouseLocation.y - (offsetY * 48)) / 48) * 48;
		int width = buildAction.getWidth() * 48;
		int height = buildAction.getHeight() * 48;
		
		return createBuildLocation(x / 48, y / 48, width / 48, height / 48);
	}
	
	private WorldObject createBuildLocation(int x, int y, int width, int height) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, width);
		properties.put(Constants.HEIGHT, height);
		return new WorldObjectImpl(properties);
	}

	public boolean inBuildMode() {
		return buildMode;
	}
}
