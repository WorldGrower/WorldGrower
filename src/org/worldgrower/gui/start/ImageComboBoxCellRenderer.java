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

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

class ImageComboBoxCellRenderer extends JLabel implements ListCellRenderer<ImageIds> {

	private final ImageInfoReader imageInfoReader;

	public ImageComboBoxCellRenderer(ImageInfoReader imageInfoReader) {
		super();
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public Component getListCellRendererComponent(JList list, ImageIds value, int index, boolean isSelected, boolean cellHasFocus) {

		if (value != null) {
			Image image = imageInfoReader.getImage(value, null);
			ImageIcon icon = new ImageIcon(image);
			setIcon(icon);
		} else {
			setIcon(null);
		}
		
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		return this;
	}
}