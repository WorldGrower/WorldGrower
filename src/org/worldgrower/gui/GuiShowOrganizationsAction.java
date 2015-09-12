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
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.util.IconUtils;

public class GuiShowOrganizationsAction extends AbstractAction {
	private final WorldObject playerCharacter;
	private final World world;
	private final WorldPanel parent;
	
	public GuiShowOrganizationsAction(WorldObject playerCharacter, World world, WorldPanel parent) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JDialog dialog = new JDialog();
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(dialog);
		
		OrganizationsModel worldModel = new OrganizationsModel(playerCharacter, world);
		JTable table = new JTable(worldModel);
		table.setBounds(50, 50, 300, 700);
		dialog.add(new JScrollPane(table));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, dialog);
		dialog.getRootPane().setDefaultButton(okButton);
		
		dialog.setSize(400, 800);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
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