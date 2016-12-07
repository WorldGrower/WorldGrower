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
package org.worldgrower.gui.debug;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class GuiShowImagesOverviewAction extends AbstractAction {
	private ImageInfoReader imageInfoReader;
	
	public GuiShowImagesOverviewAction(ImageInfoReader imageInfoReader) {
		super();
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new ImageModel(getImages()));
		table.setBounds(50, 50, 1000, 800);
		table.setRowHeight(50);
		frame.add(new JScrollPane(table));
		
		frame.setBounds(100,  100, 900, 900);
		frame.setVisible(true);
	}
	
	private List<ImageInfo> getImages() {
		List<ImageInfo> imageInfos = new ArrayList<>();
		for (ImageIds imageId : ImageIds.values()) {
			imageInfos.add(new ImageInfo(imageId, imageInfoReader));
		}
		return imageInfos;
	}
	
	private static class ImageInfo {
		private final ImageIds imageId;
		private final Image image;
		
		public ImageInfo(ImageIds imageId, ImageInfoReader imageInfoReader) {
			this.imageId = imageId;
			this.image = imageInfoReader.getImage(imageId, null);
		}
		
		ImageIds getImageId() {
			return imageId;
		}
		Image getImage() {
			return image;
		}
		
		
	}
	
	private class ImageModel extends AbstractTableModel {

		private List<ImageInfo> imageInfos;
		
		public ImageModel(List<ImageInfo> imageInfos) {
			super();
			this.imageInfos = imageInfos;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return imageInfos.size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "ImageId";
			} else if (columnIndex == 1) {
				return "Image";
			} else {
				return null;
			}
		}
		
		

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return String.class;
			} else if (columnIndex == 1) {
				return ImageIcon.class;
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return imageInfos.get(rowIndex).getImageId().name();
			} else if (columnIndex == 1) {
				return new ImageIcon(imageInfos.get(rowIndex).getImage());
			} else {
				return null;
			}
		}
	}
}