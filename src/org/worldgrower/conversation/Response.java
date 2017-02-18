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

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class Response {
	
	public static final boolean RESPONSE_IS_POSSIBLE = true;
	public static final boolean RESPONSE_IS_IMPOSSIBLE = false;
	
	private final int id;
	private final int subjectId;
	private final boolean isPossible;
	private final FormattableText formattableText;
	private int historyItemId = -1;
	
	public Response(int id, WorldObject subject, TextId text, Object... objects) {
		this.id = id;
		this.formattableText = new FormattableText(text, objects);
		this.subjectId = (subject != null ? subject.getProperty(Constants.ID) : -1);
		this.isPossible = RESPONSE_IS_POSSIBLE;
	}
	
	public Response(int id, TextId text, Object... objects) {
		this.id = id;
		this.formattableText = new FormattableText(text, objects);
		this.subjectId = -1;
		this.isPossible = RESPONSE_IS_POSSIBLE;
	}
	
	public Response(int id, boolean isPossible, TextId text, Object... objects) {
		this.id = id;
		this.formattableText = new FormattableText(text, objects);
		this.subjectId = -1;
		this.isPossible = isPossible;
	}

	public int getId() {
		return id;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public String getResponsePhrase(ConversationFormatter conversationFormatter) {
		return conversationFormatter.format(formattableText);
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