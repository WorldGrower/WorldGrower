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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.util.SentenceUtils;

public class ProfessionConversation implements Conversation {

	private static final int MY_PROFESSION = 0;
	private static final int NO_PROFESSION = 1;
	private static final int STILL_THE_SAME = 2;
	private static final int NEW_PROFESSION = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject target = conversationContext.getTarget();
		Profession targetProfession = target.getProperty(Constants.PROFESSION);
		final int replyId;
		if (historyItems.size() == 0) {
			if (targetProfession != null) {
				replyId = MY_PROFESSION;
			} else {
				replyId = NO_PROFESSION;
			}
		} else {
			HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
			Profession professionInLastConversation = lastHistoryItem.getOperationInfo().getTarget().getProperty(Constants.PROFESSION);
			if (targetProfession == professionInLastConversation) {
				replyId = STILL_THE_SAME;
			} else {
				replyId = NEW_PROFESSION;
			}
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, "What is your profession?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		String professionDescription = getProfessionDescription(target);
		String article = SentenceUtils.getArticle(professionDescription);
		
		return Arrays.asList(
			new Response(MY_PROFESSION, "I'm " + article + " " + professionDescription),
			new Response(NO_PROFESSION, "I don't have a profession"),
			new Response(STILL_THE_SAME, "It's still the same as the last time you asked, namely " + (professionDescription.length() > 0 ? professionDescription : "I don't have a profession")),
			new Response(NEW_PROFESSION, "I'm " + article + " " + professionDescription + " now")
			);
	}

	private String getProfessionDescription(WorldObject target) {
		Profession profession = target.getProperty(Constants.PROFESSION);
		String professionDescription = (profession != null ? profession.getDescription() : "");
		return professionDescription;
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(target, Constants.PROFESSION, target.getProperty(Constants.PROFESSION));
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about my profession";
	}
}
