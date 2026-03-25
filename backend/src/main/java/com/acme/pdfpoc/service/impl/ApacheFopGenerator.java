package com.acme.pdfpoc.service.impl;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;
import com.acme.pdfpoc.service.PdfGenerator;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.time.format.DateTimeFormatter;

@Service
public class ApacheFopGenerator implements PdfGenerator {

    @Override
    public byte[] generate(ComprobanteData data) throws Exception {
        String foContent = buildFoXml(data);
        var baos = new ByteArrayOutputStream();
        var fopFactory = FopFactory.newInstance(new java.io.File(".").toURI());
        var fop = fopFactory.newFop(MimeConstants.MIME_PDF, baos);
        var transformer = TransformerFactory.newInstance().newTransformer();
        var source = new StreamSource(new StringReader(foContent));
        var result = new SAXResult(fop.getDefaultHandler());
        transformer.transform(source, result);
        return baos.toByteArray();
    }

    private String buildFoXml(ComprobanteData data) {
        var sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">\n");
        sb.append("<fo:layout-master-set>\n");
        sb.append("<fo:simple-page-master master-name=\"A4\" page-height=\"297mm\" page-width=\"210mm\" ");
        sb.append("margin-top=\"15mm\" margin-bottom=\"15mm\" margin-left=\"15mm\" margin-right=\"15mm\">\n");
        sb.append("<fo:region-body margin-top=\"10mm\" margin-bottom=\"15mm\"/>\n");
        sb.append("<fo:region-after extent=\"12mm\"/>\n");
        sb.append("</fo:simple-page-master>\n");
        sb.append("</fo:layout-master-set>\n");

        sb.append("<fo:page-sequence master-reference=\"A4\">\n");

        // Footer
        sb.append("<fo:static-content flow-name=\"xsl-region-after\">\n");
        sb.append("<fo:block font-size=\"8pt\" color=\"#999\" text-align=\"center\" ");
        sb.append("border-top=\"0.5pt solid #ddd\" padding-top=\"3mm\">");
        sb.append("Este comprobante es valido como constancia - Generado con Apache FOP | ");
        sb.append("Pagina <fo:page-number/> de <fo:page-number-citation ref-id=\"last-page\"/>");
        sb.append("</fo:block>\n");
        sb.append("</fo:static-content>\n");

        sb.append("<fo:flow flow-name=\"xsl-region-body\">\n");

        // Title
        sb.append("<fo:block font-size=\"18pt\" font-weight=\"bold\" color=\"#1a237e\" ");
        sb.append("text-align=\"center\" space-after=\"8mm\">");
        sb.append("COMPROBANTE DE TRAMITE</fo:block>\n");

        // Separator
        sb.append("<fo:block border-bottom=\"2pt solid #1a237e\" space-after=\"5mm\"/>\n");

        // Fields
        addFoField(sb, "Empresa:", data.empresa());
        addFoField(sb, "Nro. Tramite:", data.numeroTramite());
        addFoField(sb, "Fecha:", data.fecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addFoField(sb, "Titular:", data.nombreTitular());
        addFoField(sb, "DNI:", data.dni());
        addFoField(sb, "Tipo de Tramite:", data.tipoTramite());
        addFoField(sb, "Estado:", data.estado());

        sb.append("<fo:block border-bottom=\"2pt solid #1a237e\" space-before=\"5mm\" space-after=\"5mm\"/>\n");

        // Items title
        sb.append("<fo:block font-size=\"12pt\" font-weight=\"bold\" space-after=\"3mm\">");
        sb.append("Detalle de Items</fo:block>\n");

        // Items table
        sb.append("<fo:table table-layout=\"fixed\" width=\"100%\" border-collapse=\"collapse\">\n");
        sb.append("<fo:table-column column-width=\"8%\"/>\n");
        sb.append("<fo:table-column column-width=\"22%\"/>\n");
        sb.append("<fo:table-column column-width=\"45%\"/>\n");
        sb.append("<fo:table-column column-width=\"25%\"/>\n");

        // Header
        sb.append("<fo:table-header>\n<fo:table-row background-color=\"#1a237e\" color=\"white\">\n");
        for (String h : new String[]{"Item", "Concepto", "Descripcion", "Monto ($)"}) {
            sb.append("<fo:table-cell padding=\"4pt\"><fo:block font-size=\"9pt\" font-weight=\"bold\">");
            sb.append(h).append("</fo:block></fo:table-cell>\n");
        }
        sb.append("</fo:table-row>\n</fo:table-header>\n");

        // Body
        sb.append("<fo:table-body>\n");
        boolean alt = false;
        for (ItemDetalle item : data.items()) {
            String bg = alt ? " background-color=\"#f0f0fa\"" : "";
            sb.append("<fo:table-row").append(bg).append(">\n");
            addFoCell(sb, String.valueOf(item.item()), "left");
            addFoCell(sb, item.concepto(), "left");
            addFoCell(sb, item.descripcion(), "left");
            addFoCell(sb, String.format("%,.2f", item.monto()), "right");
            sb.append("</fo:table-row>\n");
            alt = !alt;
        }

        // Total row
        sb.append("<fo:table-row border-top=\"2pt solid #333\" font-weight=\"bold\">\n");
        sb.append("<fo:table-cell number-columns-spanned=\"3\" padding=\"5pt\">");
        sb.append("<fo:block font-size=\"10pt\" text-align=\"right\">TOTAL</fo:block></fo:table-cell>\n");
        sb.append("<fo:table-cell padding=\"5pt\">");
        sb.append("<fo:block font-size=\"10pt\" text-align=\"right\">$ ");
        sb.append(String.format("%,.2f", data.getTotal())).append("</fo:block></fo:table-cell>\n");
        sb.append("</fo:table-row>\n");
        sb.append("</fo:table-body>\n</fo:table>\n");

        // Observations
        sb.append("<fo:block font-size=\"10pt\" font-weight=\"bold\" space-before=\"10mm\" space-after=\"3mm\">");
        sb.append("Observaciones:</fo:block>\n");
        sb.append("<fo:block font-size=\"9pt\" line-height=\"1.4\">");
        sb.append(escapeXml(data.observaciones())).append("</fo:block>\n");

        // Last page marker
        sb.append("<fo:block id=\"last-page\"/>\n");

        sb.append("</fo:flow>\n</fo:page-sequence>\n</fo:root>");
        return sb.toString();
    }

    private void addFoField(StringBuilder sb, String label, String value) {
        sb.append("<fo:block font-size=\"10pt\" space-after=\"2mm\">");
        sb.append("<fo:inline font-weight=\"bold\">").append(label).append("</fo:inline> ");
        sb.append(escapeXml(value));
        sb.append("</fo:block>\n");
    }

    private void addFoCell(StringBuilder sb, String text, String align) {
        sb.append("<fo:table-cell padding=\"4pt\" border-bottom=\"0.5pt solid #e0e0e0\">");
        sb.append("<fo:block font-size=\"9pt\" text-align=\"").append(align).append("\">");
        sb.append(escapeXml(text)).append("</fo:block></fo:table-cell>\n");
    }

    private String escapeXml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public String getLibraryName() { return "fop"; }

    @Override
    public String getLibraryVersion() { return "2.10"; }

    @Override
    public String getLicense() { return "Apache 2.0"; }

    @Override
    public String getApproach() { return "XSL-FO (XML/XSLT a PDF)"; }
}
