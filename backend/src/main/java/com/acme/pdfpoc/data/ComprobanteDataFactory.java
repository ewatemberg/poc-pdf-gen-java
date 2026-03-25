package com.acme.pdfpoc.data;

import com.acme.pdfpoc.model.ComprobanteData;
import com.acme.pdfpoc.model.ItemDetalle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class ComprobanteDataFactory {

    private ComprobanteDataFactory() {}

    public static ComprobanteData createSampleData() {
        var items = List.of(
            new ItemDetalle(1, "Derecho de tramite",
                "Arancel administrativo por inicio de tramite", new BigDecimal("1500.00")),
            new ItemDetalle(2, "Emision de credencial",
                "Confeccion y emision de credencial plastica", new BigDecimal("2800.00")),
            new ItemDetalle(3, "Seguro de cobertura",
                "Prima mensual del seguro basico", new BigDecimal("4200.50")),
            new ItemDetalle(4, "Gastos administrativos",
                "Gastos de gestion y archivo", new BigDecimal("750.00")),
            new ItemDetalle(5, "Sellado fiscal",
                "Tasa provincial de sellado", new BigDecimal("320.75"))
        );

        return new ComprobanteData(
            "ACME Corp S.A.",
            "TRM-2024-00158234",
            LocalDate.of(2024, 11, 15),
            "Juan Carlos Perez",
            "30.555.888",
            "Renovacion de Credencial",
            "Aprobado",
            items,
            "Este comprobante es valido como constancia del tramite realizado. "
                + "Conserve este documento para futuras consultas. "
                + "Ante cualquier duda comuniquese al centro de atencion al cliente."
        );
    }
}
