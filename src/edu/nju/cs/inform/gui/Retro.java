package edu.nju.cs.inform.gui;

import edu.nju.cs.inform.core.diff.CodeElementsComparer;
import edu.nju.cs.inform.core.type.Artifact;
import edu.nju.cs.inform.core.type.CodeElementChange;
import edu.nju.cs.inform.util._;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.graphics.Point;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.Set;

import static edu.nju.cs.inform.gui.ProgressDialog.CodeElementChanges;

//import org.eclipse.swt.custom.*;

/**
 * Created by ruicosta on 2016/3/21.
 */
public class Retro {

    static Display display;
    static Shell entrance;
    static Shell retro;
    static Font boldFont;
    static Font normalFont;
    static String call_project_name;
    static String call_requirement_location;
    static String call_old_code_location;
    static String call_new_code_location;
    static Table codeElementsTable;
    static Table requirementElementsTable;
    static Text codeText;
    static Text requirementText;
    static String prevPath = "";
    static int newCount;

    static {
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
       /* entrance.setImage(new Image(entrance.getDisplay(), new ImageData(
                "src\\images\\icon_small.gif")));*/

        int entrance_width = LayoutConstants.screenWidth / 2;
        int entrance_height = LayoutConstants.screenHeight / 2;
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


        addCompositeWithoutButton(width, height, boldFont);
        addCompositeWithSingleButton(width, height, boldFont);
        addCompositeWithDoubleButton(width, height);

    }

