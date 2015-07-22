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

public class Question {
	private int id;
	private final int subjectId;
	private final String questionPhrase;
	private int historyItemId = -1;
	private final int additionalValue;
	
	public Question(WorldObject subject, String questionPhrase) {
		this(subject, questionPhrase, 0);
	}
	
	public Question(WorldObject subject, String questionPhrase, int additionalValue) {
		this.subjectId = (subject != null ? subject.getProperty(Constants.ID) : -1);
		this.questionPhrase = questionPhrase;
		this.additionalValue = additionalValue;
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

	public String getQuestionPhrase() {
		return questionPhrase;
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

	@Override
	public String toString() {
		return getQuestionPhrase();
	}
}