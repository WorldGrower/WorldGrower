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

import java.util.List;
import java.util.Set;

import org.worldgrower.ManagedOperation;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.gui.music.SoundIdReader;

public class MagicSpellSubMenuStructure extends ActionSubMenuStructure<SkillProperty> {
	
	private final SkillImageIds skillImageIds = new SkillImageIds();
	
	public MagicSpellSubMenuStructure(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		super(imageInfoReader, soundIdReader);
	}

	@Override
	public boolean isApplicable(ManagedOperation action) {
		return action instanceof MagicSpell;
	}
	
	@Override
	public SkillProperty getActionKey(ManagedOperation action) {
		return ((MagicSpell) action).getSkill();
	}
	
	@Override
	public List<SkillProperty> getActionKeys(Set<SkillProperty> actionKeys) {
		return Actions.sortSkillProperties(actionKeys);
	}
	
	@Override
	public String getActionKeyDescription(SkillProperty actionKey) {
		return actionKey.getName();
	}

	@Override
	public ImageIds getImageFor(SkillProperty actionKey) {
		return skillImageIds.getImageFor(actionKey);
	}
}