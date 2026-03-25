# Guia de Instalacion y Ejecucion

## Prerequisitos

- **Java 21** (JDK)
- **Maven 3.8+** (o usar el wrapper `mvnw` si se incluye)
- **Node.js 18+**
- **npm 9+**

## Backend

```bash
cd backend
mvn spring-boot:run
```

El backend iniciara en `http://localhost:8080`.

### Verificar que funciona

```bash
# Listar librerias disponibles
curl http://localhost:8080/api/pdf/libraries

# Generar un PDF con PDFBox
curl -o comprobante-pdfbox.pdf http://localhost:8080/api/pdf/pdfbox

# Generar con todas las librerias
for lib in pdfbox itext flysaucer jasper openpdf openhtmltopdf fop; do
  curl -o "comprobante-$lib.pdf" "http://localhost:8080/api/pdf/$lib"
  echo "Generado: comprobante-$lib.pdf"
done
```

## Frontend

```bash
cd frontend
npm install
npx ng serve
```

El frontend iniciara en `http://localhost:4200`.

El proxy esta configurado para redirigir `/api/*` al backend en `localhost:8080`.

## Uso

1. Abrir `http://localhost:4200` en el navegador
2. Se mostraran 7 tarjetas, una por cada libreria
3. Hacer clic en **"Generar"** en cada tarjeta para generar el PDF
4. El PDF se muestra en un preview inline (iframe)
5. Hacer clic en **"Descargar"** para guardar el PDF
6. Hacer clic en **"Generar Todos"** para ejecutar todas las librerias y ver la tabla comparativa
7. La tabla comparativa muestra tiempos de generacion y tamanos de archivo

## Documentacion

```bash
# Instalar MkDocs (si no esta instalado)
pip install mkdocs-techdocs-core

# Servir documentacion localmente
mkdocs serve
```

La documentacion estara disponible en `http://localhost:8000`.
