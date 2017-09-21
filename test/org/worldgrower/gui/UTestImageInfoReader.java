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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.worldgrower.attribute.GhostImageIds;

public class UTestImageInfoReader {

	@Test
	public void testInitialize() throws IOException {
		ImageInfoReader imageInfoReader = new ImageInfoReader(new GhostImageIds());
		assertEquals(true, imageInfoReader.getCharacterImageIds().size() > 0);
	}
	
	 @Test
	 public void testGhostImages() throws IOException {
		 GhostImageIds ghostImageIds = new GhostImageIds();
		 new ImageInfoReader(ghostImageIds);
		 assertEquals(ImageIds.KNIGHT_GHOST, ghostImageIds.getGhostImageIdFor(ImageIds.KNIGHT));
	 
		 CommonerImageIds commonerImageIds = new CommonerImageIds();
		 
		 ImageIds imageId = null;
		 while(imageId != ImageIds.BLUE_HAIRED_COMMONER) {
			 imageId = commonerImageIds.getNextMaleCommonerImageId();
			 assertNotNull("No ghost imageId found for " + imageId, ghostImageIds.getGhostImageIdFor(imageId));	 
		 }
		 
		 while(imageId != ImageIds.BLACK_HAIRED_FEMALE_COMMONER) {
			 imageId = commonerImageIds.getNextFemaleCommonerImageId();
			 assertNotNull("No ghost imageId found for " + imageId, ghostImageIds.getGhostImageIdFor(imageId));	 
		 }
		 
	 }
}
