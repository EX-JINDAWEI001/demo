package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping(value = "/getFileInputStream.do", method = RequestMethod.POST)
    public void getFileInputStream(String fileName, HttpServletResponse response) throws IOException {
        OutputStream out = null;
        BufferedInputStream bis = null;
        try{
            File f = new File("/Users/david/Downloads/项目精华整理.txt");
            if (!f.exists()) {
                response.sendError(404, "file not found !");
                return;
            }

            bis = new BufferedInputStream(new FileInputStream(f));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            response.setContentType("multipart/form-data");
            out = response.getOutputStream();
            while ((len = bis.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

}
