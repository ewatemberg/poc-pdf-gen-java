# Comparacion Tecnica

## Tabla Comparativa General

| Criterio | PDFBox | iText | Flying Saucer | JasperReports | OpenPDF | OpenHTMLtoPDF | Apache FOP |
|----------|--------|-------|---------------|---------------|---------|---------------|------------|
| **Licencia** | Apache 2.0 | AGPL/Comercial | LGPL 2.1 | LGPL 3.0 | LGPL+MPL | LGPL 2.1 | Apache 2.0 |
| **Costo** | Gratis | Pago (comercial) | Gratis | Gratis | Gratis | Gratis | Gratis |
| **Enfoque** | Programatico | Programatico | HTML/CSS | Template JRXML | Programatico | HTML/CSS | XSL-FO |
| **Complejidad de codigo** | Alta | Media | Baja | Media | Media | Baja | Alta |
| **Peso de dependencias** | Liviano | Medio | Liviano | Pesado | Liviano | Medio | Pesado |
| **Soporte CSS** | N/A | N/A | CSS 2.1 parcial | N/A | N/A | CSS 2.1 + algo CSS3 | N/A |
| **Tablas automaticas** | Manual | Si | Si (HTML) | Si (template) | Si | Si (HTML) | Si (XSL-FO) |
| **Paginacion automatica** | Manual | Si | Si | Si | Si | Si | Si |
| **Header/Footer** | Manual | Event handler | CSS @page | Bands | Manual | CSS @page | Regions |
| **Imagenes** | Si | Si | Si (base64/URL) | Si | Si | Si (base64/URL) | Si |
| **PDF/A** | Si | Si | No | Limitado | Parcial | No | Si |
| **Firma digital** | Si | Si | No | No | Si | No | No |
| **Mantenimiento** | Activo (Apache) | Activo (comercial) | Bajo | Activo | Activo | Moderado | Activo (Apache) |

## Complejidad del Codigo (lineas aproximadas para el comprobante)

| Libreria | Lineas de codigo | Templates externos | Observacion |
|----------|------------------|--------------------|-------------|
| PDFBox | ~150 | No | Todo manual: posiciones X/Y, dibujo de lineas |
| iText | ~100 | No | API high-level reduce codigo significativamente |
| Flying Saucer | ~80 + HTML template | Si (XHTML) | La mayor parte del "codigo" es HTML/CSS |
| JasperReports | ~30 + JRXML template | Si (JRXML) | Codigo Java minimo, la complejidad esta en el template |
| OpenPDF | ~100 | No | API similar a iText, codigo comparable |
| OpenHTMLtoPDF | ~80 + HTML template | Si (HTML) | Similar a Flying Saucer pero con mejor soporte CSS |
| Apache FOP | ~120 (XSL-FO XML) | Si (XSL-FO embebido) | El "codigo" es XML de XSL-FO |

## Rendimiento

> Los valores de rendimiento se miden en la POC al generar el comprobante. Los tiempos dependen del hardware y la primera ejecucion suele ser mas lenta por la carga de clases.

Los resultados exactos se pueden ver en el frontend de la POC al hacer clic en "Generar Todos".

## Resumen

- **Mas simple de adoptar**: OpenHTMLtoPDF o Flying Saucer (si el equipo conoce HTML/CSS)
- **Mas potente para reportes**: JasperReports o iText
- **Mas libre (licencia)**: PDFBox o Apache FOP (Apache 2.0)
- **Mejor relacion costo/beneficio**: OpenHTMLtoPDF + Thymeleaf
