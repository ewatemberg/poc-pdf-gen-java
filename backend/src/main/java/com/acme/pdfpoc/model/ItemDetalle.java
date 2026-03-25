package com.acme.pdfpoc.model;

import java.math.BigDecimal;

public record ItemDetalle(
    int item,
    String concepto,
    String descripcion,
    BigDecimal monto
) {}
