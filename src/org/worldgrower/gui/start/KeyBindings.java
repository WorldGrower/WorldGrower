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
package org.worldgrower.gui.start;

import java.util.ArrayList;
import java.util.List;

public class KeyBindings {
	private List<GuiActionValue> keyBindings = new ArrayList<>();
	
	public KeyBindings(List<GuiAction> guiActions, char... values) {
		int index = 0;
		for(GuiAction guiAction : guiActions) {
			keyBindings.add(new GuiActionValue(guiAction, values[index]));
			index++;
		}
	}
	
	public int size() {
		return keyBindings.size();
	}
	
	public String getDescription(int index) {
		return keyBindings.get(index).getGuiAction().getDescription();
	}
	
	public char getValue(int index) {
		return keyBindings.get(index).getValue();
	}
	
	public void setValue(int index, char value) {
		keyBindings.get(index).setValue(value);
	}
	
	private static class GuiActionValue {
		private final GuiAction guiAction;
		private char value;
		
		public GuiActionValue(GuiAction guiAction, char value) {
			this.guiAction = guiAction;
			this.value = value;
		}

		public char getValue() {
			return value;
		}

		public void setValue(char value) {
			this.value = value;
		}

		public GuiAction getGuiAction() {
			return guiAction;
		}
	}
}
