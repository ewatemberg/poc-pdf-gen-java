package com.acme.pdfpoc.service.impl;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;
import com.acme.pdfpoc.service.PdfGenerator;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class JasperReportsGenerator implements PdfGenerator {

    @Override
    public byte[] generate(ComprobanteData data) throws Exception {
        var jrxmlStream = new ClassPathResource("jasper/comprobante.jrxml").getInputStream();
        JasperReport report = JasperCompileManager.compileReport(jrxmlStream);

        var params = new HashMap<String, Object>();
        params.put("empresa", data.empresa());
        params.put("numeroTramite", data.numeroTramite());
        params.put("fecha", data.fecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        params.put("nombreTitular", data.nombreTitular());
        params.put("dni", data.dni());
        params.put("tipoTramite", data.tipoTramite());
        params.put("estado", data.estado());
        params.put("total", String.format("$ %,.2f", data.getTotal()));
        params.put("observaciones", data.observaciones());
        try {
            params.put("logoPath", new ClassPathResource("static/logo.png").getURL().toString());
        } catch (Exception ignored) {}

        var dataSource = new JRBeanCollectionDataSource(data.items());
        JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);

        var baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(print, baos);
        return baos.toByteArray();
    }

    @Override
    public String getLibraryName() { return "jasper"; }

    @Override
    public String getLibraryVersion() { return "7.0.1"; }

    @Override
    public String getLicense() { return "LGPL 3.0"; }

    @Override
    public String getApproach() { return "Templates JRXML (visual)"; }
}
