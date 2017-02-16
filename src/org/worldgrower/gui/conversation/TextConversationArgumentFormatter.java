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
package org.worldgrower.gui.conversation;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;

public class TextConversationArgumentFormatter implements ConversationArgumentFormatter {

	@Override
	public String formatObject(Object object) {
		if (object instanceof String) {
			return (String) object;
		} else if (object instanceof Integer) {
			return ((Integer) object).toString();
		} else if (object instanceof WorldObject) {
			WorldObject worldObject = (WorldObject) object;
			return worldObject.getProperty(Constants.NAME);
		} else if (object instanceof Item) {
			Item item = (Item) object;
			return item.getDescription();
		} else {
			throw new IllegalStateException("Object " + object + " of class " + object.getClass() + " cannot be mapped");
		}
	}
}
