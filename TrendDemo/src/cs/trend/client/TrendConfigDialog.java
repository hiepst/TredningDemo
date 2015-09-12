package cs.trend.client;

import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

/*
 * DialogDemo.java requires these files:
 *   CustomDialog.java
 *   images/middle.gif
 */
public class TrendConfigDialog extends JFrame {
	JLabel label;
	ImageIcon icon = createImageIcon("images/middle.gif");
	String simpleDialogDesc = "Some simple message dialogs";
	String iconDesc = "A JOptionPane has its choice of icons";
	String moreDialogDesc = "Some more dialogs";
	// CustomDialog customDialog;
	private JPanel contentPanel;

	private JPanel buttonPanel;
	private JButton okButton;
	private JButton cancelButton;

	/** Creates the GUI shown inside the frame's content pane. */
	public TrendConfigDialog(String title) {
		super.setTitle(title);

		contentPanel = new JPanel(new BorderLayout());

		// Create the components.
		JPanel frequentPanel = createSimpleDialogBox();
		JPanel featurePanel = createFeatureDialogBox();
		JPanel iconPanel = createIconDialogBox();

		// Lay them out.
		Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
		frequentPanel.setBorder(padding);
		featurePanel.setBorder(padding);
		iconPanel.setBorder(padding);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Simple Modal Dialogs", null, frequentPanel, simpleDialogDesc); // tooltip
																							// text
		tabbedPane.addTab("More Dialogs", null, featurePanel, moreDialogDesc); // tooltip
																				// text
		tabbedPane.addTab("Dialog Icons", null, iconPanel, iconDesc); // tooltip
																		// text
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				hideDialog();
			}
		});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				hideDialog();
			}
		});

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		contentPanel.setOpaque(true);
		contentPanel.add(frequentPanel, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.PAGE_END);

		this.setContentPane(contentPanel);
		this.pack();
	}

	public void addOkActionListener(ActionListener listener) {
		okButton.addActionListener(listener);
	}

	public void showDialog() {
		setVisible(true);
	}

	public void hideDialog() {
		this.setVisible(false);
	}

	/** Sets the text displayed at the bottom of the frame. */
	void setLabel(String newText) {
		label.setText(newText);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = TrendConfigDialog.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/** Creates the panel shown by the first tab. */
	private JPanel createSimpleDialogBox() {
		JComboBox<String> teleParameters = new JComboBox<>(
				new String[] { "CAS 1 Hot 1 Temp", "CAS 1 Hot 1 Set Point", "CAS 1 Cold Set Point", "CAS 1 Humidity" });

		JButton plotColorButton = new JButton("Plot Color...");
		plotColorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Color newColor = JColorChooser.showDialog(TrendConfigDialog.this, "Choose Plot Color", Color.black);
			}
		});

		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		box.add(teleParameters);
		box.add(plotColorButton);

		return box;
	}

	/**
	 * Used by createSimpleDialogBox and createFeatureDialogBox to create a pane
	 * containing a description, a single column of radio buttons, and the Show
	 * it! button.
	 */
	private JPanel createPane(String description, JRadioButton[] radioButtons, JButton showButton) {

		int numChoices = radioButtons.length;
		JPanel box = new JPanel();
		JLabel label = new JLabel(description);

		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		box.add(label);

		for (int i = 0; i < numChoices; i++) {
			box.add(radioButtons[i]);
		}

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(box, BorderLayout.PAGE_START);
		// pane.add(showButton, BorderLayout.PAGE_END);
		return pane;
	}

	/**
	 * Like createPane, but creates a pane with 2 columns of radio buttons. The
	 * number of buttons passed in *must* be even.
	 */
	private JPanel create2ColPane(String description, JRadioButton[] radioButtons, JButton showButton) {
		JLabel label = new JLabel(description);
		int numPerColumn = radioButtons.length / 2;

		JPanel grid = new JPanel(new GridLayout(0, 2));
		for (int i = 0; i < numPerColumn; i++) {
			grid.add(radioButtons[i]);
			grid.add(radioButtons[i + numPerColumn]);
		}

		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		box.add(label);
		grid.setAlignmentX(0.0f);
		box.add(grid);

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(box, BorderLayout.PAGE_START);
		// pane.add(showButton, BorderLayout.PAGE_END);

		return pane;
	}

	/*
	 * Creates the panel shown by the 3rd tab. These dialogs are implemented
	 * using showMessageDialog, but you can specify the icon (using similar
	 * code) for any other kind of dialog, as well.
	 */
	private JPanel createIconDialogBox() {
		JButton showItButton = null;

		final int numButtons = 6;
		JRadioButton[] radioButtons = new JRadioButton[numButtons];
		final ButtonGroup group = new ButtonGroup();

		final String plainCommand = "plain";
		final String infoCommand = "info";
		final String questionCommand = "question";
		final String errorCommand = "error";
		final String warningCommand = "warning";
		final String customCommand = "custom";

		radioButtons[0] = new JRadioButton("Plain (no icon)");
		radioButtons[0].setActionCommand(plainCommand);

		radioButtons[1] = new JRadioButton("Information icon");
		radioButtons[1].setActionCommand(infoCommand);

		radioButtons[2] = new JRadioButton("Question icon");
		radioButtons[2].setActionCommand(questionCommand);

		radioButtons[3] = new JRadioButton("Error icon");
		radioButtons[3].setActionCommand(errorCommand);

		radioButtons[4] = new JRadioButton("Warning icon");
		radioButtons[4].setActionCommand(warningCommand);

		radioButtons[5] = new JRadioButton("Custom icon");
		radioButtons[5].setActionCommand(customCommand);

		for (int i = 0; i < numButtons; i++) {
			group.add(radioButtons[i]);
		}
		radioButtons[0].setSelected(true);

		return create2ColPane(iconDesc + ":", radioButtons, showItButton);
	}

	/** Creates the panel shown by the second tab. */
	private JPanel createFeatureDialogBox() {
		final int numButtons = 5;
		JRadioButton[] radioButtons = new JRadioButton[numButtons];
		final ButtonGroup group = new ButtonGroup();

		JButton showItButton = null;

		final String pickOneCommand = "pickone";
		final String textEnteredCommand = "textfield";
		final String nonAutoCommand = "nonautooption";
		final String customOptionCommand = "customoption";
		final String nonModalCommand = "nonmodal";

		radioButtons[0] = new JRadioButton("Pick one of several choices");
		radioButtons[0].setActionCommand(pickOneCommand);

		radioButtons[1] = new JRadioButton("Enter some text");
		radioButtons[1].setActionCommand(textEnteredCommand);

		radioButtons[2] = new JRadioButton("Non-auto-closing dialog");
		radioButtons[2].setActionCommand(nonAutoCommand);

		radioButtons[3] = new JRadioButton("Input-validating dialog " + "(with custom message area)");
		radioButtons[3].setActionCommand(customOptionCommand);

		radioButtons[4] = new JRadioButton("Non-modal dialog");
		radioButtons[4].setActionCommand(nonModalCommand);

		for (int i = 0; i < numButtons; i++) {
			group.add(radioButtons[i]);
		}
		radioButtons[0].setSelected(true);

		showItButton = new JButton("Show it!");
		showItButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = group.getSelection().getActionCommand();

				// pick one of many
				if (command == pickOneCommand) {
					Object[] possibilities = { "ham", "spam", "yam" };
					// String s = (String) JOptionPane.showInputDialog(frame,
					// "Complete the sentence:\n" + "\"Green eggs and...\"",
					// "Customized Dialog",
					// JOptionPane.PLAIN_MESSAGE, icon, possibilities, "ham");
					//
					// // If a string was returned, say so.
					// if ((s != null) && (s.length() > 0)) {
					// setLabel("Green eggs and... " + s + "!");
					// return;
					// }

					// If you're here, the return value was null/empty.
					setLabel("Come on, finish the sentence!");

					// text input
				} else if (command == textEnteredCommand) {
					// String s = (String) JOptionPane.showInputDialog(frame,
					// "Complete the sentence:\n" + "\"Green eggs and...\"",
					// "Customized Dialog",
					// JOptionPane.PLAIN_MESSAGE, icon, null, "ham");
					//
					// // If a string was returned, say so.
					// if ((s != null) && (s.length() > 0)) {
					// setLabel("Green eggs and... " + s + "!");
					// return;
					// }
					//
					// If you're here, the return value was null/empty.
					setLabel("Come on, finish the sentence!");

					// non-auto-closing dialog
				} else if (command == nonAutoCommand) {
					final JOptionPane optionPane = new JOptionPane("The only way to close this dialog is by\n"
							+ "pressing one of the following buttons.\n" + "Do you understand?",
							JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);

					// You can't use pane.createDialog() because that
					// method sets up the JDialog with a property change
					// listener that automatically closes the window
					// when a button is clicked.
					// final JDialog dialog = new JDialog(frame, "Click a
					// button", true);
					// dialog.setContentPane(optionPane);
					// dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
					// dialog.addWindowListener(new WindowAdapter() {
					// public void windowClosing(WindowEvent we) {
					// setLabel("Thwarted user attempt to close window.");
					// }
					// });
					// optionPane.addPropertyChangeListener(new
					// PropertyChangeListener() {
					// public void propertyChange(PropertyChangeEvent e) {
					// String prop = e.getPropertyName();
					//
					// if (dialog.isVisible() && (e.getSource() == optionPane)
					// && (JOptionPane.VALUE_PROPERTY.equals(prop))) {
					// // If you were going to check something
					// // before closing the window, you'd do
					// // it here.
					// dialog.setVisible(false);
					// }
					// }
					// });
					// dialog.pack();
					// dialog.setLocationRelativeTo(frame);
					// dialog.setVisible(true);

					int value = ((Integer) optionPane.getValue()).intValue();
					if (value == JOptionPane.YES_OPTION) {
						setLabel("Good.");
					} else if (value == JOptionPane.NO_OPTION) {
						setLabel("Try using the window decorations " + "to close the non-auto-closing dialog. "
								+ "You can't!");
					} else {
						setLabel("Window unavoidably closed (ESC?).");
					}

					// non-auto-closing dialog with custom message area
					// NOTE: if you don't intend to check the input,
					// then just use showInputDialog instead.
				} else if (command == customOptionCommand) {
					// customDialog.setLocationRelativeTo(frame);
					// customDialog.setVisible(true);
					//
					// String s = customDialog.getValidatedText();
					// if (s != null) {
					// // The text is valid.
					// setLabel("Congratulations! " + "You entered \"" + s +
					// "\".");
					// }

					// non-modal dialog
				} else if (command == nonModalCommand) {
					// Create the dialog.
					// final JDialog dialog = new JDialog(frame, "A Non-Modal
					// Dialog");

					// Add contents to it. It must have a close button,
					// since some L&Fs (notably Java/Metal) don't provide one
					// in the window decorations for dialogs.
					JLabel label = new JLabel("<html><p align=center>" + "This is a non-modal dialog.<br>"
							+ "You can have one or more of these up<br>" + "and still use the main window.");
					label.setHorizontalAlignment(JLabel.CENTER);
					Font font = label.getFont();
					label.setFont(label.getFont().deriveFont(font.PLAIN, 14.0f));

					JButton closeButton = new JButton("Close");
					// closeButton.addActionListener(new ActionListener() {
					// public void actionPerformed(ActionEvent e) {
					// dialog.setVisible(false);
					// dialog.dispose();
					// }
					// });
					JPanel closePanel = new JPanel();
					closePanel.setLayout(new BoxLayout(closePanel, BoxLayout.LINE_AXIS));
					closePanel.add(Box.createHorizontalGlue());
					closePanel.add(closeButton);
					closePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));

					JPanel contentPane = new JPanel(new BorderLayout());
					contentPane.add(label, BorderLayout.CENTER);
					contentPane.add(closePanel, BorderLayout.PAGE_END);
					contentPane.setOpaque(true);
					// dialog.setContentPane(contentPane);
					//
					// // Show it.
					// dialog.setSize(new Dimension(300, 150));
					// dialog.setLocationRelativeTo(frame);
					// dialog.setVisible(true);
				}
			}
		});

		return createPane(moreDialogDesc + ":", radioButtons, showItButton);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		// Create and set up the content pane.
		TrendConfigDialog frame = new TrendConfigDialog("Trend Configuration");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.showDialog();
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
