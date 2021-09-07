//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.apache.poi.xwpf.usermodel.XWPFTable;
//import org.apache.poi.xwpf.usermodel.XWPFTableCell;
//import org.apache.poi.xwpf.usermodel.XWPFTableRow;
//
//import com.aspose.words.License;
//import com.newtec.myqdp.server.utils.StringUtils;
//
//
//public class WordUtil {
//
//    /**
//     *
//     * @Title: replaceAndGenerateWord
//     * @Description: 替换word中需要替换的特殊字符
//     * 优化后是为了分别减少map遍历，增加性能，前提是表格的替换的数据不一样，所以将两者分离处理（加空间，提性能）
//     * @param srcPath 需要替换的文档全路径
//     * @param destPath 替换后文档的保存路径
//     * @param contentMap {key:将要被替换的内容,value:替换后的内容}
//     * @param replaceTableMap {key:将要被替换的表格内容,value:替换后的表格内容}
//     * @return boolean 返回成功状态
//     */
//    public static boolean replaceAndGenerateWord(String  srcPath, String exportFile, Map<String, String> contentMap, Map<String, String> replaceTableMap) {
//        boolean bool = true;
//        try {
//            FileInputStream inputStream = new FileInputStream(srcPath);
//            XWPFDocument document = new XWPFDocument(inputStream);
//            // 替换段落中的指定文字
//            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
//            while (itPara.hasNext()) {
//                XWPFParagraph paragraph = itPara.next();
//                commonCode(paragraph, contentMap);
//            }
//            // 替换表格中的指定文字
//            Iterator<XWPFTable> itTable = document.getTablesIterator();
//            while (itTable.hasNext()) {
//                XWPFTable table = itTable.next();
//                int rcount = table.getNumberOfRows();
//                for (int i = 0; i < rcount; i++) {
//                    XWPFTableRow row = table.getRow(i);
//                    List<XWPFTableCell> cells = row.getTableCells();
//                    for (XWPFTableCell cell : cells) {
//                        //单元格中有段落，得做段落处理
//                        List<XWPFParagraph> paragraphs = cell.getParagraphs();
//                        for (XWPFParagraph paragraph : paragraphs) {
//                            commonCode(paragraph, replaceTableMap);
//                        }
//                    }
//                }
//            }
//
//            FileOutputStream outStream = new FileOutputStream(exportFile);
//            document.write(outStream);
//            outStream.close();
//            inputStream.close();
//        }catch (Exception e){
//            bool = false;
//            e.printStackTrace();
//        }
//        return bool;
//    }
//
//    /**
//     *
//     * @Title: commonCode
//     * @Description: 替换内容
//     * @param paragraph 被替换的文本信息
//     * @param contentMap {key:将要被替换的内容,value:替换后的内容}
//     */
//    private static void commonCode(XWPFParagraph paragraph,Map<String, String> contentMap){
//        List<XWPFRun> runs = paragraph.getRuns();
//        for (XWPFRun run : runs) {
//            String oneparaString = run.getText(run.getTextPosition());
//            if (StringUtils.isStrNull(oneparaString)){
//                continue;
//            }
//            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
//                oneparaString = oneparaString.replace(entry.getKey(), StringUtils.isStrNull(entry.getValue()) ? "--" : entry.getValue());
//            }
//            run.setText(oneparaString, 0);
//        }
//    }
//
//    /**
//     *
//     * @Title: getLicense
//     * @Description:验证license许可凭证
//     * @return boolean 返回验证License状态
//     */
//    private static boolean getLicense() {
//        boolean result = true;
//        try {
////        	new License().setLicense(new FileInputStream(new File("D:\\develop\\template\\license.xml").getAbsolutePath()));
//            new License().setLicense(new FileInputStream(new File(FilesUtil.getLicensePath()).getAbsolutePath()));
//        } catch (Exception e) {
//            result = false;
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     *
//     * @Title: wordConverterToPdf
//     * @Description: word转pdf(aspose转换)
//     * @param wordPath word 全路径，包括文件全称
//     * @param pdfPath pdf 保存路径，包括文件全称
//     * @return boolean 返回转换状态
//     */
//    public static boolean wordConverterToPdf(String wordPath, String pdfPath) {
//        System.out.println("===================aspose开始转换=======================");
//        //开始时间
//        long start = System.currentTimeMillis();
//        boolean bool = false;
//        // 验证License,若不验证则转化出的pdf文档会有水印产生
//        if (!getLicense()) return bool;
//        try {
//            FileOutputStream os = new FileOutputStream(new File(pdfPath));// 新建一个pdf文档输出流
//            com.aspose.words.Document doc = new com.aspose.words.Document(wordPath);// Address是将要被转化的word文档
//            doc.save(os, com.aspose.words.SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
//            os.close();
//            bool = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("(aspose)word转换PDF完成，用时"+(System.currentTimeMillis()-start)/1000.0+"秒");
//        }
//        return bool;
//    }
//
//    /**
//     *
//     * @Title: wordConverterToPdfJacob
//     * @Description: word转pdf(jacob转换)
//     * @param wordFile word 全路径，包括文件全称
//     * @param pdfFile pdf 保存路径，包括文件全称
//     * @return boolean 返回转换状态
//     */
////    public static boolean wordConverterToPdfJacob(String wordFile, String pdfFile) {
////		System.out.println("===================jacob开始转换=======================");
////		//开始时间
////		long start = System.currentTimeMillis();
////		ActiveXComponent app = new ActiveXComponent("Word.Application");// 创建对象，花了很长时间
////		boolean bool = false;
////		try {
////			// 打开word
////			// 获得word中所有打开的文档
////			Dispatch documents = app.getProperty("Documents").toDispatch();
//			System.out.println("打开文件: " + wordFile);
////			// 打开文档
////			Dispatch document = Dispatch.call(documents, "Open", wordFile, false, true).toDispatch();
////			// 如果文件存在的话，不会覆盖，会直接报错，所以我们需要判断文件是否存在
////			File target = new File(pdfFile);
////			if (target.exists()) {
////				target.delete();
////			}
//			System.out.println("另存为: " + pdfFile);
////			Dispatch.call(document, "SaveAs", pdfFile, 17);
////			// 关闭文档
////			Dispatch.call(document, "Close", false);
////			bool = true;
////		} catch (Exception e) {
////			System.out.println("转换失败" + e.getMessage());
////		} finally {
////			// 关闭office
////			app.invoke("Quit", 0);
////			System.out.println("生成完成，用时"+(System.currentTimeMillis()-start)/1000.0+"秒");
////		}
////		return bool;
////    }
//
//    /**
//     *
//     * @Title: insertRow
//     * @Description: 在word表格中指定位置插入一行，并将某一行的样式复制到新增行
//     * @param table 需要插入的表格
//     * @param copyrowIndex 需要复制的行位置
//     * @param newrowIndex 需要新增一行的位置
//     * @return void 返回类型
//     */
//    public static void insertRow(XWPFTable table, int copyrowIndex, int newrowIndex) {
//        // 在表格中指定的位置新增一行
//        XWPFTableRow targetRow = table.insertNewTableRow(newrowIndex);
//        // 获取需要复制行对象
//        XWPFTableRow copyRow = table.getRow(copyrowIndex);
//        //复制行对象
//        targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());
//        //或许需要复制的行的列
//        List<XWPFTableCell> copyCells = copyRow.getTableCells();
//        //复制列对象
//        XWPFTableCell targetCell = null;
//        for (int i = 0; i < copyCells.size(); i++) {
//            XWPFTableCell copyCell = copyCells.get(i);
//            targetCell = targetRow.addNewTableCell();
//            targetCell.getCTTc().setTcPr(copyCell.getCTTc().getTcPr());
//            if (copyCell.getParagraphs() != null && copyCell.getParagraphs().size() > 0) {
//                targetCell.getParagraphs().get(0).getCTP().setPPr(copyCell.getParagraphs().get(0).getCTP().getPPr());
//                if (copyCell.getParagraphs().get(0).getRuns() != null
//                        && copyCell.getParagraphs().get(0).getRuns().size() > 0) {
//                    XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
//                    cellR.setBold(copyCell.getParagraphs().get(0).getRuns().get(0).isBold());
//                }
//            }
//        }
//
//    }
//
//}
