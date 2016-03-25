package edu.nju.cs.inform.gui;

import edu.nju.cs.inform.core.diff.CodeElementsComparer;
import edu.nju.cs.inform.core.type.CodeElementChange;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Set;

/**
 * Created by Xufy on 2016/3/20.
 */
public class ProgressDialog extends Dialog{
    final static int FINISH = 9999;
    static ProgressBar progress;
    static TableItem[] CodeElementChanges;

    public ProgressDialog(Shell parentShell) {
        super(parentShell);
    }

    public static TableItem[] getTableItems(){
        return CodeElementChanges;
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        final Composite area = new Composite(parent, SWT.NULL);
        progress = new ProgressBar(area,SWT.NULL);
        progress.setMinimum(0);
        progress.setMaximum(100);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = Retro.screen_height / 50;
        layout.marginWidth = Retro.screen_width / 50;
        area.setLayout(layout);
        GridData progressStyle = new GridData();
        progressStyle.widthHint = Retro.screen_width / 10;
        progressStyle.heightHint = Retro.screen_height / 40;
        progress.setLayoutData(progressStyle);

        updateProgress();

        return area;

    }

    /**
     * 使用Thread更新线程
     */
    protected void updateProgress(){
        new Thread(new Runnable() {
            private int start = 0;
            private int end = 100;
            private static final int INCREMENT = 10;


            @Override
            public void run() {
                while(!progress.isDisposed()){
                    Display.getDefault().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            if(progress.getSelection() != end) {
                                progress.setSelection(start += INCREMENT);
                            }else{
                                getButton(FINISH).setEnabled(true);
                            }
                        }
                    });

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        //添加确定按钮
        Button finish = createButton(parent,FINISH,"Finish",true);
        finish.setEnabled(false);
        finish.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {

                close();
                //当点击对话框Finish按钮时，显示TableItem内容
//                call_new_code_location = "data/sample/AquaLush_Change4";
//                call_old_code_location = "data/sample/AquaLush_Change3";
                final CodeElementsComparer comparer = new CodeElementsComparer(Retro.call_new_code_location, Retro.call_old_code_location);
                comparer.diff();
                final Set<CodeElementChange> codeElementChangesList = comparer.getCodeElementChangesList();
                CodeElementChanges = new TableItem[codeElementChangesList.size()];
                int i = 0;
                for(CodeElementChange elementChange : codeElementChangesList){
                    TableItem item = new TableItem(Retro.codeElementsTable,0);
                    String[] codeInfo = {"" + ++i,elementChange.getElementName(),"" + elementChange.getElementType(), "" +elementChange.getChangeType()};
                    item.setText(codeInfo);
                    CodeElementChanges[i - 1] = item;
                }

            }
        });

    }

}