    private static void addCompositeWithoutButton(int width, int height,
                                                  Font font) {
        Font file_font = new Font(entrance.getDisplay(), "Î¢ÈíÑÅºÚ", 12, SWT.NORMAL);
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
        Font file_font = new Font(entrance.getDisplay(), "Î¢ÈíÑÅºÚ", 12, SWT.NORMAL);


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

        // µÚÈý¸öÃæ°å
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

        // µÚËÄ¸öÃæ°å
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
                    fileFolder.setText("Select new version location");
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
        Font font = new Font(entrance.getDisplay(), "Î¢ÈíÑÅºÚ", 12, SWT.NORMAL);
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
                if(Retro.call_new_code_location!=null&&Retro.call_old_code_location!=null&&Retro.call_requirement_location!=null) {
                    entrance.setVisible(false);
                    retro.open();
                    retro.forceFocus();
                    final CodeElementsComparer comparer = new CodeElementsComparer(Retro.call_new_code_location, Retro.call_old_code_location);
                    comparer.diff();
                    final Set<CodeElementChange> codeElementChangesList = comparer.getCodeElementChangesList();
                    CodeElementChanges = new TableItem[codeElementChangesList.size()];
                    int i = 0;
                    for (CodeElementChange elementChange : codeElementChangesList) {
                        TableItem item = new TableItem(Retro.codeElementsTable, 0);
                        String[] codeInfo = {"" + ++i, elementChange.getElementName(), "" + elementChange.getElementType(), "" + elementChange.getChangeType()};
                        item.setText(codeInfo);
                        CodeElementChanges[i - 1] = item;
                    }
                }

            }
        });
    }

    private static void initRetro() {
        retro = new Shell(display, SWT.BORDER | SWT.MIN|SWT.MAX|SWT.RESIZE);
       /* Image icon = new Image(entrance.getDisplay(), new ImageData(
                "src\\images\\icon_small.gif"));
        retro.setImage(icon);*/
        retro.setText("Main");
        retro.setMaximized(true);
        retro.setSize(LayoutConstants.screenWidth,LayoutConstants.screenHeight);
        retro.setMinimumSize(LayoutConstants.retroMinSizeWidth, LayoutConstants.retroMinSizeHeight);

        Menu menuBar = new Menu(retro, SWT.BAR);
        fillInMenuBar(menuBar);

        int everyWidth = LayoutConstants.screenWidth / 2 - LayoutConstants.screenWidth / 25;
        final GridLayout retroLayout = new GridLayout(2, false);
        setRetroLayout(retroLayout,LayoutConstants.screenWidth);

        final Label codeElementsLabel = addLabel(retro,LayoutConstants.screenHeight, everyWidth, "Differing Code Elements", boldFont);
        final Label requirementElementsLabel = addLabel(retro,LayoutConstants.screenHeight, everyWidth, "Requirement Elements", boldFont);

        int tableHeight = LayoutConstants.screenHeight / 4;
        codeElementsTable = fillInElementsTable(new String[]{"No","Id","Type","Changed"},
                new int[]{everyWidth / 20, everyWidth / 2,everyWidth / 10, everyWidth * 2 / 5},
                everyWidth, tableHeight, normalFont, "Left DoubleClick on Tableitem to Show Call Paragraph");
        requirementElementsTable = fillInElementsTable(new String[]{"No","Score","Id","Status"},
                new int[]{everyWidth / 20, everyWidth / 2, everyWidth / 10, everyWidth * 2 / 5},
                everyWidth, tableHeight, normalFont, "Left DoubleClick on Status Column to Mark the State of Requirement");

        final Label codeTextLabel = addLabel(retro,LayoutConstants.screenHeight,everyWidth,"Code Text",boldFont);
        final Label requirementTextLabel = addLabel(retro,LayoutConstants.screenHeight,everyWidth,"Requirements Text",boldFont);

        int textHeight = LayoutConstants.screenHeight / 3;
        codeText = addText(everyWidth, textHeight);
        requirementText = addText(everyWidth, textHeight);

        //Retrieve
        final Button retrieve = new Button(retro, SWT.BUTTON1);
        retrieve.setText("Retrieve");
        setRetrieveButtonLayout(retrieve,LayoutConstants.screenWidth,LayoutConstants.screenHeight);
        //Table监听事件
        codeElementsTable.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(event.button==1){
                    TableItem[] itemList =codeElementsTable.getItems();
                    int listHaveChouse = codeElementsTable.getSelectionIndex();
                    String text=itemList[listHaveChouse].getText(1)+"("+itemList[listHaveChouse].getText(2)+")"+itemList[listHaveChouse].getText(3);
                    codeText.setText(text);
                    }
            }

        });


        retrieve.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                new ProgressDialog(retro).open();
                retrieve.setEnabled(false);
            }
        });

        retro.setLayout(retroLayout);
        retro.setMenuBar(menuBar);
        retro.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent controlEvent) {
                Point size = retro.getSize();
                int width = size.x;
                int height = size.y;
                setRetroLayout(retroLayout,width);
                int everyWidth = width / 2 - width / 25;
                setLabelLayout(codeElementsLabel,height,everyWidth);
                setLabelLayout(requirementElementsLabel,height,everyWidth);
                setLabelLayout(codeTextLabel,height,everyWidth);
                setLabelLayout(requirementTextLabel,height,everyWidth);
                int tableHeight = height / 4;
                setTableLayout(codeElementsTable,new int[]{everyWidth / 20, everyWidth / 2,everyWidth / 10, everyWidth * 2 / 5},
                        everyWidth,tableHeight);
                setTableLayout(requirementElementsTable,
                        new int[]{everyWidth / 20, everyWidth / 10, everyWidth / 2, everyWidth * 2 / 5},
                        everyWidth,tableHeight);
                int textHeight = height / 3;
                setTextLayout(codeText,everyWidth - width / 30,textHeight);
                setTextLayout(requirementText,everyWidth - width / 30,textHeight);
                setRetrieveButtonLayout(retrieve,width,height);
            }
        });
        retro.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                super.shellClosed(e);
                retro.dispose();
            }
        });
    }

    private static void setRetroLayout(final GridLayout layout,int width){
        layout.marginLeft = width / 200;
        layout.horizontalSpacing = width / 50;
    }

    private static Menu addMenu(Menu menuBar,String menuText,String[] menuItemTexts){
        MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
        menuHeader.setText(menuText);
        Menu menu = new Menu(retro, SWT.DROP_DOWN);
        menuHeader.setMenu(menu);
        if(menuItemTexts != null){
            for (int i = 0;i < menuItemTexts.length;i++){
                if(menuItemTexts[i] != null){
                    MenuItem item = new MenuItem(menu,SWT.PUSH);
                    item.setText(menuItemTexts[i]);
                }else{
                    @SuppressWarnings("unused")
                    MenuItem seperator = new MenuItem(menu, SWT.SEPARATOR);
                }
            }
        }

        return menu;
    }

    private static void fillInMenuBar(Menu menuBar) {

        Menu fileMenu = addMenu(menuBar,"&File",new String[]{"Start New Project","Load Project     ",
                "Load RTM              ","Save.....              ","Close Current Project",null,"Exit"});
        int exitItemIndex = 6;
        fileMenu.getItem(exitItemIndex).addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                retro.close();
            }
        });

        Menu actionMenu = addMenu(menuBar,"&Action",null);
        Menu optionsMenu = addMenu(menuBar,"&Options",null);

        Menu dataMenu = addMenu(menuBar,"&Data",new String[]{"Import","Export"});
        int importItemIndex = 0,exportItemIndex = 1;
        dataMenu.getItem(importItemIndex).setEnabled(false);
        dataMenu.getItem(exportItemIndex).setEnabled(false);

        Menu helpMenu = addMenu(menuBar,"&Help",new String[]{"Help","Export"});
    }

    private static Label addLabel(final Shell retro,int height, int everyWidth, String s, Font boldFont) {
        Label label = new Label(retro, SWT.VERTICAL | SWT.BEGINNING);
        label.setText(s);
        label.setFont(boldFont);
        setLabelLayout(label,height,everyWidth);
        return label;
    }

    private static void setLabelLayout(Label label,int height,int everyWidth){
        GridData layout = new GridData();
        layout.widthHint = everyWidth;
        layout.verticalIndent = height / 30;
        label.setLayoutData(layout);
    }

    private static Table fillInElementsTable(String[] s, int[] w,int everyWidth, int tableHeight, Font normalFont, String tip) {
        Table table = new Table(retro, SWT.IGNORE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setFont(normalFont);
        table.setToolTipText(tip);
        for(int i = 0;i < s.length;i++){
            TableColumn column = new TableColumn(table, SWT.CASCADE);
            column.setText(s[i]);
            column.setAlignment(SWT.LEFT);
            column.setWidth(w[i]);
        }
        setTableLayout(table,w,everyWidth,tableHeight);

        return table;
    }

    private static void setTableLayout(final Table table,int[] w,int width,int height){
        for(int i = 0;i < w.length;i++){
            table.getColumn(i).setWidth(w[i]);
        }
        GridData layout = new GridData();
        layout.widthHint = width;
        layout.heightHint = height;
        table.setLayoutData(layout);
    }

    private static Text addText(int everyWidth, int textHeight) {
        Text text = new Text(retro, SWT.V_SCROLL | SWT.BORDER);
        text.setText("");
        setTextLayout(text,everyWidth - LayoutConstants.screenWidth / 30,textHeight);
        return text;
    }

    private static void setTextLayout(final Text text,int width,int height){
        GridData layout = new GridData();
        layout.heightHint = height;
        layout.widthHint = width;
        text.setLayoutData(layout);
    }
    private static void setRetrieveButtonLayout(final Button retrieve,int width,int height){
        GridData retrieveLayout = new GridData();
        retrieveLayout.verticalIndent = height / 100;
        retrieveLayout.heightHint = height / 25;
        retrieveLayout.widthHint = width / 15;
        retrieve.setLayoutData(retrieveLayout);
    }
}