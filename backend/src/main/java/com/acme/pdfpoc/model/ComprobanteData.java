package com.acme.pdfpoc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ComprobanteData(
    String empresa,
    String numeroTramite,
    LocalDate fecha,
    String nombreTitular,
    String dni,
    String tipoTramite,
    String estado,
    List<ItemDetalle> items,
    String observaciones
) {
    public BigDecimal getTotal() {
        return items.stream()
            .map(ItemDetalle::monto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
