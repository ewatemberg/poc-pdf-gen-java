# Apache PDFBox

## Informacion General

| Campo | Valor |
|-------|-------|
| **Version** | 3.0.3 |
| **Licencia** | Apache 2.0 (libre, sin restricciones) |
| **Mantenedor** | Apache Software Foundation |
| **Repositorio** | github.com/apache/pdfbox |
| **Enfoque** | API low-level programatica |

## Descripcion

Apache PDFBox es una libreria Java de codigo abierto para trabajar con documentos PDF. Permite crear, manipular, extraer texto, llenar formularios y firmar documentos PDF.

Es una libreria **low-level**: el desarrollador tiene control total sobre cada elemento del PDF, pero debe manejar manualmente las coordenadas, el layout y la paginacion.

## Ventajas

- **Licencia Apache 2.0**: Sin restricciones para uso comercial
- **Control total**: Posicionamiento preciso de cada elemento
- **Liviana**: Pocas dependencias transitivas
- **Versatil**: No solo genera PDFs, tambien los lee, modifica y firma
- **PDF/A**: Soporte para generacion de PDFs conformes a PDF/A
- **Mantenimiento activo**: Proyecto Apache con releases regulares

## Desventajas

- **Codigo verbose**: Requiere muchas lineas para layouts simples
- **Sin layout automatico**: Tablas, alineacion y saltos de pagina son manuales
- **Coordenadas absolutas**: Sistema de coordenadas con origen en esquina inferior-izquierda
- **Curva de aprendizaje media**: Requiere entender la estructura interna del formato PDF

## Codigo de Ejemplo (simplificado)

```java
PDDocument document = new PDDocument();
PDPage page = new PDPage(PDRectangle.A4);
document.addPage(page);

try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
    cs.beginText();
    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
    cs.newLineAtOffset(200, 750);
    cs.showText("COMPROBANTE DE TRAMITE");
    cs.endText();
}
```

## Cuando Usar

- Cuando se necesita **control total** sobre el PDF
- Para **manipular PDFs existentes** (llenar formularios, agregar paginas)
- Cuando la **licencia Apache 2.0** es un requisito
- Para proyectos que necesitan **firma digital**
- Cuando el **peso de las dependencias** es critico
