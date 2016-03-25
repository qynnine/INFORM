package edu.nju.cs.inform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.awt.*;

//import org.eclipse.swt.custom.*;

public class Retro {
	static Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
	final static int screen_width = screen_size.width;
	final static int screen_height = screen_size.height;

	static Display display;// ����swt�����ϵͳ֮���ͨ��
	static Shell entrance;// ��������
	static Shell retro;// ������
	static Font boldFont;
	static Font normalFont;
	static String call_project_name;
	static String call_requirement_location;
	static String call_old_code_location;
	static String call_new_code_location;

	static String prevPath = "";// ��һ��ѡ����ļ��е�·��
	static int newCount;// Project������,ĳһ��ʱ��ֻ�������һ��

	static {// �Ծ�̬��Ա�����ĳ�ʼ��
		display = new Display();
		retro = new Shell(display);
		entrance = new Shell(retro, SWT.CLOSE);
		boldFont = new Font(display,"Arial",14,SWT.BOLD);
		normalFont = new Font(display,"Arial",10,SWT.NORMAL);
		
	}

	public Retro() {

	}

	public static void main(String args[]) {
		initRetro();
		initEntrance();
		entrance.open();
		while (!retro.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void initEntrance() {
		entrance.setText("START A NEW PROJECT");
		entrance.setImage(new Image(entrance.getDisplay(), new ImageData(
				"images/icon_small.gif")));

		int entrance_width = screen_width / 2;
		int entrance_height = screen_height / 2;
		entrance.setBounds(entrance_width / 2, entrance_height / 2,
				entrance_width, entrance_height);
		entrance.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				super.shellClosed(e);
				retro.dispose();
			}
		});
		setEntranceUI(entrance_width, entrance_height);
	}

	private static void setEntranceUI(int width, int height) {
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = height / 10;
		layout.verticalSpacing = height / 24;
		entrance.setLayout(layout);

		Font font = new Font(entrance.getDisplay(), "΢���ź�", 14, SWT.BOLD);
		addCompositeWithoutButton(width, height, font);
		addCompositeWithSingleButton(width, height, font);
		addCompositeWithDoubleButton(width, height);
	}

	private static void addCompositeWithoutButton(int width, int height,
			Font font) {
		Font file_font = new Font(entrance.getDisplay(), "΢���ź�", 12, SWT.NORMAL);
		Composite composite = new Composite(entrance, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		Label label = new Label(composite, SWT.VERTICAL | SWT.BEGINNING);
		label.setText("  Project name:");
		label.setFont(font);
		GridData label_style = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		label_style.heightHint = height / 12;
		label_style.widthHint = width * 13 / 32;
		label.setLayoutData(label_style);

		final Text txt_project_name = new Text(composite, SWT.SINGLE
				| SWT.VERTICAL | SWT.BORDER);
		txt_project_name.setFont(file_font);
		GridData text_style = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		text_style.heightHint = height / 18;
		text_style.widthHint = width * 2 / 5;
		txt_project_name.setLayoutData(text_style);
	}

	private static void addCompositeWithSingleButton(int width, int height,
			Font font) {
		Font file_font = new Font(entrance.getDisplay(), "΢���ź�", 12, SWT.NORMAL);

		// �ڶ������
		Composite rcomposite = new Composite(entrance, SWT.NONE);
		GridLayout rlayout = new GridLayout(3, false);
		rcomposite.setLayout(rlayout);
		Label rlabel = new Label(rcomposite, SWT.VERTICAL | SWT.BEGINNING);
		rlabel.setText("  Requirement location:");
		rlabel.setFont(font);
		GridData rlabel_style = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		rlabel_style.heightHint = height / 12;
		rlabel_style.widthHint = width * 13 / 32;
		rlabel.setLayoutData(rlabel_style);

		final Text rtext = new Text(rcomposite, SWT.SINGLE | SWT.VERTICAL
				| SWT.BORDER);
		rtext.setFont(file_font);
		GridData rtext_style = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		rtext_style.verticalIndent = 2;
		rtext_style.heightHint = height / 18;
		rtext_style.widthHint = width * 2 / 5;
		rtext.setLayoutData(rtext_style);

		Button rbutton = new Button(rcomposite, SWT.BUTTON1);
		rbutton.setFont(file_font);
		rbutton.setText("Choose");
		GridData rbutton_style = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		rbutton_style.verticalIndent = 1;
		rbutton_style.heightHint = height / 14;
		rbutton_style.widthHint = width / 9;
		rbutton.setLayoutData(rbutton_style);
		rbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				DirectoryDialog fileFolder = new DirectoryDialog(entrance,
						SWT.SAVE);
				try {
					fileFolder.setText("Select old version location");
					fileFolder.setFilterPath(prevPath);
					call_requirement_location = fileFolder.open();
					rtext.setText(call_requirement_location);
					Retro.prevPath = call_requirement_location;
				} catch (Exception exception) {

				}

			}
		});

		// ��������
		Composite ocomposite = new Composite(entrance, SWT.NONE);
		GridLayout olayout = new GridLayout(3, false);
		ocomposite.setLayout(olayout);
		Label olabel = new Label(ocomposite, SWT.VERTICAL | SWT.BEGINNING);
		olabel.setText("  Code location(old version):");
		olabel.setFont(font);
		GridData olabel_style = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		olabel_style.heightHint = height / 12;
		olabel_style.widthHint = width * 13 / 32;
		olabel.setLayoutData(olabel_style);

		final Text otext = new Text(ocomposite, SWT.SINGLE | SWT.VERTICAL
				| SWT.BORDER);
		otext.setFont(file_font);
		GridData otext_style = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		otext_style.verticalIndent = 2;
		otext_style.heightHint = height / 18;
		otext_style.widthHint = width * 2 / 5;
		otext.setLayoutData(otext_style);

		Button obutton = new Button(ocomposite, SWT.BUTTON1);
		obutton.setFont(file_font);
		obutton.setText("Choose");
		GridData obutton_style = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		obutton_style.verticalIndent = 1;
		obutton_style.heightHint = height / 14;
		obutton_style.widthHint = width / 9;
		obutton.setLayoutData(obutton_style);
		obutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				DirectoryDialog fileFolder = new DirectoryDialog(entrance,
						SWT.SAVE);
				try {
					fileFolder.setText("Select old version location");
					fileFolder.setFilterPath(prevPath);
					call_old_code_location = fileFolder.open();
					otext.setText(call_old_code_location);
					Retro.prevPath = call_old_code_location;
				} catch (Exception exception) {

				}

			}
		});

		// ���ĸ����
		Composite composite = new Composite(entrance, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		Label label = new Label(composite, SWT.VERTICAL | SWT.BEGINNING);
		label.setText("  Code location(new version):");
		label.setFont(font);
		GridData label_style = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		label_style.heightHint = height / 12;
		label_style.widthHint = width * 13 / 32;
		label.setLayoutData(label_style);

		final Text text = new Text(composite, SWT.SINGLE | SWT.VERTICAL
				| SWT.BORDER);
		text.setFont(file_font);
		GridData text_style = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		text_style.verticalIndent = 2;
		text_style.heightHint = height / 18;
		text_style.widthHint = width * 2 / 5;
		text.setLayoutData(text_style);

		Button button = new Button(composite, SWT.BUTTON1);
		button.setFont(file_font);
		button.setText("Choose");
		GridData button_style = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button_style.verticalIndent = 1;
		button_style.heightHint = height / 14;
		button_style.widthHint = width / 9;
		button.setLayoutData(button_style);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				DirectoryDialog fileFolder = new DirectoryDialog(entrance,
						SWT.SAVE);
				try {
					fileFolder.setText("Select old version location");
					fileFolder.setFilterPath(prevPath);
					call_new_code_location = fileFolder.open();
					text.setText(call_new_code_location);
					Retro.prevPath = call_new_code_location;
				} catch (Exception exception) {

				}

			}
		});
	}

	private static void addCompositeWithDoubleButton(int width, int height) {
		Font font = new Font(entrance.getDisplay(), "΢���ź�", 12, SWT.NORMAL);
		Composite composite = new Composite(entrance, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginHeight = height / 30;
		layout.marginWidth = width / 6;
		layout.spacing = width / 3;
		composite.setLayout(layout);

		Button btn_cancel = new Button(composite, SWT.BUTTON1);
		btn_cancel.setFont(font);
		btn_cancel.setText("Cancel");
		btn_cancel.setLayoutData(new RowData(width / 6, height / 12));
		btn_cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				super.mouseUp(e);
				newCount = 0;
				retro.close();
			}
		});

		Button btn_finish = new Button(composite, SWT.BUTTON1);
		btn_finish.setFont(font);
		btn_finish.setText("Finish");
		btn_finish.setLayoutData(new RowData(width / 6, height / 12));
		btn_finish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				entrance.setVisible(false);
				retro.open();
				retro.forceFocus();
			}
		});
	}

	private static void initRetro() {
		retro = new Shell(display, SWT.BORDER | SWT.MIN);
		Image icon = new Image(entrance.getDisplay(), new ImageData(
				"images/icon_small.gif"));
		retro.setImage(icon);
		retro.setText("Main");
		retro.setMaximized(true);// Ĭ�ϴ������

		Menu menuBar = new Menu(retro, SWT.BAR);// ��Ӳ˵���

		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");
		Menu fileMenu = new Menu(retro, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);
		MenuItem newProjectItem = new MenuItem(fileMenu, SWT.PUSH);
		newProjectItem.setText("Start New Project");
		MenuItem loadProjectItem = new MenuItem(fileMenu, SWT.PUSH);
		loadProjectItem.setText("Load Project     ");
		MenuItem loadRTMItem = new MenuItem(fileMenu, SWT.PUSH);
		loadRTMItem.setText("Load RTM              ");
		MenuItem saveProjectItem = new MenuItem(fileMenu, SWT.PUSH);
		saveProjectItem.setText("Save.....              ");
		MenuItem closeProjectItem = new MenuItem(fileMenu, SWT.PUSH);
		closeProjectItem.setText("Close Current Project");
		@SuppressWarnings("unused")
		MenuItem seperator = new MenuItem(fileMenu, SWT.SEPARATOR);
		MenuItem exit = new MenuItem(fileMenu, SWT.PUSH);
		exit.setText("Exit");
		exit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				retro.close();
			}
		});

		MenuItem actionMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		actionMenuHeader.setText("&Action");
		Menu actionMenu = new Menu(retro, SWT.DROP_DOWN);
		actionMenuHeader.setMenu(actionMenu);

		MenuItem optionsMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		optionsMenuHeader.setText("&Options");
		Menu optionsMenu = new Menu(retro, SWT.DROP_DOWN);
		optionsMenuHeader.setMenu(optionsMenu);

		MenuItem dataMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		dataMenuHeader.setText("&Data");
		Menu dataMenu = new Menu(retro, SWT.DROP_DOWN);
		dataMenuHeader.setMenu(dataMenu);
		MenuItem importItem = new MenuItem(dataMenu, SWT.PUSH);
		importItem.setText("Import");
		importItem.setEnabled(false);
		MenuItem exportItem = new MenuItem(dataMenu, SWT.PUSH);
		exportItem.setText("Export");
		exportItem.setEnabled(false);

		MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuHeader.setText("&Help");
		Menu helpMenu = new Menu(retro, SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);
		MenuItem helpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpItem.setText("Help");
		MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
		aboutItem.setText("Export");
		
		//ҳ�沼��
		int everyWidth = screen_width / 2 - screen_width / 25;
		GridLayout retroLayout = new GridLayout(2,false);
		retroLayout.marginTop = screen_height / 80;
		retroLayout.marginLeft = screen_width / 100;
		retroLayout.horizontalSpacing = screen_width / 50;
		Label codeElementsTitle = new Label(retro,SWT.VERTICAL|SWT.BEGINNING);
		codeElementsTitle.setText("Differing Code Elements");
		codeElementsTitle.setFont(boldFont);
		GridData codeElementsTitleLayout = new GridData();
		codeElementsTitleLayout.widthHint = everyWidth;
		codeElementsTitle.setLayoutData(codeElementsTitleLayout);
		
		Label requirementElementsTitle = new Label(retro,SWT.VERTICAL|SWT.BEGINNING);
		requirementElementsTitle.setText("Requirement Elements");
		requirementElementsTitle.setFont(boldFont);
		GridData requirementElementsTitleLayout = new GridData();
		requirementElementsTitleLayout.widthHint = everyWidth;
		requirementElementsTitle.setLayoutData(requirementElementsTitleLayout);
		
		//Code Elements���
		int tableHeight = screen_height / 5;
		final Table codeElementsTable = new Table(retro,SWT.IGNORE);
		codeElementsTable.setLinesVisible(true);
		codeElementsTable.setHeaderVisible(true);
		codeElementsTable.setFont(normalFont);
		codeElementsTable.setToolTipText("Right Click on Tableitem to Show Call Paragraph");
		TableColumn codeNo = new TableColumn(codeElementsTable, SWT.CASCADE);
		codeNo.setText("No");
		codeNo.setAlignment(SWT.CENTER);
		codeNo.setWidth(everyWidth / 20);
		TableColumn codeId = new TableColumn(codeElementsTable, SWT.CASCADE);
		codeId.setText("Id");
		codeId.setAlignment(SWT.CENTER);
		codeId.setWidth(everyWidth / 2);
		TableColumn codeType = new TableColumn(codeElementsTable,SWT.CASCADE);
		codeType.setText("Type");
		codeType.setAlignment(SWT.CENTER);
		codeType.setWidth(everyWidth / 10);
		TableColumn codeChanged = new TableColumn(codeElementsTable,SWT.CASCADE);
		codeChanged.setText("Changed");
		codeChanged.setAlignment(SWT.CENTER);
		codeChanged.setWidth(everyWidth *  2/ 5);
		GridData codeElementsTableLayout = new GridData();
		codeElementsTableLayout.widthHint = everyWidth;
		codeElementsTableLayout.heightHint = tableHeight;
		codeElementsTable.setLayoutData(codeElementsTableLayout);
		
		//Requirement Elements���
		final Table requirementElementsTable = new Table(retro,SWT.IGNORE);
		requirementElementsTable.setLinesVisible(true);
		requirementElementsTable.setHeaderVisible(true);
		requirementElementsTable.setFont(normalFont);
		requirementElementsTable.setToolTipText("Right Click on Status Column to Mark the State of Requirement");
		TableColumn requirementNo = new TableColumn(requirementElementsTable, SWT.CASCADE);
		requirementNo.setText("No");
		requirementNo.setAlignment(SWT.CENTER);
		requirementNo.setWidth(everyWidth / 20);
		TableColumn requirementScore = new TableColumn(requirementElementsTable,SWT.CASCADE);
		requirementScore.setText("Score");
		requirementScore.setAlignment(SWT.CENTER);
		requirementScore.setWidth(everyWidth / 10);
		TableColumn requirementId = new TableColumn(requirementElementsTable,SWT.CASCADE);
		requirementId.setText("Id");
		requirementId.setAlignment(SWT.CENTER);
		requirementId.setWidth(everyWidth / 2);
		TableColumn requirementStatus = new TableColumn(requirementElementsTable, SWT.CASCADE);
		requirementStatus.setText("Status");
		requirementStatus.setAlignment(SWT.CENTER);
		requirementStatus.setWidth(everyWidth * 2  / 5);
		GridData requirementElementsTableLayout = new GridData();
		requirementElementsTableLayout.widthHint = everyWidth;
		requirementElementsTableLayout.heightHint = tableHeight;
		requirementElementsTable.setLayoutData(requirementElementsTableLayout);

		Label codeTextLabel = new Label(retro,SWT.VIRTUAL|SWT.BEGINNING);
		codeTextLabel.setText("Code Text");
		codeTextLabel.setFont(boldFont);
		GridData codeTextLabelLayout = new GridData();
		codeTextLabelLayout.verticalIndent = screen_height / 30;
		codeTextLabelLayout.widthHint = everyWidth;
		codeTextLabel.setLayoutData(codeTextLabelLayout);
		Label requirementTextLabel = new Label(retro,SWT.VIRTUAL|SWT.BEGINNING);
		requirementTextLabel.setText("Requirements Text");
		requirementTextLabel.setFont(boldFont);
		GridData requirementTextLayoutLabel = new GridData();
		requirementTextLayoutLabel.verticalIndent = screen_height / 30;
		requirementTextLayoutLabel.widthHint = everyWidth;
		requirementTextLabel.setLayoutData(requirementTextLayoutLabel);

		//�ı���
		int textHeight = screen_height / 3;
		Text codeText = new Text(retro,SWT.V_SCROLL|SWT.BORDER);
		codeText.setText("Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!");
		GridData codeTextLayout = new GridData();
		codeTextLayout.heightHint = textHeight;
		codeTextLayout.widthHint = everyWidth - screen_width / 30;
		codeText.setLayoutData(codeTextLayout);
		Text requirementText = new Text(retro,SWT.V_SCROLL|SWT.BORDER);
		requirementText.setText("Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!");
		GridData requirementTextLayout = new GridData();
		requirementTextLayout.heightHint = textHeight;
		requirementTextLayout.widthHint = everyWidth - screen_width / 30;
		requirementText.setLayoutData(requirementTextLayout);
		
		//�����
		//Retrieve
		Button retrieve = new Button(retro,SWT.BUTTON1);
		retrieve.setText("Retrieve");
		GridData retrieveLayout = new GridData();
		retrieveLayout.verticalIndent = screen_height / 30;
		retrieveLayout.heightHint = screen_height / 25;
		retrieveLayout.widthHint = screen_width / 15;
		retrieve.setLayoutData(retrieveLayout);
		retrieve.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				
			}
		});
		
		retro.setLayout(retroLayout);
		retro.setMenuBar(menuBar);
		retro.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				super.shellClosed(e);
				retro.dispose();
			}
		});
	}
}