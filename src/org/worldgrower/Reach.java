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
package org.worldgrower;

public class Reach {

	//TODO: add reach 2, 3, etc
	public static int evaluateTarget(WorldObject performer, int[] args, WorldObject target, int reach) {
		if (performer == null) {
			throw new IllegalArgumentException("performer is null");
		}
		if (target == null) {
			throw new IllegalArgumentException("target is null");
		}
		
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		int targetX = target.getProperty(Constants.X);
		int targetY = target.getProperty(Constants.Y);
		int targetWidth = target.getProperty(Constants.WIDTH);
		int targetHeight = target.getProperty(Constants.HEIGHT);
		
		final int realTargetX;
		final int realTargetY;
		if (targetWidth == 1 && targetHeight == 1) {
			realTargetX = targetX;
			realTargetY = targetY;
		} else {
			int lowestDistance = Integer.MAX_VALUE;
			int bestX = targetX;
			int bestY = targetY;
			for(int x=targetX; x<targetX+targetWidth; x++) {
				for(int y=targetY; y<targetY+targetHeight; y++) {
					int distance = distance(x, y, performerX, performerY);
					if (distance < lowestDistance) {
						lowestDistance = distance;
						bestX = x;
						bestY = y;
					}
				}
			}
			realTargetX = bestX;
			realTargetY = bestY;
		}
		
		int differenceX = Math.abs(performerX - realTargetX);
		int differenceY = Math.abs(performerY - realTargetY);
 
		int distance = differenceX*differenceX + differenceY*differenceY;
		
		if (distance == 1 || distance == 2) {
			return 0;
		} else {
			return distance;
		}
		
		/*
		 *   X X X
		 *   X A X
		 *   X X X
		 * 
		 */
	}
	
	public static int distance(WorldObject performer, WorldObject target) {
		if (performer == null) {
			throw new IllegalArgumentException("performer is null");
		}
		if (target == null) {
			throw new IllegalArgumentException("target is null");
		}
		
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		int targetX = target.getProperty(Constants.X);
		int targetY = target.getProperty(Constants.Y);
		
		return distance(performerX, performerY, targetX, targetY);
		
	}

	private static int distance(int performerX, int performerY, int targetX, int targetY) {
		return Math.max(Math.abs(performerX - targetX), Math.abs(performerY - targetY));
	}
}
