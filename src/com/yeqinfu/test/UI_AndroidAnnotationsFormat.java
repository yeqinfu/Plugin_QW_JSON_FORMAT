package com.yeqinfu.test;

import com.intellij.openapi.ui.Messages;
import com.yeqinfu.test.utils.Utils_XmlPraser;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yeqinfu on 10/19/16.
 */
public class UI_AndroidAnnotationsFormat {
    private JTextArea textArea1;
    private JButton button1;
    private JPanel jpanel;
    JFrame frame;

    public void showDialog() {
        frame.setVisible(true);
    }

    public UI_AndroidAnnotationsFormat() {
        super();
        initUIAction();
    }

    public void initUIAction() {
        frame = new JFrame("xml 转android annotations");
        frame.setContentPane(jpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int Swing1x = 500;
        int Swing1y = 300;
        frame.setBounds(screensize.width / 2 - Swing1x / 2, screensize.height / 2 - Swing1y / 2, Swing1x, Swing1y);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeFile();
            }


        });
    }

    private void makeFile() {

        String str = textArea1.getText();
        Document document = Utils_XmlPraser.xmlToDocument(str);
        if (document!=null){
            // 获取根节点元素对象
            Element root = document.getRootElement();
            String result = Utils_XmlPraser.elementFindId(root);
            textArea1.setText(result);
        }else{
            Messages.showMessageDialog("爷,你xml格式错误啦,我解释不了", "Information", Messages.getInformationIcon());
        }
    }

    private formatInterface interfaceInsert;

    public formatInterface getInterfaceInsert() {
        return interfaceInsert;
    }

    public void setInterfaceInsert(formatInterface interfaceInsert) {
        this.interfaceInsert = interfaceInsert;
    }

    interface formatInterface {
        void insertResult(String insertStr);
    }
}
