# OpenHTMLtoPDF

## Informacion General

| Campo | Valor |
|-------|-------|
| **Version** | 1.1.4 |
| **Licencia** | LGPL 2.1 |
| **Repositorio** | github.com/openhtmltopdf/openhtmltopdf |
| **Enfoque** | HTML/CSS a PDF (moderno, basado en PDFBox) |

## Descripcion

OpenHTMLtoPDF es el sucesor espiritual de Flying Saucer. Renderiza HTML/CSS a PDF usando Apache PDFBox como motor de renderizado (en lugar de iText como Flying Saucer). Ofrece mejor soporte CSS, mejor rendimiento y mantenimiento mas activo.

Es la **opcion recomendada** para equipos que quieren generar PDFs desde HTML/CSS en Spring Boot, especialmente combinado con Thymeleaf para templates dinamicos.

## Ventajas

- **HTML/CSS moderno**: Mejor soporte CSS que Flying Saucer
- **Basado en PDFBox**: Motor robusto y bien mantenido
- **Modo rapido**: `useFastMode()` mejora significativamente el rendimiento
- **SVG y MathML**: Soporte nativo
- **Accesibilidad**: Soporte para PDF/UA (accesible)
- **Thymeleaf**: Integracion natural con Spring Boot + Thymeleaf
- **Gratuito**: LGPL permite uso comercial

## Desventajas

- **Sin CSS3 completo**: No soporta flexbox ni grid
- **Sin firma digital**: No tiene soporte nativo
- **Mantenimiento moderado**: El proyecto original (danfickle) tiene menos actividad
- **Sin JavaScript**: No ejecuta JS en los templates (a diferencia de Playwright)

## Integracion con Thymeleaf (patron recomendado)

```java
// Template HTML en src/main/resources/templates/comprobante.html
// con expresiones Thymeleaf: th:text="${data.empresa}"

@Autowired TemplateEngine templateEngine;

Context ctx = new Context();
ctx.setVariable("data", comprobanteData);
String html = templateEngine.process("comprobante", ctx);

PdfRendererBuilder builder = new PdfRendererBuilder();
builder.useFastMode();
builder.withHtmlContent(html, null);
builder.toStream(outputStream);
builder.run();
```

## Comparacion con Flying Saucer

| Aspecto | OpenHTMLtoPDF | Flying Saucer |
|---------|---------------|---------------|
| Motor PDF | PDFBox | iText/OpenPDF |
| Soporte CSS | Mejor (algo de CSS3) | CSS 2.1 basico |
| Rendimiento | Mas rapido (modo fast) | Mas lento |
| Mantenimiento | Moderado | Bajo |
| SVG | Si | Limitado |
| Accesibilidad (PDF/UA) | Si | No |

## Cuando Usar

- Como **reemplazo moderno de Flying Saucer**
- Cuando el equipo conoce **HTML/CSS**
- Con **Thymeleaf** en Spring Boot para templates dinamicos
- Para documentos de **complejidad baja a media** (facturas, comprobantes, certificados)
- Cuando se necesita **prototipado rapido** con HTML
