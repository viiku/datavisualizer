# Data Visualizer

## Quick Start

1. **Build the Project**

    ```sh
    mvn clean install
    ```

2. **Visit Swagger for API Documentation**

    [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## REST APIs Design

### 1. File Upload Endpoint

- **URL:** `POST /api/v1/maps/uploads`
- **Auth:** Required (User must be logged in)
- **Request:** `multipart/form-data` containing the file.
    - `file`: The actual file blob.
    - *(Optional)* `name`: A user-friendly name for this map/dataset.
    - *(Optional)* `description`: A description.

**Processing:**
- Validate file type and size.
- Save the file to temporary storage or cloud storage (e.g., S3).
- Create an UploadedFile record in the database with `status=PENDING`.
- Trigger an asynchronous background task to process the file, passing the UploadedFile ID and storage path.

**Response:**  
`202 Accepted` — Indicates the request is accepted for processing.

**Body (JSON):**
```json
{
  "upload_id": "unique_upload_file_id_123",
  "status": "PENDING",
  "message": "File uploaded successfully and queued for processing."
}
```

---

### 2. Upload Status Endpoint

- **URL:** `GET /api/v1/maps/uploads/{upload_id}/status`
- **Auth:** Required (User must own the upload)

**Processing:**
- Fetch the UploadedFile record by `upload_id`.
- Check user ownership.

**Response:**  
`200 OK`
```json
{
  "upload_id": "unique_upload_file_id_123",
  "status": "PROCESSING", // or PENDING, COMPLETED, FAILED
  "detected_geo_level": "STATE", // Available after partial/full processing
  "detected_metrics": ["population", "literacy"], // Available after partial/full processing
  "error_message": null // or "Error details..." if status is FAILED
}
```
- `404 Not Found`: If `upload_id` doesn't exist or user doesn't have access.

---

### 3. Map Data Retrieval Endpoint

- **URL:** `GET /api/v1/maps/uploads/{upload_id}/data`
- **Auth:** Required (User must own the upload)
- **Query Parameters:**
    - `metric`: String (e.g., population). **Required.**
    - *(Optional)* `geo_level`: String (e.g., STATE, DISTRICT).

**Processing:**
- Fetch the UploadedFile record. Ensure status is `COMPLETED`. Check ownership.
- Query the GeoDataPoint table for records linked to this `upload_id`.
- Filter by `geo_level` if provided.
- Filter out features where `is_matched` is false (or handle them differently if needed).
- Construct a GeoJSON FeatureCollection.

**Response:**  
`200 OK`  
**Body: GeoJSON FeatureCollection**
```json
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "geometry": { /* GeoJSON Polygon or MultiPolygon */ },
      "properties": {
        "name": "California",
        "metric_name": "population",
        "value": 39500000
        // optional other properties like 'literacy': 92.5
      }
    },
    {
      "type": "Feature",
      "geometry": { /* GeoJSON Polygon or MultiPolygon */ },
      "properties": {
        "name": "Texas",
        "metric_name": "population",
        "value": 29100000
      }
    }
    // ... more features
  ]
}
```
- `400 Bad Request`: If `metric` parameter is missing/invalid, or if the requested metric wasn't found.
- `404 Not Found`: If `upload_id` doesn't exist or user doesn't have access.
- `409 Conflict`: If the upload processing is not yet `COMPLETED`.

---

### 4. (Optional) List User Uploads Endpoint

- **URL:** `GET /api/v1/maps/uploads`
- **Auth:** Required

**Processing:** Fetch all UploadedFile records for the current user.

**Response:**  
`200 OK` with a list of upload summaries (ID, name, status, timestamp, etc.).

---

## API Endpoints Summary

|  # | Method | Endpoint                                         | Auth | Description                                                         | Response (Status)                                               |
| -: | ------ | ------------------------------------------------ | ---- | ------------------------------------------------------------------- | --------------------------------------------------------------- |
|  1 | POST   | `/api/v1/uploads`                                | ✅    | **Upload** a CSV/Excel/PDF file for background processing           | `202 Accepted` → `{ uploadId, status, message }`                |
|  2 | GET    | `/api/v1/uploads/{uploadId}/status`              | ✅    | Get the **processing status** of a specific upload                  | `200 OK` → `{ uploadId, status, metrics, errors }`              |
|  3 | GET    | `/api/v1/uploads/{uploadId}/summary`             | ✅    | Fetch **metadata summary** (columns, geo hints, metrics, types)     | `200 OK` → `{ uploadId, schema, detectedMetrics, geoHints }`    |
|  4 | GET    | `/api/v1/uploads/{uploadId}/data`                | ✅    | Fetch **cleaned raw JSON/tabular** data                             | `200 OK` → `[ { row }, ... ]`                                   |
|  5 | GET    | `/api/v1/uploads/{uploadId}/visualization/map`   | ✅    | **GeoJSON FeatureCollection** for **map visualization**             | `200 OK` → `FeatureCollection (GeoJSON)`                        |
|  6 | GET    | `/api/v1/uploads/{uploadId}/visualization/chart` | ✅    | Aggregated data for **chart visualization** (bar/line/pie)          | `200 OK` → `{ labels, data, series }`                           |
|  7 | GET    | `/api/v1/uploads`                                | ✅    | **List uploads** by the logged-in user (for dashboard/history view) | `200 OK` → `[ { uploadId, fileName, status, createdAt }, ... ]` |


## REST APIs Implementation

1. File Management APIs

a. Upload Data File
``
POST /api/v1/files/upload
Content-Type: multipart/form-data
``
``
Request Body:-
- file: (binary) - CSV/Excel/PDF file
- metadata: (JSON) - Optional metadata about the file
``

Response Body:-
``
{
  "fileId": "uuid-string",
  "fileName": "population_data.csv",
  "fileSize": 2048576,
  "fileType": "CSV",
  "uploadTimestamp": "2025-06-26T10:30:00Z",
  "status": "UPLOADED",
  "columnsDetected": ["country", "state", "district", "population", "literacy_rate"]
}
``

b. Get File Processing Status

Request
``
GET /api/v1/files/{fileId}/status
``

Response
``
{
  "fileId": "uuid-string",
  "status": "PROCESSING|COMPLETED|FAILED",
  "progress": 85,
  "message": "Parsing geographical data...",
  "processedRecords": 1250,
  "totalRecords": 1500,
  "errors": []
}
``

c. List Uploaded Files

Request
``
GET /api/v1/files?page=0&size=10&sortBy=uploadDate&sortDir=desc
``

d. Delete file
Request
``
DELETE /api/v1/files/{fileId}
``

2. Data Mapping & Configuration APIs

a. Configure Data Mapping
Request
``
POST /api/v1/datasets/{fileId}/mapping
``

Request Body:-

``
{
  "columnMappings": {
    "country": "Country Name",
    "state": "State/Province", 
    "district": "District",
    "population": "Population Count",
    "literacy": "Literacy Rate (%)"
  },
  "dataTypes": {
    "population": "INTEGER",
    "literacy": "DECIMAL"
  },
  "geographicalLevel": "DISTRICT", // COUNTRY, STATE, DISTRICT
  "coordinateSystem": "WGS84"
}
``

b. Get Data Preview

Request
``
GET /api/v1/datasets/{fileId}/preview?rows=10&columns=country,state,population
``

Response Body:
``
{
  "columns": ["country", "state", "district", "population", "literacy"],
  "data": [
    ["India", "Bihar", "Patna", 5838465, 70.68],
    ["India", "Bihar", "Gaya", 4379383, 63.80]
  ],
  "totalRows": 15000,
  "dataTypes": {
    "population": "INTEGER",
    "literacy": "DECIMAL"
  }
}
``

3. Geographical Data APIs
   
a. Get Administrative Boundaries
Request
``
GET /api/v1/geography/boundaries?level=district&country=India&state=Bihar
``

Response
``
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "properties": {
        "name": "Patna",
        "level": "district",
        "country": "India",
        "state": "Bihar"
      },
      "geometry": {
        "type": "Polygon",
        "coordinates": [[[85.0, 25.5], [85.5, 25.5], [85.5, 26.0], [85.0, 26.0], [85.0, 25.5]]]
      }
    }
  ]
}
``

