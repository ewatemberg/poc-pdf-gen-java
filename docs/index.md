# POC - Comparacion de Librerias de Generacion de PDF

## Objetivo

Evaluar y comparar **7 librerias** de generacion de PDF disponibles en el ecosistema Java, generando el mismo documento (comprobante de tramite) con cada una para una comparacion justa.

## Librerias Evaluadas

| # | Libreria | Version | Licencia | Enfoque |
|---|----------|---------|----------|---------|
| 1 | [PDFBox](libraries/pdfbox.md) | 3.0.3 | Apache 2.0 | API low-level programatica |
| 2 | [iText](libraries/itext.md) | 8.0.5 | AGPL / Comercial | API high-level programatica |
| 3 | [Flying Saucer](libraries/flysaucer.md) | 9.9.1 | LGPL 2.1 | HTML/CSS a PDF (XHTML) |
| 4 | [JasperReports](libraries/jasper.md) | 7.0.1 | LGPL 3.0 | Templates JRXML |
| 5 | [OpenPDF](libraries/openpdf.md) | 2.0.3 | LGPL + MPL | Fork libre de iText 4 |
| 6 | [OpenHTMLtoPDF](libraries/openhtmltopdf.md) | 1.1.4 | LGPL 2.1 | HTML/CSS a PDF (moderno) |
| 7 | [Apache FOP](libraries/fop.md) | 2.10 | Apache 2.0 | XSL-FO (XML/XSLT) |

## Indice de Documentacion

- **[Arquitectura](architecture.md)** - Decisiones de diseno y estructura del proyecto
- **[Comparacion Tecnica](comparison.md)** - Tabla comparativa lado a lado
- **[Costos de Aprendizaje](cost-analysis.md)** - Tiempo de adopcion, prerequisitos y curva de aprendizaje
- **[Casos de Uso](use-cases.md)** - Que libreria elegir segun el tipo de documento (facturas, reportes, certificados)
- **[Guia de Instalacion](setup.md)** - Como ejecutar la POC

## Stack Tecnologico

- **Backend**: Java 21, Spring Boot 3.2, Maven
- **Frontend**: Angular 17, Angular Material
- **Documentacion**: MkDocs (Backstage-compatible)

## Documento de Prueba

Todas las librerias generan el mismo **Comprobante de Tramite** con:

- Header con logo y titulo
- Datos del tramite (numero, fecha, titular, DNI, tipo, estado)
- Tabla de detalle de items con montos
- Total calculado
- Observaciones
- Footer con leyenda de validez y numero de pagina
