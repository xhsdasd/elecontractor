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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
                filePath = "C:\\Users\\Administrator\\Desktop\\test.png";
                ImageIO.write(image, "png", new File(filePath)); //将其保存在D:\\下，得有这个目录
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        @Test
        void test2() throws IOException {
            //定义一个BufferedImage对象，用于保存缩小后的位图
            BufferedImage bufferedImage=new BufferedImage(217,190,BufferedImage.TYPE_INT_RGB);
            Graphics2D  graphics=bufferedImage.createGraphics();
            bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage(217,190, Transparency.TRANSLUCENT);

            //读取原始位图
            Image srcImage= ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\test.png"));

            //将原始位图缩小后绘制到bufferedImage对象中
            graphics.drawImage(srcImage,0,0,217,190,null);
            //将bufferedImage对象输出到磁盘上
            ImageIO.write(bufferedImage,"png",new File("C:\\Users\\Administrator\\Desktop\\test2.png"));

  }


}
