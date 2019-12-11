package com.example.demo.point.proxy.asm;

import java.io.*;

public class FileUtil {
//    private static final String path = "E:\\DAVID\\JAVA\\workspace\\demo\\target\\classes\\com\\example\\demo\\point\\proxy\\asm\\AutoSizeColumnTracker.class";
    private static final String path = "/Users/david/Downloads/data/JAVA/IDEA-SPACE/demo/target/classes/com/example/demo/point/proxy/asm/AutoSizeColumnTracker.class";

    // 将 class 文件转成 byte 数组（就是下方 MyMain 实体类编译后的 class 文件位置）
    public static byte[] File2Byte() throws IOException {
        // 获取 class 文件所在的位置
//        String path = App.getMyMainPath();
        File file = new File(path);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            fileInputStream.read(bytes);
        }
        return bytes;
    }
    // 将 byte 数组写进 class 文件
    public static boolean Byte2File(byte[] bytes) throws IOException {
//        String path = App.getMyMainPath();
        File file = new File(path);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}