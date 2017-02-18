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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestText {

	@Test
	public void testGetMessage() {
		assertEquals("I'm here to receive my reward for participating in the arena", TextId.QUESTION_ARENA_PAY_CHECK.get());
		assertEquals("yes, here is your 7 gold", TextId.ANSWER_ARENA_PAY_CHECK_YES.get("7", "gold"));
	}
	
	@Test
	public void testValidate() {
		TextId.getConversationDescriptions();
	}
	
	@Test
	public void testParse() {
		TextParser textParser = createTextParser();
		TextId.ANSWER_ANGRY_GETLOST.parse(textParser);
		assertEquals("Get lost", textParser.toString());
		
		textParser = createTextParser();
		TextId.QUESTION_SET_PRICE.parse(textParser);
		assertEquals("I'd like to set the price for {0} for {1} to {2}, can you take care of this?", textParser.toString());
		
		textParser = createTextParser();
		TextId.QUESTION_SHARE_KNOWLEDGE.parse(textParser);
		assertEquals("{0}", textParser.toString());
	}

	private TextParser createTextParser() {
		return new TextParser() {
			StringBuilder builder = new StringBuilder();
			@Override
			public void variableFound(int index) {
				builder.append("{").append(index).append("}");
			}
			
			@Override
			public void constantStringFound(String string) {
				builder.append(string);
			}
			
			@Override
			public String toString() {
				return builder.toString();
			}
		};
	}
}
