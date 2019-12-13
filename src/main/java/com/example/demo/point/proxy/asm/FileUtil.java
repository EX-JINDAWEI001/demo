package com.example.demo.point.proxy.asm;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil {
    private static final String path = "/Users/david/Downloads/data/JAVA/IDEA-SPACE/demo/target/classes/com/example/demo/point/proxy/asm/MyMain.class";

    // 将 class 文件转成 byte 数组（就是下方 MyMain 实体类编译后的 class 文件位置）
    public static byte[] File2Byte() throws IOException {
        File file = new File(path);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            fileInputStream.read(bytes);
        }
        return bytes;
    }

    // 将 byte 数组写进 class 文件
    public static boolean Byte2File(byte[] bytes) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static final String jarRootPath = "E:\\poi-ooxml-4.0.1-原始包.jar";

    public static String getClasPath() throws NoSuchMethodException, MalformedURLException, ClassNotFoundException {

        // 系统类库路径
        File libPath = new File(jarRootPath);

        // 获取所有的.jar文件
        File[] jarFiles = libPath.listFiles((dir, name) -> name.endsWith(".jar"));

        if (jarFiles != null) {
            // 从URLClassLoader类中获取类所在文件夹的方法
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            boolean accessible = method.isAccessible();        // 获取方法的访问权限
            try {
                if (accessible == false) {
                    method.setAccessible(true);        // 设置方法的访问权限
                }
                // 获取系统类加载器
                URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                for (File file : jarFiles) {
                    URL url = file.toURI().toURL();
                    try {
                        method.invoke(classLoader, url);
                        System.out.println("读取jar文件[name={}]" + file.getName());
                    } catch (Exception e) {
                        System.out.println("读取jar文件[name={}]失败" + file.getName());
                    }
                }
            } finally {
                method.setAccessible(accessible);
            }
        }
        Class<?> clz = Class.forName("org.apache.poi.xssf.streaming.AutoSizeColumnTracker");
        return null;
    }

    public static InputStream getJarFile() {
        InputStream in = null;
        try {
            JarFile jf = new JarFile(jarRootPath);

            Enumeration enu = jf.entries();
            while (enu.hasMoreElements()) {
                JarEntry element = (JarEntry) enu.nextElement();
                String name = element.getName();
                if (name.equals("org/apache/poi/xssf/streaming/AutoSizeColumnTracker.class")) {
                    Long size = element.getSize();
                    Long time = element.getTime();
                    Long compressedSize = element.getCompressedSize();

                    System.out.println(name);
                    System.out.println(size);
                    System.out.println(compressedSize);
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date(time)));

                    in = jf.getInputStream(element);

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return in;
    }

    public static void main(String[] args) throws NoSuchMethodException, MalformedURLException, ClassNotFoundException {
//        getClasPath();
        getJarFile();
    }
}