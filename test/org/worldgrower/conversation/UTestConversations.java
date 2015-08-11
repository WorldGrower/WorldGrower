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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Response;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.history.Turn;

public class UTestConversations {

	private Conversations conversations = new Conversations();
	private WorldObject performer = TestUtils.createWorldObject(1, "test1");
	private WorldObject target = TestUtils.createWorldObject(1, "test2");
	private World world = new WorldImpl(10, 10, null, null);
	
	@Test
	public void testCreateArgs() {
		assertArrayEquals(new int[] {0, -1, -1, 0}, Conversations.createArgs(Conversations.NAME_CONVERSATION));
		assertArrayEquals(new int[] {0, 2, -1, 0}, Conversations.createArgs(Conversations.NAME_CONVERSATION, TestUtils.createWorldObject(2, "testworldobject")));
		assertArrayEquals(new int[] {0, -1, 3, 0}, Conversations.createArgs(Conversations.NAME_CONVERSATION, new HistoryItem(3, null, new Turn())));
	}
	
	@Test
	public void testGetQuestionPhrase() {
		String questionPhrase = conversations.getQuestionPhrase(0, -1, -1, performer, target, world);
		assertEquals("What is your name?", questionPhrase);
	}
	
	@Test
	public void testGetReplyPhrase() {
		Response response = conversations.getReplyPhrase(0, -1, -1, performer, target, world, 0);
		assertEquals(0, response.getId());
		assertEquals(-1, response.getSubjectId());
		assertEquals(-1, response.getHistoryItemId());
		assertEquals(true, response.isPossible());
		assertEquals("My name is test2", response.getResponsePhrase());
	}
}