b. Search Geographical Locations

Request
``
GET /api/v1/geography/search?q=patna&level=district&limit=10
``

c. Get Coordinate Information
``
POST /api/v1/geography/geocode
``

Request Body
``
{
  "locations": [
    {"country": "India", "state": "Bihar", "district": "Patna"},
    {"country": "India", "state": "Bihar", "district": "Gaya"}
  ]
}
``

4. Visualization Configuration APIs

a. Create Visualization 
``
POST /api/v1/visualizations
``

Request Body:
``
{
  "name": "Bihar Population Density 2024",
  "description": "Population and literacy data visualization for Bihar districts",
  "datasetId": "file-uuid",
  "visualizationType": "CHOROPLETH", // HEATMAP, BUBBLE, MARKER
  "configuration": {
    "colorColumn": "population",
    "colorScheme": "Blues",
    "colorScale": "LINEAR", // LOG, SQRT
    "sizeColumn": "literacy",
    "tooltipColumns": ["district", "population", "literacy"],
    "legendTitle": "Population Count",
    "opacity": 0.8,
    "borderColor": "#333333",
    "borderWidth": 1
  },
  "mapSettings": {
    "center": {"lat": 25.0961, "lng": 85.3131},
    "zoom": 7,
    "mapStyle": "TERRAIN" // SATELLITE, ROADMAP, HYBRID
  }
}
``

