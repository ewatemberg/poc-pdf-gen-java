package com.acme.pdfpoc.service.impl;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;
import com.acme.pdfpoc.service.PdfGenerator;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class OpenPdfGenerator implements PdfGenerator {

    private static final Color HEADER_COLOR = new Color(26, 35, 126);
    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, HEADER_COLOR);
    private static final Font BOLD_FONT = new Font(Font.HELVETICA, 10, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL);
    private static final Font SMALL_BOLD = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
    private static final Font FOOTER_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY);

    @Override
    public byte[] generate(ComprobanteData data) throws Exception {
        var baos = new ByteArrayOutputStream();
        var document = new Document(PageSize.A4, 50, 50, 50, 60);
        PdfWriter.getInstance(document, baos);
        document.open();

        // Header
        var headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{30, 70});

        try {
            var logoResource = new ClassPathResource("static/logo.png");
            var logo = Image.getInstance(logoResource.getContentAsByteArray());
            logo.scaleToFit(120, 36);
            var logoCell = new PdfPCell(logo);
            logoCell.setBorder(0);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(logoCell);
        } catch (Exception e) {
            var logoCell = new PdfPCell(new Phrase("ACME Corp", BOLD_FONT));
            logoCell.setBorder(0);
            headerTable.addCell(logoCell);
        }

        var titleCell = new PdfPCell(new Phrase("COMPROBANTE DE TRAMITE", TITLE_FONT));
        titleCell.setBorder(0);
        titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTable.addCell(titleCell);
        document.add(headerTable);

        document.add(new Paragraph(" "));
        document.add(createSeparator());

        // Fields
        var fieldsTable = new PdfPTable(2);
        fieldsTable.setWidthPercentage(100);
        fieldsTable.setWidths(new float[]{30, 70});
        fieldsTable.setSpacingBefore(5);
        addFieldRow(fieldsTable, "Empresa:", data.empresa());
        addFieldRow(fieldsTable, "Nro. Tramite:", data.numeroTramite());
        addFieldRow(fieldsTable, "Fecha:", data.fecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addFieldRow(fieldsTable, "Titular:", data.nombreTitular());
        addFieldRow(fieldsTable, "DNI:", data.dni());
        addFieldRow(fieldsTable, "Tipo de Tramite:", data.tipoTramite());
        addFieldRow(fieldsTable, "Estado:", data.estado());
        document.add(fieldsTable);

        document.add(createSeparator());

        // Items title
        document.add(new Paragraph("Detalle de Items", BOLD_FONT));

        // Items table
        var itemsTable = new PdfPTable(4);
        itemsTable.setWidthPercentage(100);
        itemsTable.setWidths(new float[]{8, 22, 45, 25});
        itemsTable.setSpacingBefore(5);

        for (String h : new String[]{"Item", "Concepto", "Descripcion", "Monto ($)"}) {
            var cell = new PdfPCell(new Phrase(h, SMALL_BOLD));
            cell.setBackgroundColor(HEADER_COLOR);
            cell.setPadding(5);
            itemsTable.addCell(cell);
        }

        boolean alt = false;
        for (ItemDetalle item : data.items()) {
            Color bg = alt ? new Color(240, 240, 250) : Color.WHITE;
            addItemCell(itemsTable, String.valueOf(item.item()), bg, Element.ALIGN_LEFT);
            addItemCell(itemsTable, item.concepto(), bg, Element.ALIGN_LEFT);
            addItemCell(itemsTable, item.descripcion(), bg, Element.ALIGN_LEFT);
            addItemCell(itemsTable, String.format("%,.2f", item.monto()), bg, Element.ALIGN_RIGHT);
            alt = !alt;
        }

        // Total
        var totalLabelCell = new PdfPCell(new Phrase("TOTAL", BOLD_FONT));
        totalLabelCell.setColspan(3);
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setPadding(5);
        totalLabelCell.setBorderWidthTop(2);
        itemsTable.addCell(totalLabelCell);

        var totalValueCell = new PdfPCell(new Phrase(
            String.format("$ %,.2f", data.getTotal()), BOLD_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setPadding(5);
        totalValueCell.setBorderWidthTop(2);
        itemsTable.addCell(totalValueCell);

        document.add(itemsTable);

        // Observations
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Observaciones:", BOLD_FONT));
        document.add(new Paragraph(data.observaciones(), SMALL_FONT));

        // Footer
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(
            "Este comprobante es valido como constancia - Generado con OpenPDF", FOOTER_FONT));

        document.close();
        return baos.toByteArray();
    }

    private Paragraph createSeparator() {
        var p = new Paragraph();
        p.add(new Chunk(new com.lowagie.text.pdf.draw.LineSeparator()));
        p.setSpacingBefore(5);
        p.setSpacingAfter(5);
        return p;
    }

    private void addFieldRow(PdfPTable table, String label, String value) {
        var labelCell = new PdfPCell(new Phrase(label, BOLD_FONT));
        labelCell.setBorder(0);
        labelCell.setPaddingBottom(3);
        table.addCell(labelCell);
        var valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(0);
        valueCell.setPaddingBottom(3);
        table.addCell(valueCell);
    }

    private void addItemCell(PdfPTable table, String text, Color bg, int alignment) {
        var cell = new PdfPCell(new Phrase(text, SMALL_FONT));
        cell.setBackgroundColor(bg);
        cell.setPadding(4);
        cell.setHorizontalAlignment(alignment);
        table.addCell(cell);
    }

    @Override
    public String getLibraryName() { return "openpdf"; }

    @Override
    public String getLibraryVersion() { return "2.0.3"; }

    @Override
    public String getLicense() { return "LGPL + MPL"; }

    @Override
    public String getApproach() { return "API programatica (fork iText 4)"; }
}
