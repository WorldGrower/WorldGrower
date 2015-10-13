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
package org.worldgrower.gui.start;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

class ImageComboBoxModel extends DefaultComboBoxModel<ImageIds> {

	private final List<ImageIds> characterImageIds;
	
	public ImageComboBoxModel(ImageInfoReader imageInfoReader) {
		characterImageIds = imageInfoReader.getCharacterImageIds();
	}
	
	@Override
	public ImageIds getElementAt(int index) {
		return characterImageIds.get(index);
	}

	@Override
	public int getSize() {
		return characterImageIds.size();
	}
}