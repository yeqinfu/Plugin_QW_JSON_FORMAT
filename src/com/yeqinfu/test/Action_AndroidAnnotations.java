package com.yeqinfu.test;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

/**
 * Created by yeqinfu on 10/19/16.
 */
public class Action_AndroidAnnotations extends AnAction {

    private Editor mEditor;
    private Project mProject;
    @Override
    public void actionPerformed(AnActionEvent e) {
        UI_AndroidAnnotationsFormat ui_androidAnnotationsFormat=new UI_AndroidAnnotationsFormat();
        ui_androidAnnotationsFormat.showDialog();
        mEditor = e.getData(PlatformDataKeys.EDITOR);
        mProject = e.getData(PlatformDataKeys.PROJECT);
        ui_androidAnnotationsFormat.setInterfaceInsert(new UI_AndroidAnnotationsFormat.formatInterface(){

            @Override
            public void insertResult(String insertStr) {
                insertSomething( insertStr);
            }
        });

    }

    public void insertSomething(String insertStr){
        Document document = mEditor.getDocument();
        SelectionModel selectionModel = mEditor.getSelectionModel();

        final int start = selectionModel.getSelectionStart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (document!=null){
                    document.insertString(start,insertStr);
                }

            }
        };
        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }
}
