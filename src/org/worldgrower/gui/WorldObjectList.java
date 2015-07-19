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

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

public class WorldObjectList extends JScrollPane {

	private JList<WorldObject> worldObjectList;
	private ImageInfoReader imageInfoReader;

	public WorldObjectList(ImageInfoReader imageInfoReader, List<WorldObject> worldObjects) {
		
		this.imageInfoReader = imageInfoReader;
		
		initializeGui(worldObjects);
	}

	private void initializeGui(List<WorldObject> worldObjects) {
		worldObjectList = new JList<>();
		worldObjectList.setListData(worldObjects.toArray(new WorldObject[0]));
		worldObjectList.setCellRenderer(new ListRenderer());
		this.setViewportView(worldObjectList);
	}

	public void addMouseListener(MouseListener l) {
		worldObjectList.addMouseListener(l);
	}
	
	public WorldObject getSelectedValue() {
		return worldObjectList.getSelectedValue();
	}
	
	public int getSelectedIndex() {
		return worldObjectList.getSelectedIndex();
	}
	
	public void setSelectedIndex(int index) {
		worldObjectList.setSelectedIndex(index);
	}
	
	class ListRenderer extends JLabel implements ListCellRenderer<WorldObject> {
		public ListRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(
                    JList list,
                    WorldObject value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

			WorldObject worldObject = value;

			Image image = imageInfoReader.getImage(worldObject.getProperty(Constants.IMAGE_ID), null);

			setIcon(new ImageIcon(image));
			
			if (isSelected) {
				setBackground(Color.BLACK);
			} else {
				setBackground(Color.WHITE);
			}
			setToolTipText(worldObject.getProperty(Constants.NAME));

			return this;
		}
	}
}
