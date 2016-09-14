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
package org.worldgrower.gui.status;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.worldgrower.gui.util.ImageUtils;
import org.worldgrower.gui.util.JLabelFactory;

class StatusMessageListRenderer implements ListCellRenderer<StatusMessage> {

	private JLabel defaultRenderer = JLabelFactory.createJLabel("");
	
	@Override
	public Component getListCellRendererComponent(JList<? extends StatusMessage> list, StatusMessage value, int index, boolean isSelected, boolean cellHasFocus) {
		
		Icon icon;
		if (value.getImage() != null) {
			Image image = value.getImage();
			if (image.getWidth(null) > 48 || image.getHeight(null) > 48) {
				image = ImageUtils.cropImage((BufferedImage) image, 48, 48);
			}
			
			icon = new ImageIcon(image);
		} else {
			icon = null;
		}
		
		defaultRenderer.setIcon(icon);
		defaultRenderer.setText(value.getMessage());
		return defaultRenderer;
	}

}
