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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.World;
import org.worldgrower.gui.AnimationPainter.AnimationDescription;
import org.worldgrower.gui.WorldPanel;

public class GuiShowExplorationOverviewAction extends AbstractAction {
	private WorldPanel worldPanel;
	private World world;
	
	public GuiShowExplorationOverviewAction(WorldPanel worldPanel, World world) {
		super();
		this.worldPanel = worldPanel;
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		JPanel contentPanel = new JPanel(new FlowLayout());
		frame.add(contentPanel);
		
		int screenWidth = worldPanel.getWidth() / 48;
	    int screenHeight = worldPanel.getHeight() / 48;
		JLabel infoLabel = new JLabel("offSetX=" + worldPanel.getScreenX(0) + ",OffSetY="+worldPanel.getScreenY(0) + ",screenWidth=" + screenWidth + ",screenHeight=" + screenHeight);
		contentPanel.add(infoLabel);
		
		JButton isWorldObjectExploredButton = new JButton("WorldObject Explored?");
		contentPanel.add(isWorldObjectExploredButton);
		isWorldObjectExploredButton.addActionListener(ev -> showWorldObjectExploredGui());
		
		JButton isTileExploredButton = new JButton("Tile Explored?");
		contentPanel.add(isTileExploredButton);
		isTileExploredButton.addActionListener(ev -> showTileExploredGui());
		
		JTable table = new JTable(new AnimationModel(worldPanel.getAnimatedWorldObjects()));
		contentPanel.add(new JScrollPane(table));
		
		frame.setBounds(100,  100, 500, 800);
		frame.setVisible(true);
	}
	
	private class AnimationModel extends AbstractTableModel {
		
		private final List<AnimationDescription> animatedWorldObjects;
		
		public AnimationModel(List<AnimationDescription> animatedWorldObjects) {
			this.animatedWorldObjects = animatedWorldObjects;
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return animatedWorldObjects.size();
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Name";
			} else if (columnIndex == 1) {
				return "X";
			} else if (columnIndex == 2) {
				return "Y";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return animatedWorldObjects.get(rowIndex).getName();
			} else if (columnIndex == 1) {
				return animatedWorldObjects.get(rowIndex).getX();
			} else if (columnIndex == 2) {
				return animatedWorldObjects.get(rowIndex).getY();
			} else {
				return null;
			}
		}
	}
	
	private void showWorldObjectExploredGui() {
		int x = Integer.parseInt(JOptionPane.showInputDialog("X?"));
		int y = Integer.parseInt(JOptionPane.showInputDialog("Y?"));
		int width = Integer.parseInt(JOptionPane.showInputDialog("width?"));
		int height = Integer.parseInt(JOptionPane.showInputDialog("height?"));
		JOptionPane.showMessageDialog(null, "WorldObject isExplored=" + world.getTerrain().isExplored(x, y, width, height));
	}
	
	private void showTileExploredGui() {
		int x = Integer.parseInt(JOptionPane.showInputDialog("X?"));
		int y = Integer.parseInt(JOptionPane.showInputDialog("Y?"));
		JOptionPane.showMessageDialog(null, "Tile isExplored=" + world.getTerrain().isExplored(x, y));
	}
}