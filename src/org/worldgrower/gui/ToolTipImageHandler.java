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
package org.worldgrower.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.imageio.ImageIO;

import org.worldgrower.gui.font.Fonts;

class ToolTipImageHandler {

	// source: http://stackoverflow.com/questions/861500/url-to-load-resources-from-the-classpath-in-java
	private static final String IMAGE_PROTOCOL = "image";

	public ToolTipImageHandler(ImageInfoReader imageInfoReader) {
		initializeUrlHandler(imageInfoReader);
	}

	private URL getPath(ImageIds imageIds) {
		try {
			return new URL(IMAGE_PROTOCOL + ":" + imageIds.name());
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	public String smallImageTag(ImageIds imageIds) {
		int fontSize = Fonts.getFontSize();
		return "<img src=\"" + getPath(imageIds) + "\" width=\"" + fontSize + "\" height=\"" + fontSize + "\">";
	}

	private void initializeUrlHandler(ImageInfoReader imageInfoReader) {
		URL.setURLStreamHandlerFactory(new ConfigurableStreamHandlerFactory(IMAGE_PROTOCOL, new ImageHandler(imageInfoReader)));
	}

	private class ImageHandler extends URLStreamHandler {
		private final ImageInfoReader imageInfoReader;
		private ImageIds imageId;

		public ImageHandler(ImageInfoReader imageInfoReader) {
			super();
			this.imageInfoReader = imageInfoReader;
		}

		protected void parseURL(URL u, String spec, int start, int end) {
			String imageName = spec.substring(spec.indexOf(':') + 1);
			this.imageId = ImageIds.valueOf(imageName);
			super.parseURL(u, spec, start, end);
		}

		protected URLConnection openConnection(URL url) throws IOException {
			return new ImageConnection(url, imageId, imageInfoReader);
		}
	}

	private class ImageConnection extends URLConnection {
		private final ImageIds imageIds;
		private final ImageInfoReader imageInfoReader;

		protected ImageConnection(URL url, ImageIds imageIds, ImageInfoReader imageInfoReader) {
			super(url);
			this.imageIds = imageIds;
			this.imageInfoReader = imageInfoReader;
		}

		@Override
		public void connect() throws IOException {
		}

		@Override
		public InputStream getInputStream() throws IOException {
			Image image = imageInfoReader.getImage(imageIds, null);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write((BufferedImage) image, "png", os);
			return new ByteArrayInputStream(os.toByteArray());
		}
	}
}
