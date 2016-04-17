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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class CommunityDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tlbChildren;
	private JTable tblAcquaintances;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CommunityDialog dialog = new CommunityDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CommunityDialog() {
		setBounds(100, 100, 542, 705);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblMate = new JLabel("Mate:");
			lblMate.setBounds(12, 13, 109, 16);
			contentPanel.add(lblMate);
		}
		
		JLabel lblMateValue = new JLabel("<no mate>");
		lblMateValue.setBounds(149, 13, 180, 16);
		contentPanel.add(lblMateValue);
		
		JLabel lblChildren = new JLabel("Children:");
		lblChildren.setBounds(12, 42, 109, 16);
		contentPanel.add(lblChildren);
		
		tlbChildren = new JTable();
		tlbChildren.setBounds(149, 43, 305, 207);
		contentPanel.add(tlbChildren);
		
		JLabel lblAcquaintances = new JLabel("Acquaintances:");
		lblAcquaintances.setBounds(12, 288, 109, 16);
		contentPanel.add(lblAcquaintances);
		
		tblAcquaintances = new JTable();
		tblAcquaintances.setBounds(149, 289, 305, 321);
		contentPanel.add(tblAcquaintances);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
