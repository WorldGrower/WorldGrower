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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.worldgrower.ManagedOperation;
import org.worldgrower.actions.CraftEquipmentAction;
import org.worldgrower.actions.EquipmentType;
import org.worldgrower.gui.music.SoundIdReader;

public class EquipmentTypeSubMenuStructure extends ActionSubMenuStructure<EquipmentType> {
	
	public EquipmentTypeSubMenuStructure(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		super(imageInfoReader, soundIdReader);
	}

	@Override
	public boolean isApplicable(ManagedOperation action) {
		return action instanceof CraftEquipmentAction;
	}
	
	@Override
	public EquipmentType getActionKey(ManagedOperation action) {
		return ((CraftEquipmentAction) action).getEquipmentType();
	}
	
	@Override
	public List<EquipmentType> getActionKeys(Set<EquipmentType> actionKeys) {
		List<EquipmentType> equipmentTypes = new ArrayList<>(actionKeys);
		Collections.sort(equipmentTypes);
		return equipmentTypes;
	}
	
	@Override
	public String getActionKeyDescription(EquipmentType actionKey) {
		return actionKey.getDescription();
	}

	@Override
	public ImageIds getImageFor(EquipmentType actionKey) {
		return actionKey.getImageId();
	}
}