package com.background.system.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpUtil;
import com.background.system.exception.ServiceException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * pdf工具类
 *
 * @author menghui.wan
 */
@Slf4j
@Component
public class PdfUtil {

    private static List<String> tempPdfPath = new ArrayList<>();
    private static List<String> tempImagePath = new ArrayList<>();
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss";

    private static String FILE_PATH = "/Users/sugar/Desktop/BackgroundSystem/upload";

    private static String OCR_PATH = FILE_PATH + "/new.png";
    private static String CUP_PATH = FILE_PATH + "/new.";
    private static String mergedPdfName;

    private static boolean flag = true;

    public static void main(String[] args) {
        List<String> lis = new ArrayList<>();
        lis.add("/Users/sugar/Desktop/BackgroundSystem/upload/1.png");
        lis.add("/Users/sugar/Desktop/BackgroundSystem/upload/1.png");
        try {
            String nidie = imageToMergePdf(lis, UUID.randomUUID().toString() + ".pdf", 60, 92);
            System.out.println(nidie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 多图片转pdf并且进行pdf合并
     *
     * @param sourcePaths
     * @return
     * @throws Exception
     */
    public static String imageToMergePdf(List<String> sourcePaths, String name, Integer width, Integer height)
        throws Exception {
        // add ocr
        sourcePaths.add(0, "https://img.asugar.cn/asugar/1.png");
        sourcePaths.add(0, "https://img.asugar.cn/asugar/2.png");

        File uploadDirectory = new File(FILE_PATH);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        LocalDateTime now = LocalDateTime.now();
        mergedPdfName = "final_merged_" + LocalDateTimeUtil.format(now, DATE_FORMAT) + ".pdf";

        // 标记
        int cnt = 0;
        for (String file : sourcePaths) {
            if (file.toLowerCase().endsWith(".png")
                || file.toLowerCase().endsWith(".jpg")
                || file.toLowerCase().endsWith(".jpeg")) {

                String url;
                try {
                    cnt++;
                    if (cnt > 2)
                        url = adjustImageSize2(file, width, height);
                    else
                        url = adjustImageSize(file, width, height);
                } catch (Exception e) {
                    log.error("调整图片大小失败，异常{}", e.getMessage());
                    throw new ServiceException(500, e.getMessage());
                }
                if (StringUtils.isBlank(url)) {
                    continue;
                }
                // 图片转pdf
                String pdfFile = imgToPdf(url);
                tempPdfPath.add(pdfFile);
            }
        }
        // 合并pdf
        mergePdf(tempPdfPath, mergedPdfName);
        // 清除临时文件
        clearFile();
        String pdfUrl = FILE_PATH + "/" + mergedPdfName;
        File pdf = new File(pdfUrl);
        if (!pdf.exists()) {
            throw new ServiceException(500, "合并生成的pdf文件不存在，可能被人为删除，请再次重试");
        }
        String url = AliUploadUtils.uploadPdf(pdf, name);
        pdf.delete();
        return url;
    }

    /**
     * 调整图片尺寸
     *
     * @param sourcePath 图片路径
     * @return
     * @throws Exception
     */
    private static String adjustImageSize(String sourcePath, Integer width, Integer height) throws Exception {
        HttpUtil.downloadFile(
            "https://etumwb-duigzq-8000.preview.myide.io/hello?url=" + sourcePath + "&type=1&os=1&xnx=2", OCR_PATH);
        tempImagePath.add(OCR_PATH);
        return OCR_PATH;
    }

    private static String adjustImageSize2(String sourcePath, Integer width, Integer height) throws Exception {
        // target
        String[] split = sourcePath.split("\\.");

        // init
        String url = AliUploadUtils.uploadPdf(new File(sourcePath), UUID.randomUUID() + "." + split[1]);

        String sb = "";
        if (flag) {
            sb = "xnx=0";
        } else {
            sb = "xnx=1";
        }

        // type select
        if (split[1].equals("jpg") || split[1].equals("jpeg")) {
            CUP_PATH = FILE_PATH + "/new.jpg";
            HttpUtil.downloadFile("https://etumwb-duigzq-8000.preview.myide.io/hello?url=" + url + "&type=0&os=0&" + sb,
                CUP_PATH);
        } else {
            CUP_PATH = FILE_PATH + "/new.png";
            HttpUtil.downloadFile("https://etumwb-duigzq-8000.preview.myide.io/hello?url=" + url + "&type=1&os=0&" + sb,
                CUP_PATH);
        }

        flag = !flag;
        return CUP_PATH;
    }

    /**
     * 图片转pdf
     *
     * @param adjustImgPath 图片路径
     * @return
     * @throws IOException
     */
    private static String imgToPdf(String adjustImgPath) throws IOException {
        BufferedImage img = ImageIO.read(new File(adjustImgPath));
        String pdfPath = "";
        try {
            //图片操作
            Image image = null;
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                file.mkdirs();
            }

            pdfPath = FILE_PATH + "/" + System.currentTimeMillis() + ".pdf";
            Document doc = new Document(null, 0, 0, 0, 0);

            //更换图片图层
            BufferedImage bufferedImage =
                new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            bufferedImage.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
            bufferedImage =
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB), null).filter(bufferedImage, null);

            //图片流处理
            doc.setPageSize(new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
            image = Image.getInstance(adjustImgPath);
            // 调整pdf图片的dpi
            float scalePercentage = (72 / 300f) * 100.0f;
            image.scalePercent(scalePercentage, scalePercentage);
            //写入PDF
            log.info("写入PDf:" + pdfPath);
            FileOutputStream fos = new FileOutputStream(pdfPath);
            PdfWriter.getInstance(doc, fos);
            doc.open();
            doc.add(image);
            doc.close();
        } catch (IOException | DocumentException e) {
            log.error("图片转pdf失败。异常{}", e.getMessage());
            throw new ServiceException(500, e.getMessage());
        }
        return pdfPath;
    }

    private static void clearFile() {
        // 清除转换后的图片文件
        if (CollectionUtils.isNotEmpty(tempImagePath)) {
            tempImagePath.forEach(path -> {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            });
            tempImagePath.clear();
        }

        // 清除单张图片转换成的pdf文件
        if (CollectionUtils.isNotEmpty(tempPdfPath)) {
            tempPdfPath.forEach(path -> {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            });
            tempPdfPath.clear();
        }
    }

    /**
     * 合并pdf
     *
     * @param files         pdf文件路径集合
     * @param mergeFileName 合并之后生成的文件名
     * @throws Exception
     */
    private static void mergePdf(List<String> files, String mergeFileName) throws Exception {

        PDFMergerUtility mergePdf = new PDFMergerUtility();

        for (String file : files) {
            if (file.toLowerCase().endsWith("pdf")) {
                mergePdf.addSource(file);
            }
        }

        mergePdf.setDestinationFileName(FILE_PATH + "/" + mergeFileName);
        mergePdf.mergeDocuments();
        log.info("成功合并～");
    }
}
