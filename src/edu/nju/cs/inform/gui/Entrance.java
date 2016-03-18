package edu.nju.cs.inform.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Entrance extends JFrame implements ActionListener {

	Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
	final public int screen_width = screen_size.width;// �������Ļ�Ŀ�
	final public int screen_height = screen_size.height;// �������Ļ�ĸ�

	private JTextField project_name;
	private JTextField requirement_location;
	private JTextField old_code_location;
	private JTextField new_code_location;
	private JButton btn_requirement_location;
	private JButton btn_old_code_location;
	private JButton btn_new_code_location;
	
	private static String call_project_name;
	private static String call_requirement_location;
	private static String call_old_code_location;
	private static String call_new_code_location;

	private JButton btn_cancel;
	private JButton btn_finish;

	public Entrance(String title) {
		super(title);
		Image icon_image = (new ImageIcon("src\\image\\icon_small.gif"))
				.getImage();
		setIconImage(icon_image);
		setBounds(screen_width / 4, screen_height / 4, screen_width / 2,
				screen_height / 2);
		setLayout(new GridLayout(6, 1, 1, 2));

		int frame_width = screen_width / 4;
		int frame_height = screen_height / 4;

		final Font font1 = new Font("SansSerif", Font.BOLD, 18);
		JPanel blank = new JPanel();
		JPanel row1 = new JPanel();
		row1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lab_project_name = new JLabel("    Project name:");
		lab_project_name.setFont(font1);
		project_name = new JTextField();
		project_name.setColumns(15);
		project_name.setFont(font1);
		row1.add(lab_project_name);
		row1.add(project_name);

		JPanel row2 = createPanel1(font1);

		JPanel row3 = new JPanel();
		row3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lab_old_code_location = new JLabel(
				"    Code location(old version):");
		lab_old_code_location.setFont(font1);
		old_code_location = new JTextField();
		old_code_location.setColumns(15);
		old_code_location.setFont(font1);
		btn_old_code_location = new JButton("Choose");
		btn_old_code_location.addActionListener(this);
		row3.add(lab_old_code_location);
		row3.add(old_code_location);
		row3.add(btn_old_code_location);

		JPanel row4 = new JPanel();
		row4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lab_new_code_location = new JLabel(
				"    Code location(new version):");
		lab_new_code_location.setFont(font1);
		new_code_location = new JTextField();
		new_code_location.setColumns(15);
		new_code_location.setFont(font1);
		btn_new_code_location = new JButton("Choose");
		btn_new_code_location.addActionListener(this);
		row4.add(lab_new_code_location);
		row4.add(new_code_location);
		row4.add(btn_new_code_location);

		JPanel row5 = new JPanel();
		row5.setLayout(null);
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(frame_width * 13 / 34, 0, frame_width * 10 / 34,
				frame_height * 3 / 19);
		btn_cancel.addActionListener(this);
		btn_finish = new JButton("Finish");
		btn_finish.setBounds(frame_width * 45 / 34, 0, frame_width * 10 / 34,
				frame_height * 3 / 19);
		btn_finish.addActionListener(this);
		row5.add(btn_cancel);
		row5.add(btn_finish);

		add(blank);
		add(row1);
		add(row2);
		add(row3);
		add(row4);
		add(row5);
		setResizable(false);
		setVisible(true);
	}

	private JPanel createPanel1(Font font1) {
		JPanel row2 = new JPanel();
		row2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lab_requirement_location = new JLabel(
				"    Requirement location:");
		lab_requirement_location.setFont(font1);
		requirement_location = new JTextField();
		requirement_location.setColumns(15);
		requirement_location.setFont(font1);
		btn_requirement_location = new JButton("Choose");
		btn_requirement_location.addActionListener(this);
		row2.add(lab_requirement_location);
		row2.add(requirement_location);
		row2.add(btn_requirement_location);
		return row2;
	}

	public String getCall_project_name() {
		return call_project_name;
	}

	public String getCall_requirement_location() {
		return call_requirement_location;
	}

	public String getCall_old_code_location() {
		return call_old_code_location;
	}

	public String getCall_new_code_location() {
		return call_new_code_location;
	}

	public static void main(String[] args) {
		new Entrance("START A NEW PROJECT");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton obj = (JButton) e.getSource();
		JFileChooser chooser;
		if (obj == btn_requirement_location) {
			chooser = new JFileChooser();
			int result = chooser.showOpenDialog(getParent());
			if (result == JFileChooser.APPROVE_OPTION) {
				call_requirement_location = chooser.getSelectedFile()
						.toString();
				requirement_location.setText(call_requirement_location);
				requirement_location.requestFocus();
			} else if (result == JFileChooser.CANCEL_OPTION) {
				requirement_location.requestFocus();
			}
		} else if (obj == btn_old_code_location) {
			chooser = new JFileChooser();
			int result = chooser.showOpenDialog(getParent());
			if (result == JFileChooser.APPROVE_OPTION) {
				call_old_code_location = chooser.getSelectedFile().toString();
				old_code_location.setText(call_old_code_location);
				old_code_location.requestFocus();
			} else if (result == JFileChooser.CANCEL_OPTION) {
				old_code_location.requestFocus();
			}
		} else if (obj == btn_new_code_location) {
			chooser = new JFileChooser();
			int result = chooser.showOpenDialog(getParent());
			if (result == JFileChooser.APPROVE_OPTION) {
				call_new_code_location = chooser.getSelectedFile().toString();
				new_code_location.setText(call_new_code_location);
				new_code_location.requestFocus();
			} else if (result == JFileChooser.CANCEL_OPTION) {
				new_code_location.requestFocus();
			}
		} else if (obj == btn_cancel) {
			 System.exit(0);
		} else if (obj == btn_finish) {
			call_project_name = project_name.getText();
			setVisible(false);
			new Main();
		}
	}
}
