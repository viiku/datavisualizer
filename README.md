# Visualizer

### Quick Start
1. Build this project

    ``
    mvn clean install
    ``

2. Visit here to see rest api implementation
   ``
http://localhost:8080/swagger-ui.html
   ``

### REST APIs Design
1. File Upload Endpoint

URL: POST /api/v1/maps/uploads
Auth: Required (User must be logged in).
Request: multipart/form-data containing the file.
file: The actual file blob.
(Optional) name: A user-friendly name for this map/dataset.
(Optional) description: A description.

Processing:
Validate file type and size.
Save the file to temporary storage or cloud storage (e.g., S3).
Create an UploadedFile record in the database with status=PENDING.
Trigger an asynchronous background task (e.g., Celery task) to process the file, passing the UploadedFile ID and storage path.

Response:
202 Accepted: Indicates the request is accepted for processing.

Body (JSON):

``
{
  "upload_id": "unique_upload_file_id_123",
  "status": "PENDING",
  "message": "File uploaded successfully and queued for processing."
}
``
2. Upload Status Endpoint

URL: GET /api/v1/maps/uploads/{upload_id}/status
Auth: Required (User must own the upload).
Processing:
Fetch the UploadedFile record by upload_id.
Check user ownership.

Response:
200 OK:

``
{
  "upload_id": "unique_upload_file_id_123",
  "status": "PROCESSING", // or PENDING, COMPLETED, FAILED
  "detected_geo_level": "STATE", // Available after partial/full processing
  "detected_metrics": ["population", "literacy"], // Available after partial/full processing
  "error_message": null // or "Error details..." if status is FAILED
}
``

404 Not Found: If upload_id doesn't exist or user doesn't have access.

3. Map Data Retrieval Endpoint

URL: GET /api/v1/maps/uploads/{upload_id}/data
Auth: Required (User must own the upload).
Query Parameters:
metric: String (e.g., population). Required. Specifies which metric value to include in the output features.
(Optional) geo_level: String (e.g., STATE, DISTRICT). Filter results to a specific level if the source file contained mixed levels.

Processing:
Fetch the UploadedFile record. Ensure status is COMPLETED. Check ownership.
Query the GeoDataPoint table for records linked to this upload_id.
Filter by geo_level if provided.
Filter out features where is_matched is false (or handle them differently if needed).
Construct a GeoJSON FeatureCollection. Each Feature will have:
   
geometry: The Polygon/MultiPolygon from GeoDataPoint.geometry.
properties: An object containing at least:
name: The GeoDataPoint.geo_name.
metric_name: The requested metric (e.g., "population").
value: The value for the requested metric from GeoDataPoint.metrics. (e.g., 39500000).
(Optional) Other metrics from the GeoDataPoint.metrics field.

Response:
200 OK:
Body: GeoJSON FeatureCollection

``
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
``

400 Bad Request: If metric parameter is missing or invalid, or if the requested metric wasn't found in the processed data.
404 Not Found: If upload_id doesn't exist or user doesn't have access.
409 Conflict: If the upload processing is not yet COMPLETED.

4. (Optional) List User Uploads Endpoint

URL: GET /api/v1/maps/uploads
Auth: Required.
Processing: Fetch all UploadedFile records for the current user.
Response: 200 OK with a list of upload summaries (ID, name, status, timestamp, etc.).

