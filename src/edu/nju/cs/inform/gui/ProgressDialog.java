package edu.nju.cs.inform.gui;

import edu.nju.cs.inform.core.diff.CodeElementsComparer;
import edu.nju.cs.inform.core.ir.IRModelConst;
import edu.nju.cs.inform.core.ir.Retrieval;
import edu.nju.cs.inform.core.type.ArtifactsCollection;
import edu.nju.cs.inform.core.type.CodeElementChange;
import edu.nju.cs.inform.core.type.SimilarityMatrix;
import edu.nju.cs.inform.io.ArtifactsReader;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Map;
import java.util.Set;

import static edu.nju.cs.inform.gui.Retro.*;

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
        area.setBounds(LayoutConstants.screenWidth / 2 - LayoutConstants.screenWidth / 30,LayoutConstants.screenHeight / 2 - LayoutConstants.screenHeight / 60,
                LayoutConstants.screenWidth / 15,LayoutConstants.screenHeight / 30);
        addProgressBar(area);
        /*progress = new ProgressBar(area,SWT.NULL);
        progress.setMinimum(0);
        progress.setMaximum(100);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = Retro.screen_height / 50;
        layout.marginWidth = Retro.screen_width / 50;
        area.setLayout(layout);
        GridData progressStyle = new GridData();
        progressStyle.widthHint = Retro.screen_width / 10;
        progressStyle.heightHint = Retro.screen_height / 40;
        progress.setLayoutData(progressStyle);*/

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
            private static final int INCREMENT = 25;


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
                CodeElementsComparer comparer = new CodeElementsComparer(call_new_code_location, call_old_code_location);
                comparer.diff();

                // get change description from code changes
                ArtifactsCollection changeDescriptionCollection = comparer.getChangeDescriptionCollection();
                final ArtifactsCollection requirementCollection = ArtifactsReader.getCollections(call_requirement_location, ".txt");

                // retrieval change description to requirement
                Retrieval retrieval = new Retrieval(changeDescriptionCollection, requirementCollection, IRModelConst.VSM);
                retrieval.tracing();


                SimilarityMatrix similarityMatrix = retrieval.getSimilarityMatrix();
                Map<String, Double> candidatedOutdatedRequirementsRank = retrieval.getCandidateOutdatedRequirementsRank();
                int rindex=1;
                for(Map.Entry<String,Double> map:candidatedOutdatedRequirementsRank.entrySet()){
                    TableItem item = new TableItem(requirementElementsTable,SWT.NONE);
                    item.setText(new String[]{String.valueOf(rindex++),String.valueOf(map.getValue()),map.getKey(),"default"});
                    //System.out.println(map.getKey()+"\t"+map.getValue());
                }
                requirementElementsTable.addListener(SWT.MouseDoubleClick,new Listener(){

                    @Override
                    public void handleEvent(Event event) {
                        if(event.button==1){
                            TableItem[] ritemList=requirementElementsTable.getItems();
                            int lisheHaveChouse=requirementElementsTable.getSelectionIndex();
                            String idname=ritemList[lisheHaveChouse].getText(2);
                            Retro.requirementText.setText(requirementCollection.get(idname).text);
                        }
                    }
                });

            }
        });
        GridData buttonStyle = new GridData();
        buttonStyle.widthHint = LayoutConstants.screenWidth / 20;
        buttonStyle.heightHint = LayoutConstants.screenHeight / 30;
        buttonStyle.verticalIndent = LayoutConstants.screenHeight / 60;
        buttonStyle.horizontalIndent = LayoutConstants.screenWidth / 40;
        finish.setLayoutData(buttonStyle);

    }

    private void addProgressBar(Composite parent){
        progress = new ProgressBar(parent,SWT.NULL);
        progress.setMinimum(0);
        progress.setMaximum(100);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = LayoutConstants.screenHeight / 50;
        layout.marginWidth = LayoutConstants.screenWidth / 50;
        parent.setLayout(layout);
        GridData progressStyle = new GridData();
        progressStyle.widthHint = LayoutConstants.screenWidth / 10;
        progressStyle.heightHint = LayoutConstants.screenHeight / 30;
        progress.setLayoutData(progressStyle);
    }

}
