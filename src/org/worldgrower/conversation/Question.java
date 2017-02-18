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

public class Question {
	private int id;
	private final int subjectId;
	private int historyItemId = -1;
	private final int additionalValue;
	private final int additionalValue2;
	
	private final FormattableText formattableText;
	
	public Question(TextId text, Object... objects) {
		this(null, text, objects);
	}
	
	public Question(WorldObject subject, TextId text, Object... objects) {
		this(subject, 0, 0, text, objects);
	}
	
	public Question(int additionalValue, TextId text, Object... objects) {
		this(null, additionalValue, 0, text, objects);
	}
	
	public Question(WorldObject subject, int additionalValue, TextId text, Object... objects) {
		this(subject, additionalValue, 0, text, objects);
	}
	
	public Question(WorldObject subject, int additionalValue, int additionalValue2, TextId text, Object... objects) {
		this.subjectId = (subject != null ? subject.getProperty(Constants.ID) : -1);
		this.additionalValue = additionalValue;
		this.additionalValue2 = additionalValue2;
		this.formattableText = new FormattableText(text, objects);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public String getQuestionPhrase(ConversationFormatter conversationFormatter) {
		return conversationFormatter.format(formattableText);
	}		

	public int getHistoryItemId() {
		return historyItemId;
	}

	public void setHistoryItemId(int historyItemId) {
		this.historyItemId = historyItemId;
	}

	public int getAdditionalValue() {
		return additionalValue;
	}

	public int getAdditionalValue2() {
		return additionalValue2;
	}
}