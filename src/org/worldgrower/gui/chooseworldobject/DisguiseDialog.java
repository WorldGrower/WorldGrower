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
package org.worldgrower.gui.chooseworldobject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.TiledImagePanel;
import org.worldgrower.gui.WorldObjectList;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.debug.PropertiesModel;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JRadioButtonFactory;
import org.worldgrower.gui.util.JTableFactory;

public class DisguiseDialog extends JDialog {

	private final JPanel contentPanel;
	private JTable table;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton noDisguiseRadioButton;
	private JRadioButton chooseExistingPersonRadioButton;
	private JRadioButton createNewPersonRadioButton;
	private WorldObjectList personList;
	private JButton okButton;
	
	private WorldObject playerCharacter;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private ManagedOperation disguiseAction;
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;

	public DisguiseDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, List<WorldObject> disguiseWorldObjects, WorldPanel parent, World world, DungeonMaster dungeonMaster, ManagedOperation disguiseAction, JFrame parentFrame) {
		this.contentPanel = new TiledImagePanel(imageInfoReader);
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.disguiseAction = disguiseAction;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		
		initializeGui(parent, disguiseWorldObjects, imageInfoReader, parentFrame);
		
		transferDataToScreen(playerCharacter, disguiseWorldObjects);
		handleActions();
	}

	private void transferDataToScreen(WorldObject playerCharacter, List<WorldObject> disguiseWorldObjects) {
		WorldObject facade = playerCharacter.getProperty(Constants.FACADE);
		
		if (facade == null) {
			noDisguiseRadioButton.setSelected(true);
			table.setModel(new PropertiesModel(playerCharacter));
		} else if (facade.getProperty(Constants.ID) != null) {
			chooseExistingPersonRadioButton.setSelected(true);
			
			int facadeId = facade.getProperty(Constants.ID);
			int selectedIndex = getSelectedIndex(disguiseWorldObjects, facadeId);
			personList.setSelectedIndex(selectedIndex);
			table.setModel(new PropertiesModel(playerCharacter));
			
		} else {
			createNewPersonRadioButton.setSelected(true);
			table.setModel(new PropertiesModel(facade));
		}
		
		if (personList.getSelectedIndex() == -1) {
			personList.setSelectedIndex(0);
		}
	}

	private int getSelectedIndex(List<WorldObject> disguiseWorldObjects, int facadeId) {
		int selectedIndex = 0;
		for(WorldObject disguiseWorldObject : disguiseWorldObjects) {
			if (disguiseWorldObject.getProperty(Constants.ID) == facadeId) {
				break;
			}
			selectedIndex++;
		}
		return selectedIndex;
	}

	private void initializeGui(Component parent, List<WorldObject> disguiseWorldObjects, ImageInfoReader imageInfoReader, JFrame parentFrame) {
		int width = 644;
		int height = 502;
		setBounds(100, 100, width, height);
		contentPanel.setPreferredSize(getSize());
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setUndecorated(true);
		IconUtils.setIcon(this);
		setCursor(Cursors.CURSOR);
		
		noDisguiseRadioButton = JRadioButtonFactory.createJRadioButton("No disguise");
		noDisguiseRadioButton.setOpaque(false);
		buttonGroup.add(noDisguiseRadioButton);
		noDisguiseRadioButton.setBounds(21, 19, 231, 25);
		contentPanel.add(noDisguiseRadioButton);
		
		chooseExistingPersonRadioButton = JRadioButtonFactory.createJRadioButton("Choose existing Person:");
		chooseExistingPersonRadioButton.setOpaque(false);
		buttonGroup.add(chooseExistingPersonRadioButton);
		chooseExistingPersonRadioButton.setBounds(21, 49, 299, 25);
		contentPanel.add(chooseExistingPersonRadioButton);
		
		createNewPersonRadioButton = JRadioButtonFactory.createJRadioButton("Create new Person:");
		createNewPersonRadioButton.setOpaque(false);
		buttonGroup.add(createNewPersonRadioButton);
		createNewPersonRadioButton.setBounds(21, 239, 299, 25);
		contentPanel.add(createNewPersonRadioButton);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(340, 243, 259, 164);
		contentPanel.add(scrollPane_1);
		
		table = JTableFactory.createJTable();
		scrollPane_1.setViewportView(table);
		
		personList = new WorldObjectList(imageInfoReader, disguiseWorldObjects);
		personList.setBounds(341, 68, 173, 130);
		contentPanel.add(personList);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 415, 614, 44);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane);
		
		okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		SwingUtils.makeTransparant(table, scrollPane_1);
		
		// temporary disable
		createNewPersonRadioButton.setEnabled(false);
		table.setEnabled(false);
		
		this.setLocationRelativeTo(parent);
		SwingUtils.installEscapeCloseOperation(this);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}

	public void showMe() {
		this.setVisible(true);
	}
	

	private void handleActions() {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WorldObject facade = playerCharacter.getProperty(Constants.FACADE);
				int[] args = null;
				if (noDisguiseRadioButton.isSelected()) {
					facade = null;
					args = new int[] { -1 };
				} else if (chooseExistingPersonRadioButton.isSelected()) {
					WorldObject selectedPerson = personList.getSelectedValue();
					args = new int[] { selectedPerson.getProperty(Constants.ID) };
					
					facade = selectedPerson.deepCopy();
				} else if (createNewPersonRadioButton.isSelected()) {
					Map<ManagedProperty<?>, Object> properties = new HashMap<ManagedProperty<?>, Object>();
					
					args = new int[] { -1 };
					
					facade = new WorldObjectImpl(properties);
				}
				
				playerCharacter.setProperty(Constants.FACADE, facade);
				
				Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, disguiseAction, args, world, dungeonMaster, playerCharacter, parent, imageInfoReader, soundIdReader);
				
				DisguiseDialog.this.dispose();
			}
		});
		
		personList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				chooseExistingPersonRadioButton.setSelected(true);
			}
		});
	}
}
