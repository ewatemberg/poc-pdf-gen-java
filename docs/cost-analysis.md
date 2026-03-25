# Costos de Aprendizaje

## Tabla de Costos por Libreria

| Libreria | Curva de Aprendizaje | Tiempo de Adopcion | Prerequisitos | Calidad de Docs | Comunidad |
|----------|---------------------|--------------------|---------------|-----------------|-----------|
| PDFBox | Media | 2-3 semanas | Java, geometria de coordenadas PDF | Buena (Apache) | Grande |
| iText | Baja-Media | 1-2 semanas | Java basico | Excelente (sitio comercial con tutoriales) | Grande |
| Flying Saucer | Baja | 3-5 dias | HTML/CSS basico | Limitada (wiki viejo) | Pequena |
| JasperReports | Alta | 3-4 semanas | XML, JasperStudio IDE, conceptos de reporting | Buena (Jaspersoft) | Grande |
| OpenPDF | Baja-Media | 1-2 semanas | Java basico (API similar a iText 4) | Moderada (GitHub wiki) | Mediana |
| OpenHTMLtoPDF | Baja | 3-5 dias | HTML/CSS, opcionalmente Thymeleaf | Moderada (GitHub) | Mediana |
| Apache FOP | Alta | 3-4 semanas | XML, XSLT, especificacion XSL-FO | Buena (Apache) | Mediana |

## Desglose por Libreria

### PDFBox - Curva Media
- **Que se necesita aprender**: Sistema de coordenadas PDF (origen inferior-izquierdo), manejo manual de posiciones X/Y, streams de contenido.
- **Mayor dificultad**: No hay layout automatico. Tablas, saltos de pagina y alineacion son responsabilidad del desarrollador.
- **Ventaja de aprendizaje**: Una vez dominado, se tiene control total sobre el PDF.

### iText - Curva Baja-Media
- **Que se necesita aprender**: API de alto nivel con Document, Paragraph, Table, Cell.
- **Mayor dificultad**: Entender el modelo de licenciamiento (AGPL vs comercial) y las diferencias entre versiones 5.x y 7/8.x.
- **Ventaja de aprendizaje**: Documentacion excelente con muchos ejemplos en el sitio oficial.

### Flying Saucer - Curva Baja
- **Que se necesita aprender**: XHTML valido (tags cerrados, estructura estricta) y CSS 2.1.
- **Mayor dificultad**: Las limitaciones de CSS (sin flexbox, sin grid, sin CSS3 avanzado).
- **Ventaja de aprendizaje**: Si el equipo ya conoce HTML/CSS, la curva es casi nula.

### JasperReports - Curva Alta
- **Que se necesita aprender**: Formato JRXML, IDE JasperStudio, conceptos de bands (title, header, detail, footer), parametros, fields, variables.
- **Mayor dificultad**: La configuracion inicial y el manejo de dependencias. Los templates JRXML son verbosos.
- **Ventaja de aprendizaje**: JasperStudio ofrece un disenador visual drag-and-drop.

### OpenPDF - Curva Baja-Media
- **Que se necesita aprender**: API programatica similar a iText 4 (Document, Paragraph, PdfPTable, PdfPCell).
- **Mayor dificultad**: La documentacion esta dispersa entre el wiki de GitHub y tutoriales de iText 4.
- **Ventaja de aprendizaje**: Miles de tutoriales de iText 4 aplican directamente.

### OpenHTMLtoPDF - Curva Baja
- **Que se necesita aprender**: HTML estandar + CSS. Opcionalmente Thymeleaf para templates dinamicos.
- **Mayor dificultad**: Algunas propiedades CSS avanzadas no estan soportadas.
- **Ventaja de aprendizaje**: El equipo de frontend puede colaborar en el diseno de los templates.

### Apache FOP - Curva Alta
- **Que se necesita aprender**: Especificacion XSL-FO (un lenguaje de formato basado en XML), XSLT para transformaciones.
- **Mayor dificultad**: XSL-FO es un estandar poco conocido. El debugging de templates es complejo.
- **Ventaja de aprendizaje**: Una vez dominado, permite generar multiples formatos (PDF, PS, PNG, etc.) desde la misma fuente.

## Costo por Licencia

| Libreria | Costo Monetario | Restriccion Principal |
|----------|-----------------|----------------------|
| PDFBox | $0 | Ninguna |
| iText | $0 (AGPL) o **miles USD/ano** (comercial) | AGPL obliga a publicar TODO el codigo fuente de la app |
| Flying Saucer | $0 | LGPL: publicar cambios solo si se modifica la libreria |
| JasperReports | $0 (Community) | LGPL. Version Pro es paga |
| OpenPDF | $0 | LGPL: publicar cambios solo si se modifica la libreria |
| OpenHTMLtoPDF | $0 | LGPL: publicar cambios solo si se modifica la libreria |
| Apache FOP | $0 | Ninguna |

## Recomendacion

Para un equipo que viene de generar PDFs desde HTML en el backend, las opciones con **menor costo de adopcion** son:

1. **OpenHTMLtoPDF** - Curva baja, HTML/CSS moderno, LGPL
2. **Flying Saucer** - Curva baja, HTML/CSS, pero CSS mas limitado
3. **OpenPDF** - Si se necesita mas control programatico sin costo de licencia
