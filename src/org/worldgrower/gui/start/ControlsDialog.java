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
package org.worldgrower.gui.start;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JCheckBoxFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JRadioButtonFactory;
import org.worldgrower.gui.util.JTableFactory;

public class ControlsDialog extends AbstractDialog {

	private static final String MUSIC_TOOL_TIP = "Play background music";
	private static final String SOUND_TOOL_TIP = "Play sound effects";
	
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private final MusicPlayer musicPlayer;
	
	public ControlsDialog(KeyBindings keyBindings, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer) {
		super(400, 800, imageInfoReader);
		
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.musicPlayer = musicPlayer;
		
		addKeyBindingsTable(keyBindings);
		addMouseControlPanel(keyBindings);
		addSoundControlPanel();
		addButtonPane();
		
		((JComponent)getRootPane()).setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
	}

	private void addKeyBindingsTable(KeyBindings keyBindings) {
		JTable table = JTableFactory.createJTable(new ControlsTableModel(keyBindings));
		JComboBox<Character> comboBox = JComboBoxFactory.createJComboBox(new Character[]{'A', 'B', 'C'}, imageInfoReader);
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
		
        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        
        comboBox.setRenderer(new ComboBoxRenderer());
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() > -1) {
                	comboBox.setModel(new DefaultComboBoxModel<>(keyBindings.getPossibleValues(table.getSelectedRow()).toArray(new Character[0])));
                	comboBox.setSelectedItem(table.getValueAt(table.getSelectedRow(), 1));
                }
            }
        });
        
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(15, 15, 368, 400);
		addComponent(scrollPane);
		
		SwingUtils.makeTransparant(table, scrollPane);
	}
	
	static class ComboBoxRenderer extends JLabel implements ListCellRenderer<Character> {

		public ComboBoxRenderer() {
			setOpaque(false);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Character value, int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());

			return this;
		}
	}

	private void addMouseControlPanel(KeyBindings keyBindings) {
		JPanel mouseControlPanel = JPanelFactory.createJPanel("Mouse");
		
		mouseControlPanel.setOpaque(false);
		mouseControlPanel.setBounds(12, 430, 368, 150);
		mouseControlPanel.setLayout(null);
		
		JRadioButton defaultMouseControl = JRadioButtonFactory.createJRadioButton("<html>left-click: center map<br>right-click: show possible actions</html>");
		defaultMouseControl.setSelected(keyBindings.leftMouseClickCentersMap());
		defaultMouseControl.setOpaque(false);
		defaultMouseControl.setBounds(12, 20, 360, 50);
		mouseControlPanel.add(defaultMouseControl);
		
		JRadioButton alternateMouseControl = JRadioButtonFactory.createJRadioButton("<html>right-click: center map<br>left-click: show possible actions</html>");
		alternateMouseControl.setSelected(!keyBindings.leftMouseClickCentersMap());
		alternateMouseControl.setOpaque(false);
		alternateMouseControl.setBounds(12, 80, 360, 50);
		mouseControlPanel.add(alternateMouseControl);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(defaultMouseControl);
		buttonGroup.add(alternateMouseControl);
		
		addComponent(mouseControlPanel);
		
		defaultMouseControl.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				keyBindings.setLeftMouseClickCentersMap(defaultMouseControl.isSelected());
			}
		});
	}
	
	private void addSoundControlPanel() {
		JPanel soundControlPanel = JPanelFactory.createJPanel("Sound");
		
		soundControlPanel.setOpaque(false);
		soundControlPanel.setBounds(12, 600, 368, 100);
		soundControlPanel.setLayout(null);
		
		JCheckBox chkBackgroundMusic = JCheckBoxFactory.createJCheckBox("Music");
		chkBackgroundMusic.setToolTipText(MUSIC_TOOL_TIP);
		chkBackgroundMusic.setSelected(musicPlayer.isEnabled());
		chkBackgroundMusic.setOpaque(false);
		chkBackgroundMusic.setBounds(228, 20, 137, 25);
		soundControlPanel.add(chkBackgroundMusic);
		
		JLabel lblPlayBackgroundMusic = JLabelFactory.createJLabel("Play background music:");
		lblPlayBackgroundMusic.setToolTipText(MUSIC_TOOL_TIP);
		lblPlayBackgroundMusic.setBounds(12, 20, 191, 26);
		soundControlPanel.add(lblPlayBackgroundMusic);
		
		JCheckBox chkSoundEffects = JCheckBoxFactory.createJCheckBox("Sound Effects");
		chkSoundEffects.setToolTipText(SOUND_TOOL_TIP);
		chkSoundEffects.setSelected(soundIdReader.isEnabled());
		chkSoundEffects.setOpaque(false);
		chkSoundEffects.setBounds(228, 50, 137, 25);
		soundControlPanel.add(chkSoundEffects);
		
		JLabel lblPlaySoundEffects = JLabelFactory.createJLabel("Play sound effects:");
		lblPlaySoundEffects.setToolTipText(SOUND_TOOL_TIP);
		lblPlaySoundEffects.setBounds(12, 50, 191, 26);
		soundControlPanel.add(lblPlaySoundEffects);
		
		addComponent(soundControlPanel);
		
		chkBackgroundMusic.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					musicPlayer.setEnabled(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					musicPlayer.setEnabled(false);
				}
			}
		});
		
		chkSoundEffects.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					soundIdReader.setEnabled(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					soundIdReader.setEnabled(false);
				}
				
			}
		});
	}

	private void addButtonPane() {
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 740, 383, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, this);
		getRootPane().setDefaultButton(okButton);
	}
	
	public void showMe() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addActionHandlers(JButton okButton, JDialog dialog) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
	}
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ControlsDialog.this.dispose();
		}
	}
	
	private static class ControlsTableModel extends AbstractTableModel {

		private final KeyBindings keyBindings;
		
		public ControlsTableModel(KeyBindings keyBindings) {
			super();
			this.keyBindings = keyBindings;
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0) {
				return "Description";
			} else if (column == 1) {
				return "Value";
			} else {
				return null;
			}
		}
		
		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return keyBindings.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == 0) {
				return keyBindings.getDescription(row);
			} else if (column == 1) {
				return keyBindings.getValue(row);
			} else {
				return null;
			}
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			keyBindings.setValue(row, (Character) value);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1;
		}
	}
}
