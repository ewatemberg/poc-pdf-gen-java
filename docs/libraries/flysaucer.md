# Flying Saucer

## Informacion General

| Campo | Valor |
|-------|-------|
| **Version** | 9.9.1 |
| **Licencia** | LGPL 2.1 |
| **Repositorio** | github.com/flyingsaucerproject/flyingsaucer |
| **Enfoque** | HTML/CSS a PDF (XHTML estricto) |

## Descripcion

Flying Saucer es un renderizador de XHTML/CSS que genera PDF. Toma un documento XHTML con estilos CSS y lo convierte a PDF. Es ideal para equipos que ya conocen HTML/CSS y quieren reutilizar ese conocimiento.

## Ventajas

- **Familiar**: Usa HTML/CSS que todo desarrollador web conoce
- **Rapido de desarrollar**: Prototipar un PDF es tan facil como crear una pagina web
- **Separacion de diseno y datos**: El template HTML es independiente de la logica Java
- **Ligero**: Pocas dependencias
- **Gratuito**: LGPL permite uso en software propietario

## Desventajas

- **XHTML estricto**: Requiere tags cerrados, nesting correcto (no acepta HTML5 suelto)
- **CSS limitado**: Solo CSS 2.1, sin flexbox, sin grid, sin CSS3 avanzado
- **Mantenimiento bajo**: El proyecto tiene actividad reducida
- **Sin firma digital**: No soporta firmas ni PDF/A
- **Fuentes limitadas**: Configuracion de fuentes custom puede ser compleja

## Codigo de Ejemplo (simplificado)

```java
String xhtml = "<html><body><h1>COMPROBANTE</h1></body></html>";
ITextRenderer renderer = new ITextRenderer();
renderer.setDocumentFromString(xhtml);
renderer.layout();
renderer.createPDF(outputStream);
```

## Cuando Usar

- Cuando el equipo **conoce HTML/CSS**
- Para documentos **simples con tablas y texto**
- Cuando se necesita **prototipado rapido**
- Para **migrar** de una solucion HTML-to-PDF existente

## Cuando NO Usar

- Si se necesita CSS3 moderno (flexbox, grid)
- Si se necesita firma digital o PDF/A
- Para documentos con posicionamiento preciso al pixel
