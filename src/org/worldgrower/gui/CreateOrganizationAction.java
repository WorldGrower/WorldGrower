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

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.OrganizationNamer;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class CreateOrganizationAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private World world;
	private JComponent parent;
	private DungeonMaster dungeonMaster;
	
	public CreateOrganizationAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, JComponent parent, DungeonMaster dungeonMaster) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] professionNames = Professions.getDescriptions().toArray(new String[0]);
		String professionName = (String) JOptionPane.showInputDialog(parent, "Choose Profession", "Choose Profession", JOptionPane.QUESTION_MESSAGE, null, professionNames, professionNames[0]);
		Profession profession = Professions.getProfessionByDescription(professionName);
		int professionIndex = Professions.indexOf(profession);
		
		String[] organizationNames = new OrganizationNamer().getNames(profession, world).toArray(new String[0]);
		String organizationName = (String) JOptionPane.showInputDialog(parent, "Choose Organization name", "Choose Organization name", JOptionPane.QUESTION_MESSAGE, null, organizationNames, organizationNames[0]);
		
		int indexOfOrganization = Arrays.asList(organizationNames).indexOf(organizationName);
		
		Main.executeAction(playerCharacter, Actions.CREATE_ORGANIZATION_ACTION, new int[] { professionIndex, indexOfOrganization}, world, dungeonMaster, playerCharacter, parent);
	}
}