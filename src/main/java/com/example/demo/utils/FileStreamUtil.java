package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileStreamUtil {

    /**
     * 通过http请求获取远端服务器上的文件流
     * @param fileName
     * @throws IOException
     */
    public static void getFileInputStream(String fileName) throws IOException {
        InputStream is = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/file/getFileInputStream.do?fileName=jdw.txt").openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);

            int code = connection.getResponseCode();
            if (code == 200 || code == 206) {
                is = connection.getInputStream();
                printFileContent(is, fileName);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
    }

    private static void printFileContent (InputStream is, String fileName) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int count = 1;
            String line = null;
            List<String> list = new ArrayList<>();
            while(StringUtils.isNotEmpty(line = br.readLine())){
                list.add(line.trim());
                if (count % 5000 == 0) {
                    System.out.println(list);
                    list = new ArrayList<>();
                }
                count ++;
            }
            if (list.size() > 0){
                System.out.println(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param file 将要上传的源文件
     * @param response 输出到浏览器的下载流
     */
    public static void downLoad(File file, HttpServletResponse response) {
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("utf-8"), "ISO-8859-1"));
            response.addHeader("Content-Length", ""+file.length());
            response.setContentType("application/octet-stream");

            byte[] buf = new byte[1024];
            int len = 0;
            while((len = bis.read()) >0){
                bos.write(buf, 0, len);
            }
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param file 将要生成的文件
     * @param is 源文件的输入流
     */
    public static void upLoad(File file, InputStream is){
        try(BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] b = new byte[1024 * 8];
            int len;
            while((len = bis.read(b)) > 0){
                bos.write(b, 0, len);
            }
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
