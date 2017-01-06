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

import javax.imageio.ImageIO;

import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
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
	}

	private static void generateMagicSpellOverview(File outputDir, ImageInfoReader imageInfoReader) {
		StringBuilder spellsHtmlBuilder = new StringBuilder("<html>");
		spellsHtmlBuilder.append("<table>");
		spellsHtmlBuilder.append("<tr><th>Icon</th><th>Name</th><th>Magic School</th><th>Description</th></tr>");
		for(MagicSpell magicSpell : Actions.getMagicSpells()) {
			String magicSpellFilename = "_gen_" + magicSpell.getClass().getName() + ".png";
			saveImage(magicSpell.getImageIds(null) , imageInfoReader, new File(outputDir, magicSpellFilename));
			spellsHtmlBuilder.append("<tr>");
			spellsHtmlBuilder.append("<td><img src=\"").append(magicSpellFilename).append("\"></td>");
			spellsHtmlBuilder.append("<td>").append(magicSpell.getSimpleDescription()).append("</td>");
			spellsHtmlBuilder.append("<td>").append(magicSpell.getSkill().getName()).append("</td>");
			spellsHtmlBuilder.append("<td>").append(magicSpell.getDescription()).append("</td>");
			spellsHtmlBuilder.append("</tr>");
		}
		spellsHtmlBuilder.append("</table>");
		spellsHtmlBuilder.append("</html>");
		
		FileUtils.writeTextFile(new File(outputDir, "_gen_spells.html"), spellsHtmlBuilder.toString());
	}
	
	private static void generateToolsOverview(File outputDir, ImageInfoReader imageInfoReader) {
		StringBuilder toolsHtmlBuilder = new StringBuilder("<html>");
		toolsHtmlBuilder.append("<table>");
		toolsHtmlBuilder.append("<tr><th>Icon</th><th>Name</th></tr>");
		for(Item item : Item.values()) {
			if (item.getItemType() == ItemType.TOOL) {
				String toolFilename = "_gen_" + item.getDescription() + ".png";
				saveImage(item.getImageId(), imageInfoReader, new File(outputDir, toolFilename));
				toolsHtmlBuilder.append("<tr>");
				toolsHtmlBuilder.append("<td><img src=\"").append(toolFilename).append("\"></td>");
				toolsHtmlBuilder.append("<td>").append(item.getDescription()).append("</td>");
				toolsHtmlBuilder.append("</tr>");
			}
		}
		toolsHtmlBuilder.append("</table>");
		toolsHtmlBuilder.append("</html>");
		
		FileUtils.writeTextFile(new File(outputDir, "_gen_tools.html"), toolsHtmlBuilder.toString());
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
