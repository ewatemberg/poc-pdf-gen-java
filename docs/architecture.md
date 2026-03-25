# Arquitectura

## Estructura del Proyecto

```
poc-pdf-lib/
├── backend/                    # Spring Boot 3.2 + Java 21
│   ├── pom.xml                 # 7 dependencias PDF
│   └── src/main/java/com/acme/pdfpoc/
│       ├── model/              # Records: ComprobanteData, ItemDetalle
│       ├── data/               # ComprobanteDataFactory (datos ficticios)
│       ├── service/            # PdfGenerator interface
│       │   └── impl/           # 7 implementaciones
│       ├── controller/         # PdfController (REST API)
│       └── config/             # CorsConfig
├── frontend/                   # Angular 17 + Material
│   └── src/app/
│       ├── services/           # PdfService (HttpClient)
│       ├── models/             # Interfaces TypeScript
│       └── components/         # UI components
├── docs/                       # Documentacion Backstage
└── mkdocs.yml
```

## Patron de Diseno: Strategy

Se utiliza el patron **Strategy** para desacoplar cada libreria del resto del sistema.

```
PdfGenerator (interface)
├── generate(ComprobanteData): byte[]
├── getLibraryName(): String
├── getLibraryVersion(): String
├── getLicense(): String
└── getApproach(): String

Implementaciones:
├── PdfBoxGenerator
├── ITextGenerator
├── FlyingSaucerGenerator
├── JasperReportsGenerator
├── OpenPdfGenerator
├── OpenHtmlToPdfGenerator
└── ApacheFopGenerator
```

Spring Boot autowire todas las implementaciones como una `List<PdfGenerator>` y el controller las expone como un mapa por nombre.

## Flujo de Datos

```
Angular Frontend
    │
    ├── GET /api/pdf/libraries     → Lista de librerias disponibles
    │
    └── GET /api/pdf/{library}     → PDF generado como bytes
            │
            ├── ComprobanteDataFactory.createSampleData()
            │       (datos identicos para todas las librerias)
            │
            ├── PdfGenerator.generate(data)
            │       (implementacion especifica)
            │
            └── Response: PDF bytes
                    + Header X-Generation-Time-Ms
                    + Header X-Pdf-Size-Bytes
```

## Decisiones Clave

1. **Mismos datos de entrada**: `ComprobanteDataFactory` genera datos identicos para todas las librerias, garantizando una comparacion justa.

2. **Metricas en headers HTTP**: El tiempo de generacion y tamano se exponen via headers custom para que el frontend los capture sin parsear el PDF.

3. **Sin persistencia**: Los PDFs se generan on-the-fly y no se almacenan. Esto simplifica la POC y evita efectos de cache.

4. **Nombres ofuscados**: Se usa "ACME Corp S.A." como empresa ficticia. No se referencian nombres de empresas reales.
