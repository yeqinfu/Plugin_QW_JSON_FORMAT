package com.yeqinfu.test.mvp;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import java.io.IOException;

/**
 * Created by yeqinfu on 10/25/16.
 */
public class Action_QWMVPhelper extends AnAction {
    private AnActionEvent _event;//当前事件
    private Editor _editor;//editor
    private ClassModel _classModel;//model
    private String _content;//当前页面的内容
    private String _path;
    private Project _project;//当前项目对象
    private PsiJavaFile _javaFile; //当前事件所在的java
    private VirtualFile _currentFile;    //当前文件
    private PsiDirectory _currrentDir; //当前路径


    @Override
    public void actionPerformed(AnActionEvent e) {
        this._event = e;
        _editor = e.getData(PlatformDataKeys.EDITOR);
        _classModel = new ClassModel();
        _project = getEventProject(e);
        _javaFile = (PsiJavaFile) _event.getData(CommonDataKeys.PSI_FILE);
        _currentFile = DataKeys.VIRTUAL_FILE.getData(_event.getDataContext());
        _currrentDir=PsiDirectoryFactory.getInstance(_project).createDirectory(_currentFile);

        getClassModel();
        createFiles();
        createClassFiles();
        //刷新工程
        e.getProject().getBaseDir().refresh(false, true);

    }

    private void getClassModel() {
        _content = _editor.getDocument().getText();

        String[] words = _content.split(" ");

        for (String word : words) {
            if (word.contains("Contract")) {
                String className = word.substring(0, word.indexOf("Contract"));
                _classModel.set_className(className);
                _classModel.set_classFullName(word);
                MessagesCenter.showDebugMessage(className, "class name");
            }
        }
        if (null == _classModel.get_className()) {
            MessagesCenter.showErrorMessage("Create failed ,Can't found 'Contract' or 'Presenter' in your class name,your class name must contain 'Contract'", "error");
        }
    }

    /**
     * 生成 contract类内容
     * create Contract Model Presenter
     */
    private void createFiles() {
        if (null == _classModel.get_className()) {
            return;
        }
        //获取当前类所在的路径
        _path = ClassCreateHelper.getCurrentPath(_event, _classModel.get_classFullName());
        System.out.println("_path replace contract " + _path);
        setFileDocument();

    }

    /**
     * 生成 contract类内容
     * create Contract Model Presenter
     */
    private void setFileDocument() {
        int lastIndex = _content.lastIndexOf("}");
        _content = _content.substring(0, lastIndex);
        MessagesCenter.showDebugMessage(_content, "debug");
        final String content = setContractContent();
        //导入IBaseView 包
        PsiClass[] importClass =PsiShortNamesCache.getInstance(_project).getClassesByName("IBaseView",GlobalSearchScope.projectScope(_project));

        //wirte in runWriteAction
        WriteCommandAction.runWriteCommandAction(_project,
                new Runnable() {
                    @Override
                    public void run() {
                        _editor.getDocument().setText(content);
                        if (importClass != null&&importClass.length!=0) {//FIXME 导入包错误
//                            _javaFile.importClass(importClass[0]);
                        }
                    }
                });

    }

    private String setContractContent() {
        String content = _content + "interface " + _javaFile.getName().replace("Contract.java", "") + "View extends IBaseView {\n}\n\n"
                + "interface " + _javaFile.getName().replace("Contract.java", "") + "Model{\n}\n\n"
                + "\n}";

        return content;
    }

    /**
     * 创建class文件
     * create class files
     */
    private void createClassFiles() {
        //在当前包下,创建model impl类
        PsiClass clazzModel = JavaDirectoryService.getInstance()
                .createClass(_currrentDir.getParentDirectory(), _javaFile.getName().replace("Contract.java", "") + "ModelImpl");
        //在当前包下,创建model impl类
        PsiClass clazzView = JavaDirectoryService.getInstance()
                .createClass(_currrentDir.getParentDirectory(), _javaFile.getName().replace("Contract.java", "") + "ViewImpl");

        //写操作必须在子线程
        WriteCommandAction.runWriteCommandAction(_editor.getProject(),
                new Runnable() {
                    @Override
                    public void run() {
                        //添加package字段
                        ((PsiJavaFile) clazzModel.getContainingFile()).setPackageName(_javaFile.getPackageName());
                        //添加package字段
                        ((PsiJavaFile) clazzView.getContainingFile()).setPackageName(_javaFile.getPackageName());

                    }
                });
    }


}
