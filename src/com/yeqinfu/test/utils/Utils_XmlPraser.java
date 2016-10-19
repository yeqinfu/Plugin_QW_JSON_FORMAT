package com.yeqinfu.test.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Utils_XmlPraser {
    private static String filePath = "temp_xml.txt";


    public static String elementFindId(Element node){
        String result="";
        // 使用递归
        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) { // 遍历里面的节点
            Element e = iterator.next();
            // 首先获取当前节点的所有属性节点
            List<Attribute> list = e.attributes();
            // 遍历节点属性
            for (Attribute attr : list) {
                if (attr.getName().equals("id")){//如果是id节点
                    result+=" \n@ViewById\n"+attr.getValue().substring(attr.getValue().indexOf("/")+1);
                    break;
                }
            }
                result+=elementFindId(e);
        }

        return result;
    }



    public static Document xmlToDocument(String xmlStr) {
        // 创建SAXReader对象
        SAXReader reader = new SAXReader();
        // 读取文件 转换成Document
        File file = new File(filePath);
        if (!file.exists()){
            try {
                if (file.createNewFile()){//创建文件成功

                }else{
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        if (Utils_File.string2File(xmlStr, file)) {
            try {
                Document document = reader.read(file);
                return document;
            } catch (DocumentException e) {//praser xml throws exception
                e.printStackTrace();
                return null;
            }

        } else {//write to file throws exception
            return null;
        }
    }

    public static void checkXml(String fileUrl) throws DocumentException {
        // 创建SAXReader对象
        SAXReader reader = new SAXReader();
        // 读取文件 转换成Document
        File file = new File(fileUrl);
        Document document = reader.read(file);
        // 获取根节点元素对象
        Element root = document.getRootElement();

        // 遍历
        listNodes(root, file.getName());
    }

    /**
     * 传入的是一个xml的根节点
     *
     * @param node
     * @param fileName
     */
    private static void listNodes(Element node, String fileName) {
        /*
         * System.out.println("当前节点的名称：" + node.getName()); //首先获取当前节点的所有属性节点
		 * List<Attribute> list = node.attributes(); //遍历属性节点 for(Attribute
		 * attribute : list){ System.out.println("属性"+attribute.getName() +":" +
		 * attribute.getValue()); } //如果当前节点内容不为空，则输出
		 * if(!(node.getTextTrim().equals(""))){ System.out.println(
		 * node.getName() + "：" + node.getText()); }
		 */

        if ("shape".equals(node.getName()) || "selector".equals(node.getName())) {// 如果根节点是shape节点，对其中的shape节点进行检查
            System.out.println("找到目标文件++++++++++" + node.getName() + "  路径：" + fileName);
            checkShape(node, fileName);
            return;
        } else {
            // System.out.println("文件不是目标文件 -------"+ fileName+
            // "不是目标文件因为节点是"+node.getName());
        }

		/*
         * // 同时迭代当前节点下面的所有子节点 // 使用递归 Iterator<Element> iterator =
		 * node.elementIterator(); while (iterator.hasNext()) { Element e =
		 * iterator.next(); listNodes(e, fileName); }
		 */
    }

    /**
     * 得到的节点是shape节点 对里面的子节点的属性进行判断
     *
     * @param node
     */
    private static void checkShape(Element node, String fileName) {

        // 使用递归
        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) { // 遍历里面的slid stoke corners等节点
            Element e = iterator.next();
            // 首先获取当前节点的所有属性节点
            List<Attribute> list = e.attributes();
            // 遍历属性节点
            for (Attribute attribute : list) {

                Pattern p = Pattern.compile("\\?");
                Matcher m = p.matcher(attribute.getValue());
                ArrayList<String> strs = new ArrayList<String>();
                while (m.find()) {
                    strs.add(m.group(0));
                }
                if (strs.isEmpty()) {// 匹配未空，查找下一个节点
                    continue;
                } else {
                    System.out.println("filename: " + fileName);
                    System.out.println("属性" + attribute.getName() + ":" + attribute.getValue());
                    break;
                }
            }
        }

    }

}
