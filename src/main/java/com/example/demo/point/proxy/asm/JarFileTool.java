package com.example.demo.point.proxy.asm;

import java.io.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JarFileTool {

    public static void doModify(String jarPath, String jarPathNew, String jarFilePath, int defaultCharWidth) throws IOException {
        if (jarPath != null && jarPathNew != null && jarFilePath != null && defaultCharWidth > 0) {
            // 通过jar包的路径创建jar包实例;
            JarFile jarFile = new JarFile(jarPath);
            // 通过某个文件在jar包中的位置来获取这个文件;
            JarEntry entry = jarFile.getJarEntry(jarFilePath);
            // 获取该文件输入流;
            InputStream input = jarFile.getInputStream(entry);
            // 获取jar包中的所有entry;
            List<JarEntry> lists = new LinkedList<>();
            for (Enumeration<JarEntry> entrys = jarFile.entries(); entrys.hasMoreElements();) {
                JarEntry jarEntry = entrys.nextElement();
                lists.add(jarEntry);
            }
            // 调用ASM框架修改class文件;
            byte[] bytes = ASMTool.modifyClassFile(input, defaultCharWidth);
            // 将修改后的内容写入jar包中的指定文件;
            write(jarPathNew, jarFilePath, jarFile, lists, bytes);
            jarFile.close();

            File oldFile = new File(jarPath);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            File newFile = new File(jarPathNew);
            if (newFile.exists()) {
                newFile.renameTo(oldFile);
                newFile.delete();
            }
        }
    }

    private static void write(String jarPathNew, String jarFilePath, JarFile jarFile, List<JarEntry> lists, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(jarPathNew);
        JarOutputStream jos = new JarOutputStream(fos);
        try {
            InputStream is = null;
            for (JarEntry je : lists) {
                JarEntry newEntry = new JarEntry(je.getName());
                jos.putNextEntry(newEntry);
                if (je.getName().equals(jarFilePath)) {
                    jos.write(bytes);
                    continue;
                }
                is = jarFile.getInputStream(je);
                if (is != null) {
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    jos.write(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (jos != null) {
                try {
                    jos.close();
                } catch (IOException e) {
                    jos = null;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String jarPath = "/Users/david/.m2/repository/org/apache/poi/poi-ooxml/4.0.1/poi-ooxml-4.0.1.jar";
        String jarPathNew = "/Users/david/.m2/repository/org/apache/poi/poi-ooxml/4.0.1/poi-ooxml-4.0.2.jar";
        String jarFilePath = "org/apache/poi/xssf/streaming/AutoSizeColumnTracker.class";
        doModify(jarPath, jarPathNew, jarFilePath, 5);
    }

}
