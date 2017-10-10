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
package org.worldgrower.actions;

import java.io.ObjectStreamException;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class CreateProfessionOrganizationAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int professionIndex = args[0];
		int organizationIndex = args[1];
		
		Profession profession = Professions.getAllProfessions().get(professionIndex);
		List<String> organizationNames = new OrganizationNamer().getProfessionOrganizationNames(profession, world);
		
		String organizationName = organizationNames.get(organizationIndex);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(performer.getProperty(Constants.ID), organizationName, profession, world);
		
		performer.getProperty(Constants.GROUP).add(organization);
		WorldObject facade = performer.getProperty(Constants.FACADE);
		if (facade != null && facade.getProperty(Constants.GROUP) != null) {
			facade.getProperty(Constants.GROUP).add(organization);
		}
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}
	
	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating a profession organization";
	}
	
	@Override
	public String getSimpleDescription() {
		return "create profession organization";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.BLACK_CROSS;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.RANDOM1;
	}	
}