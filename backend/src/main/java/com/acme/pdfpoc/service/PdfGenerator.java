package com.acme.pdfpoc.service;

import com.acme.pdfpoc.model.ComprobanteData;

public interface PdfGenerator {
    byte[] generate(ComprobanteData data) throws Exception;
    String getLibraryName();
    String getLibraryVersion();
    String getLicense();
    String getApproach();
}
