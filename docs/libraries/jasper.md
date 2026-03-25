# JasperReports

## Informacion General

| Campo | Valor |
|-------|-------|
| **Version** | 7.0.1 |
| **Licencia** | LGPL 3.0 (Community Edition) |
| **Mantenedor** | TIBCO / Jaspersoft |
| **Sitio** | community.jaspersoft.com |
| **Enfoque** | Templates JRXML (disenador visual) |

## Descripcion

JasperReports es un motor de reportes empresariales que genera documentos a partir de templates XML (JRXML). Los templates se pueden disenar visualmente con JasperStudio (IDE basado en Eclipse). Es la solucion mas completa para reportes con datos variables, agrupaciones, subreportes y graficos.

## Ventajas

- **Disenador visual**: JasperStudio permite disenar templates con drag-and-drop
- **Reportes complejos**: Subreportes, graficos, cross-tabs, agrupaciones
- **Multi-formato**: Exporta a PDF, Excel, CSV, HTML, Word
- **Parametros y variables**: Sistema de data binding robusto
- **Comunidad grande**: Amplia base de usuarios y documentacion
- **Gratuito**: La Community Edition es LGPL

## Desventajas

- **Curva de aprendizaje alta**: Los templates JRXML son verbosos y complejos
- **Dependencias pesadas**: Muchas dependencias transitivas
- **Configuracion compleja**: Setup inicial requiere esfuerzo
- **JasperStudio obligatorio**: Editar JRXML a mano es impractico
- **Conflictos de dependencias**: Puede colisionar con otras librerias PDF en el classpath
- **Sin firma digital**: No soporta firmas nativas

## Codigo de Ejemplo (simplificado)

```java
JasperReport report = JasperCompileManager.compileReport("template.jrxml");
Map<String, Object> params = Map.of("titulo", "COMPROBANTE");
JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);
JasperExportManager.exportReportToPdfStream(print, outputStream);
```

## Cuando Usar

- Para **reportes empresariales** con datos variables y agrupaciones
- Cuando se necesita **exportar a multiples formatos** (PDF + Excel + CSV)
- Cuando hay un **disenador** que usa JasperStudio
- Para reportes con **graficos y visualizaciones**
- Para reportes con **subreportes anidados**

## Cuando NO Usar

- Para documentos simples (overkill)
- Si el equipo no quiere aprender JRXML/JasperStudio
- Si el peso de las dependencias es critico
