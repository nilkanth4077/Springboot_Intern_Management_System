package com.rh4.models;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.rh4.entities.GroupEntity;
import com.rh4.entities.Guide;
import com.rh4.entities.Intern;

import jakarta.servlet.http.HttpServletResponse;

public class InternPDFExporter {
    private List<Intern> listIntern;

    public InternPDFExporter(List<Intern> listIntern) {
        super();
        this.listIntern = listIntern;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("InternID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("GroupId", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("CancellationStatus", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("FirstName", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("LastName", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Gender", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Domain", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("ProjectDefinition", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("JoiningDate", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("CompletionDate", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("ExternalGuide", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("InternalGuide", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("ContactNo", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("DOB", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("College", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Branch", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Semester", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Degree", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("AggregatePercentage", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Address", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("UsedResource", font));
        table.addCell(cell);
    }

    private String getCellValue(String value) {
        return value != null ? value : "";
    }

    private void writeTableData(PdfPTable table) {
        for (Intern intern : listIntern) {
            table.addCell(getCellValue(intern.getInternId()));
            table.addCell(getCellValue(Optional.ofNullable(intern.getGroup()).map(GroupEntity::getGroupId).orElse(null)));
            table.addCell(getCellValue(intern.getCancellationStatus()));
            table.addCell(getCellValue(intern.getFirstName()));
            table.addCell(getCellValue(intern.getLastName()));
            table.addCell(getCellValue(intern.getGender()));
            table.addCell(getCellValue(intern.getDomain()));
            table.addCell(getCellValue(intern.getProjectDefinitionName()));
            table.addCell(getCellValue(String.valueOf(intern.getJoiningDate())));
            table.addCell(getCellValue(String.valueOf(intern.getCompletionDate())));
            table.addCell(getCellValue(Optional.ofNullable(intern.getGuide()).map(Guide::getName).orElse(null)));
            table.addCell(getCellValue(intern.getCollegeGuideHodName()));
            table.addCell(getCellValue(intern.getEmail()));
            table.addCell(getCellValue(intern.getContactNo()));
            table.addCell(getCellValue(String.valueOf(intern.getDateOfBirth())));
            table.addCell(getCellValue(intern.getCollegeName()));
            table.addCell(getCellValue(intern.getBranch()));
            table.addCell(getCellValue(String.valueOf(intern.getSemester())));
            table.addCell(getCellValue(intern.getDegree()));
            table.addCell(getCellValue(String.valueOf(intern.getAggregatePercentage())));
            table.addCell(getCellValue(intern.getPermanentAddress()));
            table.addCell(getCellValue(intern.getUsedResource()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A0); // Landscape orientation
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Interns", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(getColumnCount());
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f}); // Adjust column widths if needed
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }
    private int getColumnCount() {
        // You can adjust this based on the number of fields in your Intern class
        return 22;
    }

}