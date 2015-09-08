package cs.trend.demo;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TrendAppMenuBar extends JMenuBar {
	private static final long serialVersionUID = 2015090701;

	private JMenu fileMenu;

	private JMenu editMenu;

	private JMenu helpMenu;

	private JMenuItem openMenuItem;

	private JMenuItem saveMenuItem;

	private JMenuItem newTabMenuItem;

	private JMenuItem newWindowMenuItem;

	private JMenuItem configMenuItem;

	private JMenuItem aboutMenuItem;

	public void init() {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		add(fileMenu);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		add(editMenu);

		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		add(helpMenu);

		// a group of JMenuItems
		newTabMenuItem = new JMenuItem("New Tab", KeyEvent.VK_T);
		newWindowMenuItem = new JMenuItem("New Window", KeyEvent.VK_W);
		openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
		saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);

		fileMenu.add(newTabMenuItem);
		fileMenu.add(newWindowMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);

		configMenuItem = new JMenuItem("Configuration");
		editMenu.add(configMenuItem);

		aboutMenuItem = new JMenuItem("About");
		helpMenu.add(aboutMenuItem);
	}

	public JMenuItem getSaveMenuItem() {
		return saveMenuItem;
	}

	public JMenuItem getOpenMenuItem() {
		return openMenuItem;
	}

	public JMenu getFileMenu() {
		return fileMenu;
	}

	public JMenu getEditMenu() {
		return editMenu;
	}

	public JMenu getHelpMenu() {
		return helpMenu;
	}

	public JMenuItem getConfigMenuItem() {
		return configMenuItem;
	}

	public JMenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}

	public JMenuItem getNewTabMenuItem() {
		return newTabMenuItem;
	}

	public JMenuItem getNewWindowMenuItem() {
		return newWindowMenuItem;
	}

}
