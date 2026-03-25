# OpenPDF

## Informacion General

| Campo | Valor |
|-------|-------|
| **Version** | 2.0.3 |
| **Licencia** | LGPL + MPL (libre) |
| **Repositorio** | github.com/LibrePDF/OpenPDF |
| **Enfoque** | API programatica (fork de iText 4) |

## Descripcion

OpenPDF es un fork comunitario de iText 4, creado cuando iText cambio su licencia a AGPL en la version 5. Mantiene la API familiar de iText 4 con mejoras continuas, y ofrece una alternativa **gratuita** para quienes no pueden o no quieren pagar la licencia comercial de iText.

La version 3.0 (2025) agrego un modulo HTML para conversion HTML-to-PDF y soporte para Java 21 virtual threads.

## Ventajas

- **Gratuito**: LGPL + MPL permite uso comercial sin restricciones
- **API familiar**: Misma API que iText 4 (miles de tutoriales disponibles)
- **Mantenimiento activo**: Releases frecuentes, comunidad activa
- **Ligero**: Menos dependencias que iText 7/8
- **Firma digital**: Soporte basico para firmas
- **Nuevo modulo HTML**: Version 3.0 agrega conversion HTML-to-PDF

## Desventajas

- **API legacy**: La API es de iText 4, menos moderna que iText 7/8
- **Documentacion dispersa**: Se basa en tutoriales de iText 4, no tiene sitio propio robusto
- **Menos funcionalidades que iText**: No tiene todas las features avanzadas de iText 7/8
- **PDF/A parcial**: Soporte limitado comparado con iText

## Codigo de Ejemplo (simplificado)

```java
Document document = new Document(PageSize.A4);
PdfWriter.getInstance(document, outputStream);
document.open();
document.add(new Paragraph("COMPROBANTE DE TRAMITE", titleFont));
PdfPTable table = new PdfPTable(4);
table.addCell(new PdfPCell(new Phrase("Item")));
document.add(table);
document.close();
```

## Relacion con iText

| Aspecto | OpenPDF | iText 7/8 |
|---------|---------|-----------|
| Licencia | LGPL + MPL (libre) | AGPL (requiere abrir codigo) o comercial (pago) |
| API | iText 4 (clasica) | API moderna, rediseñada |
| Costo | $0 | $0 (AGPL) o miles USD/ano |
| Funcionalidades | Basicas + firma | Avanzadas (PDF/A, PDF/UA, etc.) |

## Cuando Usar

- Como **alternativa gratuita a iText** para proyectos comerciales
- Cuando se tiene experiencia con **iText 4/5** y se quiere migrar sin costo
- Para documentos de **complejidad media** (tablas, imagenes, firmas basicas)
- Cuando la licencia AGPL de iText es **inaceptable** y no hay presupuesto para licencia comercial
