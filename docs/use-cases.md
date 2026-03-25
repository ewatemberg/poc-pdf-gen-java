# Casos de Uso - Que Libreria Elegir

## Factura Digital / Comprobante

| Necesidad | Mejor Opcion | Alternativa | Por que |
|-----------|-------------|-------------|---------|
| Factura con diseno HTML | **OpenHTMLtoPDF** + Thymeleaf | Flying Saucer | El equipo de frontend puede disenar el template. CSS moderno soportado |
| Factura programatica simple | **OpenPDF** | iText (si hay licencia) | API clara, sin costo, buena para documentos con estructura fija |
| Factura con datos XML/EDI | **Apache FOP** | JasperReports | Los datos ya vienen en XML, FOP los transforma directamente |
| Factura con firma digital | **iText** | PDFBox | iText tiene el soporte de firma digital mas completo |
| Factura electronica AFIP/regulatoria | **iText** (comercial) | PDFBox | Soporte PDF/A y firma digital robusto para cumplimiento normativo |

## Reporte Empresarial

| Necesidad | Mejor Opcion | Alternativa | Por que |
|-----------|-------------|-------------|---------|
| Reportes tabulares con datos dinamicos | **JasperReports** | iText | JasperReports esta disenado especificamente para reportes con datos variables |
| Dashboards con graficos complejos | **JasperReports** | Playwright (headless browser) | JasperReports soporta graficos nativos. Playwright renderiza dashboards HTML |
| Reportes simples (logo + tabla) | **OpenHTMLtoPDF** | Flying Saucer | HTML/CSS es mas rapido de desarrollar para reportes simples |
| Reportes con subreportes anidados | **JasperReports** | N/A | Unica libreria con soporte nativo de subreportes |
| Exportacion multiformat (PDF+Excel+CSV) | **JasperReports** | Apache FOP | Ambos soportan multiples formatos de salida |

## Certificados / Documentos con Diseno

| Necesidad | Mejor Opcion | Alternativa | Por que |
|-----------|-------------|-------------|---------|
| Posicionamiento preciso de elementos | **PDFBox** | iText | Control total pixel-a-pixel del PDF |
| Diseno basado en template HTML | **OpenHTMLtoPDF** | Flying Saucer | CSS para layout, facil de iterar en el diseno |
| Output multiformat (PDF + PNG + PS) | **Apache FOP** | PDFBox | FOP genera multiples formatos desde una unica fuente |
| Llenado de formularios PDF existentes | **PDFBox** | iText | Ambos soportan AcroForms, PDFBox es gratuito |

## Migracion desde HTML-to-PDF actual

Si el equipo actualmente genera PDFs convirtiendo HTML y busca mejorar el proceso:

| Situacion Actual | Recomendacion | Razon |
|------------------|---------------|-------|
| HTML simple con tablas | **OpenHTMLtoPDF** | Sigue usando HTML/CSS pero con mejor renderizado |
| HTML complejo con CSS3 | **Playwright** (headless browser) | Unica opcion que soporta CSS3 completo + JS |
| Templates HTML con datos del backend | **OpenHTMLtoPDF + Thymeleaf** | Thymeleaf integra naturalmente con Spring Boot |
| El backend no deberia manejar HTML | **OpenPDF** o **iText** | API programatica pura, sin dependencia de HTML |

## Matriz de Decision Rapida

Para elegir rapidamente, responder estas preguntas:

1. **El equipo conoce HTML/CSS?**
   - Si → OpenHTMLtoPDF o Flying Saucer
   - No → Continuar con pregunta 2

2. **Se necesitan reportes complejos con datos variables?**
   - Si → JasperReports
   - No → Continuar con pregunta 3

3. **Se necesita firma digital o PDF/A?**
   - Si → iText (comercial) o PDFBox
   - No → Continuar con pregunta 4

4. **Los datos vienen en XML?**
   - Si → Apache FOP
   - No → OpenPDF (API simple, gratuita)
