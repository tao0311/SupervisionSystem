package com.chtc.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    //1.获取工作簿
    private static Workbook getWorkBook(InputStream in, String path) throws IOException {
        return path.endsWith(".xls") ? (new HSSFWorkbook(in))
                : (path.endsWith(".xlsx") ? (new XSSFWorkbook(in)) : (null));
    }

    //2.获取所有工作表
    public static List<Sheet> getSheets(InputStream in, String path) throws IOException {
        Workbook book = getWorkBook(in, path);
        int numberOfSheets = book.getNumberOfSheets();
        List<Sheet> sheets = new ArrayList<Sheet>();
        for (int i = 0; i < numberOfSheets; i++) {
            sheets.add(book.getSheetAt(i));
        }
        return sheets;
    }


}
