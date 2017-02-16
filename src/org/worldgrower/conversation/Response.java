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
package org.worldgrower.conversation;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;
import org.worldgrower.text.Text;

public class Response {
	
	public static final boolean RESPONSE_IS_POSSIBLE = true;
	public static final boolean RESPONSE_IS_IMPOSSIBLE = false;
	
	private final int id;
	private final int subjectId;
	private final boolean isPossible;
	private final Text text;
	private final Object[] objects;
	private int historyItemId = -1;
	
	public Response(int id, WorldObject subject, Text text, Object... objects) {
		this.id = id;
		this.text = text;
		this.subjectId = (subject != null ? subject.getProperty(Constants.ID) : -1);
		this.isPossible = RESPONSE_IS_POSSIBLE;
		this.objects = objects;
	}
	
	public Response(int id, Text text, Object... objects) {
		this.id = id;
		this.text = text;
		this.subjectId = -1;
		this.isPossible = RESPONSE_IS_POSSIBLE;
		this.objects = objects;
	}
	
	public Response(int id, boolean isPossible, Text text, Object... objects) {
		this.id = id;
		this.text = text;
		this.subjectId = -1;
		this.isPossible = isPossible;
		this.objects = objects;
	}

	public int getId() {
		return id;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public String getResponsePhrase(ConversationFormatter conversationFormatter) {
		return conversationFormatter.format(text, objects);
	}	

	public boolean isPossible() {
		return isPossible;
	}

	public int getHistoryItemId() {
		return historyItemId;
	}

	public void setHistoryItemId(int historyItemId) {
		this.historyItemId = historyItemId;
	}
}