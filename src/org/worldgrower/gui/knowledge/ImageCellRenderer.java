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
package org.worldgrower.gui.knowledge;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class ImageCellRenderer implements TableCellRenderer {
	private final ImageInfoReader imageInfoReader;

	public ImageCellRenderer(ImageInfoReader imageInfoReader) {
		super();
		this.imageInfoReader = imageInfoReader;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel label = new JLabel();
		label.setOpaque(false);
		
		label.setIcon(new ImageIcon(imageInfoReader.getImage((ImageIds) value, null)));

		if (isSelected) {
			label.setForeground(table.getSelectionForeground());
			label.setBackground(table.getSelectionBackground());
		} else {
			label.setForeground(table.getForeground());
			label.setBackground(table.getBackground());
		}

		return label;
	}

}