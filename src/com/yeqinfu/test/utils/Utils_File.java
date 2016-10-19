package com.yeqinfu.test.utils;

import com.oracle.tools.packager.Log;
import org.dom4j.DocumentException;

import java.io.*;

public class Utils_File {
    /**
     * 将字符串存储为一个文件，当文件不存在时候，自动创建该文件，当文件已存在时候，重写文件的内容，特定情况下，还与操作系统的权限有关。
     *
     * @param text     字符串
     * @param distFile 存储的目标文件
     * @return 当存储正确无误时返回true，否则返回false
     */
    public static boolean string2File(String text, File distFile) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        boolean flag = true;
        try {
            br = new BufferedReader(new StringReader(text));
            bw = new BufferedWriter(new FileWriter(distFile));
            char buf[] = new char[1024 * 64];         //字符缓冲区
            int len;
            while ((len = br.read(buf)) != -1) {
                bw.write(buf, 0, len);
            }
            bw.flush();
            br.close();
            bw.close();
        } catch (IOException e) {
            flag = false;
            Log.debug("将字符串写入文件发生异常！");
        }
        return flag;
    }

    /**
     * 读取文件为一个内存字符串,保持文件原有的换行格式
     *
     * @param file    文件对象
     * @param charset 文件字符集编码
     * @return 文件内容的字符串
     */
    public static String file2String(File file, String charset) {
        StringBuffer sb = new StringBuffer();
        try {
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(file), charset)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
        } catch (UnsupportedEncodingException e) {
            Log.debug("读取文件为一个内存字符串失败，失败原因是使用了不支持的字符编码" + charset);
        } catch (FileNotFoundException e) {
            Log.debug("读取文件为一个内存字符串失败，失败原因所给的文件" + file + "不存在！");
        } catch (IOException e) {
            Log.debug("读取文件为一个内存字符串失败，失败原因是读取文件异常！");
        }
        return sb.toString();
    }


    public static void checkFileEndWith(String filePath, String endStr) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
              /*
               * 递归调用
		       */
                checkFileEndWith(file.getAbsolutePath(), endStr);
                // filelist.add(file.getAbsolutePath());
                //  System.out.println("显示"+filePath+"下所有子目录及其文件"+file.getAbsolutePath());
            } else {
                if (file.getName().endsWith(endStr)) {//如果是目标文件类型 检查里面的节点
                    try {
                        Utils_XmlPraser.checkXml(file.getAbsolutePath());
                    } catch (DocumentException e) {

                        e.printStackTrace();
                    }

                }


                //  System.out.println("显示"+filePath+"下所有子目录"+file.getAbsolutePath());
            }

        }

    }


}
