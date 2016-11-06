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

import java.awt.Component;
import java.awt.Image;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;

class ComboboxRenderer extends DefaultListCellRenderer {
    private final String[] tooltips;
    private final Image[] images;

    public ComboboxRenderer(List<String> tooltips) {
		super();
		this.tooltips = tooltips.toArray(new String[0]);
		this.images = null;
	}
    
    public ComboboxRenderer(ListData listData) {
		super();
		this.tooltips = listData.getTooltips();
		this.images = listData.getImages();
		this.setOpaque(false);
	}

	@Override
    public Component getListCellRendererComponent(JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {

        JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);

        if (index == -1) {
        	index = list.getSelectedIndex();
        }
        
        if (index != -1) {
        	if (tooltips != null) {
        		list.setToolTipText(tooltips[index]);
        	}
        	if (images != null) {
        		setIcon(new ImageIcon(images[index]));
        	}
        }
        return comp;
    }
}