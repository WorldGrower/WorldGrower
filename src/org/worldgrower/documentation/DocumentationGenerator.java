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
package org.worldgrower.documentation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.ItemType;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.text.ConversationDescription;
import org.worldgrower.text.Text;
import org.worldgrower.util.FileUtils;

public class DocumentationGenerator {

	public static void main(String[] args) throws IOException {
		File outputDir = new File(args[0]);
		ImageInfoReader imageInfoReader = new ImageInfoReader();

		generateMagicSpellOverview(outputDir, imageInfoReader);
		generateToolsOverview(outputDir, imageInfoReader);
		generateEquipmentOverview(outputDir, imageInfoReader);
		generateDiseasesOverview(outputDir, imageInfoReader);
		generateAlchemyOverview(outputDir, imageInfoReader);
		generateDeitiesOverview(outputDir, imageInfoReader);
		generateSkillsOverview(outputDir, imageInfoReader);
		generateBuildingsOverview(outputDir, imageInfoReader);
		generateConversationsOverview(outputDir, imageInfoReader);
		generateResourcesOverview(outputDir, imageInfoReader);
	}

	private static void generateMagicSpellOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Spells";
		String description = "Spells are researched at a library and take several turns and a high enough skill level to research.";
		File outputFile = new File(outputDir, "gen_spells.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Magic School", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(MagicSpell magicSpell : Actions.getMagicSpells()) {
			List<String> tableRow = new ArrayList<>();
			String magicSpellDescription = magicSpell.getClass().getName();
			String magicSpellFilename = "gen_" + magicSpellDescription + ".png";
			saveImage(magicSpell.getImageIds(null) , imageInfoReader, new File(outputDir, magicSpellFilename));
			
			tableRow.add(imageTag(magicSpellFilename, magicSpellDescription));
			tableRow.add(magicSpell.getSimpleDescription());
			tableRow.add(magicSpell.getSkill().getName());
			tableRow.add(magicSpell.getDescription());
			
			tableValues.add(tableRow);
		}
		
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateToolsOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Tools";
		String description = "Tools are created at a workbench and makes certain actions more productive.";
		File outputFile = new File(outputDir, "gen_tools.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Item item : Item.values()) {
			if (item.getItemType() == ItemType.TOOL || item.generate(1f).hasProperty(Constants.WOOD_CUTTING_QUALITY)) {
				List<String> tableRow = new ArrayList<>();
				String toolFilename = "gen_" + item.getDescription() + ".png";
				saveImage(item.getImageId(), imageInfoReader, new File(outputDir, toolFilename));
				
				tableRow.add(imageTag(toolFilename, item.getDescription()));
				tableRow.add(item.getDescription());
				tableRow.add(item.getLongDescription());

				tableValues.add(tableRow);
			}
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}

	private static void generateEquipmentOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Equipment";
		String description = "Equipment can be worn by characters to increase damage or armor.";
		File outputFile = new File(outputDir, "gen_equipment.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Damage", "Armor");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Item item : Item.values()) {
			if (item.getItemType() == ItemType.ARMOR || item.getItemType() == ItemType.WEAPON) {
				List<String> tableRow = new ArrayList<>();
				String equipmentFilename = "gen_" + item.getDescription() + ".png";
				saveImage(item.getImageId(), imageInfoReader, new File(outputDir, equipmentFilename));

				tableRow.add(imageTag(equipmentFilename, item.getDescription()));
				tableRow.add(item.getDescription());
				tableRow.add(getPropertyValue(item, Constants.DAMAGE));
				tableRow.add(getPropertyValue(item, Constants.ARMOR));
				
				tableValues.add(tableRow);
			}
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateDiseasesOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Diseases";
		String description = "This is a complete list of the diseases you can catch.";
		File outputFile = new File(outputDir, "gen_diseases.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Condition condition : Condition.ALL_CONDITIONS) {
			if (condition.isDisease()) {
				List<String> tableRow = new ArrayList<>();
				String conditionFilename = "gen_" + condition.getDescription() + ".png";
				saveImage(condition.getImageIds(), imageInfoReader, new File(outputDir, conditionFilename));

				tableRow.add(imageTag(conditionFilename, condition.getDescription()));
				tableRow.add(condition.getDescription());
				tableRow.add(condition.getLongerDescription());
				
				tableValues.add(tableRow);
			}
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateAlchemyOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Alchemy";
		String description = "Most alchemy items are created at an apothecary, except for water which is collected at a drinking well.";
		File outputFile = new File(outputDir, "gen_alchemy.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Item item : Item.values()) {
			if (item.getItemType() == ItemType.DRINK) {
				List<String> tableRow = new ArrayList<>();
				String filename = "gen_" + item.getDescription() + ".png";
				saveImage(item.getImageId(), imageInfoReader, new File(outputDir, filename));

				tableRow.add(imageTag(filename, item.getDescription()));
				tableRow.add(item.getDescription());
				tableRow.add(item.getLongDescription());
				
				tableValues.add(tableRow);
			}
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateDeitiesOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Deities";
		String description = "Deities are worshipped at shrines and give skill bonuses and boons to devout followers.";
		File outputFile = new File(outputDir, "gen_deities.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Description", "Skill", "Boon");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Deity deity : Deity.ALL_DEITIES) {
			List<String> tableRow = new ArrayList<>();
			String filename = "gen_" + deity.getName() + ".png";
			saveImage(deity.getBoonImageId(), imageInfoReader, new File(outputDir, filename));

			tableRow.add(imageTag(filename, deity.getName()));
			tableRow.add(deity.getName());
			tableRow.add(deity.getExplanation());
			tableRow.add(deity.getSkill().getName());
			tableRow.add(deity.getBoonDescription());
			
			tableValues.add(tableRow);
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateSkillsOverview(File outputDir, ImageInfoReader imageInfoReader) {
		List<SkillProperty> skills = SkillUtils.getSortedSkills();
		String title = "WorldGrower:Skills";
		String description = "There are " + skills.size() + " skills in WorldGrower, each of which determines how well you can perform various tasks. Each skill is governed by an Attribute. A higher attribute results in a higher starting skill level and faster skill level increases.";
		File outputFile = new File(outputDir, "gen_skills.html");
		List<String> headerFields = Arrays.asList("Name", "Description", "Attribute");
		List<List<String>> tableValues = new ArrayList<List<String>>();

		for(SkillProperty skillProperty : skills) {
			List<String> tableRow = new ArrayList<>();

			tableRow.add(skillProperty.getName());
			tableRow.add(skillProperty.getLongDescription());
			tableRow.add(SkillUtils.getAttributeForSkill(skillProperty).getName());
			
			tableValues.add(tableRow);
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateBuildingsOverview(File outputDir, ImageInfoReader imageInfoReader) {
		WorldObject buildingOwner = createBuildingOwner();
		List<WorldObject> buildings = BuildingGenerator.getAllBuildings(buildingOwner);
		String title = "WorldGrower:Buildings";
		String description = "This lists all the buildings that can be built.";
		File outputFile = new File(outputDir, "gen_buildings.html");
		List<String> headerFields = Arrays.asList("Image", "Name", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();

		for(WorldObject building : buildings) {
			List<String> tableRow = new ArrayList<>();

			String name = building.getProperty(Constants.NAME);
			name = name.replace("character's", "").trim();
			String filename = "gen_" + name + ".png";
			saveImage(building.getProperty(Constants.IMAGE_ID), imageInfoReader, new File(outputDir, filename));
			tableRow.add(imageTag(filename, name));
			tableRow.add(name);
			tableRow.add(building.getProperty(Constants.LONG_DESCRIPTION));
			
			tableValues.add(tableRow);
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateConversationsOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Conversations";
		String description = "This lists all the conversations that can be had.";
		File outputFile = new File(outputDir, "gen_conversations.html");
		List<String> headerFields = Arrays.asList("Questions", "Answers");
		List<List<String>> tableValues = new ArrayList<List<String>>();

		List<ConversationDescription> conversationDescriptions = Text.getConversationDescriptions();
		
		for(ConversationDescription conversationDescription : conversationDescriptions) {
			List<String> tableRow = new ArrayList<>();

			tableRow.add(unorderedList(conversationDescription.getQuestions()));
			tableRow.add(unorderedList(conversationDescription.getAnswers()));
			
			tableValues.add(tableRow);
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateResourcesOverview(File outputDir, ImageInfoReader imageInfoReader) {
		String title = "WorldGrower:Resources";
		String description = "Resources are obtained by cutting trees, mining, etc and are used to build building, create tools, weapons, gold and armor.";
		File outputFile = new File(outputDir, "gen_resources.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Item item : Item.values()) {
			if (item.getItemType() == ItemType.RESOURCE) {
				List<String> tableRow = new ArrayList<>();
				String filename = "gen_" + item.getDescription() + ".png";
				saveImage(item.getImageId(), imageInfoReader, new File(outputDir, filename));

				tableRow.add(imageTag(filename, item.getDescription()));
				tableRow.add(item.getDescription());
				tableRow.add(item.getLongDescription());
				
				tableValues.add(tableRow);
			}
		}
		createHtmlFile(title, description, outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	public static WorldObject createBuildingOwner() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "character");
		WorldObject buildingOwner = new WorldObjectImpl(properties);
		return buildingOwner;
	}

	private static String getPropertyValue(Item item, IntProperty property) {
		String value = "";
		if (item.generate(1f).hasProperty(property)) {
			value = item.generate(1f).getProperty(property).toString();	
		}
		return value;
	}
	
	private static String imageTag(String imageSource, String alternateText) {
		return "<img src=\"" + imageSource + "\" alt=\"" + alternateText + "\">";
	}
	
	private static String unorderedList(List<String> descriptions) {
		StringBuilder tableBuilder = new StringBuilder("<ul>");
		
		for(String description : descriptions) {
			tableBuilder.append("<li>").append(description).append("</li>");
		}
		
		tableBuilder.append("</ul>");
		return tableBuilder.toString();
	}
	
	private static void createHtmlFile(String title, String description, File outputFile, ImageInfoReader imageInfoReader, List<String> headerFields, List<List<String>> tableValues) {
		StringBuilder htmlBuilder = new StringBuilder("<html>");
		htmlBuilder.append("<head>");
		htmlBuilder.append("<title>").append(title).append("</title>");
		htmlBuilder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../worldgrower.css\">");
		htmlBuilder.append("</head>");
		htmlBuilder.append("<body>");
		htmlBuilder.append("<h2>").append(title).append("</h2>");
		htmlBuilder.append(description).append("<br><br>");
		htmlBuilder.append("<table>");
		htmlBuilder.append("<tr>");
		for(String headerField : headerFields) {
			htmlBuilder.append("<th>").append(headerField).append("</th>");
		}
		htmlBuilder.append("</tr>");
		for(List<String> tableRow : tableValues) {
			htmlBuilder.append("<tr>");
			for(String tableValue : tableRow) {
				htmlBuilder.append("<td>").append(tableValue).append("</td>");
			}
			htmlBuilder.append("</tr>");
		}
		htmlBuilder.append("</table>");
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");
		
		FileUtils.writeTextFile(outputFile, htmlBuilder.toString());		
	}
	
	private static void saveImage(ImageIds imageIds, ImageInfoReader imageInfoReader, File outputFile) {
		try {
		    BufferedImage bi = (BufferedImage) imageInfoReader.getImage(imageIds, null);
		    ImageIO.write(bi, "png", outputFile);
		} catch (IOException e) {
			throw new IllegalStateException("");
		}
	}
}
