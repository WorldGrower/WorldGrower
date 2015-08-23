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
package org.worldgrower;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

public class Version {

	private static String VERSION;
	
	public static String getVersion() {
		if (VERSION == null) {
			VERSION = readVersionFromManifest();
		}
		return VERSION;
	}

	private static String readVersionFromManifest() {
		try {
			Enumeration<URL> resources = Version.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				String implementationTitle = manifest.getMainAttributes().getValue("Implementation-Title");
				if ("WorldGrower".equals(implementationTitle)) {
					return manifest.getMainAttributes().getValue("Implementation-Version");
				}
			}
			return "Development";
		} catch(IOException ex) {
			return "Development";
		}
	}
}