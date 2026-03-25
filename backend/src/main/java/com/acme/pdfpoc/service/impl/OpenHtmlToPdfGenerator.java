package com.acme.pdfpoc.service.impl;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;
import com.acme.pdfpoc.service.PdfGenerator;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class OpenHtmlToPdfGenerator implements PdfGenerator {

    @Override
    public byte[] generate(ComprobanteData data) throws Exception {
        String html = buildHtml(data);
        var baos = new ByteArrayOutputStream();
        var builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(baos);
        builder.run();
        return baos.toByteArray();
    }

    private String buildHtml(ComprobanteData data) throws Exception {
        String logoBase64 = "";
        try {
            var logoResource = new ClassPathResource("static/logo.png");
            logoBase64 = Base64.getEncoder().encodeToString(logoResource.getContentAsByteArray());
        } catch (Exception ignored) {}

        var sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<style>\n");
        sb.append("""
            body { font-family: 'Helvetica', 'Arial', sans-serif; font-size: 10pt; margin: 40px; color: #333; }
            .header { overflow: hidden; margin-bottom: 15px; }
            .header-left { float: left; width: 30%; }
            .header-right { float: right; width: 65%; text-align: right; padding-top: 5px; }
            .title { font-size: 18pt; font-weight: bold; color: #1a237e; }
            .separator { border-top: 2px solid #1a237e; margin: 10px 0; clear: both; }
            .field-table { width: 100%; margin-bottom: 10px; border-collapse: collapse; }
            .field-table td { padding: 3px 0; }
            .field-label { font-weight: bold; width: 30%; }
            .items-title { font-weight: bold; font-size: 12pt; margin: 15px 0 5px 0; }
            table.items { width: 100%; border-collapse: collapse; margin-top: 5px; }
            table.items th { background-color: #1a237e; color: white; padding: 6px; font-size: 9pt; text-align: left; }
            table.items td { padding: 5px 6px; font-size: 9pt; border-bottom: 1px solid #e0e0e0; }
            table.items tr:nth-child(even) { background-color: #f5f5ff; }
            .total-row { font-weight: bold; font-size: 10pt; border-top: 2px solid #1a237e !important; }
            .total-row td { border-top: 2px solid #1a237e; padding-top: 8px; }
            .text-right { text-align: right; }
            .observations { margin-top: 20px; }
            .observations h3 { font-size: 10pt; margin-bottom: 5px; }
            .observations p { font-size: 9pt; line-height: 1.4; }
            .footer { margin-top: 40px; font-size: 8pt; color: #999; border-top: 1px solid #ddd; padding-top: 8px; }
            @page { size: A4; margin: 50px; }
            """);
        sb.append("</style>\n</head>\n<body>\n");

        // Header
        sb.append("<div class=\"header\">\n");
        sb.append("<div class=\"header-left\">\n");
        if (!logoBase64.isEmpty()) {
            sb.append("<img src=\"data:image/png;base64,").append(logoBase64)
                .append("\" width=\"120\" height=\"36\" />\n");
        }
        sb.append("</div>\n");
        sb.append("<div class=\"header-right\"><span class=\"title\">COMPROBANTE DE TRAMITE</span></div>\n");
        sb.append("</div>\n<div class=\"separator\"></div>\n");

        // Fields
        sb.append("<table class=\"field-table\">\n");
        appendField(sb, "Empresa:", data.empresa());
        appendField(sb, "Nro. Tramite:", data.numeroTramite());
        appendField(sb, "Fecha:", data.fecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        appendField(sb, "Titular:", data.nombreTitular());
        appendField(sb, "DNI:", data.dni());
        appendField(sb, "Tipo de Tramite:", data.tipoTramite());
        appendField(sb, "Estado:", data.estado());
        sb.append("</table>\n<div class=\"separator\"></div>\n");

        // Items
        sb.append("<p class=\"items-title\">Detalle de Items</p>\n");
        sb.append("<table class=\"items\">\n<thead><tr>");
        sb.append("<th>Item</th><th>Concepto</th><th>Descripcion</th><th class=\"text-right\">Monto ($)</th>");
        sb.append("</tr></thead>\n<tbody>\n");
        for (ItemDetalle item : data.items()) {
            sb.append("<tr>");
            sb.append("<td>").append(item.item()).append("</td>");
            sb.append("<td>").append(item.concepto()).append("</td>");
            sb.append("<td>").append(item.descripcion()).append("</td>");
            sb.append("<td class=\"text-right\">").append(String.format("%,.2f", item.monto())).append("</td>");
            sb.append("</tr>\n");
        }
        sb.append("<tr class=\"total-row\">");
        sb.append("<td colspan=\"3\" class=\"text-right\">TOTAL</td>");
        sb.append("<td class=\"text-right\">$ ").append(String.format("%,.2f", data.getTotal())).append("</td>");
        sb.append("</tr>\n</tbody>\n</table>\n");

        // Observations
        sb.append("<div class=\"observations\">\n");
        sb.append("<h3>Observaciones:</h3>\n");
        sb.append("<p>").append(data.observaciones()).append("</p>\n</div>\n");

        // Footer
        sb.append("<div class=\"footer\">");
        sb.append("Este comprobante es valido como constancia - Generado con OpenHTMLtoPDF");
        sb.append("</div>\n");

        sb.append("</body>\n</html>");
        return sb.toString();
    }

    private void appendField(StringBuilder sb, String label, String value) {
        sb.append("<tr><td class=\"field-label\">").append(label)
            .append("</td><td>").append(value).append("</td></tr>\n");
    }

    @Override
    public String getLibraryName() { return "openhtmltopdf"; }

    @Override
    public String getLibraryVersion() { return "1.1.4"; }

    @Override
    public String getLicense() { return "LGPL 2.1"; }

    @Override
    public String getApproach() { return "HTML/CSS a PDF (moderno, sobre PDFBox)"; }
}
