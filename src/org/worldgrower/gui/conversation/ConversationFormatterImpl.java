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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.conversation.ConversationFormatter;
import org.worldgrower.text.Text;

public class ConversationFormatterImpl implements ConversationFormatter {

	private final ConversationArgumentFormatter conversationArgumentFormatter;

	public ConversationFormatterImpl(ConversationArgumentFormatter conversationArgumentFormatter) {
		super();
		this.conversationArgumentFormatter = conversationArgumentFormatter;
	}

	@Override
	public String format(Text text, Object[] objects) {
		List<String> args = new ArrayList<>();
		for(Object object : objects) {
			args.add(conversationArgumentFormatter.formatObject(object));
		}
		return text.get(args);
	}	
}
