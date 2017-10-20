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

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldObjectList;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.JButtonFactory;

public class ChooseWorldObjectDialog extends AbstractDialog {

	private WorldObjectList personList;
	private JButton okButton;
	
	private ActionContainingArgs guiAction;
	private WorldObject playerCharacter;
	private WorldObjectMapper worldObjectMapper;

	public ChooseWorldObjectDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, List<WorldObject> disguiseWorldObjects, Component parent, World world, DungeonMaster dungeonMaster, ActionContainingArgs guiAction, JFrame parentFrame, WorldObjectMapper worldObjectMapper) {
		super(400, 502, imageInfoReader);
		initializeGui(parent, disguiseWorldObjects, imageInfoReader, soundIdReader, parentFrame);
		
		this.guiAction = guiAction;
		this.playerCharacter = playerCharacter;
		this.worldObjectMapper = worldObjectMapper;
		
		handleActions();
	}

	private void initializeGui(Component parent, List<WorldObject> disguiseWorldObjects, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		personList = new WorldObjectList(imageInfoReader, disguiseWorldObjects);
		personList.setBounds(15, 15, 370, 420);
		this.addComponent(personList);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(5, 440, 385, 50);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		this.addComponent(buttonPane);
		
		okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		okButton.setEnabled(false);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}

	public void showMe() {
		this.setVisible(true);
	}
	

	private void handleActions() {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WorldObject selectedWorldObject = personList.getSelectedValue();
				guiAction.setArgs(worldObjectMapper.map(playerCharacter, selectedWorldObject));
				
				guiAction.actionPerformed(null);		
				ChooseWorldObjectDialog.this.dispose();
			}
		});
		
		personList.addSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				okButton.setEnabled(true);
			}
		});
	}

	public void enableReadOnlyView() {
		okButton.setEnabled(true);
	    for( ActionListener al : okButton.getActionListeners() ) {
	    	okButton.removeActionListener(al);
	    }
	    okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChooseWorldObjectDialog.this.dispose();
			}	    	
	    });
	}
}
