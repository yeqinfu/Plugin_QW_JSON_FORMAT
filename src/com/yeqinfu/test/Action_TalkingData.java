package com.yeqinfu.test;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by yeqinfu on 10/21/16.
 */
public class Action_TalkingData extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        UI_TalkingData ui_talkingData=new UI_TalkingData();
        ui_talkingData.showDialog();
    }
}
