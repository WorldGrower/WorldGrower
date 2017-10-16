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

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

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
		
		JTable table = new JTable(new ImageModel(Arrays.asList(ImageIds.values())));
		table.setBounds(50, 50, 1000, 800);
		table.setRowHeight(100);
		ImageCellRenderer renderer = new ImageCellRenderer(imageInfoReader);
		table.setDefaultRenderer(ImageIcon.class, renderer);
		frame.add(new JScrollPane(table));
		
		frame.setBounds(100,  100, 900, 900);
		frame.setVisible(true);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				renderer.incrementImageIndex();
				table.repaint();
			}
		}, 0, 100);
	}
	
	private static class ImageInfo {
		private final ImageIds imageId;
		private final List<Image> images;
		
		public ImageInfo(ImageIds imageId, ImageInfoReader imageInfoReader) {
			this.imageId = imageId;
			int frameCount = imageInfoReader.getNumberOfFrames(imageId);
			if (frameCount > 1) {
				this.images = new ArrayList<>();
				for(int i=0; i<frameCount ;i++) {
					this.images.add(imageInfoReader.getImage(imageId, i));
				}
			} else {
				this.images = Arrays.asList(imageInfoReader.getImage(imageId, null));
			}
		}
		
		ImageIds getImageId() {
			return imageId;
		}
		List<Image> getImages() {
			return images;
		}
		
		
	}
	
	private class ImageModel extends AbstractTableModel {

		private List<ImageIds> imageIds;
		
		public ImageModel(List<ImageIds> imageIds) {
			super();
			this.imageIds = imageIds;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return imageIds.size();
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
				return imageIds.get(rowIndex).name();
			} else if (columnIndex == 1) {
				return imageIds.get(rowIndex);
			} else {
				return null;
			}
		}
	}
	
	static class ImageCellRenderer implements TableCellRenderer {
		private final ImageInfoReader imageInfoReader;
		private int index = 0;

		public ImageCellRenderer(ImageInfoReader imageInfoReader) {
			super();
			this.imageInfoReader = imageInfoReader;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel();
			label.setOpaque(false);
			
			ImageIds imageId = (ImageIds) value;
			ImageInfo imageInfo = new ImageInfo(imageId, imageInfoReader);
			
			if (imageInfo.getImages().size() == 1) {
				label.setIcon(new ImageIcon(imageInfo.getImages().get(0)));
			} else {
				label.setIcon(new ImageIcon(imageInfo.getImages().get(index)));
			}

			return label;
		}

		public void incrementImageIndex() {
			index = (index + 1) % 10;
		}
	}
}