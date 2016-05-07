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

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.util.ButtonFactory;
import org.worldgrower.gui.util.JTableFactory;
import org.worldgrower.gui.util.JTreeFactory;

public class GuiShowOrganizationsAction extends AbstractAction {
	private final WorldObject playerCharacter;
	private final World world;
	private final WorldPanel parent;
	private final ImageInfoReader imageInfoReader;
	
	public GuiShowOrganizationsAction(WorldObject playerCharacter, World world, WorldPanel parent, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.parent = parent;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		OrganizationsDialog dialog = new OrganizationsDialog(400, 800);
		
		OrganizationsModel worldModel = new OrganizationsModel(playerCharacter, world);
		JTable table = JTableFactory.createJTable(worldModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(15, 15, 365, 200);
		dialog.addComponent(scrollPane);
		
		JTree tree = JTreeFactory.createJTree(createRootNode(playerCharacter, world));
		tree.setRootVisible(false);
		expandAllNodes(tree, 0, tree.getRowCount());
		tree.setCellRenderer(new OrganizationMemberRenderer());
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setBounds(15, 230, 365, 490);
		dialog.addComponent(treeView);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 725, 385, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.addComponent(buttonPane);
		
		JButton okButton = ButtonFactory.createButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, dialog);
		dialog.getRootPane().setDefaultButton(okButton);
		
		SwingUtils.makeTransparant(table, scrollPane);
		
		dialog.setVisible(true);
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
	
	private static class OrganizationsDialog extends AbstractDialog {

		public OrganizationsDialog(int width, int height) {
			super(width, height);
		}
	}
}