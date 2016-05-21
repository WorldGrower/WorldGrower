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
package org.worldgrower.gui.start;

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.worldgrower.Version;
import org.worldgrower.World;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ExceptionHandler;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.MenuFactory;

public class StartScreen {

	private StartScreenDialog frame;
	private JButton btnSaveGame;
	private JButton btnControlsGame;
	private World world;
	private final KeyBindings keyBindings;
	private final Preferences preferences = Preferences.userNodeForPackage(getClass());
	
	private static ImageInfoReader imageInfoReader = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ExceptionHandler.registerExceptionHandler();
		loadImagesInBackGround();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartScreen window = new StartScreen(imageInfoReader);
					window.frame.setVisible(true);
				} catch (Exception e) {
					ExceptionHandler.handle(e);
				}
			}
		});
	}
	
	private static void loadImagesInBackGround() {
		new Thread() {
			@Override
			public void run() {
				try {
					imageInfoReader = new ImageInfoReader();
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}
		}.start();
	}

	public StartScreen(ImageInfoReader imageInfoReaderValue) {
		initialize();
		imageInfoReader = imageInfoReaderValue;
		this.keyBindings = createKeyBindings(preferences);
	}
	
	public StartScreen(ImageInfoReader imageInfoReaderValue, KeyBindings keyBindings) {
		initialize();
		imageInfoReader = imageInfoReaderValue;
		this.keyBindings = keyBindings;
	}
	
	private static KeyBindings createKeyBindings(Preferences preferences) {
		char[] values = new char[GuiAction.values().length];
		for(int i=0; i<values.length; i++) {
			values[i] = GuiAction.values()[i].getDefaultValue();
		}
		KeyBindings keyBindings = new KeyBindings(Arrays.asList(GuiAction.values()), values);
		keyBindings.loadSettings(preferences);
		return keyBindings;
	}

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	private void showNewGamePopupMenu() {
		JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
		
		popupMenu.add(MenuFactory.createJMenuItem(new TutorialAction()));
		popupMenu.add(MenuFactory.createJMenuItem(new CustomGameAction()));
		
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouseLocation, frame);
		popupMenu.show(frame, mouseLocation.x, mouseLocation.y);
	}
	
	private class TutorialAction extends AbstractAction {

		public TutorialAction() {
			super("Tutorial");
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			frame.setVisible(false);
			new Thread() {
				public void run() {
					try {
						Game.run(new CharacterAttributes(10, 10, 10, 10, 10, 10), imageInfoReader, ImageIds.KNIGHT, new TutorialGameParameters(), keyBindings);
					} catch (Exception e1) {
						ExceptionHandler.handle(e1);
					}
				}
			}.start();
		}
	}
	
	private class CustomGameAction extends AbstractAction {

		public CustomGameAction() {
			super("Custom Game");
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			frame.setVisible(false);
			try {
				CharacterCustomizationScreen characterCustomizationScreen = new CharacterCustomizationScreen(imageInfoReader, keyBindings);
				characterCustomizationScreen.setVisible(true);
			} catch (Exception e1) {
				ExceptionHandler.handle(e1);
			}
		}
	}
	
	private void initialize() {
		frame = new StartScreenDialog();
		
		addNewButton();
		addLoadButton();
		addExitButton();
		
		addVersionLabel();
		
		addSaveButton();
		addControlsButton();
	}

	private void addVersionLabel() {
		JLabel lblVersion = JLabelFactory.createJLabel("Version " + Version.getVersion());
		lblVersion.setToolTipText("Current version");
		frame.addComponent(lblVersion);
		SwingUtils.setBoundsAndCenterHorizontally(lblVersion, 83, 450, 167, 21);
	}

	private void addSaveButton() {
		btnSaveGame = JButtonFactory.createButton("Save Game", IconUtils.getSaveIcon());
		btnSaveGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnSaveGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnSaveGame.setToolTipText("Saves current game");
		btnSaveGame.setEnabled(false);
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame parentFrame = new JFrame();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				String defaultFilename = getDefaultFilename();
				fileChooser.setSelectedFile(new File(defaultFilename));

				int userSelection = fileChooser.showSaveDialog(parentFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    saveGame(fileToSave);
				}
			}

			private String getDefaultFilename() {
				Date currentTime = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				return format.format(currentTime) + ".sav";
			}
		});
		frame.addComponent(btnSaveGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnSaveGame, 78, 220, 167, 60);
	}
	
	private void addControlsButton() {
		btnControlsGame = JButtonFactory.createButton("Controls", IconUtils.getControlsIcon());
		btnControlsGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnControlsGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnControlsGame.setToolTipText("View and change game controls");
		btnControlsGame.setEnabled(true);
		btnControlsGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControlsDialog controlsDialog = new ControlsDialog(keyBindings);
				controlsDialog.showMe();
				keyBindings.saveSettings(preferences);
			}
		});
		frame.addComponent(btnControlsGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnControlsGame, 78, 290, 167, 60);
	}

	private void addExitButton() {
		JButton btnExit = JButtonFactory.createButton("Exit", IconUtils.getExitIcon());
		btnExit.setHorizontalAlignment(SwingConstants.LEFT);
		btnExit.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnExit.setToolTipText("Exits program");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		frame.addComponent(btnExit);
		SwingUtils.setBoundsAndCenterHorizontally(btnExit, 78, 360, 167, 60);
	}

	private void addNewButton() {
		JButton btnNewGame = JButtonFactory.createButton("New Game", IconUtils.getNewIcon());
		btnNewGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnNewGame.setToolTipText("Starts a new game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showNewGamePopupMenu();
			}
		});
		
		frame.getRootPane().setDefaultButton(btnNewGame);
		frame.addComponent(btnNewGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnNewGame, 80, 81, 167, 60);
	}

	private void addLoadButton() {
		JButton btnLoadGame = JButtonFactory.createButton("Load Game", IconUtils.getLoadIcon());
		btnLoadGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnLoadGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnLoadGame.setToolTipText("Loads a game");
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    loadGame(selectedFile);
				}
			}
		});
		frame.addComponent(btnLoadGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnLoadGame, 78, 150, 167, 60);
	}
	
	private void loadGame(File selectedFile) {
		Game.load(selectedFile, imageInfoReader, keyBindings);
		setVisible(false);
	}
	

	private void saveGame(File fileToSave) {
		if (world == null) {
			throw new IllegalStateException("world is null");
		}
		world.save(fileToSave);
	}

	public void enableSave(boolean enabled, World world) {
		this.btnSaveGame.setEnabled(enabled);
		this.world = world;
	}
	
	private static class StartScreenDialog extends AbstractDialog {

		public StartScreenDialog() {
			super(337, 520);
		}
	}
}
