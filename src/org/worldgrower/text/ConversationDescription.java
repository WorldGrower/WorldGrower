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
package org.worldgrower.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.worldgrower.text.Text.ConversationKey;

public class ConversationDescription {
	private final ConversationKey conversationKey;
	private final List<String> questions;
	private final List<String> answers;
	
	public ConversationDescription(ConversationKey conversationKey) {
		super();
		this.conversationKey = conversationKey;
		this.questions = new ArrayList<>();
		this.answers = new ArrayList<>();
	}
	
	public ConversationKey getConversationKey() {
		return conversationKey;
	}

	public List<String> getQuestions() {
		return Collections.unmodifiableList(questions);
	}

	public List<String> getAnswers() {
		return Collections.unmodifiableList(answers);
	}

	public void addQuestion(String string) {
		questions.add(string);
	}

	public void addAnswer(String string) {
		answers.add(string);
		
	}
}
