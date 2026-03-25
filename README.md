# POC - Comparacion de Librerias de Generacion de PDF

Proof of Concept para evaluar y comparar librerias de generacion de PDF en Java.

## Librerias Evaluadas

| Libreria | Licencia | Enfoque |
|----------|----------|---------|
| PDFBox 3.x | Apache 2.0 | API low-level programatica |
| iText 8.x | AGPL / Comercial | API high-level programatica |
| Flying Saucer 9.x | LGPL | HTML/CSS a PDF |
| JasperReports 7.x | LGPL | Templates JRXML |
| OpenPDF 3.x | LGPL + MPL | Fork libre de iText 4 |
| OpenHTMLtoPDF 1.1.x | LGPL | HTML/CSS a PDF (moderno) |
| Apache FOP 2.11 | Apache 2.0 | XSL-FO (XML/XSLT) |

## Stack Tecnologico

- **Backend**: Java 21, Spring Boot 3.2, Maven
- **Frontend**: Angular 17, Angular Material
- **Docs**: MkDocs (Backstage-compatible)

## Quick Start

```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend (en otra terminal)
cd frontend
npm install
npx ng serve
```

Abrir http://localhost:4200

## Documentacion

Ver carpeta `docs/` o ejecutar `mkdocs serve` para documentacion completa.
