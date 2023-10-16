package com.app.service.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public final class PdfServiceImpl implements PdfService {
    @Override
    public byte[] generatePDF(List<String> content, String filename) {
        var path = new File("service/src/main/resources/pdf/%s.pdf".formatted(filename))
                .getAbsoluteFile().getPath();
        try (var pdf = new PdfDocument(new PdfWriter(path))){
            var document = new Document(pdf);

            content.forEach(data -> document.add(new Paragraph(data)));
            document.close();
            return Files.readAllBytes(Path.of(path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new byte[] {0};
    }
}
