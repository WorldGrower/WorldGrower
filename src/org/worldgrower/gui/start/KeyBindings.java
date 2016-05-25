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
import java.util.prefs.Preferences;

public class KeyBindings {
	private static final String LEFT_MOUSE_CLICK_CENTERS_MAP = "leftMouseClickCentersMap";
	private List<GuiActionValue> keyBindings = new ArrayList<>();
	private boolean leftMouseClickCentersMap;
	
	public KeyBindings(List<GuiAction> guiActions, char... values) {
		int index = 0;
		for(GuiAction guiAction : guiActions) {
			keyBindings.add(new GuiActionValue(guiAction, values[index]));
			index++;
		}
		leftMouseClickCentersMap = false;
	}
	
	public int size() {
		return keyBindings.size();
	}
	
	public boolean leftMouseClickCentersMap() {
		return leftMouseClickCentersMap;
	}
	
	public void setLeftMouseClickCentersMap(boolean leftMouseClickCentersMap) {
		this.leftMouseClickCentersMap = leftMouseClickCentersMap;
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
	
	public List<Character> getPossibleValues(int index) {
		char currentChar = getValue(index);
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		ArrayList<Character> chars = new ArrayList<>();
		for (char c : str.toCharArray()) {
			if (c == currentChar || !valueAlreadyUsed(c)) {
				chars.add(c);
			}
		}
		
		return chars;
	}
	
	private boolean valueAlreadyUsed(char c) {
		for(GuiActionValue guiActionValue : keyBindings) {
			if (guiActionValue.getValue() == c) {
				return true;
			}
		}
		return false;
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

	public char getValue(GuiAction guiAction) {
		for(GuiActionValue guiActionValue : keyBindings) {
			if (guiActionValue.getGuiAction() == guiAction) {
				return guiActionValue.getValue();
			}
		}
		throw new IllegalStateException("GuiAction " + guiAction + " not found in " + keyBindings);
	}
	
	public void saveSettings(Preferences preferences) {
		for(GuiActionValue guiActionValue : keyBindings) {
			preferences.put(guiActionValue.getGuiAction().name(), Character.toString(guiActionValue.getValue()));
		}
		preferences.putBoolean(LEFT_MOUSE_CLICK_CENTERS_MAP, leftMouseClickCentersMap);
	}
	
	public void loadSettings(Preferences preferences) {
		for(GuiActionValue guiActionValue : keyBindings) {
			String value = preferences.get(guiActionValue.getGuiAction().name(), Character.toString(guiActionValue.getValue()));
			guiActionValue.setValue(value.charAt(0));
		}
		leftMouseClickCentersMap = preferences.getBoolean(LEFT_MOUSE_CLICK_CENTERS_MAP, leftMouseClickCentersMap);
	}
}
