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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ReasonsImpl;
import org.worldgrower.deity.Deity;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class ChooseDeityAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int deityIndex = args[0];
		int reasonIndex = args[1];
		
		Deity deity = Deity.ALL_DEITIES.get(deityIndex);
		performer.setProperty(Constants.DEITY, deity);
		
		if (reasonIndex != -1) {
			performer.getProperty(Constants.REASONS).addReason(Constants.DEITY, deity.getReasons().get(reasonIndex));
		}
		
		setFacade(performer);
	}

	private void setFacade(WorldObject performer) {
		WorldObject performerFacade = performer.getProperty(Constants.FACADE);
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		boolean professionNeedsHiding = performerProfession == Professions.THIEF_PROFESSION || performerProfession == Professions.TRICKSTER_PROFESSION;
		if (professionNeedsHiding && performerFacade != null) {
			performerFacade.setProperty(Constants.DEITY, Deity.DEMETER);
			
			if (performerFacade.getProperty(Constants.REASONS) == null) {
				performerFacade.setProperty(Constants.REASONS, new ReasonsImpl());
			}
			performerFacade.getProperty(Constants.REASONS).addReason(Constants.DEITY, Deity.DEMETER.getReasons().get(0));
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
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "choosing a deity";
	}

	@Override
	public String getSimpleDescription() {
		return "choose deity";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.BLACK_CROSS;
	}
}