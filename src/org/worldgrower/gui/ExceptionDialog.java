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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.IconUtils;

public class ExceptionDialog extends JDialog {

	private int dialogWidth = 500;
	private int dialogHeight = 140;
	private String version;

	private JLabel iconLabel = new JLabel();

	// is error panel opened up
	private boolean open = false;

	private JLabel errorLabel = new JLabel();
	private JTextArea errorTextArea = new JTextArea("");

	private JTextArea exceptionTextArea = new JTextArea("");
	private JScrollPane exceptionTextAreaSP = new JScrollPane();

	private JButton okButton = JButtonFactory.createButton("OK");
	private JButton viewButton = JButtonFactory.createButton("View Error");
	private JButton copyButton = JButtonFactory.createButton("Copy Error to Clipboard");
	private JButton emailButton = JButtonFactory.createButton("Email Error");

	private JPanel topPanel = new JPanel(new BorderLayout());

	public ExceptionDialog(String version, String errorLabelText, String errorDescription, Throwable e) {

		this.version = version;
		String errorString = extractErrorString(e);

		setSize(dialogWidth, dialogHeight);

		setResizable(false);

		errorTextArea.setText(errorDescription);

		errorLabel.setText(errorLabelText);

		exceptionTextArea.setText(errorString);

		exceptionTextAreaSP = new JScrollPane(exceptionTextArea);

		iconLabel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

		iconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		setupUI();

		setUpListeners();
		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setLocationRelativeTo(findActiveFrame());
		IconUtils.setIcon(this);
	}
	
	private Frame findActiveFrame() {
	    Frame[] frames = JFrame.getFrames();
	    for (int i = 0; i < frames.length; i++) {
	      if (frames[i].isVisible()) {
	        return frames[i];
	      }
	    }
	    return null;
	  }

	private String extractErrorString(Throwable e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		String errorString = errors.toString();
		return errorString;
	}

	public void setupUI() {

		this.setTitle("Error");

		errorTextArea.setLineWrap(true);
		errorTextArea.setWrapStyleWord(true);
		errorTextArea.setEditable(false);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(okButton);
		buttonPanel.add(emailButton);
		buttonPanel.add(copyButton);
		buttonPanel.add(viewButton);

		errorTextArea.setBackground(iconLabel.getBackground());

		JScrollPane textAreaSP = new JScrollPane(errorTextArea);

		textAreaSP.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		errorLabel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		exceptionTextArea.setPreferredSize(new Dimension(100, 100));
		exceptionTextArea.setEditable(false);

		topPanel.add(iconLabel, BorderLayout.WEST);

		JPanel p = new JPanel(new BorderLayout());
		p.add(errorLabel, BorderLayout.NORTH);
		p.add(textAreaSP);

		topPanel.add(p);

		this.add(topPanel);

		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setUpListeners() {

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ExceptionDialog.this.setVisible(false);
			}
		});

		viewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (open) {
					viewButton.setText("View Error");

					topPanel.remove(exceptionTextAreaSP);

					ExceptionDialog.this.setSize(dialogWidth, dialogHeight);

					topPanel.revalidate();

					open = false;

				} else {

					viewButton.setText("Hide Error");

					topPanel.add(exceptionTextAreaSP, BorderLayout.SOUTH);

					ExceptionDialog.this.setSize(dialogWidth, dialogHeight + 100);

					topPanel.revalidate();

					open = true;
				}
			}
		});

		emailButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String completeErrorDescription = getCompleteErrorDescription();
								
				Desktop desktop;
				if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
				  
					try {
						URI mailto = new URI("mailto:john@example.com?subject=Hello%20World&body="+urlEncode(completeErrorDescription));
						desktop.mail(mailto);
					} catch (URISyntaxException | IOException ex) {
						JOptionPane.showMessageDialog(ExceptionDialog.this, "Problem emailing error: " + ex.getMessage());
					}
				  
				}
			}
		});
		
		copyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String completeErrorDescription = getCompleteErrorDescription();
				StringSelection stringSelection = new StringSelection(completeErrorDescription);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard ();
				clpbrd.setContents(stringSelection, null);
			}
		});
	}
	
	private String getCompleteErrorDescription() {
		String completeErrorDescription = version + "\n" + errorLabel.getText() + ": " + errorTextArea.getText() + "\n" + exceptionTextArea.getText();
		return completeErrorDescription;
	}

	private static final String urlEncode(String str) {
	    try {
	        return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException(e);
	    }
	}	
}