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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.worldgrower.ManagedOperation;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.MenuFactory;

public abstract class ActionSubMenuStructure<T> {
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private final Map<T, JMenu> existingSubMenus = new HashMap<>();

	public ActionSubMenuStructure(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		super();
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
	}

	public final JComponent addAction(ManagedOperation action, JComponent parentMenu) {
		if (isApplicable(action)) {
			T actionKey = getActionKey(action);
			JMenu subMenu = existingSubMenus.get(actionKey);
			if (subMenu == null) {
				subMenu = createSkillMenu(actionKey);
				existingSubMenus.put(actionKey, subMenu);
			}
			parentMenu = subMenu;
		}
		return parentMenu;
	}
	
	public final void addSubMenus(JPopupMenu menu) {
		List<T> actionKeys = getActionKeys(existingSubMenus.keySet());
		for(T actionKey : actionKeys) {
			JMenu actionMenu = existingSubMenus.get(actionKey);
			if (actionMenu.getItemCount() > 0) {
				menu.add(actionMenu);
			}
		}
	}
	
	private JMenu createSkillMenu(T actionKey) {
		String actionName = getActionKeyDescription(actionKey);
		actionName = Character.toUpperCase(actionName.charAt(0)) + actionName.substring(1);
		return MenuFactory.createJMenu(actionName, getImageFor(actionKey), imageInfoReader, soundIdReader);
	}

	public abstract boolean isApplicable(ManagedOperation action);
	public abstract T getActionKey(ManagedOperation action);
	public abstract List<T> getActionKeys(Set<T> actionKeys);
	public abstract String getActionKeyDescription(T actionKey);
	public abstract ImageIds getImageFor(T actionKey);
}