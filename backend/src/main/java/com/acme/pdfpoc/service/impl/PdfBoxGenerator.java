package com.acme.pdfpoc.service.impl;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;
import com.acme.pdfpoc.service.PdfGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class PdfBoxGenerator implements PdfGenerator {

    private static final float MARGIN = 50;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN;

    @Override
    public byte[] generate(ComprobanteData data) throws Exception {
        try (var document = new PDDocument(); var baos = new ByteArrayOutputStream()) {
            var page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            var fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            var fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            float y = PAGE_HEIGHT - MARGIN;

            try (var cs = new PDPageContentStream(document, page)) {
                // Logo
                try {
                    var logoResource = new ClassPathResource("static/logo.png");
                    var logo = PDImageXObject.createFromByteArray(document,
                        logoResource.getContentAsByteArray(), "logo.png");
                    cs.drawImage(logo, MARGIN, y - 40, 120, 36);
                } catch (Exception ignored) {
                    // Logo not available, skip
                }

                // Title
                y -= 20;
                cs.beginText();
                cs.setFont(fontBold, 18);
                cs.newLineAtOffset(200, y);
                cs.showText("COMPROBANTE DE TRAMITE");
                cs.endText();

                // Horizontal line
                y -= 30;
                cs.setLineWidth(1f);
                cs.moveTo(MARGIN, y);
                cs.lineTo(PAGE_WIDTH - MARGIN, y);
                cs.stroke();

                // Fields
                y -= 25;
                y = drawField(cs, fontBold, fontNormal, "Empresa:", data.empresa(), y);
                y = drawField(cs, fontBold, fontNormal, "Nro. Tramite:", data.numeroTramite(), y);
                y = drawField(cs, fontBold, fontNormal, "Fecha:",
                    data.fecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), y);
                y = drawField(cs, fontBold, fontNormal, "Titular:", data.nombreTitular(), y);
                y = drawField(cs, fontBold, fontNormal, "DNI:", data.dni(), y);
                y = drawField(cs, fontBold, fontNormal, "Tipo de Tramite:", data.tipoTramite(), y);
                y = drawField(cs, fontBold, fontNormal, "Estado:", data.estado(), y);

                // Separator
                y -= 15;
                cs.moveTo(MARGIN, y);
                cs.lineTo(PAGE_WIDTH - MARGIN, y);
                cs.stroke();

                // Table header
                y -= 20;
                cs.beginText();
                cs.setFont(fontBold, 10);
                cs.newLineAtOffset(MARGIN, y);
                cs.showText("Item");
                cs.endText();
                cs.beginText();
                cs.setFont(fontBold, 10);
                cs.newLineAtOffset(MARGIN + 40, y);
                cs.showText("Concepto");
                cs.endText();
                cs.beginText();
                cs.setFont(fontBold, 10);
                cs.newLineAtOffset(MARGIN + 180, y);
                cs.showText("Descripcion");
                cs.endText();
                cs.beginText();
                cs.setFont(fontBold, 10);
                cs.newLineAtOffset(PAGE_WIDTH - MARGIN - 70, y);
                cs.showText("Monto ($)");
                cs.endText();

                y -= 5;
                cs.moveTo(MARGIN, y);
                cs.lineTo(PAGE_WIDTH - MARGIN, y);
                cs.stroke();

                // Table rows
                for (ItemDetalle item : data.items()) {
                    y -= 18;
                    cs.beginText();
                    cs.setFont(fontNormal, 9);
                    cs.newLineAtOffset(MARGIN + 10, y);
                    cs.showText(String.valueOf(item.item()));
                    cs.endText();
                    cs.beginText();
                    cs.setFont(fontNormal, 9);
                    cs.newLineAtOffset(MARGIN + 40, y);
                    cs.showText(item.concepto());
                    cs.endText();
                    cs.beginText();
                    cs.setFont(fontNormal, 9);
                    cs.newLineAtOffset(MARGIN + 180, y);
                    cs.showText(truncate(item.descripcion(), 35));
                    cs.endText();
                    cs.beginText();
                    cs.setFont(fontNormal, 9);
                    cs.newLineAtOffset(PAGE_WIDTH - MARGIN - 60, y);
                    cs.showText(String.format("%,.2f", item.monto()));
                    cs.endText();
                }

                // Total
                y -= 5;
                cs.moveTo(MARGIN, y);
                cs.lineTo(PAGE_WIDTH - MARGIN, y);
                cs.stroke();
                y -= 18;
                cs.beginText();
                cs.setFont(fontBold, 11);
                cs.newLineAtOffset(MARGIN + 180, y);
                cs.showText("TOTAL:");
                cs.endText();
                cs.beginText();
                cs.setFont(fontBold, 11);
                cs.newLineAtOffset(PAGE_WIDTH - MARGIN - 60, y);
                cs.showText(String.format("$ %,.2f", data.getTotal()));
                cs.endText();

                // Observations
                y -= 40;
                cs.beginText();
                cs.setFont(fontBold, 10);
                cs.newLineAtOffset(MARGIN, y);
                cs.showText("Observaciones:");
                cs.endText();
                y -= 15;
                cs.beginText();
                cs.setFont(fontNormal, 9);
                cs.setLeading(14f);
                cs.newLineAtOffset(MARGIN, y);
                for (String line : wrapText(data.observaciones(), 90)) {
                    cs.showText(line);
                    cs.newLine();
                }
                cs.endText();

                // Footer
                cs.beginText();
                cs.setFont(fontNormal, 8);
                cs.newLineAtOffset(MARGIN, 40);
                cs.showText("Este comprobante es valido como constancia - Generado con PDFBox");
                cs.endText();
                cs.beginText();
                cs.setFont(fontNormal, 8);
                cs.newLineAtOffset(PAGE_WIDTH - MARGIN - 50, 40);
                cs.showText("Pagina 1 de 1");
                cs.endText();
            }

            document.save(baos);
            return baos.toByteArray();
        }
    }

    private float drawField(PDPageContentStream cs, PDType1Font bold, PDType1Font normal,
                            String label, String value, float y) throws Exception {
        cs.beginText();
        cs.setFont(bold, 10);
        cs.newLineAtOffset(MARGIN, y);
        cs.showText(label);
        cs.endText();
        cs.beginText();
        cs.setFont(normal, 10);
        cs.newLineAtOffset(MARGIN + 120, y);
        cs.showText(value);
        cs.endText();
        return y - 18;
    }

    private String truncate(String text, int maxLen) {
        return text.length() > maxLen ? text.substring(0, maxLen - 3) + "..." : text;
    }

    private String[] wrapText(String text, int maxCharsPerLine) {
        var words = text.split(" ");
        var sb = new StringBuilder();
        var lines = new java.util.ArrayList<String>();
        for (String word : words) {
            if (sb.length() + word.length() + 1 > maxCharsPerLine) {
                lines.add(sb.toString().trim());
                sb = new StringBuilder();
            }
            sb.append(word).append(" ");
        }
        if (!sb.isEmpty()) lines.add(sb.toString().trim());
        return lines.toArray(String[]::new);
    }

    @Override
    public String getLibraryName() { return "pdfbox"; }

    @Override
    public String getLibraryVersion() { return "3.0.3"; }

    @Override
    public String getLicense() { return "Apache 2.0"; }

    @Override
    public String getApproach() { return "API low-level programatica"; }
}
