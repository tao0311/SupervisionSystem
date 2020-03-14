package com.chtc.util;

import com.chtc.supervision.entity.CourseRecord;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class MyExcel extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String excelName = "听课记录数据.xls";
        // 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(excelName.getBytes(),"iso-8859-1"));


        List<CourseRecord> typeList=(List<CourseRecord>) model.get("all");
//        List<HttpApplication> applicationList=(List<HttpApplication>) model.get("applicationList");
        // 创建Excel的工作sheet,对应到一个excel文档的tab
        HSSFSheet sheet = workbook.createSheet("sheet1");
        // 创建Excel的sheet的一行
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("教师姓名");
        header.createCell(1).setCellValue("教师评分");
        header.createCell(2).setCellValue("院系");
        header.createCell(3).setCellValue("班级总人数");
        header.createCell(4).setCellValue("实到人数");
        header.createCell(5).setCellValue("课程名称");
        header.createCell(6).setCellValue("开始年份");
        header.createCell(7).setCellValue("结束年份");
        header.createCell(8).setCellValue("第几学期");


        int j=1;
        HSSFRow row;
        for(CourseRecord application:typeList){
            row = sheet.createRow(j);
            row.createCell(0).setCellValue(application.getCourse().getUser().getNickName());
            row.createCell(1).setCellValue(application.getScore());
            row.createCell(2).setCellValue(application.getCourse().getUser().getDepartment().getDepartmentName());
            row.createCell(3).setCellValue(application.getTotalCount());
            row.createCell(4).setCellValue(application.getPresentCount());
            row.createCell(5).setCellValue(application.getCourse().getCourseName());
            row.createCell(6).setCellValue(application.getCourse().getSemester().getStartYear());
            row.createCell(7).setCellValue(application.getCourse().getSemester().getEndYear());
            row.createCell(8).setCellValue(application.getCourse().getSemester().getSemesterNum());
            j++;
        }
    }
}
