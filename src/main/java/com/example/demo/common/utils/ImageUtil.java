package com.example.demo.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ImageUtil {
    /**
     * 图片文件格式
     */
    public static final String FORMAT_NAME = "png";

    /**
     * PDF文件转多张图片后压缩为zip
     * @param source
     * @param target
     * @throws Exception
     */
    public static void pdfToImageZip(String source, String target) throws Exception {
        Document document = new Document();
        document.setFile(source);
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
                    Page.BOUNDARY_CROPBOX, 0F, 2.5F);
            File imageFile = new File((i + 1) + "." + FORMAT_NAME);
            ImageIO.write(image, FORMAT_NAME, imageFile);
            image.flush();
            fileList.add(imageFile);
        }
        document.dispose();
        zipFile(fileList, target);
    }

    /**
     * PDF文件转多张图片后合并为一张长图
     * @param source
     * @param target
     * @throws Exception
     */
    public static void pdfToImageLong(String source, String target) throws Exception {
        Document document = new Document();
        document.setFile(source);
        List<BufferedImage> fileList = new ArrayList<>();
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
                    Page.BOUNDARY_CROPBOX, 0F, 2.5F);
            fileList.add(image);
        }
        document.dispose();
        combineImage(fileList, target);
    }

    /**
     * 压缩文件
     * @param inputFiles 具体需要压缩的文件集合
     * @param target     目标文件
     */
    private static void zipFile(List<File> inputFiles, String target) throws Exception {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target))) {
            byte[] buffer = new byte[1024];
            for (File file : inputFiles) {
                if (file.exists()) {
                    if (file.isFile()) {
                        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                            zos.putNextEntry(new ZipEntry(file.getName()));
                            int size;
                            while ((size = bis.read(buffer)) > 0) {
                                zos.write(buffer, 0, size);
                            }
                            zos.closeEntry();
                        }
                    } else {
                        File[] files = file.listFiles();
                        if (files != null) {
                            List<File> childrenFileList = Arrays.asList(files);
                            zipFile(childrenFileList, target);
                        }
                    }
                }
            }
        }
    }

    /**
     * 多张图合并为一张长图
     * @param fileList
     * @param target
     */
    private static void combineImage(List<BufferedImage> fileList,  String target) throws IOException {
        if (fileList == null || fileList.size() <= 0) {
            System.out.println("图片数组为空!");
            return;
        }
        int height = 0, // 总高度
                width = 0, // 总宽度
                _height = 0, // 临时的高度 , 或保存偏移高度
                __height = 0, // 临时的高度，主要保存每个高度
                picNum = fileList.size();// 图片的数量
        int[] heightArray = new int[picNum]; // 保存每个文件的高度
        BufferedImage buffer = null; // 保存图片流
        List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
        int[] _imgRGB; // 保存一张图片中的RGB数据
        for (int i = 0; i < picNum; i++) {
            buffer = fileList.get(i);
            heightArray[i] = _height = buffer.getHeight();// 图片高度
            if (i == 0) {
                width = buffer.getWidth();// 图片宽度
            }
            height += _height; // 获取总高度
            _imgRGB = new int[width * _height];// 从图片中读取RGB
            _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
            imgRGB.add(_imgRGB);
        }
        _height = 0; // 设置偏移高度为0
        // 生成新图片
        BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < picNum; i++) {
            __height = heightArray[i];
            if (i != 0) _height += heightArray[i-1]; // 计算偏移高度
            imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
        }
        // 压缩图片;
        imageResult = Thumbnails.of(imageResult).size(740,4180).outputQuality(0.3).outputFormat(FORMAT_NAME).asBufferedImage();
        File imageFile = new File(target);
        ImageIO.write(imageResult, FORMAT_NAME, imageFile);// 写图片
    }

    /**
     * 将图片压缩到指定大小以下
     * @param bytes
     * @param desSize
     * @param perScale 每次递归的压缩比, 取值0~1之间, 越接近1结果越精确
     * @throws IOException
     */
    private static String cut(byte[] bytes, long desSize, double perScale) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));
            if (bytes.length < desSize) {
                File imageFile = new File("E:\\test123.jpg");
                ImageIO.write(bi, "jpg", imageFile);
                BASE64Encoder encoder = new BASE64Encoder();
                return "data:image/jpg;base64," + encoder.encode(bytes);
            }
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();
            int desWidth = new BigDecimal(srcWidth).multiply(new BigDecimal(perScale)).intValue();
            int desHeight = new BigDecimal(srcHeight).multiply(new BigDecimal(perScale)).intValue();
            Thumbnails.of(bi).size(desWidth, desHeight).outputQuality(1.0).outputFormat("jpg").toOutputStream(baos);
            byte[] bytess = baos.toByteArray();
            return cut(bytess, desSize, perScale);
        }
    }

    public static void main(String[] args) throws Exception {
//        String source = "E:/DAVID/JAVA/workspace/demo/EP201900000008983015.pdf";
//        String target = "E:/DAVID/JAVA/workspace/demo/";
//        pdfToImageZip(source, target + "def.zip");
//        pdfToImageLong(source, target + "def.png");

        // 图片压缩到指定大小以下;
        InputStream is = new FileInputStream(new java.io.File("E:\\test.jpg"));
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        String base64 = cut(bytes, 1024 * 1024, 0.8);
        System.out.println(base64);
    }
}
