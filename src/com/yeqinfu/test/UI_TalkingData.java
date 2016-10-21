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
 * Created by yeqinfu on 10/21/16.
 */
public class UI_TalkingData {
    private JTextArea textArea1;
    private JButton button1;
    private JPanel jpanel;
    JFrame frame;

    public void showDialog() {
        frame.setVisible(true);
    }

    public UI_TalkingData() {
        super();
        initUIAction();
    }

    public void initUIAction() {
        frame = new JFrame("字符转统计");
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
        String result="";
        result+="===================================变量==================\n";
        result+=checkFileByLine(str);
        result+="===================================代码==================\n";
        result+=checkFileByLine2(str);
        result+="===================================属性==================\n";
        result+=checkFileByLine3(str);
        textArea1.setText(result);

    }

    /**
     * 读取每一行，中文会乱码 生成 静态变量
     *
     * @param multiLine
     */
    private String checkFileByLine(String multiLine) {
        String[] lines = multiLine.split("\n");
        String result = "";
        String[] arrs = null;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                line.replace("-", "_");
                arrs = line.split("\\s+");
                result += "public static String " + arrs[0] + "=" + "\"" + arrs[0] + "\"" + ";//" + arrs[1] + "\n";
            }

        }
        return result;
    }

    /**
     * 读取每一行，中文会乱码 生成猪哥注入代码
     * <p>
     * // TD v4.0 Utils_Data.clickData(getContext(),
     * ZhuGeIOStatistics.s_spxq_scewm, true);
     */
    private String checkFileByLine2(String multiLine) {
        String[] lines = multiLine.split("\n");
        String result = "";
        String[] arrs = null;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                line.replace("-", "_");
                arrs = line.split("\\s+");
                result += ("//TD 4.1.0  " + arrs[1] + "\n");
                result += " Utils_Data.clickData(getContext(), ZhuGeIOStatistics." + arrs[0] + ", true);\n\n";
            }

        }
        return result;
    }

    /**
     * 读取每一行，中文会乱码 生成 properties
     *
     */
    private String checkFileByLine3(String multiLine)  {
        String[] lines = multiLine.split("\n");
        String result = "";
        String[] arrs = null;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                line.replace("-", "_");
                arrs = line.split("\\s+");
                result += (arrs[0] + "="  + arrs[1]+"\n");
            }

        }
        return result;
    }



}
