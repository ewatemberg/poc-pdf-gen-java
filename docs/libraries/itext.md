# iText

## Informacion General

| Campo | Valor |
|-------|-------|
| **Version** | 8.0.5 |
| **Licencia** | AGPL v3 / Comercial |
| **Mantenedor** | iText Group NV (empresa comercial) |
| **Sitio** | itextpdf.com |
| **Enfoque** | API high-level programatica |

## Descripcion

iText es la libreria de generacion de PDF mas completa del ecosistema Java. Ofrece una API de alto nivel con componentes como Document, Paragraph, Table y Cell que manejan automaticamente el layout, la paginacion y la alineacion.

## Ventajas

- **API de alto nivel**: Tablas, parrafos, imagenes con layout automatico
- **Paginacion automatica**: El contenido fluye entre paginas sin intervencion
- **Documentacion excelente**: Sitio comercial con tutoriales detallados
- **Funcionalidades avanzadas**: PDF/A, PDF/UA, firma digital, encriptacion
- **Soporte comercial**: Empresa dedicada con soporte profesional
- **Rendimiento**: Rapido para documentos complejos

## Desventajas

- **Licencia AGPL**: Obliga a publicar TODO el codigo fuente de la aplicacion que la use
- **Costo comercial**: Para uso en software propietario se requiere licencia paga (miles de USD/ano)
- **Peso medio**: Mas dependencias que PDFBox
- **Cambios entre versiones**: API diferente entre v5, v7 y v8

## Alerta de Licencia

> **IMPORTANTE**: iText 7/8 usa licencia **AGPL v3**. Esto significa que cualquier software que use iText debe:
> - Publicar su codigo fuente completo bajo AGPL, O
> - Adquirir una licencia comercial
>
> Para software empresarial privado, **se necesita licencia comercial**.
> Consultar precios en itextpdf.com.

## Codigo de Ejemplo (simplificado)

```java
Document document = new Document(new PdfDocument(new PdfWriter(outputStream)));
document.add(new Paragraph("COMPROBANTE DE TRAMITE")
    .setFont(boldFont).setFontSize(18));
document.add(new Table(4).useAllAvailableWidth()
    .addHeaderCell(new Cell().add(new Paragraph("Item"))));
document.close();
```

## Cuando Usar

- Cuando se necesita **funcionalidad avanzada** (firma digital, PDF/A, accesibilidad)
- Cuando hay **presupuesto** para licencia comercial
- Para proyectos que necesitan **soporte profesional**
- Cuando se valora la **documentacion de calidad**
