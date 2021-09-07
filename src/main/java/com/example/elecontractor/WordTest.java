package com.example.elecontractor;

import org.apache.commons.io.FileUtils;
import org.docx4j.TraversalUtil;
import org.docx4j.dml.CTNonVisualDrawingProps;
import org.docx4j.dml.wordprocessingDrawing.Anchor;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.finders.RangeFinder;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;

import javax.xml.bind.JAXBElement;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

public class WordTest {
    public static void main(String[] args) throws Exception {
        String wordFilePath = "C:\\Users\\Administrator\\Desktop\\test.docx";
        WordprocessingMLPackage wordMLPackage = load(wordFilePath);
        // 提取正文
        MainDocumentPart main = wordMLPackage.getMainDocumentPart();
        Document doc = main.getContents();
        Body body = doc.getBody();
//提取模板
        String xPath = "//w:drawing";

        List<Object> list = main.getJAXBNodesViaXPath(xPath, true);

        @SuppressWarnings("unchecked")

        JAXBElement<Drawing> element = (JAXBElement<Drawing>) list.get(0);

        Drawing drawing = element.getValue();




        //获取原图的相关信息，再取创建一个新的图片，用户替换原图
        Anchor anchor = (Anchor) drawing.getAnchorOrInline().get(0);//当前的图片
        Integer posH = anchor.getPositionH().getPosOffset();//原占位图的坐标位置

        Integer posV = anchor.getPositionV().getPosOffset();
        CTNonVisualDrawingProps docPr = anchor.getDocPr();

        int xId = (int) docPr.getId();

        String filenameHint = docPr.getName();

        String altText = docPr.getDescr();

        int yId = (int) anchor.getGraphic().getGraphicData().getPic().getNvPicPr().getCNvPr().getId();



        String imagePath = "C:\\Users\\Administrator\\Desktop\\test2.png";;

        byte bytes[] = FileUtils.readFileToByteArray(new File(imagePath));

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);


        Anchor inline = imagePart.createImageAnchor(filenameHint, altText, xId, yId, false, posH, posV);
//        Inline inline2 = imagePart.createImageInline(filenameHint, altText, xId, yId,false,0);
        drawing.getAnchorOrInline().set(0, inline);


        //需要替换的map
        HashMap<String, String> mappings = new HashMap<String, String>();
        mappings.put("date", "男");
        main.variableReplace(mappings);

        // 获取段落
        List<Object> paragraphs  = body.getContent();

        // 提取书签并获取书签的游标
        RangeFinder rt = new RangeFinder("CTBookmark", "CTMarkupRange");
        new TraversalUtil(paragraphs, rt);

        // 遍历书签
//        for (CTBookmark bm : rt.getStarts()) {
//            System.out.println("name:"+bm.getName());
//            // 替换image
//            if ("sign".equals(bm.getName())){
//                addImage(wordMLPackage, bm);
//                break;
//            }
//        }

        save(wordMLPackage,"C:\\Users\\Administrator\\Desktop\\test2.docx");
    }

    public static void addImage(WordprocessingMLPackage wPackage,CTBookmark bm) throws Exception{
        P p = (P) (bm.getParent());
        ObjectFactory factory = Context.getWmlObjectFactory();
        R run = factory.createR();

        // 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
        // byte[] bytes = IOUtils.toByteArray(new FileInputStream("图片文件路径"));
        byte[] bytes = getFileBytes("C:\\Users\\hylerp\\Desktop\\sign.png");
        // 开始创建一个行内图片
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage, bytes);
        // 最后一个是限制图片的宽度，缩放的依据
        Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 0);

        // 构建绘图
        Drawing drawing = factory.createDrawing();
        // 加入图片段落
        drawing.getAnchorOrInline().add(inline);
        run.getContent().add(drawing);
        // 清理书签所在数据
        // p.getContent().clear();
        // 加入图片信息
        p.getContent().add(run);
    }

    /**
     * 构建文件
     */
    public static WordprocessingMLPackage build() throws Exception{
        return WordprocessingMLPackage.createPackage();
    }

    /**
     * 读取存在的word文件
     * @param wordFilePath word文件路径
     */
    public static WordprocessingMLPackage load(String wordFilePath) throws Exception{
        return WordprocessingMLPackage.load(new File(wordFilePath));
    }

    /**
     * 保存
     * @param wordMLPackage word
     */
    public static void save(WordprocessingMLPackage wordMLPackage,String wordFilePath) throws Exception{
        wordMLPackage.save(new File(wordFilePath));
    }

    public static byte[] getFileBytes(String filePath) throws Exception{
        File file = new File(filePath);
        if(!file.exists()){
            throw new Exception("文件不存在!");
        }

        byte[] data = null;
        try(FileInputStream fis = new FileInputStream(file);ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            data = baos.toByteArray();
        }
        return data;
    }
//public void repleacePic(){
//    String xPath = "//w:drawing";
//
//    List<Object> list = documentPart.getJAXBNodesViaXPath(xPath, true);
//
//    @SuppressWarnings("unchecked")
//
//    JAXBElement<Drawing> element = (JAXBElement<Drawing>) list.get(0);
//
//    Drawing drawing = element.getValue();
//
//
//
//    //获取原图的相关信息，再取创建一个新的图片，用户替换原图
//
//    Anchor anchor = (Anchor) drawing.getAnchorOrInline().get(0);//当前的图片
//
//    Integer posH = anchor.getPositionH().getPosOffset();//原占位图的坐标位置
//
//    Integer posV = anchor.getPositionV().getPosOffset();
//
//    CTNonVisualDrawingProps docPr = anchor.getDocPr();
//
//    int xId = (int) docPr.getId();
//
//    String filenameHint = docPr.getName();
//
//    String altText = docPr.getDescr();
//
//    int yId = (int) anchor.getGraphic().getGraphicData().getPic().getNvPicPr().getCNvPr().getId();
//
//
//
//    String imagePath = ImageTest.class.getResource("/resources/images/badge.png").getPath();
//
//    byte bytes[] = FileUtils.readFileToByteArray(new File(imagePath));
//
//    BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
//
//
//
//    Anchor inline = imagePart.createImageAnchor(filenameHint, altText, xId, yId, false, posH, posV);
//
//    drawing.getAnchorOrInline().set(0, inline);
//
//    System.out.println("--------图片已经替换--------");
//
//}
}