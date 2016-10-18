package com.yeqinfu.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yeqinfu on 10/18/16.
 */
public class ui_qwjsonformat  {
    private JTextArea textArea1;
    private JButton btn_sure;
    private JPanel jpanel;
    JFrame frame;

    public void showDialog() {
        frame.setVisible(true);
    }
    public ui_qwjsonformat(){
        super();
        initUIAction();
    }
    public void initUIAction(){
        frame = new JFrame("ui_qwjsonformat");
        frame.setContentPane(jpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int Swing1x = 500;
        int Swing1y = 300;
        frame.setBounds(screensize.width / 2 - Swing1x / 2, screensize.height / 2 - Swing1y / 2, Swing1x, Swing1y);
        btn_sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeFile();
                frame.dispose();
            }
        });
    }

    private formatInterface interfaceInsert;

    public formatInterface getInterfaceInsert() {
        return interfaceInsert;
    }

    public void setInterfaceInsert(formatInterface interfaceInsert) {
        this.interfaceInsert = interfaceInsert;
    }

    interface formatInterface{
        void insertResult(String insertStr);
    }

    /**
     *
     apiStatus (int, optional),
     apiMessage (java.lang.String, optional),
     id (java.lang.String, optional): 商品ID,
     code (java.lang.String, optional): 编码,
     name (java.lang.String, optional): 名称.含品牌,
     brand (java.lang.String, optional): 品牌,
     spec (java.lang.String, optional): 规格,
     barcode (java.lang.String, optional): 条形码,
     factory (java.lang.String, optional): 生产厂家,
     imgUrl (java.lang.String, optional): 图片,
     imgUrls (java.util.List[string], optional): 图片集,
     instruction (java.lang.String, optional): 内容简介。根据控制码展示,
     signCode (java.lang.String, optional): 显示控制码,
     branchs (java.util.List[MicroMallProductBranchVo], optional): 附近可售药房
     */
    private void makeFile(){
        String[] strs=textArea1.getText().split("\n");
        String result="";
        for (String item:strs) {
            result+=changeItem(item)+"\n";
        }
        if (interfaceInsert!=null){
            interfaceInsert.insertResult(result);
        }
    }

    /**
     *
     * @param item  branchs (java.util.List[MicroMallProductBranchVo], optional): 附近可售药房
     * @return private List<?> branchs;//附近可售药房
     */
    private String changeItem(String item) {
        String result="private ";
        result+=addSecondPart(item)+" ";
        result+=item.substring(0,item.indexOf("("))+";";
        String endPart=item.substring(item.indexOf(")"));
        if (endPart.contains(" ")){
            result+="//"+endPart.substring(endPart.indexOf(" "));
        }

        return result;
    }

    private String addSecondPart(String item) {
        String result="?";
        if (item.contains("(int")){
            result="int";
        }else if (item.contains("(java.lang.String")){
            result="String";
        }else if (item.contains("(boolean")){
            result="boolean";
        }else if (item.contains("(double")){
            result="double";
        }else if (item.contains("(float")){
            result="float";
        }else if (item.contains("(java.util.List")){
            int start=item.indexOf("[");
            int end=item.indexOf("]");
            String center=item.substring(start+1,end);
            if (center.endsWith("string")){
                center="String";
            }
            result="List<"+center+">";
        }
        return result;
    }


}
