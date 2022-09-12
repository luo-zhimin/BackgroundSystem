package com.background.system;

import com.background.system.service.impl.PictureServiceImpl;
import com.background.system.util.ZipFileUtils;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/9/3 02:44
 */
@SpringBootTest
public class FileTest {

    @Resource
    private ZipFileUtils zipFileUtils;

    @Resource
    private PictureServiceImpl pictureService;

    @SneakyThrows
    void createNetworkFile(){
        String name ="https://asugar.oss-cn-hangzhou.aliyuncs.com/default/2d6e5ff5-e57e-48bf-a40a-1512acb25395.gif";
        HttpGet httpGet = new HttpGet(name);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        //通过输入流获取图片数据
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
       // byte[] data = readInputStream(inputStream);
        //创建一个文件对象用来保存图片，默认保存当前工程根目录，起名叫Copy.jpg
        File imageFile = new File("Copy.gif");
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(imageFile);
        //写入数据
        //outStream.write(data);
        //关闭输出流，释放资源
        outStream.close();

//        File file = new File(name);
//        System.out.println(file.createNewFile());
    }


    @Test
    @SneakyThrows
    void readFile(){
        String name ="https://asugar.oss-cn-hangzhou.aliyuncs.com/default/2d6e5ff5-e57e-48bf-a40a-1512acb25395.gif";
        URL url = new URL(name);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        //byte[] bytes = readInputStream(inputStream);
        //System.out.println(bytes.length);
    }


    @Test
    void testCreatZipOnly(){
        zipFileUtils.cratePictureZip();
    }

    @Test
    void testUpload(){
        //准备文件
        zipFileUtils.deleteFile.clear();
        zipFileUtils.readyUploadFiles.clear();

        zipFileUtils.cratePictureZip();
        zipFileUtils.uploadZip();
        System.out.println("successful");
    }

    @Test
    @SneakyThrows
    void anyUpload(){
        long start = System.currentTimeMillis();
//        List<PictureResponse> responses = Lists.newArrayList();
//        List<MockMultipartFile> files = Lists.newArrayList();
        MockMultipartFile[] files = new MockMultipartFile[20];
        for (int i = 0; i < 20; i++) {
            File file = new File("/Users/luozhimin/Desktop/WechatIMG15926.jpeg");
            byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", file.getName() + "-" + System.currentTimeMillis(), "text/plain", bytes);
//            pictureService.getPicture(mockMultipartFile,"default");
//            files.add(mockMultipartFile);
            files[i] = mockMultipartFile;
//            responses.add(picture);
        }
//        Object[] array = files.toArray();
        pictureService.upload(files, "default");
        long end = System.currentTimeMillis();
        System.out.println("耗时 = " + (end - start));//24803 单个
//        System.out.println(responses);
    }
}
