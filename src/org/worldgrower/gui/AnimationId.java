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

public enum AnimationId {
	FIRE1(ImageIds.FIRE1, 20),
	DARKNESS1(ImageIds.DARKNESS1, 30),
	THUNDER1(ImageIds.THUNDER1, 30),
	SLASH1(ImageIds.SLASH1, 10),
	HEAL1(ImageIds.HEAL1, 25),
	ICE1(ImageIds.ICE1, 30),
	LIGHT4(ImageIds.LIGHT4, 25),
	HORIZONTAL_SLASH(ImageIds.HORIZONTAL_SLASH, 10),
	BLACK_CRESCENT_SLASH(ImageIds.BLACK_CRESCENT_SLASH, 10),
	WHITE_SLASH(ImageIds.WHITE_SLASH, 10),
	BLUE_ORB(ImageIds.BLUE_ORB, 10),
	PURPLE_ORB(ImageIds.PURPLE_ORB, 10),
	WHITE_ORB(ImageIds.WHITE_ORB, 10),
	RED_ORB(ImageIds.RED_ORB, 10),
	GREEN_ORB(ImageIds.GREEN_ORB, 10),
	YELLOW_ORB(ImageIds.YELLOW_ORB, 10),
	BREW_POISON_ANIMATION(ImageIds.BREW_POISON_ANIMATION, 10);
	
	private AnimationId(ImageIds imageIds, int numberOfFrames) {
		this.imageIds = imageIds;
		this.numberOfFrames = numberOfFrames;
	}
	private final ImageIds imageIds;
	private final int numberOfFrames;
	
	public ImageIds getImageIds() {
		return imageIds;
	}
	public int getNumberOfFrames() {
		return numberOfFrames;
	}
}
