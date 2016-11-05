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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.BountyPropertyUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JScrollPaneFactory;
import org.worldgrower.gui.util.JTableFactory;
import org.worldgrower.gui.util.JTreeFactory;
import org.worldgrower.profession.Profession;

public class CommunityDialog extends JDialog {

	private final JPanel contentPanel;
	private JTable tlbChildren;
	private JTable tblAcquaintances;
	private final ImageInfoReader imageInfoReader;
	
	public CommunityDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, JFrame parentFrame) {
		this.imageInfoReader = imageInfoReader;
		
		contentPanel = new TiledImagePanel(imageInfoReader);
		setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(this);
		setResizable(false);
		setTitle("Community Overview");
		setCursor(Cursors.CURSOR);
		
		int width = 1022;
		int height = 785;
		setSize(width, height);
		getContentPane().setPreferredSize(getSize());
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setUndecorated(true);
		
		JPanel familyPanel = JPanelFactory.createJPanel("Family");
		familyPanel.setLayout(null);
		familyPanel.setBounds(12, 13, 480, 340);
		contentPanel.add(familyPanel);
		
		JLabel lblMate = JLabelFactory.createJLabel("Mate:");
		lblMate.setBounds(12, 30, 109, 16);
		familyPanel.add(lblMate);
				
		JLabel lblMateValue = JLabelFactory.createJLabel("<no mate>");
		Integer mateId = playerCharacter.getProperty(Constants.MATE_ID);
		if (mateId != null) {
			WorldObject mate = world.findWorldObjectById(mateId);
			lblMateValue.setIcon(new ImageIcon(imageInfoReader.getImage(mate.getProperty(Constants.IMAGE_ID), null)));
			lblMateValue.setText(mate.getProperty(Constants.NAME));
		}
		
		lblMateValue.setBounds(149, 30, 180, 16);
		familyPanel.add(lblMateValue);
		
		JLabel lblChildren = JLabelFactory.createJLabel("Children:");
		lblChildren.setBounds(12, 60, 109, 16);
		familyPanel.add(lblChildren);
		
		tlbChildren = JTableFactory.createJTable(new ChildrenTableModel(playerCharacter, world));
		tlbChildren.setDefaultRenderer(ImageIds.class, new ImageTableRenderer(imageInfoReader));
		tlbChildren.setRowHeight(50);
		tlbChildren.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tlbChildren.setAutoCreateRowSorter(true);
		tlbChildren.getColumnModel().getColumn(0).setPreferredWidth(50);
		tlbChildren.getColumnModel().getColumn(1).setPreferredWidth(405);
		JScrollPane scrollPaneChildren = JScrollPaneFactory.createScrollPane(tlbChildren);
		scrollPaneChildren.setBounds(13, 80, 455, 241);
		familyPanel.add(scrollPaneChildren);
		SwingUtils.makeTransparant(tlbChildren, scrollPaneChildren);
		
		JPanel acquaintancesPanel = JPanelFactory.createJPanel("Acquaintances");
		acquaintancesPanel.setLayout(null);
		acquaintancesPanel.setBounds(12, 363, 480, 365);
		contentPanel.add(acquaintancesPanel);
		
		tblAcquaintances = JTableFactory.createJTable(new AcquaintancesTableModel(playerCharacter, world));
		tblAcquaintances.setDefaultRenderer(ImageIds.class, new ImageTableRenderer(imageInfoReader));
		tblAcquaintances.setRowHeight(50);
		tblAcquaintances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblAcquaintances.setAutoCreateRowSorter(true);
		tblAcquaintances.getColumnModel().getColumn(0).setPreferredWidth(50);
		tblAcquaintances.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblAcquaintances.getColumnModel().getColumn(2).setPreferredWidth(100);
		tblAcquaintances.getColumnModel().getColumn(3).setPreferredWidth(100);
		tblAcquaintances.getColumnModel().getColumn(4).setPreferredWidth(100);
		JScrollPane scrollPaneAcquaintances = JScrollPaneFactory.createScrollPane(tblAcquaintances);
		scrollPaneAcquaintances.setBounds(12, 30, 455, 321);
		acquaintancesPanel.add(scrollPaneAcquaintances);
		SwingUtils.makeTransparant(tblAcquaintances, scrollPaneAcquaintances);
		
		JPanel buttonPane = JPanelFactory.createBorderlessPanel();
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		buttonPane.setBounds(0, height - 50, width - 15, 50);
		okButton.addActionListener(new CloseDialogAction());
		getRootPane().setDefaultButton(okButton);

		JPanel ranksPanel = JPanelFactory.createJPanel("Player Character Ranks");
		ranksPanel.setLayout(null);
		ranksPanel.setBounds(510, 13, 480, 335);
		contentPanel.add(ranksPanel);
		
		OrganizationsModel worldModel = new OrganizationsModel(playerCharacter, world);
		JTable table = JTableFactory.createJTable(worldModel);
		JScrollPane scrollPane = JScrollPaneFactory.createScrollPane(table);
		scrollPane.setBounds(15, 30, 450, 290);
		ranksPanel.add(scrollPane);
		
		JPanel organizationsPanel = JPanelFactory.createJPanel("Organizations");
		organizationsPanel.setLayout(null);
		organizationsPanel.setBounds(510, 363, 480, 365);
		contentPanel.add(organizationsPanel);
		
		JTree tree = JTreeFactory.createJTree(createRootNode(playerCharacter, world));
		tree.setRootVisible(false);
		expandAllNodes(tree, 0, tree.getRowCount());
		tree.setCellRenderer(new OrganizationMemberRenderer());
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setBounds(15, 30, 450, 321);
		organizationsPanel.add(treeView);
		
		SwingUtils.makeTransparant(table, scrollPane);
		
		SwingUtils.installEscapeCloseOperation(this);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
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
				children.add(world.findWorldObjectById(childId));
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

		private final List<WorldObject> acquaintances = new ArrayList<>();
		private final List<Profession> professions = new ArrayList<>();
		private final List<Deity> deities = new ArrayList<>();
		private final List<String> bounties = new ArrayList<>();
		
		public AcquaintancesTableModel(WorldObject playerCharacter, World world) {
			super();
			
			IdMap relationshipMap = playerCharacter.getProperty(Constants.RELATIONSHIPS);
			for(int id : relationshipMap.getIds()) {
				acquaintances.add(world.findWorldObjectById(id));
			}
			
			KnowledgeMap knowledgeMap = playerCharacter.getProperty(Constants.KNOWLEDGE_MAP);
			for(int id : knowledgeMap.getIds()) {
				WorldObject acquaintance = world.findWorldObjectById(id);
				if (acquaintance.hasIntelligence() && !acquaintances.contains(acquaintance)) {
					acquaintances.add(acquaintance);
				}
			}
			
			for(WorldObject acquaintance : acquaintances) {
				int id = acquaintance.getProperty(Constants.ID);
				professions.add(knowledgeMap.getProperty(id, Constants.PROFESSION));
				deities.add(knowledgeMap.getProperty(id, Constants.DEITY));
				
				int bounty = BountyPropertyUtils.getBounty(acquaintance, world);
				final String bountyValue;
				if (bounty > 0) {
					bountyValue = Integer.toString(bounty);
				} else {
					bountyValue = "";
				}
				bounties.add(bountyValue);
			}
		}

		@Override
		public int getRowCount() {
			return acquaintances.size();
		}

		@Override
		public int getColumnCount() {
			return 5;
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
			} else if (columnIndex == 4) {
				return "Bounty";
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
				return professions.get(rowIndex) != null ? professions.get(rowIndex).getDescription() : null;
			} else if (columnIndex == 3) {
				return deities.get(rowIndex) != null ? deities.get(rowIndex).getName() : null;
			} else if (columnIndex == 4) {
				return bounties.get(rowIndex);
			}
			return null;
		}
	}
	
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
	    for(int i=startingIndex;i<rowCount;++i){
	        tree.expandRow(i);
	    }

	    if(tree.getRowCount()!=rowCount){
	        expandAllNodes(tree, rowCount, tree.getRowCount());
	    }
	}
	
	private DefaultMutableTreeNode createRootNode(WorldObject playerCharacter, World world) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Organizations");
		
		for(WorldObject organization : GroupPropertyUtils.getAllOrganizations(world)) {
			DefaultMutableTreeNode organizationNode = new DefaultMutableTreeNode(organization);
			
			List<WorldObject> members = GroupPropertyUtils.findOrganizationMembers(organization, world);
			for(WorldObject member : members) {
				if (isMemberKnownByPlayerCharacter(member, playerCharacter)) {
					DefaultMutableTreeNode memberNode = new DefaultMutableTreeNode(member);
					organizationNode.add(memberNode);
				}
			}
			if (organizationNode.getChildCount() > 0) {
				top.add(organizationNode);
			}
		}
		
		return top;
	}
	
	private static boolean isMemberKnownByPlayerCharacter(WorldObject member, WorldObject playerCharacter) {
		return playerCharacter.getProperty(Constants.RELATIONSHIPS).contains(member)
				|| playerCharacter.getProperty(Constants.KNOWLEDGE_MAP).hasKnowledge(member)
				|| playerCharacter.equals(member);
	}

	class OrganizationMemberRenderer extends DefaultTreeCellRenderer {

		public OrganizationMemberRenderer() {
			this.setBackgroundNonSelectionColor(ColorPalette.DARK_BACKGROUND_COLOR);
			this.setBackgroundSelectionColor(ColorPalette.LIGHT_BACKGROUND_COLOR);
		}
		
	    public Component getTreeCellRendererComponent(
	                        JTree tree,
	                        Object value,
	                        boolean sel,
	                        boolean expanded,
	                        boolean leaf,
	                        int row,
	                        boolean hasFocus) {

	        super.getTreeCellRendererComponent(
	                        tree, value, sel,
	                        expanded, leaf, row,
	                        hasFocus);
	        
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
	        WorldObject worldObject = (WorldObject) node.getUserObject();
	        
            setIcon(new ImageIcon(imageInfoReader.getImage(worldObject.getProperty(Constants.IMAGE_ID), null)));
            setText(worldObject.getProperty(Constants.NAME));
            setOpaque(false);
            setForeground(ColorPalette.FOREGROUND_COLOR);
            
	        return this;
	    }
	}

	private void addActionHandlers(JButton okButton, JDialog dialog) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		
	}

	private static class OrganizationsModel extends AbstractTableModel {

		private Map<String, String> organizationDescriptions;
		private List<String> organizationNames;
		
		public OrganizationsModel(WorldObject playerCharacter, World world) {
			super();
			this.organizationDescriptions = new HashMap<>();
			this.organizationNames = new ArrayList<>();
			List<WorldObject> performerOrganizations = GroupPropertyUtils.getOrganizations(playerCharacter, world);
			for(WorldObject organization : performerOrganizations) {
				final String playerCharacterStatus;
				if (GroupPropertyUtils.performerIsLeaderOfOrganization(playerCharacter, organization, world)) {
					playerCharacterStatus = "leader";
				} else {
					playerCharacterStatus = "member";
				}
				String organizationName = organization.getProperty(Constants.NAME);
				organizationDescriptions.put(organizationName, playerCharacterStatus);
				organizationNames.add(organizationName);
			}
			
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return organizationDescriptions.size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Organization Name";
			} else if (columnIndex == 1) {
				return "Player Character Status";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return organizationNames.get(rowIndex);
			} else if (columnIndex == 1) {
				return organizationDescriptions.get(organizationNames.get(rowIndex));
			} else {
				return null;
			}
		}
	}
}
