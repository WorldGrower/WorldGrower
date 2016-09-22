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
package org.worldgrower.gui.status;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.worldgrower.gui.util.ImageUtils;

public class StatusMessageImageConverter {

	public static Image convertImage(Image image) {
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		if (imageWidth > 48 || imageHeight > 48) {
			int croppedImageWidth = Math.min(48, imageWidth);
			int croppedImageHeight = Math.min(48, imageHeight);
			image = ImageUtils.cropImage((BufferedImage) image, croppedImageWidth, croppedImageHeight);
		}
		return image;
	}
}
