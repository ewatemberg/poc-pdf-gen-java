package com.acme.pdfpoc.service.impl;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;
import com.acme.pdfpoc.service.PdfGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class FlyingSaucerGenerator implements PdfGenerator {

    @Override
    public byte[] generate(ComprobanteData data) throws Exception {
        String xhtml = buildXhtml(data);
        var baos = new ByteArrayOutputStream();
        var renderer = new ITextRenderer();
        renderer.setDocumentFromString(xhtml);
        renderer.layout();
        renderer.createPDF(baos);
        return baos.toByteArray();
    }

    private String buildXhtml(ComprobanteData data) throws Exception {
        String logoBase64 = "";
        try {
            var logoResource = new ClassPathResource("static/logo.png");
            logoBase64 = Base64.getEncoder().encodeToString(logoResource.getContentAsByteArray());
        } catch (Exception ignored) {}

        var sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" ");
        sb.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<style>\n");
        sb.append("""
            body { font-family: Helvetica, Arial, sans-serif; font-size: 10pt; margin: 40px; }
            .header { display: table; width: 100%; margin-bottom: 15px; }
            .header-left { display: table-cell; width: 30%; vertical-align: middle; }
            .header-right { display: table-cell; width: 70%; text-align: right; vertical-align: middle; }
            .title { font-size: 18pt; font-weight: bold; color: #1a237e; }
            hr { border: 1px solid #333; margin: 10px 0; }
            .field-table { width: 100%; margin-bottom: 10px; }
            .field-label { font-weight: bold; width: 30%; padding: 3px 0; }
            .field-value { width: 70%; padding: 3px 0; }
            .items-title { font-weight: bold; font-size: 12pt; margin: 15px 0 5px 0; }
            table.items { width: 100%; border-collapse: collapse; margin-top: 5px; }
            table.items th { background-color: #1a237e; color: white; padding: 5px; font-size: 9pt; text-align: left; }
            table.items td { padding: 4px 5px; font-size: 9pt; border-bottom: 1px solid #ddd; }
            table.items tr.alt { background-color: #f0f0fa; }
            .total-row td { font-weight: bold; font-size: 10pt; border-top: 2px solid #333; }
            .observations { margin-top: 20px; }
            .observations h3 { font-size: 10pt; margin-bottom: 5px; }
            .observations p { font-size: 9pt; }
            .footer { position: fixed; bottom: 0; left: 0; right: 0; padding: 10px 40px;
                      font-size: 8pt; color: #666; display: table; width: 100%; }
            .footer-left { display: table-cell; text-align: left; }
            .footer-right { display: table-cell; text-align: right; }
            @page { size: A4; margin: 50px; margin-bottom: 60px;
                    @bottom-center { content: "Pagina " counter(page) " de " counter(pages); font-size: 8pt; } }
            """);
        sb.append("</style>\n</head>\n<body>\n");

        // Header
        sb.append("<div class=\"header\">\n");
        sb.append("<div class=\"header-left\">\n");
        if (!logoBase64.isEmpty()) {
            sb.append("<img src=\"data:image/png;base64,").append(logoBase64)
                .append("\" width=\"120\" height=\"36\" alt=\"Logo\" />\n");
        }
        sb.append("</div>\n");
        sb.append("<div class=\"header-right\"><span class=\"title\">COMPROBANTE DE TRAMITE</span></div>\n");
        sb.append("</div>\n<hr />\n");

        // Fields
        sb.append("<table class=\"field-table\">\n");
        appendField(sb, "Empresa:", data.empresa());
        appendField(sb, "Nro. Tramite:", data.numeroTramite());
        appendField(sb, "Fecha:", data.fecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        appendField(sb, "Titular:", data.nombreTitular());
        appendField(sb, "DNI:", data.dni());
        appendField(sb, "Tipo de Tramite:", data.tipoTramite());
        appendField(sb, "Estado:", data.estado());
        sb.append("</table>\n<hr />\n");

        // Items table
        sb.append("<p class=\"items-title\">Detalle de Items</p>\n");
        sb.append("<table class=\"items\">\n<thead>\n<tr>");
        sb.append("<th>Item</th><th>Concepto</th><th>Descripcion</th><th>Monto ($)</th>");
        sb.append("</tr>\n</thead>\n<tbody>\n");
        boolean alt = false;
        for (ItemDetalle item : data.items()) {
            sb.append(alt ? "<tr class=\"alt\">" : "<tr>");
            sb.append("<td>").append(item.item()).append("</td>");
            sb.append("<td>").append(item.concepto()).append("</td>");
            sb.append("<td>").append(item.descripcion()).append("</td>");
            sb.append("<td style=\"text-align:right;\">").append(String.format("%,.2f", item.monto())).append("</td>");
            sb.append("</tr>\n");
            alt = !alt;
        }
        sb.append("<tr class=\"total-row\">");
        sb.append("<td colspan=\"3\" style=\"text-align:right;\">TOTAL</td>");
        sb.append("<td style=\"text-align:right;\">$ ").append(String.format("%,.2f", data.getTotal())).append("</td>");
        sb.append("</tr>\n</tbody>\n</table>\n");

        // Observations
        sb.append("<div class=\"observations\">\n");
        sb.append("<h3>Observaciones:</h3>\n");
        sb.append("<p>").append(data.observaciones()).append("</p>\n");
        sb.append("</div>\n");

        // Footer
        sb.append("<div class=\"footer\">\n");
        sb.append("<div class=\"footer-left\">Este comprobante es valido como constancia - Generado con Flying Saucer</div>\n");
        sb.append("</div>\n");

        sb.append("</body>\n</html>");
        return sb.toString();
    }

    private void appendField(StringBuilder sb, String label, String value) {
        sb.append("<tr><td class=\"field-label\">").append(label)
            .append("</td><td class=\"field-value\">").append(value).append("</td></tr>\n");
    }

    @Override
    public String getLibraryName() { return "flysaucer"; }

    @Override
    public String getLibraryVersion() { return "9.9.1"; }

    @Override
    public String getLicense() { return "LGPL 2.1"; }

    @Override
    public String getApproach() { return "HTML/CSS a PDF (XHTML)"; }
}
