package com.acme.pdfpoc.controller;

import com.acme.pdfpoc.data.ComprobanteDataFactory;
import com.acme.pdfpoc.model.LibraryInfo;
import com.acme.pdfpoc.service.PdfGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:4200")
public class PdfController {

    private static final Logger log = LoggerFactory.getLogger(PdfController.class);
    private final Map<String, PdfGenerator> generators;

    public PdfController(List<PdfGenerator> generators) {
        this.generators = generators.stream()
            .collect(Collectors.toMap(
                g -> g.getLibraryName().toLowerCase(),
                Function.identity()
            ));
        log.info("Registered {} PDF generators: {}", this.generators.size(), this.generators.keySet());
    }

    @GetMapping("/{library}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String library) {
        var generator = generators.get(library.toLowerCase());
        if (generator == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            var data = ComprobanteDataFactory.createSampleData();
            long start = System.nanoTime();
            byte[] pdf = generator.generate(data);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            log.info("Generated PDF with {} in {} ms ({} bytes)",
                generator.getLibraryName(), elapsedMs, pdf.length);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=comprobante-" + library + ".pdf")
                .header("X-Generation-Time-Ms", String.valueOf(elapsedMs))
                .header("X-Pdf-Size-Bytes", String.valueOf(pdf.length))
                .header("X-Library-Name", generator.getLibraryName())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                    "X-Generation-Time-Ms, X-Pdf-Size-Bytes, X-Library-Name")
                .body(pdf);
        } catch (Exception e) {
            log.error("Error generating PDF with {}: {}", library, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/libraries")
    public List<LibraryInfo> getLibraries() {
        return generators.values().stream()
            .map(g -> new LibraryInfo(
                g.getLibraryName(),
                g.getLibraryVersion(),
                g.getLicense(),
                g.getApproach(),
                "/api/pdf/" + g.getLibraryName().toLowerCase()
            ))
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();
    }
}
