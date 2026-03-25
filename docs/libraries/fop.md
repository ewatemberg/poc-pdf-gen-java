# Apache FOP

## Informacion General

| Campo | Valor |
|-------|-------|
| **Version** | 2.10 |
| **Licencia** | Apache 2.0 (libre, sin restricciones) |
| **Mantenedor** | Apache Software Foundation |
| **Sitio** | xmlgraphics.apache.org/fop |
| **Enfoque** | XSL-FO (XML + XSLT a PDF) |

## Descripcion

Apache FOP (Formatting Objects Processor) es un procesador de XSL-FO que genera PDF y otros formatos de salida. Recibe documentos XML con formato XSL-FO y los convierte a PDF, PostScript, PNG, PCL y otros formatos.

XSL-FO es un estandar W3C para describir el formato de documentos. Si bien es poderoso, tiene una curva de aprendizaje pronunciada porque requiere conocimiento de XML, XSLT y la especificacion XSL-FO.

## Ventajas

- **Licencia Apache 2.0**: Sin restricciones para uso comercial
- **Multi-formato**: Genera PDF, PS, PNG, PCL, AFP, SVG desde la misma fuente
- **Estandar W3C**: Basado en un estandar abierto (XSL-FO)
- **Datos XML**: Ideal cuando los datos ya vienen en formato XML
- **PDF/A**: Soporte para generacion de PDFs conformes
- **Mantenimiento activo**: Proyecto Apache con releases regulares
- **Sin dependencia de HTML**: No necesita un motor de renderizado web

## Desventajas

- **Curva de aprendizaje alta**: XSL-FO es un lenguaje complejo y poco conocido
- **Verbose**: Los documentos XSL-FO son extensos y dificiles de leer
- **Dependencias pesadas**: Muchas dependencias transitivas
- **Sin disenador visual**: No existe un IDE visual popular para XSL-FO
- **Debugging complejo**: Errores en XSL-FO son dificiles de depurar
- **Sin firma digital**: No soporta firmas nativas

## Codigo de Ejemplo (simplificado)

```xml
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
    <fo:simple-page-master master-name="A4" page-height="297mm" page-width="210mm">
      <fo:region-body margin="20mm"/>
    </fo:simple-page-master>
  </fo:layout-master-set>
  <fo:page-sequence master-reference="A4">
    <fo:flow flow-name="xsl-region-body">
      <fo:block font-size="18pt" font-weight="bold">COMPROBANTE DE TRAMITE</fo:block>
    </fo:flow>
  </fo:page-sequence>
</fo:root>
```

## Cuando Usar

- Cuando los datos de origen estan en **XML** (facturacion electronica, EDI)
- Cuando se necesita **output multiformat** (PDF + PostScript + PNG)
- Cuando la **licencia Apache 2.0** es un requisito estricto
- Para **documentos altamente estructurados** con datos repetitivos
- Cuando se tiene experiencia con **XSLT y XML**

## Cuando NO Usar

- Para documentos simples (overkill significativo)
- Si el equipo no conoce XML/XSLT
- Si se necesita prototipado rapido
- Si se necesita firma digital
