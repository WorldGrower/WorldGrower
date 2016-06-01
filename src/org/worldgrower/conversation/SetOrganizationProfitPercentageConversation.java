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
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.util.SentenceUtils;

public class SetOrganizationProfitPercentageConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = YES;		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(performer, world);
		List<Question> questions = new ArrayList<>();
		
		for(WorldObject organization : organizations) {
			if (GroupPropertyUtils.canChangeLeaderOfOrganization(organization)) {
				if (!isVillagersOrganization(organization, world)) {
					if (target.getProperty(Constants.GROUP).contains(organization)) {
						Profession profession = organization.getProperty(Constants.PROFESSION);
						if (profession != null) {
							for(int profitPercentage = -100; profitPercentage<=100; profitPercentage+=50) {
								for(Item item : profession.getSellItems()) {
									int itemIndex = item.ordinal();
									int price = item.getPrice() * (1 + profitPercentage/100);
									questions.add(new Question(organization, "I'd like to set the price for " + item.getDescription() + " for " + organization.getProperty(Constants.NAME) + " to " + price + ", can you take care of this?", itemIndex, price));
								}
							}
						}
					}
				}
			}
		}

		return questions;
	}
	
	private boolean isVillagersOrganization(WorldObject organization, World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		return organization.equals(villagersOrganization);
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject organization = conversationContext.getSubject();
		int itemIndex = conversationContext.getAdditionalValue();
		int price = conversationContext.getAdditionalValue2();
		
		String itemDescription = Item.value(itemIndex).getDescription();
		String article = SentenceUtils.getArticle(itemDescription);
		return Arrays.asList(
			new Response(YES, "Yes, I'll set the price for " + article + " " + itemDescription + " to " + price + "."),
			new Response(NO, "No")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		if (replyIndex == YES) {
			WorldObject organization = conversationContext.getSubject();
			World world = conversationContext.getWorld();
			int itemIndex = conversationContext.getAdditionalValue();
			int price = conversationContext.getAdditionalValue2();
			
			List<WorldObject> members = GroupPropertyUtils.findOrganizationMembers(organization, world);
			
			for(WorldObject member : members) {
				member.getProperty(Constants.PRICES).setPrice(Item.value(itemIndex), price);
			}
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about setting the price of a product for an organization";
	}
}
