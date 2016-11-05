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

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class ImageListRenderer<T> implements ListCellRenderer<T> {

	private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private final ImageIds[] imageIds;
	private final ImageInfoReader imageInfoReader;

	public ImageListRenderer(ImageIds[] imageIds, ImageInfoReader imageInfoReader) {
		super();
		this.imageIds = imageIds;
		this.imageInfoReader = imageInfoReader;
		defaultRenderer.setOpaque(false);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		ImageIds id = imageIds[index];
		if (id != null) {
			renderer.setIcon(new ImageIcon(imageInfoReader.getImage(id, null)));
		}
		return renderer;
	}
}