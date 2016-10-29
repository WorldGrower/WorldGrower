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
package org.worldgrower.condition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.DeathReasonPropertyUtils;
import org.worldgrower.gui.ImageIds;

/**
 * A Condition is something WorldObjects can have.
 * It lasts several turns and can limit the actions of the WorldObject.
 */
public interface Condition extends Serializable {

	public boolean canTakeAction();
	public boolean canMove();
	public String getDescription();
	public String getLongerDescription();
	public void onTurn(WorldObject worldObject, World world, int startTurns, WorldStateChangedListeners creatureTypeChangedListeners);	
	public boolean isDisease();
	public boolean isMagicEffect();
	public void conditionEnds(WorldObject worldObject, World world);
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world);
	public ImageIds getImageIds();
	
	public default void decreaseHitPoints(WorldObject worldObject, DeadlyCondition deadlyCondition, int value, World world) {
		worldObject.increment(Constants.HIT_POINTS, -value);
		
		int hitPoints = worldObject.getProperty(Constants.HIT_POINTS);
		if (hitPoints == 0) {
			DeathReasonPropertyUtils.targetDiesByCondition(deadlyCondition, worldObject, world);
		}
	}
	
	public static final List<Condition> ALL_CONDITIONS = new ArrayList<>();
	
	public static final ParalyzedCondition PARALYZED_CONDITION = new ParalyzedCondition(ALL_CONDITIONS);
	public static final CocoonedCondition COCOONED_CONDITION = new CocoonedCondition(ALL_CONDITIONS);
	public static final BurningCondition BURNING_CONDITION = new BurningCondition(ALL_CONDITIONS);
	public static final PoisonedCondition POISONED_CONDITION = new PoisonedCondition(ALL_CONDITIONS);
	public static final VampireBiteCondition VAMPIRE_BITE_CONDITION = new VampireBiteCondition(ALL_CONDITIONS);
	public static final InvisibleCondition INVISIBLE_CONDITION = new InvisibleCondition(ALL_CONDITIONS);
	public static final EnlargedCondition ENLARGED_CONDITION = new EnlargedCondition(ALL_CONDITIONS);
	public static final ReducedCondition REDUCED_CONDITION = new ReducedCondition(ALL_CONDITIONS);
	public static final SleepCondition SLEEP_CONDITION = new SleepCondition(ALL_CONDITIONS);
	public static final WaterWalkCondition WATER_WALK_CONDITION = new WaterWalkCondition(ALL_CONDITIONS);
	public static final UnconsciousCondition UNCONSCIOUS_CONDITION = new UnconsciousCondition(ALL_CONDITIONS);
	public static final BurdenedCondition BURDENED_CONDITION = new BurdenedCondition(ALL_CONDITIONS);
	public static final FeatheredCondition FEATHERED_CONDITION = new FeatheredCondition(ALL_CONDITIONS);
	public static final SoulTrappedCondition SOUL_TRAPPED_CONDITION = new SoulTrappedCondition(ALL_CONDITIONS);
	public static final SilencedCondition SILENCED_CONDITION = new SilencedCondition(ALL_CONDITIONS);
	public static final IntoxicatedCondition INTOXICATED_CONDITION = new IntoxicatedCondition(ALL_CONDITIONS);
	public static final FearCondition FEAR_CONDITION = new FearCondition(ALL_CONDITIONS);
	public static final EntangledCondition ENTANGLED_CONDITION = new EntangledCondition(ALL_CONDITIONS);
	public static final AtaxiaCondition ATAXIA_CONDITION = new AtaxiaCondition(ALL_CONDITIONS);
	public static final DarkVisionCondition DARK_VISION_CONDITION = new DarkVisionCondition(ALL_CONDITIONS);
	public static final TrappedContainerCondition TRAPPED_CONTAINER_CONDITION = new TrappedContainerCondition(ALL_CONDITIONS);
	public static final DiseaseImmunityCondition DISEASE_IMMUNITY_CONDITION = new DiseaseImmunityCondition(ALL_CONDITIONS);
	public static final ProtectionFromEnergyCondition PROTECTION_FROM_FIRE_CONDITION = new ProtectionFromEnergyCondition(ALL_CONDITIONS, "protection from fire", "protection from fire cuts all fire damage in half", ImageIds.PROTECTION_FROM_FIRE);
	public static final ProtectionFromEnergyCondition PROTECTION_FROM_ICE_CONDITION = new ProtectionFromEnergyCondition(ALL_CONDITIONS, "protection from ice", "protection from ice cuts all cold damage in half", ImageIds.PROTECTION_FROM_ICE);
	public static final ProtectionFromEnergyCondition PROTECTION_FROM_LIGHTNING_CONDITION = new ProtectionFromEnergyCondition(ALL_CONDITIONS, "protection from lightning", "protection from lightning cuts all lightning damage in half", ImageIds.PROTECTION_FROM_LIGHTNING);
	
	public static final DeityBoonCondition DEMETER_BOON_CONDITION = new DeityBoonCondition(Deity.DEMETER, ALL_CONDITIONS);
	public static final DeityBoonCondition HEPHAESTUS_BOON_CONDITION = new DeityBoonCondition(Deity.HEPHAESTUS, ALL_CONDITIONS);
	public static final DeityBoonCondition HADES_BOON_CONDITION = new DeityBoonCondition(Deity.HADES, ALL_CONDITIONS);
	public static final DeityBoonCondition APHRODITE_BOON_CONDITION = new DeityBoonCondition(Deity.APHRODITE, ALL_CONDITIONS);
	public static final DeityBoonCondition APOLLO_BOON_CONDITION = new DeityBoonCondition(Deity.APOLLO, ALL_CONDITIONS);
	public static final DeityBoonCondition DIONYSUS_BOON_CONDITION = new DeityBoonCondition(Deity.DIONYSUS, ALL_CONDITIONS);
	public static final DeityBoonCondition ARES_BOON_CONDITION = new DeityBoonCondition(Deity.ARES, ALL_CONDITIONS);
	public static final DeityBoonCondition ARTEMIS_BOON_CONDITION = new DeityBoonCondition(Deity.ARTEMIS, ALL_CONDITIONS);
	public static final DeityBoonCondition HERMES_BOON_CONDITION = new DeityBoonCondition(Deity.HERMES, ALL_CONDITIONS);
	public static final DeityBoonCondition ATHENA_BOON_CONDITION = new DeityBoonCondition(Deity.ATHENA, ALL_CONDITIONS);
	public static final DeityBoonCondition ZEUS_BOON_CONDITION = new DeityBoonCondition(Deity.ZEUS, ALL_CONDITIONS);
	public static final DeityBoonCondition HERA_BOON_CONDITION = new DeityBoonCondition(Deity.HERA, ALL_CONDITIONS);
	public static final DeityBoonCondition POSEIDON_BOON_CONDITION = new DeityBoonCondition(Deity.POSEIDON, ALL_CONDITIONS);
	
	public static List<String> getDeadlyConditions() {
		return ALL_CONDITIONS.stream().filter(condition -> condition instanceof DeadlyCondition).map(condition -> condition.getDescription()).collect(Collectors.toList());
	}
}
