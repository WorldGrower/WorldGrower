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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JTableFactory;
import org.worldgrower.profession.Professions;

public class CommunityDialog extends JDialog {

	private final JPanel contentPanel = new GradientPanel();
	private JTable tlbChildren;
	private JTable tblAcquaintances;
	
	public static void main(String[] args) {
		try {
			ImageInfoReader imageInfoReader = new ImageInfoReader();
			World world = new WorldImpl(0, 0, null, null);
			WorldObject pc = TestUtils.createIntelligentWorldObject(2, "Test");
			pc.setProperty(Constants.CHILDREN, new IdList());
			
			WorldObject child = TestUtils.createIntelligentWorldObject(3, "Child");
			child.setProperty(Constants.IMAGE_ID, ImageIds.FEMALE_RED_EYE);
			pc.getProperty(Constants.CHILDREN).add(child);
			world.addWorldObject(child);
			
			WorldObject relative = TestUtils.createIntelligentWorldObject(3, "Relative");
			relative.setProperty(Constants.IMAGE_ID, ImageIds.BLACK_HAIRED_FEMALE_COMMONER);
			pc.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(relative, Constants.PROFESSION, Professions.FARMER_PROFESSION);
			world.addWorldObject(relative);
			
			CommunityDialog dialog = new CommunityDialog(pc, imageInfoReader, world);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CommunityDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(this);
		setResizable(false);
		
		setSize(542, 705);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblMate = JLabelFactory.createJLabel("Mate:");
		lblMate.setBounds(12, 13, 109, 16);
		contentPanel.add(lblMate);
				
		JLabel lblMateValue = JLabelFactory.createJLabel("<no mate>");
		Integer mateId = playerCharacter.getProperty(Constants.MATE_ID);
		if (mateId != null) {
			WorldObject mate = world.findWorldObject(Constants.ID, mateId);
			lblMateValue.setIcon(new ImageIcon(imageInfoReader.getImage(mate.getProperty(Constants.IMAGE_ID), null)));
			lblMateValue.setText(mate.getProperty(Constants.NAME));
		}
		
		lblMateValue.setBounds(149, 13, 180, 16);
		contentPanel.add(lblMateValue);
		
		JLabel lblChildren = JLabelFactory.createJLabel("Children:");
		lblChildren.setBounds(12, 42, 109, 16);
		contentPanel.add(lblChildren);
		
		tlbChildren = JTableFactory.createJTable(new ChildrenTableModel(playerCharacter, world));
		tlbChildren.setDefaultRenderer(ImageIds.class, new ImageRenderer(imageInfoReader));
		tlbChildren.setRowHeight(50);
		JScrollPane scrollPaneChildren = new JScrollPane(tlbChildren);
		scrollPaneChildren.setBounds(149, 43, 305, 207);
		contentPanel.add(scrollPaneChildren);
		SwingUtils.makeTransparant(tlbChildren, scrollPaneChildren);
		
		JLabel lblAcquaintances = JLabelFactory.createJLabel("Acquaintances:");
		lblAcquaintances.setBounds(12, 288, 109, 16);
		contentPanel.add(lblAcquaintances);
		
		tblAcquaintances = JTableFactory.createJTable(new AcquaintancesTableModel(playerCharacter, world));
		tblAcquaintances.setDefaultRenderer(ImageIds.class, new ImageRenderer(imageInfoReader));
		tblAcquaintances.setRowHeight(50);
		JScrollPane scrollPaneAcquaintances = new JScrollPane(tblAcquaintances);
		scrollPaneAcquaintances.setBounds(149, 289, 305, 321);
		contentPanel.add(scrollPaneAcquaintances);
		SwingUtils.makeTransparant(tblAcquaintances, scrollPaneAcquaintances);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		okButton.addActionListener(new CloseDialogAction());
		getRootPane().setDefaultButton(okButton);

		SwingUtils.installEscapeCloseOperation(this);
	}
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CommunityDialog.this.dispose();
		}
	}
	
	private static class ChildrenTableModel extends AbstractTableModel {

		private final List<WorldObject> children;
		
		public ChildrenTableModel(WorldObject playerCharacter, World world) {
			super();
			IdList childrenIds = playerCharacter.getProperty(Constants.CHILDREN);
			
			this.children = new ArrayList<>();
			for(int childId : childrenIds.getIds()) {
				children.add(world.findWorldObject(Constants.ID, childId));
			}
		}

		@Override
		public int getRowCount() {
			return children.size();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Image";
			} else if (columnIndex == 1) {
				return "Name";
			} else {
				return null;
			}
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return ImageIds.class;
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return children.get(rowIndex).getProperty(Constants.IMAGE_ID);
			} else if (columnIndex == 1) {
				return children.get(rowIndex).getProperty(Constants.NAME);
			}
			return null;
		}
	}
	
	private static class AcquaintancesTableModel extends AbstractTableModel {

		private final List<WorldObject> acquaintances;
		
		public AcquaintancesTableModel(WorldObject playerCharacter, World world) {
			super();
			this.acquaintances = new ArrayList<>();
			
			IdMap relationshipMap = playerCharacter.getProperty(Constants.RELATIONSHIPS);
			for(int id : relationshipMap.getIds()) {
				acquaintances.add(world.findWorldObject(Constants.ID, id));
			}
			
			KnowledgeMap knowledgeMap = playerCharacter.getProperty(Constants.KNOWLEDGE_MAP);
			for(int id : knowledgeMap.getIds()) {
				WorldObject acquaintance = world.findWorldObject(Constants.ID, id);
				if (!acquaintances.contains(acquaintance)) {
					acquaintances.add(acquaintance);
				}
			}
		}

		@Override
		public int getRowCount() {
			return acquaintances.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return ImageIds.class;
			}
			return super.getColumnClass(columnIndex);
		}

		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Image";
			} else if (columnIndex == 1) {
				return "Name";
			} else if (columnIndex == 2) {
				return "Profession";
			} else if (columnIndex == 3) {
				return "Deity";
			} else {
				return null;
			}
		}

		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return acquaintances.get(rowIndex).getProperty(Constants.IMAGE_ID);
			} else if (columnIndex == 1) {
				return acquaintances.get(rowIndex).getProperty(Constants.NAME);
			} else if (columnIndex == 2) {
				return acquaintances.get(rowIndex).getProperty(Constants.PROFESSION);
			} else if (columnIndex == 3) {
				return acquaintances.get(rowIndex).getProperty(Constants.DEITY);
			}
			return null;
		}
	}
	
	private static class ImageRenderer extends DefaultTableCellRenderer {
	    private final ImageInfoReader imageInfoReader;

	    public ImageRenderer(ImageInfoReader imageInfoReader) {
			super();
			this.imageInfoReader = imageInfoReader;
		}


		public void setValue(Object value) {
	       ImageIds imageId = (ImageIds) value;
	       setIcon(new ImageIcon(imageInfoReader.getImage(imageId, null)));
	    }
	}
}
