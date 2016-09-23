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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class GoToPainter {

	private final Image gotoImage;
	private List<Point> gotoLocations = new ArrayList<>();
	
	public GoToPainter(ImageInfoReader imageInfoReader) {
		gotoImage = imageInfoReader.getImage(ImageIds.GOTO_IMAGE, null);
	}

	public void paint(Graphics g, World world, WorldPanel worldPanel) {
		for(Point gotoLocation : gotoLocations) {
			worldPanel.drawBackgroundImage(g, gotoImage, gotoLocation.x, gotoLocation.y);
		}
	}
	
	public void setGotoOperations(WorldObject worldObject, List<OperationInfo> operationInfos) {
		gotoLocations.clear();
		
		Point currentPosition = new Point(worldObject.getProperty(Constants.X), worldObject.getProperty(Constants.Y));
		for(OperationInfo operationInfo : operationInfos) {
			if (operationInfo.getArgs().length == 2) {
				int newX = currentPosition.x + operationInfo.getArgs()[0];
				int newY = currentPosition.y + operationInfo.getArgs()[1];
				currentPosition = new Point(newX, newY);
				gotoLocations.add(currentPosition);
			}
		}
	}
}
