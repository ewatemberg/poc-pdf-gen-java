package com.acme.pdfpoc.service.impl;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;
import com.acme.pdfpoc.service.PdfGenerator;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ITextGenerator implements PdfGenerator {

    private static final DeviceRgb HEADER_BG = new DeviceRgb(26, 35, 126);

    @Override
    public byte[] generate(ComprobanteData data) throws Exception {
        var baos = new ByteArrayOutputStream();
        var writer = new PdfWriter(baos);
        var pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4);

        // Footer handler
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterHandler());

        var document = new Document(pdfDoc);
        document.setMargins(50, 50, 60, 50);

        var boldFont = PdfFontFactory.createFont("Helvetica-Bold");
        var normalFont = PdfFontFactory.createFont("Helvetica");

        // Header with logo
        var headerTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
            .useAllAvailableWidth();
        try {
            var logoResource = new ClassPathResource("static/logo.png");
            var logoImage = new Image(ImageDataFactory.create(logoResource.getContentAsByteArray()))
                .setWidth(120);
            headerTable.addCell(new Cell().add(logoImage).setBorder(null));
        } catch (Exception e) {
            headerTable.addCell(new Cell().add(new Paragraph("ACME Corp")).setBorder(null));
        }
        headerTable.addCell(new Cell()
            .add(new Paragraph("COMPROBANTE DE TRAMITE")
                .setFont(boldFont).setFontSize(18).setFontColor(HEADER_BG))
            .setTextAlignment(TextAlignment.RIGHT)
            .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
            .setBorder(null));
        document.add(headerTable);

        // Separator
        document.add(new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine())
            .setMarginTop(10).setMarginBottom(10));

        // Data fields
        var fieldsTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
            .useAllAvailableWidth();
        addFieldRow(fieldsTable, boldFont, normalFont, "Empresa:", data.empresa());
        addFieldRow(fieldsTable, boldFont, normalFont, "Nro. Tramite:", data.numeroTramite());
        addFieldRow(fieldsTable, boldFont, normalFont, "Fecha:",
            data.fecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addFieldRow(fieldsTable, boldFont, normalFont, "Titular:", data.nombreTitular());
        addFieldRow(fieldsTable, boldFont, normalFont, "DNI:", data.dni());
        addFieldRow(fieldsTable, boldFont, normalFont, "Tipo de Tramite:", data.tipoTramite());
        addFieldRow(fieldsTable, boldFont, normalFont, "Estado:", data.estado());
        document.add(fieldsTable);

        document.add(new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine())
            .setMarginTop(10).setMarginBottom(10));

        // Items table
        document.add(new Paragraph("Detalle de Items").setFont(boldFont).setFontSize(12));
        var itemsTable = new Table(UnitValue.createPercentArray(new float[]{8, 22, 45, 25}))
            .useAllAvailableWidth()
            .setMarginTop(5);

        // Table header
        for (String header : new String[]{"Item", "Concepto", "Descripcion", "Monto ($)"}) {
            itemsTable.addHeaderCell(new Cell()
                .add(new Paragraph(header).setFont(boldFont).setFontSize(9).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(HEADER_BG)
                .setPadding(5));
        }

        // Table rows
        boolean alternate = false;
        for (ItemDetalle item : data.items()) {
            var bgColor = alternate ? new DeviceRgb(240, 240, 250) : ColorConstants.WHITE;
            itemsTable.addCell(createCell(String.valueOf(item.item()), normalFont, bgColor));
            itemsTable.addCell(createCell(item.concepto(), normalFont, bgColor));
            itemsTable.addCell(createCell(item.descripcion(), normalFont, bgColor));
            itemsTable.addCell(createCell(String.format("%,.2f", item.monto()), normalFont, bgColor)
                .setTextAlignment(TextAlignment.RIGHT));
            alternate = !alternate;
        }

        // Total row
        itemsTable.addCell(new Cell(1, 3)
            .add(new Paragraph("TOTAL").setFont(boldFont).setFontSize(10))
            .setTextAlignment(TextAlignment.RIGHT)
            .setPadding(5));
        itemsTable.addCell(new Cell()
            .add(new Paragraph(String.format("$ %,.2f", data.getTotal()))
                .setFont(boldFont).setFontSize(10))
            .setTextAlignment(TextAlignment.RIGHT)
            .setPadding(5));

        document.add(itemsTable);

        // Observations
        document.add(new Paragraph("Observaciones:").setFont(boldFont).setFontSize(10).setMarginTop(20));
        document.add(new Paragraph(data.observaciones()).setFont(normalFont).setFontSize(9));

        document.close();
        return baos.toByteArray();
    }

    private void addFieldRow(Table table, com.itextpdf.kernel.font.PdfFont bold,
                             com.itextpdf.kernel.font.PdfFont normal, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label).setFont(bold).setFontSize(10))
            .setBorder(null).setPaddingBottom(3));
        table.addCell(new Cell().add(new Paragraph(value).setFont(normal).setFontSize(10))
            .setBorder(null).setPaddingBottom(3));
    }

    private Cell createCell(String text, com.itextpdf.kernel.font.PdfFont font,
                            com.itextpdf.kernel.colors.Color bgColor) {
        return new Cell()
            .add(new Paragraph(text).setFont(font).setFontSize(9))
            .setBackgroundColor(bgColor)
            .setPadding(4);
    }

    private static class FooterHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            var docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            var pdfDoc = docEvent.getDocument();
            int pageNum = pdfDoc.getPageNumber(page);
            int totalPages = pdfDoc.getNumberOfPages();
            var canvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
            try {
                var font = PdfFontFactory.createFont("Helvetica");
                canvas.beginText()
                    .setFontAndSize(font, 8)
                    .moveText(50, 30)
                    .showText("Este comprobante es valido como constancia - Generado con iText")
                    .endText();
                canvas.beginText()
                    .setFontAndSize(font, 8)
                    .moveText(480, 30)
                    .showText("Pagina " + pageNum + " de " + totalPages)
                    .endText();
            } catch (Exception ignored) {}
            canvas.release();
        }
    }

    @Override
    public String getLibraryName() { return "itext"; }

    @Override
    public String getLibraryVersion() { return "8.0.5"; }

    @Override
    public String getLicense() { return "AGPL v3 / Comercial"; }

    @Override
    public String getApproach() { return "API high-level programatica"; }
}
