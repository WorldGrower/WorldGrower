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
package org.worldgrower.gui.util;

import java.awt.Image;
import java.util.List;

import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class ListData {
	private final String[] list;
	private final String[] tooltips;
	private final Image[] images;
	
	public ListData(String[] list, String[] tooltips) {
		this.list = list;
		this.tooltips = tooltips;
		this.images = null;
	}
	
	public ListData(String[] list) {
		this.list = list;
		this.tooltips = null;
		this.images = null;
	}
	
	public ListData(List<String> list) {
		this.list = list.toArray(new String[0]);
		this.tooltips = null;
		this.images = null;
	}
	
	public ListData(List<String> list, List<ImageIds> imageIds, ImageInfoReader imageInfoReader) {
		this.list = list.toArray(new String[0]);
		this.tooltips = null;
		this.images = new Image[imageIds.size()];
		for(int i=0; i<imageIds.size(); i++) {
			this.images[i] = imageInfoReader.getImage(imageIds.get(i), null);
		}
	}

	public String[] getList() {
		return list;
	}

	public String[] getTooltips() {
		return tooltips;
	}

	public Image[] getImages() {
		return images;
	}
}
