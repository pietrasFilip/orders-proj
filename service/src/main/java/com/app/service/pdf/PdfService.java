package com.app.service.pdf;

import java.util.List;

public sealed interface PdfService permits PdfServiceImpl{
    byte[] generatePDF(List<String> content, String filename);
}
