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
import java.util.List;

import javax.imageio.ImageIO;

import org.worldgrower.Constants;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.ItemType;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.util.FileUtils;

public class DocumentationGenerator {

	public static void main(String[] args) throws IOException {
		File outputDir = new File(args[0]);
		ImageInfoReader imageInfoReader = new ImageInfoReader();

		generateMagicSpellOverview(outputDir, imageInfoReader);
		generateToolsOverview(outputDir, imageInfoReader);
		generateEquipmentOverview(outputDir, imageInfoReader);
		generateDiseasesOverview(outputDir, imageInfoReader);
	}

	private static void generateMagicSpellOverview(File outputDir, ImageInfoReader imageInfoReader) {
		File outputFile = new File(outputDir, "_gen_spells.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Magic School", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(MagicSpell magicSpell : Actions.getMagicSpells()) {
			List<String> tableRow = new ArrayList<>();
			String magicSpellFilename = "_gen_" + magicSpell.getClass().getName() + ".png";
			saveImage(magicSpell.getImageIds(null) , imageInfoReader, new File(outputDir, magicSpellFilename));
			
			tableRow.add(imageTag(magicSpellFilename));
			tableRow.add(magicSpell.getSimpleDescription());
			tableRow.add(magicSpell.getSkill().getName());
			tableRow.add(magicSpell.getDescription());
			
			tableValues.add(tableRow);
		}
		
		createHtmlFile(outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateToolsOverview(File outputDir, ImageInfoReader imageInfoReader) {
		File outputFile = new File(outputDir, "_gen_tools.html");
		List<String> headerFields = Arrays.asList("Icon", "Name");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Item item : Item.values()) {
			if (item.getItemType() == ItemType.TOOL || item.generate(1f).hasProperty(Constants.WOOD_CUTTING_QUALITY)) {
				List<String> tableRow = new ArrayList<>();
				String toolFilename = "_gen_" + item.getDescription() + ".png";
				saveImage(item.getImageId(), imageInfoReader, new File(outputDir, toolFilename));
				
				tableRow.add(imageTag(toolFilename));
				tableRow.add(item.getDescription());

				tableValues.add(tableRow);
			}
		}
		createHtmlFile(outputFile, imageInfoReader, headerFields, tableValues);
	}

	private static void generateEquipmentOverview(File outputDir, ImageInfoReader imageInfoReader) {
		File outputFile = new File(outputDir, "_gen_equipment.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Damage", "Armor");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Item item : Item.values()) {
			if (item.getItemType() == ItemType.ARMOR || item.getItemType() == ItemType.WEAPON) {
				List<String> tableRow = new ArrayList<>();
				String equipmentFilename = "_gen_" + item.getDescription() + ".png";
				saveImage(item.getImageId(), imageInfoReader, new File(outputDir, equipmentFilename));

				tableRow.add(imageTag(equipmentFilename));
				tableRow.add(item.getDescription());
				tableRow.add(getPropertyValue(item, Constants.DAMAGE));
				tableRow.add(getPropertyValue(item, Constants.ARMOR));
				
				tableValues.add(tableRow);
			}
		}
		createHtmlFile(outputFile, imageInfoReader, headerFields, tableValues);
	}
	
	private static void generateDiseasesOverview(File outputDir, ImageInfoReader imageInfoReader) {
		File outputFile = new File(outputDir, "_gen_diseases.html");
		List<String> headerFields = Arrays.asList("Icon", "Name", "Description");
		List<List<String>> tableValues = new ArrayList<List<String>>();
		for(Condition condition : Condition.ALL_CONDITIONS) {
			if (condition.isDisease()) {
				List<String> tableRow = new ArrayList<>();
				String conditionFilename = "_gen_" + condition.getDescription() + ".png";
				saveImage(condition.getImageIds(), imageInfoReader, new File(outputDir, conditionFilename));

				tableRow.add(imageTag(conditionFilename));
				tableRow.add(condition.getDescription());
				tableRow.add(condition.getLongerDescription());
				
				tableValues.add(tableRow);
			}
		}
		createHtmlFile(outputFile, imageInfoReader, headerFields, tableValues);
	}

	private static String getPropertyValue(Item item, IntProperty property) {
		String value = "";
		if (item.generate(1f).hasProperty(property)) {
			value = item.generate(1f).getProperty(property).toString();	
		}
		return value;
	}
	
	private static String imageTag(String imageSource) {
		return "<img src=\"" + imageSource + "\">";
	}
	
	private static void createHtmlFile(File outputFile, ImageInfoReader imageInfoReader, List<String> headerFields, List<List<String>> tableValues) {
		StringBuilder htmlBuilder = new StringBuilder("<html>");
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
