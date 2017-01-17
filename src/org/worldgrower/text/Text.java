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
package org.worldgrower.text;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum Text {
	QUESTION_ARENA_PAY_CHECK,
	ANSWER_ARENA_PAY_CHECK_YES,
	ANSWER_ARENA_PAY_CHECK_NO;
	
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("MessagesBundle");
	
	public String get() {
        return MESSAGES.getString(this.toString());
	}
	
	public String get(String parm) {
        MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(get());
        return formatter.format(new Object[]{ parm });
	}	
}