Response:
``
{
  "visualizationId": "viz-uuid",
  "name": "Bihar Population Density 2024",
  "status": "CREATED",
  "shareUrl": "https://yourapp.com/viz/viz-uuid",
  "embedCode": "<iframe src='https://yourapp.com/embed/viz-uuid' width='800' height='600'></iframe>"
}
``

b. Update Visualization
``
PUT /api/v1/visualizations/{vizId}
``

c. Get Visualization Data
``
GET /api/v1/visualizations/{vizId}/data
``

Response Body:-
``
{
  "type": "FeatureCollection",
  "metadata": {
    "totalFeatures": 38,
    "dataRange": {
      "population": {"min": 874919, "max": 5838465},
      "literacy": {"min": 45.2, "max": 80.1}
    },
    "colorScheme": "Blues",
    "legend": [
      {"color": "#f7fbff", "range": "< 1M", "count": 15},
      {"color": "#c6dbef", "range": "1M - 2M", "count": 12},
      {"color": "#6baed6", "range": "2M - 3M", "count": 8},
      {"color": "#2171b5", "range": "> 3M", "count": 3}
    ]
  },
  "features": [
    {
      "type": "Feature",
      "properties": {
        "district": "Patna",
        "population": 5838465,
        "literacy": 70.68,
        "colorValue": "#2171b5",
        "tooltip": "Patna: Population 5.84M, Literacy 70.7%"
      },
      "geometry": { /* GeoJSON geometry */ }
    }
  ]
}
``

5. Dashboard & Analytics APIs
   
a. List User Visualizations 

``
GET /api/v1/visualizations?userId={userId}&page=0&size=10
``

b. Get Visualization Analytics

``
GET /api/v1/visualizations/{vizId}/analytics
``

Response Body:- 
``
{
  "views": 1250,
  "shares": 45,
  "lastAccessed": "2025-06-26T09:15:00Z",
  "topRegions": ["Patna", "Gaya", "Muzaffarpur"],
  "dataQualityScore": 0.95,
  "performanceMetrics": {
    "loadTime": 850,
    "renderTime": 320
  }
}
``

6. Export & Sharing APIs
   
a. Export Visualization

``
POST /api/v1/visualizations/{vizId}/export
``

Request Body:
``
{
  "format": "PNG", // PDF, SVG, JSON
  "width": 1920,
  "height": 1080,
  "dpi": 300,
  "includeData": true
}
``

b. Generate Share Link

``
POST /api/v1/visualizations/{vizId}/share
``

Request Body:
``
{
  "expiryDays": 30,
  "permissions": ["VIEW"], // EDIT, DOWNLOAD
  "password": "optional-password"
}
``

7. Data Quality & Validation APIs

a. Validate Data Quality
``
POST /api/v1/datasets/{fileId}/validate
``

Response Body
``
{
  "overallScore": 0.87,
  "issues": [
    {
      "type": "MISSING_COORDINATES",
      "severity": "WARNING",
      "affectedRows": 25,
      "description": "Some districts could not be geocoded",
      "suggestions": ["Check district name spelling", "Provide manual coordinates"]
    },
    {
      "type": "OUTLIER_VALUES",
      "severity": "INFO", 
      "affectedRows": 3,
      "description": "Population values seem unusually high",
      "suggestions": ["Verify data source", "Check for data entry errors"]
    }
  ],
  "statistics": {
    "totalRows": 1500,
    "validRows": 1475,
    "duplicateRows": 12,
    "missingValues": 38
  }
}
``

8. Template & Preset APIs
   
a. Get Visualization Templates
``
GET /api/v1/templates?category=demographic&region=india
``

b. Save as Template
``
POST /api/v1/templates
``

Request Body:
``
{
  "name": "Indian District Demographics",
  "description": "Standard template for Indian district-level demographic data",
  "baseVisualizationId": "viz-uuid",
  "requiredColumns": ["district", "population", "literacy"],
  "isPublic": true
}
``