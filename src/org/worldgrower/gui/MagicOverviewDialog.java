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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JTableFactory;

public class MagicOverviewDialog extends JDialog {

	private final JPanel contentPanel;

	public MagicOverviewDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		contentPanel = new TiledImagePanel(imageInfoReader);
		
		int width = 900;
		int height = 850;
		setSize(width, height);
		contentPanel.setPreferredSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setUndecorated(true);
		IconUtils.setIcon(this);
		setCursor(Cursors.CURSOR);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 871, 767);
		contentPanel.add(scrollPane);
		
		JTable magicSpellsTable = new MagicSpellsTable(new MagicSpellTableModel(playerCharacter));
		magicSpellsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		magicSpellsTable.setDefaultRenderer(SkillProperty.class, new SkillTableRenderer(imageInfoReader));
		magicSpellsTable.setDefaultRenderer(ImageIds.class, new ImageTableRenderer(imageInfoReader));
		magicSpellsTable.setRowHeight(50);
		magicSpellsTable.setAutoCreateRowSorter(true);
		magicSpellsTable.getRowSorter().toggleSortOrder(1);
		
		magicSpellsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		magicSpellsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		magicSpellsTable.getColumnModel().getColumn(1).setPreferredWidth(257);
		magicSpellsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		magicSpellsTable.getColumnModel().getColumn(3).setPreferredWidth(145);
		magicSpellsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		magicSpellsTable.getColumnModel().getColumn(5).setPreferredWidth(150);
		
		JTableFactory.applyImageToHeaderColumn(magicSpellsTable, magicSpellsTable.getColumnModel().getColumn(5), ImageIds.SMALL_TURN, imageInfoReader);
		
		scrollPane.setViewportView(magicSpellsTable);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		buttonPane.setBounds(12, 790, 878, 75);
		contentPanel.add(buttonPane);
			
		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		okButton.addActionListener(new CloseDialogAction());
		SwingUtils.installEscapeCloseOperation(this);
		
		SwingUtils.makeTransparant(magicSpellsTable, scrollPane);
		
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}
	
	
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MagicOverviewDialog.this.dispose();
		}
	}
	
	private static class MagicSpellTableModel extends AbstractTableModel {

		private final WorldObject playerCharacter;
		
		public MagicSpellTableModel(WorldObject playerCharacter) {
			super();
			this.playerCharacter = playerCharacter;
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0) {
				return "Image";
			} else if (column == 1) {
				return "Spell Name";
			} else if (column == 2) {
				return "School";
			} else if (column == 3) {
				return "Skill Level";
			} else if (column == 4) {
				return "Progress";
			} else if (column == 5) {
				return "Research Cost";
			} else {
				return null;
			}
		}
		
		@Override
		public int getColumnCount() {
			return 6;
		}

		@Override
		public int getRowCount() {
			return Actions.getMagicSpells().size();
		}

		@Override
		public Class<?> getColumnClass(int column) {
			if (column == 0) {
				return ImageIds.class;
			} else if (column == 1) {
				return String.class;
			} else if (column == 2) {
				return SkillProperty.class;
			} else if (column == 3) {
				return Integer.class;
			} else if (column == 4) {
				return String.class;
			} else if (column == 5) {
				return Integer.class;
			} else {
				return super.getColumnClass(column);
			}
		}

		@Override
		public Object getValueAt(int row, int column) {
			MagicSpell magicSpell = Actions.getMagicSpells().get(row);
			if (column == 0) {
				return magicSpell.getImageIds(playerCharacter);
			} else if (column == 1) {
				return magicSpell.getSimpleDescription();
			} else if (column == 2) {
				return magicSpell.getSkill();
			} else if (column == 3) {
				return magicSpell.getRequiredSkillLevel();
			} else if (column == 4) {
				List<ManagedOperation> knownSpells = playerCharacter.getProperty(Constants.KNOWN_SPELLS);
				PropertyCountMap<MagicSpell> studyingSpells = playerCharacter.getProperty(Constants.STUDYING_SPELLS);
				if (knownSpells.contains(magicSpell)) {
					return "Known";
				} else if (studyingSpells.count(magicSpell) > 0) {
					int percentage = (studyingSpells.count(magicSpell) * 100) / magicSpell.getResearchCost();
					return percentage + "%";
				} else {
					return "Unknown";
				}
			} else if (column == 5) {
				return magicSpell.getResearchCost();
			} else {
				return null;
			}
		}
	}
	
	private static class MagicSpellsTable extends JTable {
		
		public MagicSpellsTable(TableModel tableModel) {
			super(tableModel);
			JTableFactory.setTableProperties(this);
		}

		@Override
		public String getToolTipText(MouseEvent mouseEvent) {
			int row = convertRowIndexToModel(rowAtPoint(mouseEvent.getPoint()));
			MagicSpell magicSpell = Actions.getMagicSpells().get(row);
			return magicSpell.getDescription();
		}
		
	}
}
