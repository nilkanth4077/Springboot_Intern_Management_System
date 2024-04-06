package com.rh4.models;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.util.List;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rh4.entities.GroupEntity;
import com.rh4.entities.Guide;
import com.rh4.entities.Intern;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class InternExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Intern> listInterns;
     
    public InternExcelExporter(List<Intern> listntern) {
        this.listInterns = listntern;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Interns");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "InternID", style);      
        createCell(row, 1, "GroupId-mail", style);       
        createCell(row, 2, "FirstName Name", style);    
        createCell(row, 3, "LastName", style);
        createCell(row, 4, "Gender", style);
        createCell(row, 5, "Domain", style);
        createCell(row, 6, "ProjectDefinition", style);
        createCell(row, 7, "JoiningDate", style);
        createCell(row, 8, "CompletionDate", style);
        createCell(row, 9, "ExternalGuide", style);
        createCell(row, 10, "InternalGuide", style);
        createCell(row, 11, "Email", style);
        createCell(row, 12, "ContactNo", style);
        createCell(row, 13, "DOB", style);
        createCell(row, 14, "Branch", style);
        createCell(row, 15, "Semester", style);
        createCell(row, 16, "Degree", style);
        createCell(row, 17, "AggregatePercentage", style);
        createCell(row, 18, "Address", style);
        createCell(row, 19, "UsedResource", style);  
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof String){
            cell.setCellValue((String) value);
        }else {
        	cell.setCellValue("");
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (Intern intern : listInterns) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++,intern.getInternId(), style);
            createCell(row, columnCount++,Optional.ofNullable(intern.getGroup()).map(GroupEntity::getGroupId).orElse(null), style);
            createCell(row, columnCount++,intern.getFirstName(), style);
            createCell(row, columnCount++,intern.getLastName(), style);
            createCell(row, columnCount++,intern.getGender(), style);
            createCell(row, columnCount++,intern.getProgrammingLangName(), style);
            createCell(row, columnCount++,intern.getProjectDefinitionName(), style);
            createCell(row, columnCount++,String.valueOf(intern.getJoiningDate()), style);
            createCell(row, columnCount++,String.valueOf(intern.getCompletionDate()), style);
            createCell(row, columnCount++,Optional.ofNullable(intern.getGuide()).map(Guide::getName).orElse(null), style);
            createCell(row, columnCount++,intern.getCollegeGuideHodName(), style);
            createCell(row, columnCount++,intern.getEmail(), style);
            createCell(row, columnCount++,intern.getContactNo(), style);
            createCell(row, columnCount++,String.valueOf(intern.getDateOfBirth()), style);
            createCell(row, columnCount++,intern.getCollegeName(), style);
            createCell(row, columnCount++,intern.getBranch(), style);
            createCell(row, columnCount++,String.valueOf(intern.getSemester()), style);
            createCell(row, columnCount++,intern.getDegree(), style);
            createCell(row, columnCount++,String.valueOf(intern.getAggregatePercentage()), style);
            createCell(row, columnCount++,intern.getPermanentAddress(), style);
            createCell(row, columnCount++,intern.getUsedResource(), style);
            
                    
        }
    }
     
    public void export(HttpServletResponse response) throws IOException{
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
         
    }
}