package edu.nju.cs.inform.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class Main extends JFrame {

	public Main() {
		super("Retro");
		Image icon_image = (new ImageIcon("src\\image\\icon_small.gif"))
				.getImage();
		setIconImage(icon_image);

		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		int screen_width = screen_size.width;
		int screen_height = screen_size.height;
		setBounds(0, 1, screen_width, screen_height);

		JMenuBar bar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenuItem start_new_project = new JMenuItem("Start New Project",
				new MenuItemBlankIcon_01());
		JMenuItem load_project = new JMenuItem("Load Project",
				new MenuItemBlankIcon_01());
		JMenuItem load_RTM = new JMenuItem("Load RTM",
				new MenuItemBlankIcon_01());
		JMenuItem save_item = new JMenuItem("Save...",
				new MenuItemBlankIcon_01());
		JMenuItem close_current_project = new JMenuItem(
				"Close Current Project", new MenuItemBlankIcon_02());
		JMenuItem exit = new JMenuItem("Exit", new MenuItemBlankIcon_01());
		file.add(start_new_project);
		file.add(load_project);
		file.add(load_RTM);
		file.add(save_item);
		file.add(close_current_project);
		file.add(exit);

		JMenu action = new JMenu("Action");
		JMenu options = new JMenu("Options");

		JMenu data = new JMenu("Data");
		JMenuItem import_item = new JMenuItem("Import",
				new MenuItemBlankIcon_01());
		JMenuItem export_item = new JMenuItem("Export",
				new MenuItemBlankIcon_01());
		data.add(import_item);
		data.add(export_item);

		JMenu help = new JMenu("Help");
		JMenuItem help_item = new JMenuItem("Help", new MenuItemBlankIcon_01());
		JMenuItem about_item = new JMenuItem("About",
				new MenuItemBlankIcon_01());
		help.add(help_item);
		help.add(about_item);

		JMenu[] menus = { file, action, options, data, help };

		for (final JMenu menu : menus) {
			menu.setBorder(BorderFactory.createLineBorder(new Color(176, 176,
					176, 0), 1));
			bar.add(menu);
			menu.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					super.mouseEntered(e);
					menu.setBorder(BorderFactory.createLineBorder((new Color(
							176, 176, 176)), 1));
					menu.setOpaque(true);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					super.mouseExited(e);
					menu.setBorder(BorderFactory.createLineBorder((new Color(
							176, 176, 176, 0)), 1));
					menu.setOpaque(false);
				}
			});
		}

		setJMenuBar(bar);
		
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	class MenuItemBlankIcon_01 implements Icon {

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(new Color(200, 200, 200, 100));
			g.drawLine(20, 0, 20, 20);
		}

		@Override
		public int getIconWidth() {
			return 20;
		}

		@Override
		public int getIconHeight() {
			return 10;
		}

	}

	class MenuItemBlankIcon_02 implements Icon {

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(new Color(200, 200, 200, 100));
			g.drawLine(20, 0, 20, 20);
			g.drawLine(20, 20, 200, 20);
		}

		@Override
		public int getIconWidth() {
			return 20;
		}

		@Override
		public int getIconHeight() {
			return 10;
		}

	}
}