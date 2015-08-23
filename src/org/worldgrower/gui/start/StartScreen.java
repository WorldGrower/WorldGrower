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

import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.worldgrower.Main;
import org.worldgrower.Version;
import org.worldgrower.World;
import org.worldgrower.gui.ExceptionHandler;

public class StartScreen {

	private JDialog frame;

	private JButton btnSaveGame;

	private World world;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ExceptionHandler.registerExceptionHandler();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartScreen window = new StartScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					ExceptionHandler.handle(e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartScreen() {
		initialize();
	}
	
	public StartScreen(World world) {
		this();
		this.world = world;
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JDialog();
		frame.setBounds(100, 100, 337, 438);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setModalityType(ModalityType.APPLICATION_MODAL);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				try {
					CharacterCustomizationScreen characterCustomizationScreen = new CharacterCustomizationScreen();
					characterCustomizationScreen.setVisible(true);
				} catch (Exception e1) {
					ExceptionHandler.handle(e1);
				}
			}
		});
		btnNewGame.setBounds(78, 81, 157, 44);
		frame.getRootPane().setDefaultButton(btnNewGame);
		frame.getContentPane().add(btnNewGame);
		
		JButton btnLoadGame = new JButton("Load Game");
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
		btnLoadGame.setBounds(78, 138, 157, 44);
		frame.getContentPane().add(btnLoadGame);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(78, 266, 157, 44);
		frame.getContentPane().add(btnExit);
		
		JLabel lblVersion = new JLabel("Version " + Version.getVersion());
		lblVersion.setBounds(83, 342, 168, 21);
		frame.getContentPane().add(lblVersion);
		
		btnSaveGame = new JButton("Save Game");
		btnSaveGame.setEnabled(false);
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame parentFrame = new JFrame();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");    

				int userSelection = fileChooser.showSaveDialog(parentFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    saveGame(fileToSave);
				}
			}
		});
		btnSaveGame.setBounds(78, 195, 157, 44);
		frame.getContentPane().add(btnSaveGame);
	}
	
	private void loadGame(File selectedFile) {
		Main.load(selectedFile);
		setVisible(false);
	}
	

	private void saveGame(File fileToSave) {
		if (world == null) {
			throw new IllegalStateException("world is null");
		}
		world.save(fileToSave);
	}

	public void enableSaveButton(boolean enabled) {
		btnSaveGame.setEnabled(enabled);
	}
}
