package com.example.elecontractor;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class Graphics2DUtil {

    private static final int WIDTH = 450;//图片宽度
    private static final int HEIGHT = 450;//图片高度

    /**
     *
     * @Title: splitImage
     * @Description: 分割图片
     * @param image 图片BufferedImage流
     * @param rows 分割行
     * @param cols 分割列
     * @return BufferedImage[] 返回分割后的图片流
     */
    public static BufferedImage[] splitImage(BufferedImage image, int rows, int cols) {
        // 分割成4*4(16)个小图
        int chunks = rows * cols;
        // 计算每个小图的宽度和高度
        int chunkWidth = image.getWidth() / cols + 3;// 向右移动3
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage[] imgs = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //设置小图的大小和类型
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, BufferedImage.TYPE_INT_RGB);
                //写入图像内容
                Graphics2D gr = imgs[count].createGraphics();
                // 增加下面代码使得背景透明  
                imgs[count] = gr.getDeviceConfiguration().createCompatibleImage(chunkWidth, chunkHeight, Transparency.TRANSLUCENT);
//                gr.setBackground(Color.WHITE);// 背景为白色
//                 // 加上这句才算真正将背景颜色设置为透明色
//                gr.clearRect(0, 0,chunkWidth,chunkHeight);
                gr.dispose();
                gr = imgs[count].createGraphics();
                gr.drawImage(image, 0, 0,
                        chunkWidth, chunkHeight,
                        chunkWidth * y, chunkHeight * x,
                        chunkWidth * y + chunkWidth,
                        chunkHeight * x + chunkHeight, null);
                gr.dispose();
                count++;
            }
        }
        return imgs;
    }

    /**
     *
     * @Title: startGraphics2D
     * @Description: 生成公司电子公章
     * @param message 公司名称
     * @param centerName 公章类型，如：测试章
     * @param year 时间
     * @return BufferedImage 返回类型
     */
    public static BufferedImage startGraphics2D(String message, String centerName, String year){
        // 定义图像buffer         
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 增加下面代码使得背景透明  
        buffImg = g.getDeviceConfiguration().createCompatibleImage(WIDTH, HEIGHT, Transparency.TRANSLUCENT);
        g.dispose();
        g = buffImg.createGraphics();
        // 背景透明代码结束 
        g.setColor(Color.RED);
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //绘制圆
        int radius = HEIGHT/3;//周半径
        int CENTERX = WIDTH/2;//画图所出位置
        int CENTERY = HEIGHT/2;//画图所处位置

        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius, CENTERY + radius);
        g.setStroke(new BasicStroke(10));//设置圆的宽度
        g.draw(circle);

        int num = 120;
        int num1 = 40;
//        num = 90;
        num1 = 72;

        //绘制中间的五角星
        g.setFont(new Font("宋体", Font.BOLD, num));
        g.drawString("★", CENTERX-(num/2), CENTERY+(num/3));

        //添加姓名
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 30));// 写入签名
        g.drawString(centerName, CENTERX -(num1), CENTERY +(30+50));

        //添加年份
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 20));// 写入签名
        g.drawString(year, CENTERX -(66), CENTERY +(30+78));

        //根据输入字符串得到字符数组
        char[] messages = message.toCharArray();

        //输入的字数
        int ilength = messages.length;

        //设置字体属性
        int fontsize = 40;
        Font f = new Font("Serif", Font.BOLD, fontsize);

        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(message, context);

        //字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / ilength);
        //上坡度
        double ascent = -bounds.getY() + 3;

        int first = 0,second = 0;
        boolean odd = false;
        if (ilength%2 == 1) {
            first = (ilength-1)/2;
            odd = true;
        } else {
            first = (ilength)/2-1;
            second = (ilength)/2;
            odd = false;
        }

        double radius2 = radius - ascent;
        double x0 = CENTERX;
        double y0 = CENTERY - radius + ascent;
        //旋转角度
        double a = 2*Math.asin(char_interval/(2*radius2));

        if (odd) {
            g.setFont(f);
            g.drawString(String.valueOf(messages[first]), (float)(x0 - char_interval/2), (float)y0);

            //中心点的右边
            for (int i=first+1;i<ilength;i++)
            {
                double aa = (i - first) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(String.valueOf(messages[i]), (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }
            //中心点的左边
            for (int i=first-1;i>-1;i--)
            {
                double aa = (first - i) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(String.valueOf(messages[i]), (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }

        } else {
            //中心点的右边
            for (int i=second;i<ilength;i++)
            {
                double aa = (i - second + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(String.valueOf(messages[i]), (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }

            //中心点的左边
            for (int i=first;i>-1;i--)
            {
                double aa = (first - i + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(String.valueOf(messages[i]), (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }
        }

        return buffImg;
    }


}

