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
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
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
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JProgressBarFactory;
import org.worldgrower.gui.util.JScrollPaneFactory;
import org.worldgrower.gui.util.JTableFactory;
import org.worldgrower.gui.util.JTreeFactory;
import org.worldgrower.profession.Profession;

public class CommunityDialog extends JDialog {

	private static final String ORGANIZATIONS_KEY = "organizations";
	private static final String RANKS_KEY = "ranks";
	private static final String ACQUAINTANCES_KEY = "acquaintances";
	private static final String FAMILY_KEY = "family";
	private static final String DEITIES_KEY = "deities";
	private final JPanel contentPanel;
	private JTable tlbChildren;
	private JTable tblAcquaintances;
	private final ImageInfoReader imageInfoReader;
	
	public CommunityDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, JFrame parentFrame) {
		this.imageInfoReader = imageInfoReader;
		
		contentPanel = new TiledImagePanel(imageInfoReader);
		setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(this);
		setResizable(false);
		setTitle("Community Overview");
		setCursor(Cursors.CURSOR);
		
		int width = 622;
		int height = 685;
		setSize(width, height);
		getContentPane().setPreferredSize(getSize());
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setUndecorated(true);

		int infoPanelWidth = 575;
		int infoPanelHeight = 575;
		CardLayout cardLayout = new CardLayout();
		
		JPanel infoPanel = createInfoPanel(infoPanelWidth, infoPanelHeight, cardLayout);
		
		createToggleButtonPanel(soundIdReader, infoPanelWidth, cardLayout, infoPanel);
		
		createFamilyPanel(playerCharacter, imageInfoReader, world, infoPanelWidth, infoPanelHeight, infoPanel);
		createAcquaintancesPanel(playerCharacter, imageInfoReader, world, infoPanelWidth, infoPanelHeight, infoPanel);
		createButtonPanel(imageInfoReader, width, height);

		createRanksPanel(playerCharacter, world, infoPanelWidth, infoPanelHeight, infoPanel);		
		createOrganizationsPanel(playerCharacter, world, infoPanelWidth, infoPanelHeight, infoPanel);
		createDeitiesPanel(world, infoPanelWidth, infoPanelHeight, infoPanel);

		SwingUtils.installEscapeCloseOperation(this);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}

	private JPanel createInfoPanel(int infoPanelWidth, int infoPanelHeight, CardLayout cardLayout) {
		JPanel infoPanel = JPanelFactory.createBorderlessPanel();
		infoPanel.setBounds(15, 50, infoPanelWidth, infoPanelHeight);
		infoPanel.setLayout(cardLayout);
		contentPanel.add(infoPanel);
		return infoPanel;
	}

	private void createToggleButtonPanel(SoundIdReader soundIdReader, int infoPanelWidth, CardLayout cardLayout, JPanel infoPanel) {
		JPanel toggleButtonPanel = JPanelFactory.createBorderlessPanel();
		toggleButtonPanel.setBounds(5, 5, infoPanelWidth, 40);
		toggleButtonPanel.setLayout(new FlowLayout());
		contentPanel.add(toggleButtonPanel);

		ButtonGroup buttonGroup = new ButtonGroup();
		
		JToggleButton familyButton = createToggleButton("Family", FAMILY_KEY, soundIdReader, cardLayout, infoPanel, buttonGroup, toggleButtonPanel);
	
		createToggleButton("Acquaintances", ACQUAINTANCES_KEY, soundIdReader, cardLayout, infoPanel, buttonGroup, toggleButtonPanel);
		createToggleButton("Player Character Ranks", RANKS_KEY, soundIdReader, cardLayout, infoPanel, buttonGroup, toggleButtonPanel);
		createToggleButton("Organizations", ORGANIZATIONS_KEY, soundIdReader, cardLayout, infoPanel, buttonGroup, toggleButtonPanel);
		createToggleButton("Deities", DEITIES_KEY, soundIdReader, cardLayout, infoPanel, buttonGroup, toggleButtonPanel);
		
		buttonGroup.setSelected(familyButton.getModel(), true);
	}

	private void createFamilyPanel(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, int infoPanelWidth, int infoPanelHeight, JPanel infoPanel) {
		JPanel familyPanel = JPanelFactory.createJPanel("Family");
		familyPanel.setLayout(null);
		familyPanel.setBounds(12, 13, infoPanelWidth + 5, infoPanelHeight);
		infoPanel.add(familyPanel, FAMILY_KEY);
		
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
		scrollPaneChildren.setBounds(13, 100, 525, 420);
		familyPanel.add(scrollPaneChildren);
		SwingUtils.makeTransparant(tlbChildren, scrollPaneChildren);
	}

	private void createAcquaintancesPanel(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, int infoPanelWidth, int infoPanelHeight, JPanel infoPanel) {
		JPanel acquaintancesPanel = JPanelFactory.createJPanel("Acquaintances");
		acquaintancesPanel.setLayout(null);
		acquaintancesPanel.setBounds(12, 363, infoPanelWidth + 5, infoPanelHeight);
		infoPanel.add(acquaintancesPanel, ACQUAINTANCES_KEY);
		
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
		scrollPaneAcquaintances.setBounds(12, 30, 525, 420);
		acquaintancesPanel.add(scrollPaneAcquaintances);
		SwingUtils.makeTransparant(tblAcquaintances, scrollPaneAcquaintances);
	}

	private void createButtonPanel(ImageInfoReader imageInfoReader, int width, int height) {
		JPanel buttonPane = JPanelFactory.createBorderlessPanel();
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane);

		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		buttonPane.setBounds(0, height - 55, width - 26, 50);
		okButton.addActionListener(new CloseDialogAction());
		getRootPane().setDefaultButton(okButton);
	}

	private void createRanksPanel(WorldObject playerCharacter, World world, int infoPanelWidth, int infoPanelHeight, JPanel infoPanel) {
		JPanel ranksPanel = JPanelFactory.createJPanel("Player Character Ranks");
		ranksPanel.setLayout(null);
		ranksPanel.setBounds(510, 13, infoPanelWidth + 5, infoPanelHeight);
		infoPanel.add(ranksPanel, RANKS_KEY);
		
		OrganizationsModel worldModel = new OrganizationsModel(playerCharacter, world);
		JTable organizationsTable = JTableFactory.createJTable(worldModel);
		organizationsTable.setRowHeight(30);
		JScrollPane scrollPane = JScrollPaneFactory.createScrollPane(organizationsTable);
		scrollPane.setBounds(15, 30, 525, 420);
		ranksPanel.add(scrollPane);
		SwingUtils.makeTransparant(organizationsTable, scrollPane);
	}

	private void createOrganizationsPanel(WorldObject playerCharacter, World world, int infoPanelWidth, int infoPanelHeight, JPanel infoPanel) {
		JPanel organizationsPanel = JPanelFactory.createJPanel("Organizations");
		organizationsPanel.setLayout(null);
		organizationsPanel.setBounds(510, 363, infoPanelWidth + 5, infoPanelHeight);
		infoPanel.add(organizationsPanel, ORGANIZATIONS_KEY);
		
		JTree tree = JTreeFactory.createJTree(createRootNode(playerCharacter, world));
		tree.setRootVisible(false);
		expandAllNodes(tree, 0, tree.getRowCount());
		tree.setCellRenderer(new OrganizationMemberRenderer());
		JScrollPane treeView = JScrollPaneFactory.createScrollPane(tree);
		treeView.setBounds(15, 30, 525, 420);
		organizationsPanel.add(treeView);
	}
	
	private void createDeitiesPanel(World world, int infoPanelWidth, int infoPanelHeight, JPanel infoPanel) {
		JPanel deitiesPanel = JPanelFactory.createJPanel("Deities");
		deitiesPanel.setLayout(null);
		deitiesPanel.setBounds(510, 363, infoPanelWidth + 5, infoPanelHeight);
		infoPanel.add(deitiesPanel, DEITIES_KEY);
		
		for(int i=0; i<Deity.ALL_DEITIES.size(); i++) {
			Deity deity = Deity.ALL_DEITIES.get(i);
			Image image = imageInfoReader.getImage(deity.getBoonImageId(), null);
			JLabel nameLabel = JLabelFactory.createJLabel(deity.getName(), image);
			nameLabel.setBounds(15, 30 + 40 * i, 150, 50);
			nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
			deitiesPanel.add(nameLabel);
			
			JProgressBar relationshipProgresBar = JProgressBarFactory.createHorizontalJProgressBar(-1000, 1000, imageInfoReader);
			relationshipProgresBar.setBounds(175, 40 + 40 * i, 300, 30);
			relationshipProgresBar.setValue(500); //TODO
			relationshipProgresBar.setToolTipText("deity hapiness indicator: if a deity becomes unhappy, they may lash out against the population");
			deitiesPanel.add(relationshipProgresBar);
		}
	}
	
	private JToggleButton createToggleButton(String label, String panelKey, SoundIdReader soundIdReader, CardLayout cardLayout, JPanel infoPanel, ButtonGroup buttonGroup, JPanel toggleButtonPanel) {
		JToggleButton button = JButtonFactory.createToggleButton(label, soundIdReader);
		button.addActionListener(e -> cardLayout.show(infoPanel, panelKey));
		button.setOpaque(false);
		buttonGroup.add(button);
		toggleButtonPanel.add(button);
		return button;
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
			this.setOpaque(false);
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
        
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
	        WorldObject worldObject = (WorldObject) node.getUserObject();
	        
            setIcon(new ImageIcon(imageInfoReader.getImage(worldObject.getProperty(Constants.IMAGE_ID), null)));
            setText(worldObject.getProperty(Constants.NAME));
            
            setForeground(ColorPalette.FOREGROUND_COLOR);
            
	        return this;
	    }
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
