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
package org.worldgrower.util;

public class SentenceUtils {

	public static String getArticle(String description) {
		String article = "a";
		if ( (description.length() > 0) && (isVowel(description.charAt(0)))) {
			article = "an";
		}
		return article;
	}
	
	private static boolean isVowel(char c) {
		return "AEIOUaeiou".indexOf(c) != -1;
	}
}
