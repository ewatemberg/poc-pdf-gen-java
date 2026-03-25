package com.acme.pdfpoc.model;

public record LibraryInfo(
    String name,
    String version,
    String license,
    String approach,
    String endpoint
) {}
