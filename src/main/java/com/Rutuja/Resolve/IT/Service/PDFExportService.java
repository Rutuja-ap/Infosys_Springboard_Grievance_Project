package com.Rutuja.Resolve.IT.Service;

import com.Rutuja.Resolve.IT.Model.Complaint;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList; 


import java.util.List;
import java.io.ByteArrayOutputStream;

@Service
public class PDFExportService {

    public ByteArrayResource exportToPDF(List<Complaint> complaints) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Complaints Report"));
            document.add(new Paragraph("=================="));
            document.add(new Paragraph(" "));

            for (Complaint c : complaints) {
                document.add(new Paragraph("ID: " + c.getId()));
                document.add(new Paragraph("Title: " + safe(c.getTitle())));
                document.add(new Paragraph("Category: " + safe(c.getCategory())));
                document.add(new Paragraph("Status: " + safe(c.getStatus())));
                document.add(new Paragraph("CreatedDate: " + c.getCreatedAt()));
                document.add(new Paragraph("UserId: " + c.getUserId()));
                document.add(new Paragraph("OfficerId: " + c.getOfficerId()));
                document.add(new Paragraph("Location: " + safe(c.getLocation())));
                document.add(new Paragraph("Description: " + safe(c.getDescription())));
                document.add(new Paragraph("-----------------------------"));
            }

            document.close();
            return new ByteArrayResource(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
