package com.example.elecontractor;

import org.docx4j.TraversalUtil;
import org.docx4j.finders.RangeFinder;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.elecontractor.Graphics2DUtil.startGraphics2D;

@SpringBootTest
class ElecontractorApplicationTests {

    @Test
    void contextLoads() {
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy年MM月dd日");
            String year = ft.format(dNow);
            BufferedImage image = startGraphics2D("广州思博商贸有限公司","合同专用章",year);

            String filePath = "";
            try {
                filePath = "D:\\"+new Date().getTime()+".png";
                ImageIO.write(image, "png", new File(filePath)); //将其保存在D:\\下，得有这个目录
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


}
