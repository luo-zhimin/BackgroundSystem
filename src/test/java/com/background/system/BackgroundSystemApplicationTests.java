package com.background.system;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.background.system.util.PdfUtil.imageToMergePdf;

@SpringBootTest
class BackgroundSystemApplicationTests {

    @Test
    void contextLoads() {
        List<String> picList = new ArrayList<>();
        String s = null;
        try {
            s = imageToMergePdf(picList, "hello.pdf", 100, 100);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("s = " + s);
    }

}
