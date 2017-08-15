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
import org.worldgrower.attribute.IdMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.ProfessionPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class FirePublicEmployeeAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		target.removeProperty(Constants.CAN_ATTACK_CRIMINALS);
		ProfessionPropertyUtils.endTaxCollecting(target);
		
		Profession targetProfession = target.getProperty(Constants.PROFESSION);
		if (targetProfession == Professions.TAX_COLLECTOR_PROFESSION || targetProfession == Professions.SHERIFF_PROFESSION) {
			target.setProperty(Constants.PROFESSION, null);
		}
		
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		IdMap payCheckPaidTurn = villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN);
		payCheckPaidTurn.remove(target);
		
		world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " fired " + target.getProperty(Constants.NAME));
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean canAttackCriminals = (target.hasProperty(Constants.CAN_ATTACK_CRIMINALS)) && (target.getProperty(Constants.CAN_ATTACK_CRIMINALS));
		boolean canCollectTaxes = (target.hasProperty(Constants.CAN_COLLECT_TAXES)) && (target.getProperty(Constants.CAN_COLLECT_TAXES));
		return (canAttackCriminals || canCollectTaxes);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription("target must be tax collector or sheriff", "performer must be village leader");
	}
	
	@Override
	public String getDescription() {
		return "catching fish provides fish. Using a fishing pole provides more fish";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		boolean performerIsLeader = GroupPropertyUtils.performerIsLeaderOfVillagers(performer, world);
		return (target.hasIntelligence() && target.getProperty(Constants.CREATURE_TYPE).canTalk() && !performer.equals(target)) && performerIsLeader;
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "firing someone from their job";
	}

	@Override
	public String getSimpleDescription() {
		return "fire from job";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.PARCHMENT;
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.DOOR_CLOSE;
	}	
}