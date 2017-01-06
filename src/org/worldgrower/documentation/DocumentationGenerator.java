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
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.util.FileUtils;

public class DocumentationGenerator {

	public static void main(String[] args) throws IOException {
		File outputDir = new File(args[0]);
		ImageInfoReader imageInfoReader = new ImageInfoReader();

		generateMagicSpellOverview(outputDir, imageInfoReader);
	}

	private static void generateMagicSpellOverview(File outputDir, ImageInfoReader imageInfoReader) {
		StringBuilder magicSpellsHtmlBuilder = new StringBuilder();
		for(MagicSpell magicSpell : Actions.getMagicSpells()) {
			String magicSpellFilename = "_gen_" + magicSpell.getDescription()+ ".png";
			saveImage(magicSpell.getImageIds(null) , imageInfoReader, new File(outputDir, magicSpellFilename));
			magicSpellsHtmlBuilder.append("<img src=\"").append(magicSpellFilename).append("\"");
		}
		
		FileUtils.writeTextFile(new File(outputDir, "_gen_spells.html"), magicSpellsHtmlBuilder.toString());
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
