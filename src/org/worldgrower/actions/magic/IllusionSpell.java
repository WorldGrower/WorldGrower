package org.worldgrower.actions.magic;

import java.util.List;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BuildAction;

public interface IllusionSpell extends BuildAction {

	public List<WorldObject> getIllusionSources(WorldObject performer, World world);
}
