package com.rh4.services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import com.rh4.entities.Intern;
import com.rh4.models.InternExcelExporter;
import com.rh4.models.InternPDFExporter;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class DataExportService {

	public void exportToPdf(List<Intern> filteredInterns, HttpServletResponse response) throws DocumentException, IOException {
		
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		InternPDFExporter exporter = new InternPDFExporter(filteredInterns);
        exporter.export(response);
	}

	public void exportToExcel(List<Intern> filteredInterns,HttpServletResponse response) throws Exception{
		response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=interns_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        InternExcelExporter excelExporter = new InternExcelExporter(filteredInterns);
         
        excelExporter.export(response);    
	}
}