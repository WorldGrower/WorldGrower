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
package org.worldgrower.gui.inventory;

import java.awt.Component;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.worldgrower.gui.ImageInfoReader;

public class InventoryListCellRenderer implements ListCellRenderer<InventoryItem> {
	private final ImageInfoReader imageInfoReader;
	private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	public InventoryListCellRenderer(ImageInfoReader imageInfoReader) {
		super();
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends InventoryItem> list, InventoryItem inventoryItem, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel renderer = (JLabel) defaultRenderer
				.getListCellRendererComponent(list, inventoryItem, index, isSelected, cellHasFocus);

		renderer.setIcon(new ImageIcon(imageInfoReader.getImage(inventoryItem.getImageId(), null)));
		renderer.setText(inventoryItem.getDescription());
		renderer.setToolTipText(formatAdditionalProperties(inventoryItem));
		return renderer;
	}
	
	private String formatAdditionalProperties(InventoryItem inventoryItem) {
		StringBuilder builder = new StringBuilder();
		Map<String, String> additionalProperties = inventoryItem.getAdditionalProperties();
		for(Map.Entry<String, String> entry : additionalProperties.entrySet()) {
			builder.append(entry.getKey()).append(" : ").append(entry.getValue()).append("<br/>");
		}
		
		return builder.toString();
	}
}