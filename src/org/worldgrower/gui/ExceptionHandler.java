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

import org.worldgrower.Version;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
	public void uncaughtException(Thread t, Throwable e) {
		handle(e);
	}

	public static void handle(Throwable throwable) {
		try {
			ExceptionDialog exceptionDialog = new ExceptionDialog(Version.VERSION, "Unexpected error", "An unexpected error has occurred: " + throwable.getMessage(), throwable);
			exceptionDialog.setVisible(true);
		} catch (Throwable t) {
			// don't let the exception get thrown out, will cause infinite
			// looping!
		}
	}

	public static void registerExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
	}
}